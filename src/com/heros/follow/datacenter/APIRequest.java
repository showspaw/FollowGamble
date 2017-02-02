package com.heros.follow.datacenter;

import org.apache.http.*;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.SocketException;
import java.net.URLDecoder;
import java.util.List;

//APIRequest.java - 執行http request
public class APIRequest {
//	public final static String API_DOMAIN = "http://api.hr9999.net"; // 正式
	public final static String API_DOMAIN = "http://hkapi.king588.net";	// 測試
	
	/*hero*/
	// 英雄站台設定
	public static final String SITE_SET = API_DOMAIN + "/follow/API_SiteSet.php";
	// 打開停押
	public static final String LIVE_OPEN = API_DOMAIN + "/index.php/follow/Follow_api/OpenLive";
//	public static final String LIVE_OPEN = API_DOMAIN + "/index.php/Followapi/OpenLive";
	
	// 發出BAN_LIST
	public static final String BAN_API = API_DOMAIN + "/index.php/follow/Follow_api/UpdateBan";
//	public static final String BAN_API = API_DOMAIN + "/index.php/Followapi/UpdateBan";
	
	//接收初始資料
	public static final String DATA_API = API_DOMAIN + "/index.php/Followapi/GetFollowData";
	
	//接收賽事(event)的各種資料
	public static final String API = API_DOMAIN + "/follow/API.php";
	public static final String MLBAPI = API_DOMAIN + "/listen/API_RID.php";
	//http://api.hr9999.net/listen/API_RID.php
	
	//DonBest per-match matchup_score 比分
	public static final String API_SCORE = API_DOMAIN + "/follow/API_Score.php";
	
	//DonBest inplay period_score 比分
	public static final String API_PERIODSCORE = API_DOMAIN + "/follow/API_PeriodScore.php";
	
	//測試送出比分
	public static final String API_SCORE_DEV = API_DOMAIN + "/follow/DEV/API_Score.php";
	
	//跟打取得跟隨對象注單
	public static final String API_CRITICALHIT = API_DOMAIN + "/follow/API_CriticalHit.php";
	
	//設定FollowID的確認機制
	public static final String API_FOLLOW = API_DOMAIN + "/follow/API_Follow.php";
	
	// 定時送確認訊號的api
	public static final String API_CHECK = API_DOMAIN + "/follow/CheckStatus.php";
	
	//取得Mapping資料
	public static final String API_NAMETRANSFORM = API_DOMAIN + "/follow/NameTransForm.php";
	
	//Local test server
	public static final String API_TEST = "http://localhost:8887/API.php";
	
	//關閉盤口
	public static final String API_EVENTCLOSE = API_DOMAIN + "/follow/API_EventClose.php";
	
	//打開盤口
	public static final String API_EVENTOPEN = API_DOMAIN + "/follow/API_EventOpen.php";
	
	//新增Event
	public static final String API_NEWEVENT = API_DOMAIN + "/follow/API_NewEvent.php";
	
	//新增Game
	public static final String API_NEWGAME = API_DOMAIN + "/follow/API_NewGame.php";
	
	//測試新增Event
	public static final String API_NEWEVENT_DEV = API_DOMAIN + "/follow/DEV/API_NewEvent.php";
	//測試新增Game
	public static final String API_NEWGAME_DEV = API_DOMAIN + "/follow/DEV/API_NewGame.php";

	//連結賽事
	public static final String API_LINKEVENT = API_DOMAIN + "/follow/API_LinkEvent.php";
	
	//送出比分
	public static final String API_STOPUNLOCK = API_DOMAIN + "/follow/API_StopUnlock.php";
	
	// MLB State 2016-6-22 se
	public static final String API_MLB_STATE = API_DOMAIN + "/follow/API_MLBState.php";
	
	/*
	 * 球探網api
	 */
	// 取得聯盟code
	public static final String API_LEAGUETRANSCODE = API_DOMAIN + "/index.php/Followapi/getLeagueTransCode";
	// 取得球隊code
	public static final String API_TEAMTRANSCODE = API_DOMAIN + "/index.php/Followapi/getTeamTransCode";
	
	/*fb888*/
	//應用程式版本確認
	public static final String API_VERSION = "http://app.fb888.net/API_version.php";
	//Donbset資料寫入
	public static final String API_DONBEST = "http://app.fb888.net/API_Donset.php";
	//取得Mapping資料
	public static final String API_MAPPING = "http://app.fb888.net/API_DonbestMapping.php";
	//記錄跟盤的運作時間
	public static final String API_HEARTBEAT = "http://ctrl.fb888.net/API.php";
	//確認HeartBeat時間點是否過期
	public static final String API_HEARTBEATCHECK = "http://app.fb888.net/API_HeartBeatCheck.php";
	//確認HeartBeat時間點是否過期
	public static final String API_HISTORYSCORE = "http://app.fb888.net/API_HistoryScore.php";
	// 2016-06-22 se, mlb state
	public static final String API_DONBEST_MLBSTATE = "http://app.fb888.net/API_MlbState.php";
	// error log set
	public static final String API_LOGERROR = "http://app.fb888.net/API_LogError.php";
	// Live Stop log set
	public static final String API_LIVESTOPLOG = "http://app.fb888.net/API_SetLiveStopLog.php";
	// Line Change log set
	public static final String API_LINGCHANGELOG = "http://app.fb888.net/API_LineChangeLog.php";
	
	// 新增PC資料
	public static String API_SendPCData = API_DOMAIN + "/follow/PCDataAPI.php";
	// 查詢mac資料
	public static String API_RequestMac = API_DOMAIN + "/follow/MacDataSelect.php";
	// 新增mac資料
	public static String API_SendMac = "";

	private static CloseableHttpClient httpclient = HttpClients.createDefault();
	
	
	public static int makeRequest(String path, String holder)
	{
	    //instantiates httpclient to make request
		// 改用field，因為每次都重新new HttpClient會delay0.2~0.3秒
//		CloseableHttpClient httpclient = HttpClients.createDefault();

	    //url with the post data
	    HttpPost httpost = new HttpPost(path);

	    //passes the results to a string builder/entity
	    StringEntity se = new StringEntity(holder, "UTF-8");

	    //sets the post request as the resulting string
	    httpost.setEntity(se);
	    //sets a request header so the page receving the request
	    //will know what to do with it
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");

	    //Handles what is returned from the page
	    CloseableHttpResponse responseHandler = null;
	    int status = 404;
	    try {
		    responseHandler = httpclient.execute(httpost);
		    HttpEntity httpEntity = responseHandler.getEntity();
		    if(httpEntity != null)
		    {
		    	String msString = EntityUtils.toString(httpEntity, "UTF-8");
		    	if (msString.length() > 0) {
		    		System.out.println(msString);
		    	}
		    }
		    status = responseHandler.getStatusLine().getStatusCode();
	    }catch (Exception e) {
	    	e.printStackTrace();
			resetConn();
		} finally{
			// 一定要釋放，不然就會像開瀏覽器開很多分頁一樣，等回收就太慢了
	    	httpost.releaseConnection();
	    }
	    return status;
	}
	
	public static String makeRequestNormal(String path, String holder)
	{
	    //instantiates httpclient to make request
		// 改用field，因為每次都重新new HttpClient會delay0.2~0.3秒
//		CloseableHttpClient httpclient = HttpClients.createDefault();

	    //url with the post data
	    HttpPost httpost = new HttpPost(path);
	    
	    //Handles what is returned from the page
	    CloseableHttpResponse responseHandler = null;
	    String ResponseBody = "";
	    
	    try {
		    //passes the results to a string builder/entity
	    	StringEntity se = new StringEntity(holder, "UTF-8");

		    //sets the post request as the resulting string
		    httpost.setEntity(se);
		    
		    responseHandler = httpclient.execute(httpost);
//		    ResponseBody = URLDecoder.decode(EntityUtils.toString(responseHandler.getEntity()), "UTF-8");
		    ResponseBody = EntityUtils.toString(responseHandler.getEntity());
	    }catch (Exception e) {
	    	e.printStackTrace();
			resetConn();
	    }finally{
	    	httpost.releaseConnection();
	    }
	    return ResponseBody;
	}
	
	public static String makeRequestData(String path, String holder)
	{
	    //instantiates httpclient to make request
		// 改用field，因為每次都重新new HttpClient會delay0.2~0.3秒
//		CloseableHttpClient httpclient = HttpClients.createDefault();

	    //url with the post data
	    HttpPost httpost = new HttpPost(path);

	    //passes the results to a string builder/entity
	    StringEntity se = new StringEntity(holder, "UTF-8");

	    //sets the post request as the resulting string
	    httpost.setEntity(se);
	    //sets a request header so the page receving the request
	    //will know what to do with it
	    httpost.setHeader("Accept", "application/json");
	    httpost.setHeader("Content-type", "application/json");

	    //Handles what is returned from the page
	    CloseableHttpResponse responseHandler = null;
	    String ResponseBody = "";
	    try {
		    responseHandler = httpclient.execute(httpost);
		    ResponseBody = URLDecoder.decode(EntityUtils.toString(responseHandler.getEntity()), "UTF-8");
	    } catch(Exception e) {
	    	e.printStackTrace();
			resetConn();
	    } finally {
	    	httpost.releaseConnection();
	    }
	    return ResponseBody;
	}
	public static String makeRequestAsString(String URL,Header[] headers){
		String ResponseBody = null;
		HttpGet httpGet = new HttpGet(URL);
		ResponseHandler<String> responseHandler = null;
		// 改用field，因為每次都重新new HttpClient會delay0.2~0.3秒
//		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			responseHandler = new ResponseHandler<String>() {
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
			ResponseBody = httpclient.execute(httpGet, responseHandler);

		} catch(SocketException e) {
			System.out.println("timeout");
		} catch (Exception e) {
			e.printStackTrace();
			resetConn();
			ResponseBody = null;
		} finally{
			httpGet.releaseConnection();
		}
		return ResponseBody;
	}
	public static String makeRequestAsString(String URL, List<NameValuePair> PostFiled, Header[] headers){
		String ResponseBody = null;
		// Creating an instance of HttpPost.
		HttpPost httpost = new HttpPost(URL);
		CloseableHttpResponse response = null;
		// 改用field，因為每次都重新new HttpClient會delay0.2~0.3秒
//		CloseableHttpClient httpclient = HttpClients.createDefault();
		try {
			httpost.setEntity(new UrlEncodedFormEntity(PostFiled, Consts.UTF_8));
			// Executing the request.
			httpost.setHeaders(headers);
			response = httpclient.execute(httpost);
			int status = response.getStatusLine().getStatusCode();
			if (status >= 200 && status <= 302) {
				// Do the needful with entity.
				HttpEntity entity = response.getEntity();
				ResponseBody = entity != null ? EntityUtils.toString(entity,"UTF-8") : null;
			} else {
				ResponseBody = null;
				throw new ClientProtocolException("Unexpected response status: " + status);
			}
		} catch (Exception e) {
			e.printStackTrace();
			resetConn();
			ResponseBody = null;
		} finally {
			httpost.releaseConnection();
		}
		return ResponseBody;
	}
	
	// 重製HttpClient
	private static void resetConn() {
		if (httpclient != null) {
			try {
				httpclient.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		httpclient = HttpClients.createDefault();
	}
}
