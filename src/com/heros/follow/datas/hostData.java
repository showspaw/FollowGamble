package com.heros.follow.datas;

public class hostData {
	private String url;
	private int port;
	private int maxRoute;
	
	public hostData(String url, int port, int maxRoute) {
		this.url = url;
		this.port = port;
		this.maxRoute = maxRoute;
	}
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public int getMaxRoute() {
		return maxRoute;
	}
	public void setMaxRoute(int maxRoute) {
		this.maxRoute = maxRoute;
	}
}
