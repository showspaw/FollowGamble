package com.heros.follow.source.TX_A;

import com.heros.follow.source.BASE.WebSite;

/**
 * Created by Albert on 2017/1/12.
 */
public class TX_Website extends WebSite {
//    public enum SPORTS implements WebSite.SPORTS {
//        BASKETBALL,BASEBALL
//    }

//    public SPORTS[] findAllSports(SPORTS sports) {
//        return SPORTS.values();
//    }

//    @Override
//    public Sport findAllSports() {
//        return new WebSite.SPORTS[0];
//    }

    public String findIndexPath() {
        return "http://ag.td111.net";
    }

    public String findLoginPath() {
        return "http://ag.td111.net/manage/Page/Login/CommonLogin/Login.aspx";
    }

    public String findPath() {
        return null;
    }
}
