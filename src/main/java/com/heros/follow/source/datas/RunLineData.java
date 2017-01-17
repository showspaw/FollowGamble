package com.heros.follow.source.datas;

/**
 * Created by root on 2017/1/17.
 */
public class RunLineData extends LineData {
    private String InitLine = "0";
    private String HomeInitOdds = "0";
    private String AwayInitOdds = "0";
    private String Line = "0";
    private String HomeOdds = "0";
    private String AwayOdds = "0";
    private boolean CloseLine;
    private boolean InPlay;
    private int LineNum;
    private int LineNo;
    private String ModifyTime;

    public RunLineData() {
    }

    public String getInitLine() {
        return this.InitLine;
    }

    public void setInitLine(String initLine) {
        this.InitLine = initLine;
    }

    public String getHomeInitOdds() {
        return this.HomeInitOdds;
    }

    public void setHomeInitOdds(String homeInitOdds) {
        this.HomeInitOdds = homeInitOdds;
    }

    public String getAwayInitOdds() {
        return this.AwayInitOdds;
    }

    public void setAwayInitOdds(String awayInitOdds) {
        this.AwayInitOdds = awayInitOdds;
    }

    public String getLine() {
        return this.Line;
    }

    public void setLine(String line) {
        this.Line = line;
    }

    public String getHomeOdds() {
        return this.HomeOdds;
    }

    public void setHomeOdds(String homeOdds) {
        this.HomeOdds = homeOdds;
    }

    public String getAwayOdds() {
        return this.AwayOdds;
    }

    public void setAwayOdds(String awayOdds) {
        this.AwayOdds = awayOdds;
    }

    public boolean isCloseLine() {
        return this.CloseLine;
    }

    public void setCloseLine(boolean closeLine) {
        this.CloseLine = closeLine;
    }

    public boolean isInPlay() {
        return this.InPlay;
    }

    public void setInPlay(boolean inPlay) {
        this.InPlay = inPlay;
    }

    public int getLineNum() {
        return this.LineNum;
    }

    public void setLineNum(int lineNum) {
        this.LineNum = lineNum;
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
