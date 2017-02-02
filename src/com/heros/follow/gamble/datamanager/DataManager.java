package com.heros.follow.gamble.datamanager;

import com.google.common.collect.Maps;
import com.heros.follow.source.result.GameResult;
import java.util.Map;

/**
 * Created by root on 2017/1/20.
 */
public class DataManager{
    private static DataManager dataManager;
    private static Map<Kinds, Map<String,GameResult>> gameResults;
    private DataManager() {
        gameResults = Maps.newConcurrentMap();
        for (Kinds aKind : Kinds.values()) {
            gameResults.putIfAbsent(aKind, Maps.newConcurrentMap());
        }

    }

    public enum PHA{
        全場(0),
        上半場(1),
        下半場(2),
        第一節(3),
        全場走地(7);
        private int gameType;
        private PHA(int gameType) {
            this.gameType = gameType;
        }

        public int getGameType() {
            return gameType;
        }
    }

    public enum Kinds{
        //法老所提供的運動。
        PHA_CPBL,
        PHA_JPB,
        PHA_KBO,
        PHA_MLB,
        PHA_NBA,
        PHA_NHL,
        PHA_SOCCER,
        PHA_MLS
    }

    public static PHA parsePHAGameType(int gameType) {
        PHA aKind = null;
        if(gameType==0){
            aKind = PHA.全場;
        } else if (gameType==1) {
            aKind = PHA.上半場;
        }else if (gameType==2) {
            aKind = PHA.下半場;
        }else if (gameType==3) {
            aKind = PHA.第一節;
        }else if (gameType==7) {
            aKind = PHA.全場走地;
        }
        return aKind;
    }
    public static Kinds parsePHAKinds(String heroTableName) {
        Kinds aKind=null;
        if(heroTableName.equals("CPBL")){
            aKind = Kinds.PHA_CPBL;
        }else if (heroTableName.equals("JPB")) {
            aKind = Kinds.PHA_JPB;
        }else if(heroTableName.equals("KBO")){
            aKind = Kinds.PHA_KBO;
        }else if(heroTableName.equals("MLB")){
            aKind = Kinds.PHA_MLB;
        }else if(heroTableName.equals("NBA")){
            aKind = Kinds.PHA_NBA;
        }else if(heroTableName.equals("Soccer")){
            aKind = Kinds.PHA_SOCCER;
        }else if(heroTableName.equals("KBO")){
            aKind = Kinds.PHA_KBO;
        }else if(heroTableName.equals("MLS")){
            aKind = Kinds.PHA_MLS;
        }
        return aKind;
    }
    public static Map<Kinds, Map<String,GameResult>> getGameResults() {
        return gameResults;
    }

    public static void add(Kinds aKind, GameResult gr) {
        Map<String,GameResult> gameResultSet = DataManager.gameResults.get(aKind);
        gameResultSet.put(gr.getFollowID(), gr);
    }
    public static DataManager getInstance() {
        if (dataManager == null) {
            synchronized (DataManager.class) {
                dataManager = new DataManager();
            }
        }
        return dataManager;
    }
}
