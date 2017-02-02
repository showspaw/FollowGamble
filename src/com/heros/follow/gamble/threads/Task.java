package com.heros.follow.gamble.threads;

/**
 * Created by root on 2017/1/19.
 */
public abstract class Task implements Runnable {
    private String name;
    private String sportType;
    private String gameType;

    public Task() {
    }

    public Task(String name, String sportType, String gameType) {
        this.name = name;
        this.sportType = sportType;
        this.gameType = gameType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSportType() {
        return sportType;
    }

    public void setSportType(String sportType) {
        this.sportType = sportType;
    }

    public String getGameType() {
        return gameType;
    }

    public void setGameType(String gameType) {
        this.gameType = gameType;
    }
}
