package com.heros.follow.source.PHA;

import com.google.gson.JsonObject;
import com.heros.follow.source.BASE.Engine.*;
import com.heros.follow.tools.HttpClientUtils;
import com.heros.follow.utils.HandleJSONData;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.List;

//PHA_HttpModel.java http model
// 處理登入動作
public class PHA_HttpModel {
	public  enum  BallClass{
		台棒("1","CPBL","bb"),
		日棒("2","JPB","by"),
		韓棒("3","KBO","hb"),
		美棒("4","MLB","bj"),
		美籃("5","NBA","lq"),
		冰球("6","NHL","bq"),
		足球("7","Soccer","zq"),
		美足("8","MLS","mz");
		private String gameId;
		private String heroTableName;
		private String gameType;
		private BallClass(String _gameId,String _heroTableName,String _gameType){
			gameId = _gameId;
			heroTableName = _heroTableName;
			gameType = _gameType;
		}

		public String getGameId() {
			return gameId;
		}

		public String getGameType() {
			return gameType;
		}

		public String getHeroTableName() {
			return heroTableName;
		}
	}
	public static final String LOGINPAGE="http://www.iwin168.us";
	public static final String LOGINPATH="http://www.iwin168.us/auth/login";
	private List<NameValuePair> DATANVPS;
	public static final Header[] DATAREQUESTPATH = {
		new BasicHeader("Accept", "text/html, */*; q=0.01")
		,new BasicHeader("Accept-Encoding", "gzip, deflate")
	    ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4,ja;q=0.2")
		,new BasicHeader("X-Requested-With", "XMLHttpRequest")
	    ,new BasicHeader("Connection", "keep-alive")
		,new BasicHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
	    ,new BasicHeader("User-Agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_10_1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.71 Safari/537.36")
	};
	public static final Header[] LOGINHEADERS = {
	    new BasicHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8")
	    ,new BasicHeader("Accept-Encoding", "gzip,deflate,sdch")
	    ,new BasicHeader("Accept-Language", "zh-TW,zh;q=0.8,en-US;q=0.6,en;q=0.4")
	    ,new BasicHeader("X-Requested-With", "XMLHttpRequest")
	    ,new BasicHeader("Accep", "*/*")
	    ,new BasicHeader("Connection", "keep-alive")
	    ,new BasicHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36")
	};
	private String Url = "";
	private String GameID = "";
	private int state = 3;
	protected PropertyChangeSupport propertyChangeSupport;

	PHA_HttpModel() {
		DATANVPS = new ArrayList<NameValuePair>();
		DATANVPS.add(new BasicNameValuePair("info", "0"));
		DATANVPS.add(new BasicNameValuePair("f", "1"));
		this.propertyChangeSupport = new PropertyChangeSupport(this);
	}

	public String getGameID() {
		return GameID;
	}

	public void setGameID(String gameID) {
		GameID = gameID;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

	public int getState() {
		return state;
	}

	public void setState(int state) {
		int oldstate = this.state;
		this.state = state;
		this.propertyChangeSupport.firePropertyChange("HttpModelState",oldstate, this.state);
	}

	public List<NameValuePair> getDATANVPS() {
		return DATANVPS;
	}

	public LoginStatus login(String ac,String pw) {
		try {
			Document docHTML = HttpClientUtils.httpGetAsDom(HttpClientUtils.HttpWeb.PHA, LOGINPAGE, null, null);
			if (docHTML == null) {
				this.setState(0);
				return LoginStatus.FAILED;
			}

			if(docHTML.select("div[id=dashboard_content]").size() > 0){
				this.setState(1);
				return LoginStatus.SUCCESS;
			}

			Elements element = docHTML.select("body");
			try {
				if ( ! element.isEmpty() && element.get(0).id().equals("fix")) {
					this.setState(3);
					return LoginStatus.MAINTAIN;
				}
			} catch (Exception e) {
				//
			}

			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			nvps.add(new BasicNameValuePair("username", ac));
			nvps.add(new BasicNameValuePair("password", pw));
			nvps.add(new BasicNameValuePair("agentClass", "2"));
			nvps.add(new BasicNameValuePair("uuid", docHTML.select("form[id=login]").attr("data-uuid")));
			docHTML = HttpClientUtils.httpPostAsDom(HttpClientUtils.HttpWeb.PHA, LOGINPATH, nvps, LOGINHEADERS, null);
			if (docHTML == null || ! ((JsonObject) HandleJSONData.getElement(docHTML.text())).get("status").getAsBoolean()) {
				this.setState(0);
				return LoginStatus.FAILED;
			}
			this.setState(1);
			return LoginStatus.SUCCESS;

		} catch (Exception e) {
			this.setState(0);
			return LoginStatus.FAILED;
		}
	}
}
