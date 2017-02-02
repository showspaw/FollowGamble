package com.heros.follow.source.TX_A;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.heros.follow.datacenter.LineHandler;
import com.heros.follow.source.BASE.Engine;
import com.heros.follow.source.result.GameResult;
import com.heros.follow.source.result.LineChangeListener;
import com.heros.follow.utils.GenericEnum;
import com.heros.follow.utils.GenericMethod;
import com.heros.follow.utils.HandleJSONData;
import com.heros.follow.utils.log4j2;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by root on 2017/1/16.
 */
public class TX_MainController implements LineChangeListener {
    private double cycletime=5L;
    private int reloginDelayTimes = 1;
    private ArrayList<String> UpdateOneList = new ArrayList<>();
    private long lastClearDataTime = 0L;
    private static Map<String, Map<String, GameResult>> HistoryDataResult = new ConcurrentHashMap<String, Map<String, GameResult>>();
    private Map<String, TX_Engine> engines;
    private boolean change;
    private TXSchedulers txSchedulers;
    private String ac="DD51759";
    private String pw="aa88";
    private TX_HttpModel httpModel = new TX_HttpModel();

    public double getCycletime() {
        return cycletime;
    }

    public int getReloginDelayTimes() {
        return reloginDelayTimes;
    }

    //執行狀態各球類初始化
    //XX跟盤執行中
    public void initEngine() {
        engines = new HashMap<>();
        for (GenericEnum.SportCode sc : GenericEnum.SportCode.values()) {
            engines.put(sc.getName(), new TX_Engine(this, sc.getName(), sc.getClassName(), sc.getSportPath()));
        }
    }

    public boolean login() {
        return httpModel.login(ac,pw)==Engine.LoginStatus.SUCCESS;
    }
    public void start() {
        txSchedulers = new TXSchedulers(this);
        if(login()){
            initEngine();
            engines.forEach((k,v)->{
                this.txSchedulers.setRunDataMap(k,v);
            });
            this.parseStart();
        }else{
            log4j2.getInstance().setLog("MsgRecorder","九州登入失敗,請檢查帳號密碼後再重新嘗試");
        }
    }
    /**
     * 處理抓到的資料, 處理完的資料經過以下處理
     * 1.格式轉換
     * 2.黑名單判斷
     * 3.變盤判斷
     * 以上若有變盤則發送至api
     *
     * @param dataObj - 資料格式
     * @param className - 球類
     * @param followIDList - 放入當前資料所有的followid
     * @param followID_Closed
     * @param iSdbs	- 放入當前資料所有賽事的開關盤資訊
     * @param requestTime - 請求時間
     * @throws Exception - 資料異常
     */
    public void doProcess(JsonObject dataObj, String className, Set<String> followIDList, Set<String> followID_Closed, Map<String, String> iSdbs, long requestTime) throws Exception {
        if (dataObj == null) {
            return;
        }
        long handleTime = System.currentTimeMillis();
        ArrayList<GameResult> CurrentResult = new ArrayList<>();
        JsonObject TopJsonElement = dataObj;	//gson轉json
        JsonArray TopJsonArray = new JsonArray();	//建構JsonArray
//		String[] uList = {"SQCList","DJList","XBList","QSWList","inplayList"};

        // 迴圈將 ballGameData 裡面的資料放入jsonarray
        JsonObject ballGameData = TopJsonElement.getAsJsonObject().get("JsonSub").getAsJsonObject().get("ballGameData").getAsJsonObject();
        for (Map.Entry<String, JsonElement> ele : ballGameData.entrySet()) {
            if ( ! ele.getValue().isJsonNull() && ele.getValue().isJsonArray()) {
                TopJsonArray.addAll(ele.getValue().getAsJsonArray());
            }
        }

        // 資料處理
        //將JsonArray內元素取出
        for (JsonElement element : TopJsonArray) {

            GameResult result = new GameResult();	//處理更新訊息

            result.setSite(GenericEnum.SiteCode.TX.getCode());
            result.setSource(result.getSite());

            JsonObject jsonObject = element.getAsJsonObject();

            try {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                result.setStartTime(formatter.format(new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(jsonObject.get("sGamedate").getAsString()).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
//			}

            { //GameResult基本資料對應
                result.setLeagueName(jsonObject.get("sAlliance").getAsString());
                result.setViewLeagueName(result.getLeagueName());
                result.setAwayTeam(jsonObject.get("sTeama").getAsString());
                result.setViewAwayTeam(result.getAwayTeam());
                result.setRotA(jsonObject.get("sNumbera").getAsString());
                result.setHomeTeam(jsonObject.get("sTeamb").getAsString());
                result.setViewHomeTeam(result.getHomeTeam());
                result.setRotB(jsonObject.get("sNumberb").getAsString());
                result.setISdbs(jsonObject.get("iSdbs").getAsString());	// 判斷是否反灰
                result.setType(GenericMethod.Trans_TS_GameType(jsonObject.get("iScene").getAsInt(), jsonObject.get("iKzdp").getAsInt()));
            }

            { // 識別碼
                // result.setAllID(eleObject.get("Id").getAsString());
                result.setSiteNo(jsonObject.get("sId").getAsString());
                result.setOpenID(result.getSite() + "_" + result.getSiteNo());
                result.setFollowID(result.getSite() + "_" + result.getSiteNo() + "_" + result.getType()); // 組合follow_ID
                followIDList.add(result.getFollowID());
                iSdbs.put(result.getFollowID(), jsonObject.get("iSdbs").getAsString());
//				System.out.println(result.getFollowID()+","+result.getISdbs());
            }

            { // 讓分
                result.setZF(jsonObject.get("iRrf3").getAsInt() == 1 ? 1 : 2);
                if (className.equals("Soccer")) {
                    result.setZFOption(GenericMethod.trans_TS_SC_Option(jsonObject.get("fRrf0").getAsString()));
                } else {
                    result.setZFOption(jsonObject.get("fRrf0").getAsString());
                    result.setZFValue((jsonObject.get("iRrf1").getAsInt() > 1 ? "-" : "+") + jsonObject.get("iRrf2").getAsString());
                }
                result.setAwayZF(jsonObject.get("fRzdpl").getAsString());
                result.setHomeZF(jsonObject.get("fRydpl").getAsString());
                result.setZFActive("Y");
            }

            { // 大小
                if (className.equals("Soccer")) {
                    result.setDSOption(GenericMethod.trans_TS_SC_Option(jsonObject.get("fDdx0").getAsString()));
                } else {
                    result.setDSOption(jsonObject.get("fDdx0").getAsString());
                    if (jsonObject.get("iDdx1").getAsInt() != 0 && jsonObject.get("iDdx1").getAsInt() != 3) {
                        result.setDSValue((jsonObject.get("iDdx1").getAsInt() > 1 ? "-" : "+") + jsonObject.get("iDdx2").getAsString());
                    }
                }
                result.setAwayDS(jsonObject.get("fDdpl").getAsString());
                result.setHomeDS(jsonObject.get("fDxpl").getAsString());
                result.setDSActive("Y");
            }


            {	// 獨贏
                result.setDrawDE(jsonObject.get("fYhpl").getAsString());
                result.setAwayDE(jsonObject.get("fYzdpl").getAsString());
                result.setHomeDE(jsonObject.get("fYydpl").getAsString());
                result.setDEActive("Y");
            }

            {	// 一輸二贏
                result.setAwayESRE(jsonObject.get("fYzdpl1").getAsString());
                result.setHomeESRE(jsonObject.get("fYydpl1").getAsString());
                result.setESREActive("Y");
            }

            {	// 單雙
                result.setAwaySD(jsonObject.get("fSdpl").getAsString());
                result.setHomeSD(jsonObject.get("fSspl").getAsString());
                result.setSDActive("Y");
            }


            if (jsonObject.get("sZc").getAsInt() != 0) {
                result.invert();
            }

            result.checkActive();

            System.out.println(result.getISdbs()+","+result.getType()+","+result.getFollowID()+","+result.getStartTime());
            CurrentResult.add(result);
        }

        JsonArray arrayJsonData = new JsonArray();
        Set<String> Lose_Event_List = new HashSet<>();
        Set<String> filt_Lose_List = new HashSet<>();
//        ArrayList<BanData> MustBanList = null; // 黑名單容器
//        LogManageCenter.getInstance().getSingleLineChangeList(this.getClass().getName()).clear();
        HistoryDataResult.putIfAbsent(className, new ConcurrentHashMap<String, GameResult>());
        for (GameResult Nowrs : CurrentResult) {

//            // 是黑名單ID, 則不做更新
//            if (datamanager.getInstence().getIdBanList().contains(Nowrs.getFollowID()))
//                continue;

            if (HistoryDataResult.get(className).get(Nowrs.getSiteNo()) != null) {
                GameResult Exrs = HistoryDataResult.get(className).get(Nowrs.getSiteNo());

                // 比賽類型.ID.主客隊名稱.開賽時間.聯盟名稱有變更就關盤,且不再自動開盤
                if ((Nowrs.getType() != Exrs.getType() || ! Nowrs.getAwayTeam().equals(Exrs.getAwayTeam()) ||
                        ! Nowrs.getHomeTeam().equals(Exrs.getHomeTeam()) || ! Nowrs.getStartTime().equals(Exrs.getStartTime()) ||
                        ! Nowrs.getLeagueName().equals(Exrs.getLeagueName())))
                {
                    Lose_Event_List.add(Exrs.getFollowID());

                    // 找出相同賽事一起關盤
                    HistoryDataResult.get(className).values().stream()
                            .filter(i -> ( ! i.getFollowID().equals(Exrs.getFollowID())) &&
                                    (GenericMethod.bothSame(i.getLeagueName(), Exrs.getLeagueName()) &&
                                            GenericMethod.bothSame(i.getAwayTeam(), Exrs.getAwayTeam()) &&
                                            GenericMethod.bothSame(i.getHomeTeam(), Exrs.getHomeTeam())))
                            .forEach(i -> {
                                Lose_Event_List.add(i.getFollowID());
                                filt_Lose_List.add(i.getFollowID());
                            });

//                    datamanager.getInstence().getIdBanList().add(Exrs.getFollowID());
//                    BanData bd = new BanData(
//                            className,
//                            Exrs.getFollowID(),
//                            Exrs.getLeagueName(),
//                            Exrs.getAwayTeam(),
//                            Exrs.getHomeTeam(),
//                            Generic.getGameTypeTitle(Exrs.getType()),
//                            LineHandler.getLineHandler().CompareLine(Exrs, Nowrs)
//                    );
//                    if (MustBanList == null)
//                        MustBanList = new ArrayList<>();
//                    MustBanList.add(bd); // 統一裝進暫存黑名單，等待發送
                    HistoryDataResult.get(className).remove(Exrs.getSiteNo());
                } else {
                    boolean line_change = LineHandler.getLineHandler().handleLine(this, this.getClass().getName(), className, Exrs, Nowrs);
                    // 有變盤或是總更新一次
                    if (line_change || UpdateOneList.contains(className)) {

                        arrayJsonData.add(HandleJSONData.JsonTree(Nowrs));

//                        if (line_change)
//                            notifAll(Nowrs, className); // 更新UI + 變色
                    }
                }
            } else {
                arrayJsonData.add(HandleJSONData.JsonTree(Nowrs));
            }
            // 刷新容器內該筆資料
            HistoryDataResult.get(className).put(Nowrs.getSiteNo(), Nowrs);
        }

        UpdateOneList.removeIf(i->i.equals(className));

        clearDataCenter();

        handleTime = System.currentTimeMillis() - handleTime;

//        // 發送停押打開
//        if (isChange()) {
//            LineHandler.getLineHandler().setAutoOpenLiveThread(this.getClass().getName(), GenericEnum.SiteCode.TX.getCode(), className);
//            clearLineChange();
//            LogManageCenter.getInstance().SendLiveStopLog(this.getClass().getName(), className);
//        } else {
//            LogManageCenter.getInstance().ClearCollection(this.getClass().getName());
//        }

//        // 發送黑名單
//        if (MustBanList != null && ! MustBanList.isEmpty()) {
//            SendApiCenter.getSendApiCenter().sendBan(true, MustBanList);
//        }

        // 發出關盤
        if ( ! Lose_Event_List.isEmpty()) {
//            SendApiCenter.getSendApiCenter().sendClose(SiteCode.TX.getCode(), className, Lose_Event_List, SoundPlay.CloseHandicap);
            followID_Closed.addAll(Lose_Event_List);
        }

        // 發出變盤
        if (arrayJsonData.size() > 0) {
//            SendApiCenter.getSendApiCenter().requestAPI(SiteCode.TX.getCode(), className, arrayJsonData, SoundPlay.PATH_AUDIO1UP, this.getClass().getName(), requestTime, handleTime);
        }

    }
    private long getTimeCurrentTimeMillis(String... time) {
        try {
            return new SimpleDateFormat(time[0], Locale.US).parse(time[1]).getTime();
        } catch (Exception e) {
            return 0L;
        }
    }
    // 比賽時間後8小時的賽事移除
    public void clearDataCenter() {
        // 1小時一次
        if (System.currentTimeMillis() - lastClearDataTime < 3_600_000) return;
        long historytime = 8 * 60 * 60 * 1000;
        try {
            Iterator<Map.Entry<String, Map<String, GameResult>>> e_itors = HistoryDataResult.entrySet().iterator();

            while (e_itors.hasNext()) {
                Map.Entry<String, Map<String, GameResult>> e_itor = e_itors.next();
                Iterator<Map.Entry<String, GameResult>> m_itors = e_itor.getValue().entrySet().iterator();
                while (m_itors.hasNext()) {
                    Map.Entry<String, GameResult> m_itor = m_itors.next();
                    if (getTimeCurrentTimeMillis("yyyy-MM-dd HH:mm:ss", m_itor.getValue().getStartTime()) < (System.currentTimeMillis() - historytime)) {
                        m_itors.remove();
                    }
                }
            }
            lastClearDataTime = System.currentTimeMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setLineChange() {
        this.change = true;
    }

    @Override
    public void clearLineChange() {
        this.change = false;
    }

    @Override
    public boolean isChange() {
        return change;
    }
    //啟動所有執行緒
    public void parseStart() {
        txSchedulers.StartThread();
    }

    //停止執行緒
    protected void stopThread() {
        try {
            txSchedulers.StopThread();
            txSchedulers.getPutDataMap().clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public TXSchedulers getTxSchedulers() {
        return txSchedulers;
    }
}
