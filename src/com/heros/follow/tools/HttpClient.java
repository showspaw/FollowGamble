package com.heros.follow.tools;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.conn.SystemDefaultRoutePlanner;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.net.ProxySelector;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.util.List;

public class HttpClient {

	private CloseableHttpClient httpclient;

	public HttpClient() {
		openConn();
	}

	//post請求
	public Document httpPost(String URL, List<NameValuePair> PostFiled, Header[] herds, List<Integer> outTime, Boolean releaseConnection) {
		if (outTime == null || outTime.isEmpty())
			outTime = Arrays.asList(30000, 30000, 30000);
		Document docHTML = null;
		HttpPost httpost = null;
			try {
			httpost = new HttpPost(URL);
			httpost.setEntity(new UrlEncodedFormEntity(PostFiled, Consts.UTF_8));
			httpost.setHeaders(herds);
			RequestConfig requestConfig = RequestConfig.custom()
				    .setSocketTimeout(outTime.get(0))
				    .setConnectTimeout(outTime.get(1))
				    .setConnectionRequestTimeout(outTime.get(2))
				    .build();
			httpost.setConfig(requestConfig);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status <= 302) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity,"UTF-8") : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			String responseBody = this.httpclient.execute(httpost, responseHandler);
			docHTML = Jsoup.parse(responseBody);
		} catch (Exception e) {
			e.printStackTrace();
			docHTML = null;
		} finally {
			if (httpost != null && releaseConnection) {
				httpost.releaseConnection();
			}
		}
		return docHTML;
	}

	public Document httpPost(String URL, List<NameValuePair> PostFiled, Header[] herds) {
		Document docHTML = null;
		HttpPost httpost = new HttpPost(URL);	//向取得的URL發送請求
		CloseableHttpResponse response = null;
		try {
			// 禁止自動跳轉
			RequestConfig requestConfigBuilder = RequestConfig.custom().setCircularRedirectsAllowed(true).build();
			httpost.setConfig(requestConfigBuilder);
			httpost.setEntity(new UrlEncodedFormEntity(PostFiled, Consts.UTF_8));
			httpost.setHeaders(herds);
			response = this.httpclient.execute(httpost);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status <= 302) {
				HttpEntity entity = response.getEntity();
				docHTML = Jsoup.parse(entity != null ? EntityUtils.toString(entity,"UTF-8") : null);
			} else {
				docHTML = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
			docHTML = null;
		} finally {
			httpost.releaseConnection();
			try {
				if (response!=null) {
					response.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return docHTML;
	}

	public String httpPostAsString(String URL, String PostFiled, Header[] herds, List<Integer> outTime, Boolean releaseConnection) {
		if (outTime == null || outTime.isEmpty())
			outTime = Arrays.asList(30000, 30000, 30000);
		HttpPost httpost = null;
		String responseBody = "";
		try {
			httpost = new HttpPost(URL);
			httpost.setEntity(new StringEntity(PostFiled, Consts.UTF_8));
			httpost.setHeaders(herds);
			RequestConfig requestConfig = RequestConfig.custom()
				    .setSocketTimeout(outTime.get(0))
				    .setConnectTimeout(outTime.get(1))
				    .setConnectionRequestTimeout(outTime.get(2))
				    .build();
			httpost.setConfig(requestConfig);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status <= 302) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity, Consts.UTF_8) : null;
					} else {
						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			responseBody = this.httpclient.execute(httpost, responseHandler);
		} catch (SocketTimeoutException e) {
			return "SocketTimeout";
		} catch (Exception e) {
		} finally {
			if (httpost != null && releaseConnection) {
				httpost.releaseConnection();
			}
		}
		return responseBody;
	}
	
	public Document httpGet(String URL, Header[] herds, List<Integer> outTime, Boolean releaseConnection) {
		if (outTime == null || outTime.isEmpty())
			outTime = Arrays.asList(30000, 30000, 30000);
		Document docHTML = null;
		HttpGet httpGet = null;
		httpGet = new HttpGet(URL);
		try {
			httpGet.setHeaders(herds);
			RequestConfig requestConfig = RequestConfig.custom()
				    .setSocketTimeout(outTime.get(0))
				    .setConnectTimeout(outTime.get(1))
				    .setConnectionRequestTimeout(outTime.get(2))
				    .build();
		    httpGet.setConfig(requestConfig);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status <= 302) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity,"UTF-8") : null;
					} else {
						Header[] headerls = response.getAllHeaders();
						for (Header header : headerls) {
							System.out.printf("HeaderKey : %s ,HeaderReason : %s", header.getName(), header.getValue());
						}
						return null;
					}
				}
			};
			String responseBody = this.httpclient.execute(httpGet, responseHandler);
			docHTML = Jsoup.parse(responseBody);
		} catch (Exception e) {
			docHTML = null;
		} finally {
			if (httpGet != null && releaseConnection)
				httpGet.releaseConnection();
		}
		return docHTML;
	}

	public String httpGetAsString(String URL, Header[] herds, List<Integer> outTime, Boolean releaseConnection) {
		if (outTime == null || outTime.isEmpty())
			outTime = Arrays.asList(30000, 30000, 30000);
		HttpGet httpGet = null;
		String responseBody = null;
		try {
			httpGet = new HttpGet(URL);
			httpGet.setHeaders(herds);
			RequestConfig requestConfig = RequestConfig.custom()
				    .setSocketTimeout(outTime.get(0))
				    .setConnectTimeout(outTime.get(1))
				    .setConnectionRequestTimeout(outTime.get(2))
				    .build();
		    httpGet.setConfig(requestConfig);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status <= 302) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity,"UTF-8") : null;
					} else {
						Header[] headerls = response.getAllHeaders();
						for (Header header : headerls) {
							System.out.printf("HeaderKey : %s ,HeaderReason : %s", header.getName(), header.getValue());
						}
						return null;
					}
				}
			};
			responseBody = httpclient.execute(httpGet, responseHandler);
		} catch (SocketTimeoutException e) {
			return null;
		} catch (Exception e) {
			return null;
		} finally {
			if (httpGet != null && releaseConnection)
				httpGet.releaseConnection();
		}
		return responseBody;
	}

	public String httpGetAsString(String URL, Boolean releaseConnection) {
		HttpGet httpGet = null;
		String responseBody = null;
		try {
			httpGet = new HttpGet(URL);
			ResponseHandler<String> responseHandler = new ResponseHandler<String>() {
				public String handleResponse(final HttpResponse response) throws ClientProtocolException, IOException {
					int status = response.getStatusLine().getStatusCode();
					if (status >= 200 && status <= 302) {
						HttpEntity entity = response.getEntity();
						return entity != null ? EntityUtils.toString(entity,"UTF-8") : null;
					} else {
						Header[] headerls = response.getAllHeaders();
						for (Header header : headerls) {
							System.out.printf("HeaderKey : %s ,HeaderReason : %s", header.getName(), header.getValue());
						}
						return null;
//						throw new ClientProtocolException("Unexpected response status: " + status);
					}
				}
			};
			responseBody = httpclient.execute(httpGet, responseHandler);
		} catch (SocketTimeoutException e) {
			return "SocketTimeout";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (httpGet != null && releaseConnection)
				httpGet.releaseConnection();
		}
		return responseBody;
	}



	public void openConn(){
		PoolingHttpClientConnectionManager ms = new PoolingHttpClientConnectionManager();
		ms.setValidateAfterInactivity(30000);
		this.httpclient = HttpClients.custom()
				.setRoutePlanner(new SystemDefaultRoutePlanner(ProxySelector.getDefault()))
				.setRedirectStrategy(new LaxRedirectStrategy())
		        .setConnectionManager(ms)
		        .build();
//		this.httpclient = HttpClientBuilder.create().disableRedirectHandling().setRedirectStrategy(new LaxRedirectStrategy()).setConnectionManager(ms).build();
	}
	public void closeConn(){
		try {
			this.httpclient.close();
			this.httpclient = null;
		} catch (IOException e) {
		}
	}

	public void resetHttpclient() {
		closeConn();
		openConn();
	}

	public CloseableHttpClient getHttpclient() {
		return httpclient;
	}

	public void setHttpclient(CloseableHttpClient httpclient) {
		this.httpclient = httpclient;
	}

	//主機端
	public final Header[] HOSTHEADERS = {
		 new BasicHeader("Accept-Encoding", "gzip,deflate,sdch")
	    ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,ja;q=0.2")
	    ,new BasicHeader("Accep", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
	    ,new BasicHeader("Connection", "keep-alive")
	    ,new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
	};
	//登入端
	public final Header[] LOGINHEADERS = {
		new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		,new BasicHeader("Accept-Encoding", "gzip,deflate")
	    ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,ja;q=0.2")
		,new BasicHeader("Cache-Control", "max-age=0")
	    ,new BasicHeader("Connection", "keep-alive")
		,new BasicHeader("Content-Type", "application/x-www-form-urlencoded")
	    ,new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
	};
	//數據請求路徑
	public final Header[] DATAREQUESTPATH = {
		new BasicHeader("Accept", "application/json, text/javascript, */*; q=0.01")
		,new BasicHeader("Accept-Encoding", "gzip,deflate,sdch")
	    ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,ja;q=0.2")
		,new BasicHeader("X-Requested-With", "XMLHttpRequest")
	    ,new BasicHeader("Connection", "keep-alive")
		,new BasicHeader("Content-Type", "application/json; charset=utf8")
	    ,new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
	};

	public final Header[] ASLOGINHEADER = {
		new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
		,new BasicHeader("Accept-Encoding", "gzip, deflate, sdch")
	    ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4")
		,new BasicHeader("Cache-Control", "max-age=0")
	    ,new BasicHeader("Connection", "keep-alive")
	    ,new BasicHeader("Content-Type", "application/x-www-form-urlencoded")
	    ,new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.101 Safari/537.36")
	};

	//數據請求路徑
	public final Header[] TXMEMERY_WEBSITE = {
		new BasicHeader("Accept", "*/*")
		,new BasicHeader("Accept-Encoding", "gzip, deflate")
	    ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4")
		,new BasicHeader("X-Requested-With", "XMLHttpRequest")
	    ,new BasicHeader("Connection", "keep-alive")
		,new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
	    ,new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
	};
	//test
	public final Header[] TXMEMERY_LOGIN = {
			new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
			,new BasicHeader("Accept-Encoding", "gzip, deflate")
		    ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4")
		    ,new BasicHeader("Cache-Control", "max-age=0")
			,new BasicHeader("X-Requested-With", "XMLHttpRequest")
		    ,new BasicHeader("Connection", "keep-alive")
		    ,new BasicHeader("Host", "www.as8889.com")
		    ,new BasicHeader("Origin", "http://www.as8889.com")
		    ,new BasicHeader("Referer", "http://www.as8889.com/app/member/?langx=")
			,new BasicHeader("Upgrade-Insecure-Requests", "1")
		    ,new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
		};
}
