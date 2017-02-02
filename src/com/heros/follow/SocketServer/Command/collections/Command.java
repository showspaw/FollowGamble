package com.heros.follow.SocketServer.Command.collections;

/**
 * Created by root on 2017/1/26.
 */
public interface Command {

    /**
     * 拋接資料分類
     */
    String phpClientPrefix = "2";

    /**
     * 日誌紀錄分類
     */
    String logPrefix = "6";
    String commandClass = "61";

    /**
     * 管理分類
     */
    String adminPrefix = "7";
    String threadManagerClass = "71";
    String dataManagerClass = "72";

    /**
     * 查詢指令分類
     */
    String instructionPrefix = "0";

    void execute(String request);
}
