package com.heros.follow.source.datas;

import com.heros.follow.utils.GenericEnum.*;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 2017/1/17.
 */
public class MainLineData {
    private Map<Integer, LineData> LineMap = new ConcurrentHashMap();
    private String GameID;
    private String CID;
    private LineType lineType;

    public MainLineData(String gameID, String Cid, LineType lineType) {
        this.GameID = gameID;
        this.CID = Cid;
        this.lineType = lineType;
    }

    public String getGameID() {
        return this.GameID;
    }

    public void setGameID(String gameID) {
        this.GameID = gameID;
    }

    public String getCID() {
        return this.CID;
    }

    public void setCID(String cID) {
        this.CID = cID;
    }

    public Map<Integer, LineData> getLineMap() {
        return this.LineMap;
    }

    public void setLineMap(Map<Integer, LineData> lineMap) {
        this.LineMap = lineMap;
    }

    public LineType getLineType() {
        return this.lineType;
    }

    public void setLineType(LineType lineType) {
        this.lineType = lineType;
    }
}
