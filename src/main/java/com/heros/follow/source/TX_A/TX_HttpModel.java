package com.heros.follow.source.TX_A;

import com.heros.follow.source.BASE.Engine;
import com.heros.follow.tools.HttpClientUtils;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 2017/1/16.
 */
public class TX_HttpModel {
    public static final String LOGINPATH="http://ag.td111.net/manage/Page/Login/CommonLogin/Login.aspx";
    public static final Header[] DATAREQUESTPATH2 = new Header[]{new BasicHeader("Accept", "application/json, text/javascript, */*; q=0.01"), new BasicHeader("Accept-Encoding", "gzip, deflate"), new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.5,en;q=0.3"), new BasicHeader("X-Requested-With", "XMLHttpRequest"), new BasicHeader("Connection", "keep-alive"), new BasicHeader("Content-Type", "application/json; charset=utf8"), new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:45.0) Gecko/20100101 Firefox/45.0")};
    //主機端
    public static final Header[] HOSTHEADERS = {
            new BasicHeader("Accept-Encoding", "gzip,deflate,sdch")
            ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,ja;q=0.2")
            ,new BasicHeader("Accep", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            ,new BasicHeader("Connection", "keep-alive")
            ,new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
    };
    //登入端
    public static final Header[] LOGINHEADERS = {
            new BasicHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8")
            ,new BasicHeader("Accept-Encoding", "gzip,deflate")
            ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,ja;q=0.2")
            ,new BasicHeader("Cache-Control", "max-age=0")
            ,new BasicHeader("Connection", "keep-alive")
            ,new BasicHeader("Content-Type", "application/x-www-form-urlencoded")
            ,new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.111 Safari/537.36")
    };

    public Engine.LoginStatus login(String ac, String pw) {
        String result  = HttpClientUtils.get(HttpClientUtils.HttpWeb.TX,TX_HttpModel.LOGINPATH,TX_HttpModel.HOSTHEADERS, null);
        if (result == null) {
            return Engine.LoginStatus.FAILED;
        }
        Document docHTML = Jsoup.parse(result);
        //登入頁面指定的html標籤元素
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("__LASTFOCUS", docHTML.select("input[name=__LASTFOCUS]").attr("value")));	//最後焦點
        nvps.add(new BasicNameValuePair("__EVENTTARGET", docHTML.select("input[name=__EVENTTARGET]").attr("value")));	//事件目標
        nvps.add(new BasicNameValuePair("__EVENTARGUMENT", docHTML.select("input[name=__EVENTARGUMENT]").attr("value")));
        nvps.add(new BasicNameValuePair("__EVENTVALIDATION", docHTML.select("input[name=__EVENTVALIDATION]").attr("value")));
        nvps.add(new BasicNameValuePair("__VIEWSTATE", docHTML.select("input[name=__VIEWSTATE]").attr("value")));	//view狀態
        nvps.add(new BasicNameValuePair("__VIEWSTATEGENERATOR", docHTML.select("input[name=__VIEWSTATEGENERATOR]").attr("value")));	//view狀態生成
        nvps.add(new BasicNameValuePair("txtUsername", ac));	//使用者
        nvps.add(new BasicNameValuePair("txtPassword", pw));	//密碼
        nvps.add(new BasicNameValuePair("btnLogin", docHTML.select("#btnLogin").attr("value")));	//密碼
        nvps.add(new BasicNameValuePair("accounts", docHTML.select("#accounts").attr("value")));	//密碼
        nvps.add(new BasicNameValuePair("ProblemType", docHTML.select("#ProblemType > option[selected]").attr("value")));	//密碼
        nvps.add(new BasicNameValuePair("Language", docHTML.select("#Language > option[selected]").attr("value")));	//密碼
        nvps.add(new BasicNameValuePair("accounts", docHTML.select("#phone").attr("value")));	//密碼

        result = HttpClientUtils.post(HttpClientUtils.HttpWeb.TX, TX_HttpModel.LOGINPATH, nvps, TX_HttpModel.LOGINHEADERS, null);
        try {
            if (result == null || Jsoup.parse(result).select("body[id=bodyMian]").size() == 0) {	//如果框架尺寸等於0
                return Engine.LoginStatus.FAILED;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Engine.LoginStatus.FAILED;
        }

        return Engine.LoginStatus.SUCCESS;
    }

    public Engine.LoginStatus relogin(String ac, String pw) {
        try {
            HttpClientUtils.resetClient(HttpClientUtils.HttpWeb.TX);
            if (login(ac, pw)== Engine.LoginStatus.SUCCESS) {
                return Engine.LoginStatus.SUCCESS;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Engine.LoginStatus.FAILED;
    }
}
