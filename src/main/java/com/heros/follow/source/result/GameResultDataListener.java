package com.heros.follow.source.result;
//GameResultDataListener.java - 只有法老跟盤使用,法老場次ID需要額外一次的http request,此request用Thread發出,讀取玩call method回填獲得的ID
public interface GameResultDataListener {
	public void setAllID(String ID);
}
