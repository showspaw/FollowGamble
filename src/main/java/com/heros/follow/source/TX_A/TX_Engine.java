package com.heros.follow.source.TX_A;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.heros.follow.source.BASE.Engine;
import com.heros.follow.source.BASE.WebSite;
import com.heros.follow.source.result.GameResult;
import com.heros.follow.tools.HttpClientUtils;
import com.heros.follow.utils.GenericEnum;
import com.heros.follow.utils.GenericMethod;
import com.heros.follow.utils.HandleJSONData;
import com.heros.follow.utils.log4j2;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import javax.swing.*;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static java.text.DateFormat.MEDIUM;
import static java.text.DateFormat.SHORT;
import static java.text.DateFormat.getDateTimeInstance;

/**
 * Created by Albert on 2017/1/12.
 */
public class TX_Engine extends Engine {
    private TX_MainController handler;
    private String name;
    private String className;
    private String sportPath;
    private final Set<String> FollowID_Old = new HashSet<>();
    private final Set<String> OpenData_Event_List = new HashSet<>();
    private final Set<String> FollowID_Closed = new HashSet<>();

    public TX_Engine(TX_MainController handler,String name, String className, String sportPath) {
        this.handler = handler;
        this.name = name;
        this.className = className;
        this.sportPath = sportPath;
    }

    @Override
    public void run() {
        int RetryCount = 0, RetryMax = 3;	// 若資料錯誤, 最大嘗試重撈次數

        String dataString = "";
        boolean errorBreak = false;
        do {
            // 足球很容易超過20秒造成timeout
            int timeout = 20_000;
            switch (className) {
                case "Soccer":
                    timeout = 40_000;
                    break;

                default:
                    break;
            }
            long requestTime = System.currentTimeMillis();
            dataString = HttpClientUtils.get(HttpClientUtils.HttpWeb.TX, String.format(sportPath, String.valueOf(new Date().getTime())), TX_HttpModel.DATAREQUESTPATH2, Arrays.asList(timeout, timeout, timeout));
            requestTime = System.currentTimeMillis() - requestTime;
            JsonObject TopJsonElement = null;
            try {
                TopJsonElement = (JsonObject) HandleJSONData.getElement(dataString);

                if (TopJsonElement.get("error").getAsInt() != 0) { // 九州error非0,為資料錯誤
                    log4j2.getInstance().setLog("MsgRecorder", "錯誤資料: " + dataString);
                    callErrorEvent();
                    errorBreak = true;	// 重登關係，所以這次的資料要重爬，設為true重新進入doWhile迴圈一次
                    RetryCount++;
                } else {
                    errorBreak = false;
                    Map<String, String> iSdbs = new HashMap<>();
                    Set<String> FollowIDList = new HashSet<>();
                    long handletime = System.currentTimeMillis();
                    handler.doProcess(TopJsonElement, className, FollowIDList, FollowID_Closed, iSdbs, requestTime); // 傳入TSBallCellView
                    handletime = System.currentTimeMillis() - handletime;

                    Set<String> Lose_Event_List = new HashSet<String>();
                    Set<String> Mustopen_Event_List = new HashSet<String>();

                    Iterator<String> olditor = FollowID_Old.iterator();
                    // 關盤 - 上次有出現 這次沒出現
                    while (olditor.hasNext()) {
                        String oldID = olditor.next();
                        // 這次parse名單沒有, 且未被關盤過
                        if( ! FollowIDList.contains(oldID) && ! FollowID_Closed.contains(oldID)) {
                            Lose_Event_List.add(oldID);
                            FollowID_Closed.add(oldID);
                            if (OpenData_Event_List.contains(oldID))
                                OpenData_Event_List.remove(oldID);
                        }
                    }
                    olditor = null;

                    // 處理這次parse到的名單
                    Iterator<String> curryitor = FollowIDList.iterator();
                    while (curryitor.hasNext()) {
                        String curID = curryitor.next();
                        // 關盤 - 若此賽事反灰 且 關盤名單中無此ID 送出關盤
                        if(iSdbs.get(curID).equals("0") && ! FollowID_Closed.contains(curID)) {
                            Lose_Event_List.add(curID);
                            FollowID_Closed.add(curID);
                            if (OpenData_Event_List.contains(curID))
                                OpenData_Event_List.remove(curID);
                            // 開盤 - 非反灰 且未曾經開盤過 , 不知道之後會不會有新的isdbs代碼, 故直接先以1.2為開盤代號
                        } else if ((iSdbs.get(curID).equals("1") || iSdbs.get(curID).equals("2")) && ! OpenData_Event_List.contains(curID) && ! DataCenter.getInstence().getIdBanList().contains(curID)) {
                            // 已開盤名單無此ID, 或是非黑名單ID
                            Mustopen_Event_List.add(curID);
                            OpenData_Event_List.add(curID);
                            if (FollowID_Closed.contains(curID))
                                FollowID_Closed.remove(curID);
                        }
                    }

                    curryitor = null;

                    if ( ! Mustopen_Event_List.isEmpty()) { // 開盤
                        Date date = new Date();
                        textArea.append(className + " 共有 " + Mustopen_Event_List.size() + " 筆資料 於 " + getDateTimeInstance(SHORT, MEDIUM).format(date) + " 送出開盤訊號.\r\n");
//                        SendApiCenter.getSendApiCenter().sendOpen(SiteCode.TX.getCode(), className, Mustopen_Event_List, SoundPlay.OpenLine);
                    }

                    if ( ! Lose_Event_List.isEmpty()) { // 關盤
                        Date date = new Date();
                        textArea.append(className + " 共有 " + Lose_Event_List.size() + " 筆資料 於 " + getDateTimeInstance(SHORT, MEDIUM).format(date) + " 送出關盤訊號.\r\n");
//                        SendApiCenter.getSendApiCenter().sendClose(SiteCode.TX.getCode(), className, Lose_Event_List, SoundPlay.CloseLine);
                    }
                    FollowID_Old.clear();
                    FollowID_Old.addAll(FollowIDList);

                    Mustopen_Event_List.clear();
                    Lose_Event_List.clear();
                    Mustopen_Event_List = null;
                    Lose_Event_List = null;
                    FollowIDList = null;
                    iSdbs = null;
                    textArea.append(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()) + ":" + EngineName + "跟盤執行中 (request: " + requestTime +"ms, handle: " + handletime + "ms). \r\n");

                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
                // 若拋例外 代表資料格式錯誤
                log4j2.getInstance().setLog("MsgRecorder", "發生Exception: " + e.getMessage() + ", 資料: "+ dataString);
                callErrorEvent();
                errorBreak = true;	// 重登關係，所以這次的資料要重爬，設為true重新進入doWhile迴圈一次
                RetryCount++;
            } finally {
                dataString = null;
                TopJsonElement = null;
            }

            if (RetryCount > RetryMax) {
                errorStop(true, "不正確資料超過" + RetryMax + "次, 請檢查九州官方是否正常...");
                break;
            }
        } while (errorBreak);
    }
    private void callErrorEvent() {
        int RetryCount = 0, RetryMax = 5;	// 最大嘗試重登次數
        try {
            // 錯誤 設為紅燈
            do {
                textArea.append("索取資料發生錯誤，將進行第" + (RetryCount + 1) + "次重登動作.\r\n");
                log4j2.getInstance().setLog("MsgRecorder", "索取資料發生錯誤，將進行第" + (RetryCount + 1) + "次重登動作");
                if (TX) { // 重登成功
                    textArea.append("進行重登成功，繼續當前作業.\r\n");
                    RetryCount = 0;
//                    statusLED.setIcon(new ImageIcon(LabelLED.class.getResource(LabelLED.HIGHGREENLIGHT)));
                    Thread.sleep(2000);
                    break;
                } else {
                    if (++RetryCount > RetryMax) {
                        errorStop(true, "嘗試重登失敗超過" + RetryMax + "次, 請檢查九州官方是否正常...");
                        break;
                    } else {
                        textArea.append("進行重登失敗 " + (8 * RetryCount) + "秒後將再嘗試重登.\r\n");
                        Thread.sleep(8000 * RetryCount);
                    }
                }
            } while (RetryCount <= RetryMax);
        } catch (InterruptedException e) {
            //
        }
    }
    public void clossAllLine() {
        if( ! OpenData_Event_List.isEmpty()) {
            OpenData_Event_List.forEach(i->FollowID_Closed.add(i));
//            SendApiCenter.getSendApiCenter().sendClose(SiteCode.TX.getCode(), this.className, OpenData_Event_List, SoundPlay.CloseLine);
            OpenData_Event_List.clear();
        }
    }
    private void errorStop(boolean closeGame, String reason) {
        if (closeGame) {
            Iterator<TX_Engine> itr = handler.getTxSchedulers().getPutDataMap().values().iterator();
            while (itr.hasNext()) {
                itr.next().clossAllLine();
            }
        }
        // 超過最大可重登次數，進入永久迴圈提示使用者要重開跟盤
        handler.stopThread();
        new Thread(()->{
//            SoundPlay.play(SoundPlay.Disconnection);
            log4j2.getInstance().setLog("MsgRecorder","嚴重警告:"+ WebSite.NAME.TX+":"+className+"程式已停止運行,使用者要重開跟盤, 原因: " + reason);
        }).start();

    }

}
