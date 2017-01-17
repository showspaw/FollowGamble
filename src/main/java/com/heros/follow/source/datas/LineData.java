package com.heros.follow.source.datas;

import com.heros.follow.utils.GenericEnum.ParserType;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by root on 2017/1/17.
 */
public class LineData {
    private boolean initialization;
    private long RecordID;
    private boolean LineClose;
    private ParserType lastUpdateParser;
    private long lastUpdateTime;
    private ReadWriteLock LineDataLock = new ReentrantReadWriteLock();

    public LineData() {
    }

    public ParserType getLastUpdateParser() {
        return this.lastUpdateParser;
    }

    public void setLastUpdateParser(ParserType lastUpdateParser) {
        this.lastUpdateParser = lastUpdateParser;
    }

    public long getLastUpdateTime() {
        return this.lastUpdateTime;
    }

    public void setLastUpdateTime(long lastUpdateTime) {
        this.lastUpdateTime = lastUpdateTime;
    }

    public long getRecordID() {
        return this.RecordID;
    }

    public void setRecordID(long recordID) {
        this.RecordID = recordID;
    }

    public boolean isInitialization() {
        return this.initialization;
    }

    public void setInitialization(boolean initialization) {
        this.initialization = initialization;
    }

    public boolean isLineClose() {
        return this.LineClose;
    }

    public void setLineClose(boolean lineClose) {
        this.LineClose = lineClose;
    }

    public ReadWriteLock getLineDataLock() {
        return this.LineDataLock;
    }

    public void setLineDataLock(ReadWriteLock lineDataLock) {
        this.LineDataLock = lineDataLock;
    }
}
