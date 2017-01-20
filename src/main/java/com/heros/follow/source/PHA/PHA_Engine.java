package com.heros.follow.source.PHA;

import com.google.gson.JsonArray;
import com.heros.follow.datacenter.LineHandler;
import com.heros.follow.source.BASE.Engine;
import com.heros.follow.source.errorlog.LogManageCenter;
import com.heros.follow.source.result.DataChangeListener;
import com.heros.follow.source.result.GameResult;
import com.heros.follow.source.result.LineChangeListener;
import com.heros.follow.tools.HttpClientUtils;
import com.heros.follow.utils.GenericEnum;
import com.heros.follow.utils.GenericMethod;
import com.heros.follow.utils.HandleJSONData;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by root on 2017/1/18.
 */
public class PHA_Engine extends Engine implements LineChangeListener {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    public static Map<String, Map<String, GameResult>> DataResult = new ConcurrentHashMap<String, Map<String, GameResult>>();
    private final PHA_MainController handler;
    private Map<Integer, Thread> FollowKeyTheads = new HashMap<>();
    private static ReentrantLock reetLock = new ReentrantLock();
    private Set<String> FiltrationList = new HashSet<>();
    private Set<String> Old_FollowID = new HashSet<>();		// 裝此次扒下來的資料
    private Set<String> Cur_FollowID = new HashSet<>();		// 裝上次爬下來的資料
    private Set<String> AlreadyClose_FollowID = new HashSet<>();		// 裝已開盤
    private Set<String> AlreadyOpen_FollowID = new HashSet<>();		// 裝已關盤
    private ArrayList<GameResult> ArrResult;
    private PHA_HttpModel httpModel;
//    private PHACollectionView cellView;
    private String gameClass;
    private String Host;
    private String GameID;
    private String HeroTableName;

    // beans
    protected PropertyChangeSupport propertyChangeSupport;
    private DataChangeListener dataChangeListener;
    private static boolean isReloging = false;
    private int errorRetry = 0, MaxRetry = 5;
    private boolean isalways = false;
    private int ThreadCount = 0;
    private int RunState = 0;// 生命週期完成-停止=0 ,開始=1 ,發生例外or錯誤=2,外部觸發停止=3
    private int cycletime = 2;
    private int gameType;
    private int tdcount = 0;
    private long requestTime, handleTime;
    private boolean change;

    public void setDataChangeListener(DataChangeListener dataChangeListener) {
        this.dataChangeListener = dataChangeListener;
    }

    public void setTdcount(int tdcount) {
        this.tdcount = tdcount;
    }

    public PHA_Engine(PHA_MainController handler,PHA_HttpModel PHA, String GameID, String HeroTableName, String gClass, int gametype){
        this.handler = handler;
        this.httpModel = PHA;
//        this.cellView = cellView;
        this.propertyChangeSupport = new PropertyChangeSupport(this);
        this.GameID = GameID;
        this.HeroTableName = HeroTableName;
        this.gameClass = gClass;
        this.gameType = gametype;
        this.ArrResult = new ArrayList<>();
    }
    @Override
    public void run() {
        Document docHTML = null;
        int page = 1, maxPage = 1;
        errorRetry = 0;

        try {
            while (true) {//this.getRunState() == 1
                if ( ! isReloging) {
                    requestTime = System.currentTimeMillis();
                    docHTML = HttpClientUtils.httpGetAsDom(HttpClientUtils.HttpWeb.PHA, httpModel.LOGINPAGE + "/schedule/list?g=" + this.GameID+"&h=" + this.gameClass+"&p=" + page, null, null);
                    requestTime = System.currentTimeMillis() - requestTime;
                } else {
                    excuteReLogin();
                }

                if (docHTML != null) {
                    if ( ! docHTML.select("body > div[id=pharoh_build] > div[id=middle] > div[id=game_list]").isEmpty()) {
                        Elements paginationControl = docHTML.select("div[class=paginationControl]");
                        if ( ! paginationControl.isEmpty()) {
                            maxPage = paginationControl.get(0).getAllElements().size() - 3;
                            if (maxPage < 1) maxPage = 1;
                        } else {
                            maxPage = 1;
                        }
                        this.parseString(docHTML);
                        this.compareandinsert();
                        if (page < maxPage) {
                            page++;
                        } else {
                            page = 1;
                            this.checkCurrentGame();
                            this.clearExpiredData();
                            if (isalways)
                                isalways = false;
                        }
                        this.ArrResult.clear();
                        docHTML = null;
                    } else {
                        excuteReLogin();
                    }
                }
                Thread.sleep((long) (cycletime * 1000));
            }
//            this.setRunState(0);
        } catch (InterruptedException e) {
            //
        } catch (Exception e) {
            e.printStackTrace();
            errorCloseGame();
//            this.setRunState(2);
        }
    }

    /**
     * // 重登機制
     * @throws Exception
     */
    private void excuteReLogin() throws Exception {
        if (reetLock.tryLock()) {
            try {
                isReloging = true;
                do {
                    HttpClientUtils.resetClient(HttpClientUtils.HttpWeb.PHA);
                    LoginStatus status = handler.login();
                    if (status == LoginStatus.SUCCESS) {
                        errorRetry = 0;
                        break;
                    } else if (status == LoginStatus.MAINTAIN) {	// 維修狀態 - 關閉場次, 不記失敗重登至維修完畢
                        handler.AllThreadClostLine();
                    } else {	// 超過 最大嘗試 關閉場次 和 停止運作
                        if (++errorRetry >= MaxRetry) {
                            executor.execute(()->{
//                                SoundPlay.play(SoundPlay.Disconnection);
                                JOptionPane.showMessageDialog(null, "程式已停止運行, 原因: 資料異常, 請確認法老官方狀態!", "嚴重警告", JOptionPane.ERROR_MESSAGE);
                            });
                            handler.AllThreadClostLine(); // 關閉所有該賽事的盤口
                            handler.setAllThreadRunState(2); // 停止所有該賽事的執行緒
                            break;
                        }
                    }
                    long sleepdeley = 8000 * errorRetry;
                    Thread.sleep(status == LoginStatus.MAINTAIN? 30_000 : sleepdeley > 20_000? 20_000 : sleepdeley); // 維修間隔30秒一次, 其它慢慢增加間隔時間
                } while(true);
            } finally {
                isReloging = false;
                reetLock.unlock();
                synchronized (reetLock) {
                    reetLock.notifyAll();
                }
            }
        } else {
            synchronized (reetLock) {
                reetLock.wait(5000);
            }
        }
    }

    void errorCloseGame() {
        if ( ! AlreadyOpen_FollowID.isEmpty()) {
            Set<String> Lose_Event_List = new HashSet<>();
            Lose_Event_List.addAll(AlreadyOpen_FollowID);
            AlreadyClose_FollowID.addAll(AlreadyOpen_FollowID);
            AlreadyOpen_FollowID.clear();
            if ( ! Lose_Event_List.isEmpty()){
//                SendApiCenter.getSendApiCenter().sendClose(SiteCode.httpModel.getCode(), this.HeroTableName, Lose_Event_List, SoundPlay.CloseLine);

            }
        }
    }
    private void parseString(Document docHTML){
        handleTime = System.currentTimeMillis();
        for(Element tableRow : docHTML.select("table[class=game]") ){
            String LeagueName = tableRow.select("caption").html();//leaguename
            for(Element trRow : tableRow.select("tr")){
                if(trRow.select("th").size() > 0)
                    continue;

                GameResult OBJrs = new GameResult();

                // 判斷反灰
                if (trRow.attr("class").indexOf("disable") != -1) {
                    OBJrs.setISdbs("0");
                } else {
                    OBJrs.setISdbs("1");
                }

                OBJrs.setFollowIdUrl(httpModel.LOGINPAGE + trRow.select("td").get(3 + this.tdcount).select("a").get(0).attr("href"));

                OBJrs.setSite(GenericEnum.SiteCode.PHA.getCode());
                OBJrs.setSource(OBJrs.getSite());
                OBJrs.setType(this.gameType);
                OBJrs.setLeagueName(LeagueName);
                OBJrs.setViewLeagueName(LeagueName);
                String StartTime = trRow.select("td").get(0).text();	//時間 2014-06-04 <strong>07:05</strong><br /> <span class="rollball">走地</span>
                StartTime = StartTime.replaceAll("走地", "").replaceAll("補", "").trim() + ":00";
                //StartTime = StartTime.replaceAll("\\<.*>", "").trim();
                OBJrs.setStartTime(StartTime);
                OBJrs.setHomeTeam(trRow.select("td").get(1+this.tdcount).select("span[class=teamHome]").html());	//隊伍 PHI-費城費城人</span><br /> <span class="teamHome">WSH-華盛頓國民</span>&nbsp;<span class="master">主</span>
                OBJrs.setViewHomeTeam(OBJrs.getHomeTeam());	//隊伍 PHI-費城費城人</span><br /> <span class="teamHome">WSH-華盛頓國民</span>&nbsp;<span class="master">主</span>
                OBJrs.setAwayTeam(trRow.select("td").get(1+this.tdcount).select("span[class=teamAway]").html());
                OBJrs.setViewAwayTeam(OBJrs.getAwayTeam());
                OBJrs.setFollowID("Unknow");
//				OBJrs.setSiteNo(Integer.toString((OBJrs.getStartTime()+OBJrs.getHomeTeam()+OBJrs.getAwayTeam()).hashCode()));

                Elements tds = trRow.select("td");
                //讓分
                try {
                    Element td = tds.get(2+this.tdcount);
                    String fence = td.select("span[class=fence]").get(0).html();
                    if(this.GameID.equals("7")){
                        if(!fence.equals("")){
                            OBJrs.setZF(1);
                            OBJrs.setZFOption(GenericMethod.transZFOption(fence));
                        }else {
                            fence = td.select("span[class=fence]").get(1).html();
                            OBJrs.setZF(2);
                            OBJrs.setZFOption(GenericMethod.transZFOption(fence));
                        }
                    }else{
                        if (!fence.equals("")) {
                            OBJrs.setZF(2);
                            if (fence.indexOf("+") > -1) {
                                String[] ZFdata = fence.split("\\+");
                                OBJrs.setZFOption(ZFdata[0]);
                                OBJrs.setZFValue("+" + ZFdata[1]);
                            } else if (fence.indexOf("-") > -1) {
                                String[] ZFdata = fence.split("\\-");
                                OBJrs.setZFOption(ZFdata[0]);
                                OBJrs.setZFValue("-" + ZFdata[1]);
                            } else if (fence.indexOf("輸") > -1) {
                                OBJrs.setZFOption(fence.substring(0,fence.indexOf("輸")));
                                OBJrs.setZFValue("-100");
                            } else if (fence.indexOf("平") > -1) {
                                OBJrs.setZFOption(fence.substring(0,fence.indexOf("平")));
                                OBJrs.setZFValue("+0");
                            }
                        } else {
                            fence = td.select("span[class=fence]").get(1).html();
                            OBJrs.setZF(1);
                            if(fence.indexOf("+") > -1){
                                String[] ZFdata = fence.split("\\+");
                                OBJrs.setZFOption(ZFdata[0]);
                                OBJrs.setZFValue("+" + ZFdata[1]);
                            }else if(fence.indexOf("-") > -1){
                                String[] ZFdata = fence.split("\\-");
                                OBJrs.setZFOption(ZFdata[0]);
                                OBJrs.setZFValue("-" + ZFdata[1]);
                            }else if(fence.indexOf("輸") > -1){
                                OBJrs.setZFOption(fence.substring(0,fence.indexOf("輸")));
                                OBJrs.setZFValue("-100");
                            }else if(fence.indexOf("平") > -1){
                                OBJrs.setZFOption(fence.substring(0,fence.indexOf("平")));
                                OBJrs.setZFValue("+0");
                            }
                        }
                    }

                    if(this.GameID.equals("7")){
                        OBJrs.setAwayZF(td.select("span[class=bet]").get(1).html());
                        OBJrs.setHomeZF(td.select("span[class=bet]").get(0).html());
                    }else{
                        OBJrs.setAwayZF(td.select("span[class=bet]").get(0).html());
                        OBJrs.setHomeZF(td.select("span[class=bet]").get(1).html());
                    }

                    if(!td.className().toString().equals("disable")){
                        OBJrs.setZFActive("Y");
                    }else{
                        OBJrs.setZFActive("N");
                    }
                } catch (Exception e) {
                    OBJrs.setZFActive("N");
                }

                //大小 <span class="totals">8+75</span>&nbsp;大&nbsp;<span class="bet">0.930</span><br /> <span class="totals">8-75</span>&nbsp;小&nbsp;<span class="bet">0.930</span>
                try {
                    Element td = tds.get(4+this.tdcount);
                    if(this.GameID.equals("7")){
                        OBJrs.setDSOption(GenericMethod.transDSOption(td.select("span[class=totals]").get(0).html()));
                    }else{
                        String total = td.select("span[class=totals]").get(0).html();
                        if (total.indexOf("+") > -1) {
                            String[] ZFdata = total.split("\\+");
                            OBJrs.setDSOption(ZFdata[0]);
                            OBJrs.setDSValue("+" + ZFdata[1]);
                        } else if (total.indexOf("-") > -1) {
                            if (total.indexOf("-100") > -1) {
                                String[] ZFdata = total.split("\\-");
                                OBJrs.setDSOption(ZFdata[0]+".5");
                                OBJrs.setDSValue("0");
                            }else {
                                String[] ZFdata = total.split("\\-");
                                OBJrs.setDSOption(ZFdata[0]);
                                OBJrs.setDSValue("-" + ZFdata[1]);
                            }
                        } else if (total.indexOf("平") > -1) {
                            OBJrs.setDSOption(total.substring(0,total.indexOf("平")));
                            OBJrs.setDSValue("+0");
                        }
                    }
                    Elements bet = td.select("span[class=bet]");
                    OBJrs.setAwayDS(bet.get(0).html());
                    OBJrs.setHomeDS(bet.get(1).html());

                    if (!td.className().toString().equals("disable")) {
                        OBJrs.setDSActive("Y");
                    } else {
                        OBJrs.setDSActive("N");
                    }
                } catch (Exception e) {
                    OBJrs.setDSActive("N");
                }
                //單雙
                if(tds.size() >= 10 && !(this.GameID.equals("7") && this.gameType == 0)){
                    Element td = tds.get(6+this.tdcount);
                    if(!td.className().toString().equals("disable")){
                        OBJrs.setSDActive("Y");
                        Elements bet = td.select("span[class=bet]");
                        OBJrs.setAwaySD(bet.get(0).html());
                        OBJrs.setHomeSD(bet.get(1).html());
                    }else{
                        OBJrs.setSDActive("N");
                    }
                }
                //獨贏
                if(tds.size() >= 12){
                    Element td = tds.get(8+this.tdcount);
                    if(!td.className().toString().equals("disable")){
                        OBJrs.setDEActive("Y");

                    }else{
                        OBJrs.setDEActive("N");
                    }
                    Elements bet = td.select("span[class=bet]");
                    OBJrs.setAwayDE(bet.get(0).html());
                    OBJrs.setHomeDE(bet.get(1).html());
                }else if (tds.size() >= 10 && this.GameID.equals("7") && this.gameType == 0) {
                    Element td = tds.get(6+this.tdcount);
                    if(!td.className().toString().equals("disable")){
                        OBJrs.setDEActive("Y");

                    }else{
                        OBJrs.setDEActive("N");
                    }
                    Elements bet = td.select("span[class=bet]");
                    OBJrs.setAwayDE(bet.get(0).html());
                    OBJrs.setHomeDE(bet.get(1).html());
                }

                //一輸二贏
                if(trRow.select("td").size() >= 14){
                    Element td = trRow.select("td").get(10+this.tdcount);
                    if(!td.className().toString().equals("disable")){
                        OBJrs.setESREActive("Y");
                        Elements bet = td.select("span[class=bet]");
                        OBJrs.setAwayESRE(bet.get(0).html());
                        OBJrs.setHomeESRE(bet.get(1).html());
                    }else{
                        OBJrs.setESREActive("N");
                    }
                }
                // 防止開出00盤
                OBJrs.checkActive();
                //盤口開關
                if (trRow.attr("class").indexOf("disable") > -1) {
                    OBJrs.setState("Y");
                    OBJrs.allDown();
                }else {
                    OBJrs.setState("N");
                }
                this.ArrResult.add(OBJrs);
            }
        }
    }

    private void compareandinsert(){

        JsonArray arrayJsonData = new JsonArray();

        LogManageCenter.getInstance().getSingleLineChangeList(this.getClass().getName()).clear();
        DataResult.putIfAbsent(this.HeroTableName, new ConcurrentHashMap<String, GameResult>());
        Set<String> Lose_SeriousEvent_List = new HashSet<>();
        Set<String> Lose_Event_List = new HashSet<>();
        Set<String> Open_Event_List = new HashSet<>();
//        ArrayList<BanData> MustBanList = new ArrayList<>(); // 黑名單容器
        for (GameResult Nowrs : this.ArrResult) {
            FiltrationList.add(Nowrs.getFollowIdUrl());

            if (DataResult.get(this.HeroTableName).get(Nowrs.getFollowIdUrl()) != null) {

                GameResult Exrs = DataResult.get(this.HeroTableName).get(Nowrs.getFollowIdUrl());

                if (!Exrs.getFollowID().equals("Unknow"))
                    Nowrs.setFollowID(Exrs.getFollowID());
                else {
                    if ( ! FollowKeyTheads.containsKey(Nowrs.getFollowIdUrl().hashCode()) || ! FollowKeyTheads.get(Nowrs.getFollowIdUrl().hashCode()).isAlive())
                        executor.execute(new PHA_Runnable(this, Exrs, Nowrs.getFollowIdUrl()));
                    continue;
                }
//                // 黑名單內不做任何處理
//                if (DataCenter.getInstence().getIdBanList().contains(Nowrs.getFollowID()))
//                    continue;

                Cur_FollowID.add(Nowrs.getFollowID());
                if ((Nowrs.getType() != Exrs.getType() || !Nowrs.getAwayTeam().equals(Exrs.getAwayTeam()) ||
                        !Nowrs.getHomeTeam().equals(Exrs.getHomeTeam()) || !Nowrs.getStartTime().equals(Exrs.getStartTime()) ||
                        !Nowrs.getLeagueName().equals(Exrs.getLeagueName())) )
                {
                    Lose_SeriousEvent_List.add(Exrs.getFollowID());

                    // 找出相同賽事一起關盤
                    DataResult.get(this.HeroTableName).values().stream()
                            .filter(i -> ( ! i.getFollowID().equals(Exrs.getFollowID())) &&
                                    (GenericMethod.bothSame(i.getLeagueName(), Exrs.getLeagueName()) &&
                                            GenericMethod.bothSame(i.getAwayTeam(), Exrs.getAwayTeam()) &&
                                            GenericMethod.bothSame(i.getHomeTeam(), Exrs.getHomeTeam())))
                            .forEach(i -> {
                                Lose_SeriousEvent_List.add(i.getFollowID());
                            });

//                    DataCenter.getInstence().getIdBanList().add(Exrs.getFollowID());
                    DataResult.get(this.HeroTableName).remove(Nowrs.getFollowIdUrl());
//                    BanData bd = new BanData(
//                            this.HeroTableName,
//                            Exrs.getFollowID(),
//                            Exrs.getLeagueName(),
//                            Exrs.getAwayTeam(),
//                            Exrs.getHomeTeam(),
//                            Generic.getGameTypeTitle(Exrs.getType()),
//                            LineHandler.getLineHandler().CompareLine(Exrs, Nowrs)
//                    );
//                    MustBanList.add(bd); // 統一裝進暫存黑名單，等待發送
                } else {
                    //變盤新增,需發出更新請求至英雄
                    boolean isChange = LineHandler.getLineHandler().handleLine(this, this.getClass().getName(), this.HeroTableName, Exrs, Nowrs);
                    if (isChange || this.isalways) { //持續更新有勾選
                        if (isChange)
                            dataChangeListener.fireDataChange(Nowrs, this.HeroTableName);
                        //add JsonData
                        if (!Nowrs.getFollowID().equals("Unknow"))
                            arrayJsonData.add(HandleJSONData.JsonTree(Nowrs));
                    }
                    DataResult.get(this.HeroTableName).put(Nowrs.getFollowIdUrl(), Nowrs);
                    // 非反灰且未曾開過盤 要開盤
                    if ((Nowrs.getISdbs() != null && ! Nowrs.getISdbs().equals("0")) && ! AlreadyOpen_FollowID.contains(Nowrs.getFollowID())) {
                        Open_Event_List.add(Nowrs.getFollowID());
                    } else {
                        // 反灰且未曾關過盤 要關盤
                        if ((Nowrs.getISdbs() != null && Nowrs.getISdbs().equals("0")) && ! AlreadyClose_FollowID.contains(Nowrs.getFollowID())) {
                            Lose_Event_List.add(Nowrs.getFollowID());
                        }
                    }
                }
            } else {
                executor.execute(new PHA_Runnable(this, Nowrs, Nowrs.getFollowIdUrl()));
                DataResult.get(this.HeroTableName).put(Nowrs.getFollowIdUrl(), Nowrs);
            }
        }

        if (isChange()) {
//            LineHandler.getLineHandler().setAutoOpenLiveThread(this.getClass().getName(), GenericEnum.SiteCode.PHA.getCode(), this.HeroTableName);
            clearLineChange();
//            LogManageCenter.getInstance().SendLiveStopLog(this.getClass().getName(), this.HeroTableName);
        } else {
//            LogManageCenter.getInstance().ClearCollection(this.getClass().getName());
        }

//        // 發送黑名單
//        if ( ! MustBanList.isEmpty()) {
//            SendApiCenter.getSendApiCenter().sendBan(true, MustBanList);
//        }

        // 發送緊急關盤
        if ( ! Lose_SeriousEvent_List.isEmpty()) {
//            SendApiCenter.getSendApiCenter().sendClose(SiteCode.httpModel.getCode(), this.HeroTableName, Lose_SeriousEvent_List, SoundPlay.CloseHandicap);
            AlreadyClose_FollowID.addAll(Lose_SeriousEvent_List);
        }

        // 會區分兩個關盤容器是因為 發送音效不一樣
        // 發送關盤
        if ( ! Lose_Event_List.isEmpty()) {
//            SendApiCenter.getSendApiCenter().sendClose(SiteCode.httpModel.getCode(), this.HeroTableName, Lose_Event_List, SoundPlay.CloseLine);
            AlreadyOpen_FollowID.removeIf(i-> Lose_Event_List.contains(i));
            AlreadyClose_FollowID.addAll(Lose_Event_List);
        }
        // 發送開盤
        if ( ! Open_Event_List.isEmpty()) {
//            SendApiCenter.getSendApiCenter().sendClose(SiteCode.httpModel.getCode(), this.HeroTableName, Open_Event_List, SoundPlay.OpenLine);
            AlreadyClose_FollowID.removeIf(i-> Open_Event_List.contains(i));
            AlreadyOpen_FollowID.addAll(Open_Event_List);
        }
        handleTime = System.currentTimeMillis() - handleTime;
        // 發送變盤
        if(arrayJsonData.size() > 0){
            //實作發出請求,傳送jsonData給api server處理
//            SendApiCenter.getSendApiCenter().requestAPI(SiteCode.httpModel.getCode(), this.HeroTableName, arrayJsonData, SoundPlay.PATH_AUDIO1UP, this.getClass().getName(), requestTime, handleTime);
        }
    }

    public void checkCurrentGame() {
//		Map<String, ArrayList<String>> Lose_Event_List = new HashMap<>();
        Set<String> Lose_Event_List = new HashSet<>();
        // 上次與這次的賽事做比對 如果上次出現而這次未出現 送出關盤
        if ( ! Old_FollowID.isEmpty()) {
            Old_FollowID.stream().filter(i-> ! Cur_FollowID.contains(i)).forEach(a->Lose_Event_List.add(a));
            if ( ! Lose_Event_List.isEmpty()){
//                SendApiCenter.getSendApiCenter().sendClose(SiteCode.httpModel.getCode(), this.HeroTableName, Lose_Event_List, SoundPlay.CloseLine);

            }
        }
        Old_FollowID.clear();
        Old_FollowID.addAll(Cur_FollowID);
        Cur_FollowID.clear();
    }

    public void clearExpiredData() {
        // 清除未出現的ID
        if (!FiltrationList.isEmpty()) {
            List<String> removeList = new ArrayList<String>();
            for (Map.Entry<String, GameResult> data : DataResult.get(this.HeroTableName).entrySet())
                if (!FiltrationList.contains(data.getValue().getFollowIdUrl()) && data.getValue().getType() == this.gameType) {
                    removeList.add(data.getValue().getFollowIdUrl());
                    System.out.println("預先刪除: " + this.HeroTableName+ "->" + data.getValue().getFollowIdUrl());
                }
            if (!removeList.isEmpty())
                removeList.forEach(i-> DataResult.get(this.HeroTableName).remove(i));
            FiltrationList.clear();
        }
    }

    public int getRunState() {
        return this.RunState;
    }

    public void setRunState(int runState) {
        int oldstate = this.RunState;
        this.RunState = runState;
        this.propertyChangeSupport.firePropertyChange("RunState", oldstate, this.RunState);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void setIsalways(boolean isalways) {
        this.isalways = isalways;
    }
    public boolean isIsalways() {
        return isalways;
    }
    public void setCycletime(int cycletime) {
        this.cycletime = cycletime;
    }
    public Set<String> getAlreadyClose_FollowID() {
        return AlreadyClose_FollowID;
    }
    public Set<String> getAlreadyOpen_FollowID() {
        return AlreadyOpen_FollowID;
    }
    public String getHeroTableName() {
        return HeroTableName;
    }

    public synchronized void addCount() {
        this.ThreadCount = this.ThreadCount + 1;
    }

    public synchronized void subCount() {
        this.ThreadCount = this.ThreadCount - 1;
    }

    public int getCount() {
        return this.ThreadCount;
    }

    public void sendSingleData(GameResult gData) {
        JsonArray arrayJsonData = new JsonArray();
        arrayJsonData.add(HandleJSONData.JsonTree(gData));
//        SendApiCenter.getSendApiCenter().requestAPI(SiteCode.httpModel.getCode(), this.HeroTableName, arrayJsonData, "");
    }

    public void setLineChange() {
        this.change = true;
    }

    public void clearLineChange() {
        this.change = false;
    }

    public boolean isChange() {
        return change;
    }

    public Map<Integer, Thread> getFollowKeyTheads() {
        return FollowKeyTheads;
    }
}
