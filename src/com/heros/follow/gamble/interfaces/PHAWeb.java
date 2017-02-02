package com.heros.follow.gamble.interfaces;

/**
 * Created by root on 2017/1/20.
 */
public class PHAWeb implements Web {
    public enum SportType{
        台棒("1","CPBL","bb"),
        日棒("2","JPB","by"),
        韓棒("3","KBO","hb"),
        美棒("4","MLB","bj"),
        美籃("5","NBA","lq"),
        冰球("6","NHL","bq"),
        足球("7","Soccer","zq"),
        美足("8","MLS","mz");
        private String gameId;
        private String heroName;
        private String nodeId;
        private SportType(String _gameId,String _heroName,String _nodeId){
            gameId = _gameId;
            heroName = _heroName;
            nodeId = _nodeId;
        }

        public String getGameId() {
            return gameId;
        }

        public String getNodeId() {
            return nodeId;
        }

        public String getHeroName() {
            return heroName;
        }
    }
    public enum GameType{

    }

}
