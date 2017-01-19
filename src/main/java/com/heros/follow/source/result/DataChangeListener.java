package com.heros.follow.source.result;

/*package hero.follow.myinterface - 非DonBest賭場使用的interface{
	DataChangeListener.java - 與資料中心訂閱的interface*/
public interface DataChangeListener {
	public void fireDataChange(GameResult g);
	public void fireDataChange(GameResult g, String who);
}
