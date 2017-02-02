package com.heros.follow.source.PHA;

import com.heros.follow.datacenter.StateCheckSystem;
import com.heros.follow.datas.stateData;
import com.heros.follow.gamble.datamanager.DataManager;
import com.heros.follow.gamble.threads.ThreadManager;
import com.heros.follow.source.BASE.Engine;
import com.heros.follow.source.result.DataChangeListener;

import java.util.HashMap;
import java.util.Map;

import com.heros.follow.utils.GenericEnum.*;
import com.heros.follow.source.PHA.PHA_HttpModel.BallClass;

/**
 * Created by root on 2017/1/18.
 */
public class PHA_MainController {
    private final Map<Integer, stateData> stateDatas = new HashMap<>();
    private int cycletime=3;
    private int reloginDelayTimes = 1;
    public PHA_Engine currentThread = null;
    public PHA_Engine getCurrentThread() {
        return currentThread;
    }
    private PHA_HttpModel httpModel = null;
    private DataChangeListener dataChangeListener;
    private PHA_Engine FullThread = null;
    private PHA_Engine Half1stThread = null;
    private PHA_Engine Half2ndThread = null;
    private PHA_Engine Q1Thread = null;
    private PHA_Engine LiveThread = null;
    private String ac = "avanet720";
    private String pw = "4321qaz";
    public Engine.LoginStatus login() {
        return httpModel.login(ac,pw);
    }

    public PHA_MainController() {
        httpModel = new PHA_HttpModel();

    }

    public void start() {
//        for(BallClass ballClass:BallClass.values()){
//            init(ballClass.getGameId(),ballClass.getHeroName(),ballClass.getNodeId());
//        }

        init(BallClass.足球.getGameId(), BallClass.足球.getHeroTableName(), BallClass.足球.getGameType());
//        init(BallClass.美足.getGameId(), BallClass.美足.getHeroTableName(), BallClass.美足.getGameType());
//        init(BallClass.美籃.getGameId(), BallClass.美籃.getHeroTableName(), BallClass.美籃.getGameType());
    }

    public void init(String gameId,String heroTableName,String gameType) {
        //全場Thread
        newThreadAndRun( gameId, heroTableName, "1", 0);
        this.stateDatas.put(0, new stateData(SiteCode.PHA, SportCode.valueOf(gameType), 0));

        //上半
        newThreadAndRun(gameId, heroTableName, "2", 1);
        this.stateDatas.put(1, new stateData(SiteCode.PHA, SportCode.valueOf(gameType), 1));

        // 下半
        newThreadAndRun(gameId, heroTableName, "3", 2);
        this.stateDatas.put(2, new stateData(SiteCode.PHA, SportCode.valueOf(gameType), 2));

        // 第一節
        newThreadAndRun(gameId, heroTableName, "6", 3);
        this.stateDatas.put(3, new stateData(SiteCode.PHA, SportCode.valueOf(gameType), 3));

        //走地
        newThreadAndRun(gameId, heroTableName, "4", 7);
        this.stateDatas.put(7, new stateData(SiteCode.PHA, SportCode.valueOf(gameType), 7));

        if (!stateDatas.isEmpty())
            stateDatas.values().forEach(i -> StateCheckSystem.getInstance().setData(i).excuteTimer(true));
    }
    public void newThreadAndRun(String gameId, String heroTableName, String gClass, int gameType){
        PHA_Engine thread = new PHA_Engine(this,httpModel, gameId, heroTableName,gClass,gameType);
        if (gameId.equals("7") && gameType == 7) {
            thread.setTdcount(1);
        }
//        this.currentThread.setDataChangeListener(dataChangeListener);
//        this.currentThread.addPropertyChangeListener(this);
//        this.currentThread.setRunState(1);
        thread.setCycletime(cycletime);
//        this.currentThread.setIsalways(this.cellView.getAlwaysupdatecheckBox().isSelected());
        ThreadManager.getInstance().startPHASchedule(DataManager.parsePHAGameType(gameType),thread);
    }

    protected void setAllThreadRunState(int runState) {
//        Arrays.asList(this.FullCell.currentThread, this.HalfCell.currentThread, this.Half2ndCell.currentThread, this.Q1Cell.currentThread, this.LiveCell.currentThread)
//                .forEach(i->i.setRunState(runState));
    }

    protected void AllThreadClostLine() {
//        Arrays.asList(this.FullCell.currentThread, this.HalfCell.currentThread, this.Half2ndCell.currentThread, this.Q1Cell.currentThread, this.LiveCell.currentThread)
//                .forEach(i->i.errorCloseGame());
    }
}
