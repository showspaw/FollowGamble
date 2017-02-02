package com.heros.follow.source.BASE;

/**
 * Created by Albert on 2017/1/12.
 */
public abstract class WebSite {
    public enum NAME{
        TX,PHA;
    }
//    public abstract TX_Website.SPORTS findAllSports();
    public abstract String findIndexPath();
    public abstract String findLoginPath();
    public abstract String findPath();

//    public String findSportPath(SPORTS sports) {
//        String url = "";
//
//        return url;
//    }

//    public String findSportPath(NAME website,Sport sport){
//        String path = null;
//        if (NAME.TX == website) {
//            path=new TX_Website().findPath(sport);
//        }else if(NAME.PHA==website){
//
//        }
//        return path;
//    }
}
