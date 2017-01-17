package com.heros.follow.tools;

import com.heros.follow.datacenter.APIRequest;
import com.heros.follow.datas.hostData;
import org.apache.http.*;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.LayeredConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.impl.execchain.RequestAbortedException;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLException;
import javax.net.ssl.SSLHandshakeException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.ProxySelector;
import java.net.UnknownHostException;
import java.util.*;

/**
 * Created by Albert on 2017/1/9.
 */
public class HttpClientUtils {
    public enum HttpWeb {
        OTHER, SUPER, TX, PHA
    }
    private static final int timeOut = 30 * 1000;
    private final static Map<HttpWeb, CloseableHttpClient> httpclients = new HashMap<>();

    public static void resetClient(HttpWeb web) throws IOException {
        if (httpclients.containsKey(web)) {
            try {
                httpclients.remove(web).close();
            } catch (Exception e) {
                //
            }
        }
    }
    /*
	 *	Parama 1 = 是否自動跳轉
	 *	Parama 2 =  (0) - Socket Timeout
	 *			    (1) - 瀏覽器沒反應 Timeout
	 *				(2) - 請求 Timeout
	 */
    private static RequestConfig getRequestConfig(boolean circularRedirect, List<Integer> outTime) {
        return RequestConfig.custom()
                .setSocketTimeout(outTime == null? timeOut : outTime.get(0))
                .setConnectTimeout(outTime == null? timeOut : outTime.get(1))
                .setConnectionRequestTimeout(outTime == null? timeOut : outTime.get(2))
                .setCircularRedirectsAllowed(circularRedirect)
                .setRedirectsEnabled(true)
                .build();
    }
    private static void config(HttpRequestBase httpRequestBase, Header[] herds, List<Integer> outTime) {
        // 设置Header等
        if (herds != null)
            httpRequestBase.setHeaders(herds);
        httpRequestBase.setConfig(getRequestConfig(true, outTime));
    }
    public static String handleUrl(String url) {
        url = url.replace("http://", "");
        int point = url.indexOf("/");
        return url.substring(0, point == -1 ? url.length() : point);
    }

    public static CloseableHttpClient createHttpClient(int maxTotal, int maxPerRoute, List<hostData> hosts) {
        ConnectionSocketFactory plainsf = PlainConnectionSocketFactory
                .getSocketFactory();
        LayeredConnectionSocketFactory sslsf = SSLConnectionSocketFactory
                .getSocketFactory();
        Registry<ConnectionSocketFactory> registry = RegistryBuilder
                .<ConnectionSocketFactory> create().register("http", plainsf)
                .register("https", sslsf).build();
        PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(
                registry);
        // 将最大连接数增加
        cm.setMaxTotal(maxTotal);
        // 将每个路由基础的连接增加
        cm.setDefaultMaxPerRoute(maxPerRoute);
//        if (hosts  == null || hosts.isEmpty()) {
//            cm.setMaxPerRoute(new HttpRoute(new HttpHost("some.default.host")), 50);
//        } else {
//        	hosts.forEach(i->cm.setMaxPerRoute(new HttpRoute(new HttpHost(i.getUrl(), i.getPort())), i.getMaxRoute()));
//        }
        // 将目标主机的最大连接数增加


        // 请求重试处理
        HttpRequestRetryHandler httpRequestRetryHandler = new HttpRequestRetryHandler() {
            public boolean retryRequest(IOException exception,
                                        int executionCount, HttpContext context) {
                if (executionCount >= 5) {// 如果已经重试了5次，就放弃
                    return false;
                }
                if (exception instanceof NoHttpResponseException) {// 如果服务器丢掉了连接，那么就重试
                    return true;
                }
                if (exception instanceof SSLHandshakeException) {// 不要重试SSL握手异常
                    return false;
                }
                if (exception instanceof InterruptedIOException) {// 超时
                    return false;
                }
                if (exception instanceof UnknownHostException) {// 目标服务器不可达
                    return false;
                }
                if (exception instanceof ConnectTimeoutException) {// 连接被拒绝
                    return false;
                }
                if (exception instanceof SSLException) {// SSL握手异常
                    return false;
                }

                HttpClientContext clientContext = HttpClientContext
                        .adapt(context);
                HttpRequest request = clientContext.getRequest();
                // 如果请求是幂等的，就再次尝试
                if (!(request instanceof HttpEntityEnclosingRequest)) {
                    return true;
                }
                return false;
            }
        };

        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(cm)
                .setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setRetryHandler(httpRequestRetryHandler).build();

        return httpClient;
    }

    public final static CloseableHttpClient getHttpClient(HttpWeb web, String url) {
        if ( ! httpclients.containsKey(web)) {
            synchronized (httpclients) {
                if ( ! httpclients.containsKey(web)) {
                    ArrayList<hostData> hostDatas = new ArrayList<>();
                    switch (web) {
                        case SUPER:
                            hostDatas.add(new hostData(handleUrl(APIRequest.API_DOMAIN), 80, 100));
                            httpclients.put(web, createHttpClient(200, 40, hostDatas));
                            break;

                        case PHA:
                        case TX:
                            hostDatas.add(new hostData(handleUrl(url), 80, 100));
                            httpclients.put(web, createHttpClient(200, 40, hostDatas));
                            break;

                        default:
                            httpclients.put(web, createHttpClient(200, 40, null));
                            break;
                    }
                }
            }
        }
        return httpclients.get(web);
    }
    private static void setPostParams(HttpPost httpost, Object params) {
        if (params instanceof Map) {
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            @SuppressWarnings("unchecked")
            Map<String, Object> _params = (Map<String, Object>) params;
            Set<String> keySet = _params.keySet();
            for (String key : keySet) {
                nvps.add(new BasicNameValuePair(key, _params.get(key).toString()));
            }
            try {
                httpost.setEntity(new UrlEncodedFormEntity(nvps, "UTF-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else if (params instanceof String){
            httpost.setEntity(new StringEntity((String) params, Consts.UTF_8));
        } else {
            throw new NullPointerException("Error type PostFiled, must be List<NameValuePair> or String.");
        }
    }
    /**
     * Post請求
     *
     * @param url
     * @return
     * @throws IOException
     */
    public static String post(HttpWeb web, String url, List<NameValuePair> params, Header[] herds, List<Integer> outTime) {
        HttpPost httppost = new HttpPost(url);
        config(httppost, herds, outTime);
        setPostParams(httppost, params);
        try (CloseableHttpResponse response = getHttpClient(web, url).execute(httppost,
                HttpClientContext.create())) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            entity = null;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
        return null;
    }
    public static String post(HttpWeb web, String url, String params, Header[] herds, List<Integer> outTime) {
        HttpPost httppost = new HttpPost(url);
        config(httppost, herds, outTime);
        setPostParams(httppost, params);
        try (CloseableHttpResponse response = getHttpClient(web, url).execute(httppost,
                HttpClientContext.create())) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            entity = null;
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httppost.releaseConnection();
        }
        return null;
    }
    public static String get(HttpWeb web, String url, Header[] herds, List<Integer> outTime) {
        HttpGet httpget = new HttpGet(url);
        config(httpget, herds, outTime);
        try (CloseableHttpResponse response = getHttpClient(web, url).execute(httpget,
                HttpClientContext.create())) {
            HttpEntity entity = response.getEntity();
            String result = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
            entity = null;
            return result;
        } catch (IllegalStateException | RequestAbortedException e ) {
            //
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            httpget.releaseConnection();
            httpget = null;
        }
        return null;
    }


}
