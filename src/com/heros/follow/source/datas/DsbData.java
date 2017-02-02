package com.heros.follow.source.datas;

/**
 * Created by root on 2017/1/17.
 */
public class DsbData extends LineData {
    private String InitLine = "0";
    private String InitDOdds = "0";
    private String InitSOdds = "0";
    private String Line = "0";
    private String DOdds = "0";
    private String SOdds = "0";
    private int LineNo;
    private String ModifyTime;

    public DsbData() {
    }

    public String getInitLine() {
        return this.InitLine;
    }

    public void setInitLine(String initLine) {
        this.InitLine = initLine;
    }

    public String getInitDOdds() {
        return this.InitDOdds;
    }

    public void setInitDOdds(String initDOdds) {
        this.InitDOdds = initDOdds;
    }

    public String getInitSOdds() {
        return this.InitSOdds;
    }

    public void setInitSOdds(String initSOdds) {
        this.InitSOdds = initSOdds;
    }

    public String getLine() {
        return this.Line;
    }

    public void setLine(String line) {
        this.Line = line;
    }

    public String getDOdds() {
        return this.DOdds;
    }

    public void setDOdds(String dOdds) {
        this.DOdds = dOdds;
    }

    public String getSOdds() {
        return this.SOdds;
    }

    public void setSOdds(String sOdds) {
        this.SOdds = sOdds;
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
