package com.heros.follow.source.errorlog;

public class LineChangeData {

	private String FollowID;
	private String Game;
	private String League;
	private String HomeTeam;
	private String AwayTeam;
	private String Type;
	private String Line;
	private String Before;
	private String After;
	
	public LineChangeData(String followID, String game, String league, String homeTeam, String awayTeam, String type,
                          String line, String before, String after) {
		FollowID = followID;
		Game = game;
		League = league;
		HomeTeam = homeTeam;
		AwayTeam = awayTeam;
		Type = type;
		Line = line;
		Before = before;
		After = after;
	}

	public String getGame() {
		return Game;
	}

	public void setGame(String game) {
		Game = game;
	}

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public String getFollowID() {
		return FollowID;
	}

	public void setFollowID(String followID) {
		FollowID = followID;
	}

	public String getLeague() {
		return League;
	}

	public void setLeague(String league) {
		League = league;
	}

	public String getHomeTeam() {
		return HomeTeam;
	}

	public void setHomeTeam(String homeTeam) {
		HomeTeam = homeTeam;
	}

	public String getAwayTeam() {
		return AwayTeam;
	}

	public void setAwayTeam(String awayTeam) {
		AwayTeam = awayTeam;
	}

	public String getLine() {
		return Line;
	}

	public void setLine(String line) {
		Line = line;
	}

	public String getBefore() {
		return Before;
	}

	public void setBefore(String before) {
		Before = before;
	}

	public String getAfter() {
		return After;
	}

	public void setAfter(String after) {
		After = after;
	}
	
	
}
