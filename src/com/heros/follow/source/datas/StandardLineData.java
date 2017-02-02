package com.heros.follow.source.datas;

/**
 * Created by root on 2017/1/17.
 */
public class StandardLineData extends LineData {
    private String InitHomeWinOdds = "0";
    private String InitDrawOdds = "0";
    private String InitAwayWinOdds = "0";
    private String HomeWinOdds = "0";
    private String DrawOdds = "0";
    private String AwayWinOdds = "0";
    private int LineNo;
    private String ModifyTime;

    public StandardLineData() {
    }

    public String getInitHomeWinOdds() {
        return this.InitHomeWinOdds;
    }

    public void setInitHomeWinOdds(String initHomeWinOdds) {
        this.InitHomeWinOdds = initHomeWinOdds;
    }

    public String getInitDrawOdds() {
        return this.InitDrawOdds;
    }

    public void setInitDrawOdds(String initDrawOdds) {
        this.InitDrawOdds = initDrawOdds;
    }

    public String getInitAwayWinOdds() {
        return this.InitAwayWinOdds;
    }

    public void setInitAwayWinOdds(String initAwayWinOdds) {
        this.InitAwayWinOdds = initAwayWinOdds;
    }

    public String getHomeWinOdds() {
        return this.HomeWinOdds;
    }

    public void setHomeWinOdds(String homeWinOdds) {
        this.HomeWinOdds = homeWinOdds;
    }

    public String getDrawOdds() {
        return this.DrawOdds;
    }

    public void setDrawOdds(String drawOdds) {
        this.DrawOdds = drawOdds;
    }

    public String getAwayWinOdds() {
        return this.AwayWinOdds;
    }

    public void setAwayWinOdds(String awayWinOdds) {
        this.AwayWinOdds = awayWinOdds;
    }

    public int getLineNo() {
        return this.LineNo;
    }

    public void setLineNo(int lineNo) {
        this.LineNo = lineNo;
    }

    public String getModifyTime() {
        return this.ModifyTime;
    }

    public void setModifyTime(String modifyTime) {
        this.ModifyTime = modifyTime;
    }
}
