package com.heros.follow.source.sportsmanufacturer;

import com.heros.follow.source.BASE.WebSite;

/**
 * Created by Albert on 2017/1/13.
 */
public class TestFactory {
    public Sports createSports(WebSite.NAME website) {
        Sports sports = null;
        SportsFactory sportsFactory;
        if (website == WebSite.NAME.TX) {
            sportsFactory = new TXSportsFactory();
            sports = new TXSports(sportsFactory);
            sports.collect();
        } else if (website == WebSite.NAME.PHA) {

        }
        return sports;
    }

    public static void main(String[] args) {
        new TestFactory().createSports(WebSite.NAME.TX);
    }
}
