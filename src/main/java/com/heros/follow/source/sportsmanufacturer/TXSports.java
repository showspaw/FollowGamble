package com.heros.follow.source.sportsmanufacturer;

/**
 * Created by Albert on 2017/1/12.
 */
public class TXSports extends Sports {
    SportsFactory sportsFactory;
    public TXSports(SportsFactory sportsFactory) {
        name ="TX";
        this.sportsFactory = sportsFactory;
    }

    public void collect() {
        System.out.println(name+" sports colleting...");
        baseball = sportsFactory.createBaseball();
        basketball = sportsFactory.createBasketball();
        horse = sportsFactory.createHorse();
        lottery = sportsFactory.createLottery();
        mls = sportsFactory.createMLS();
        nhl = sportsFactory.createNHL();
        soccer = sportsFactory.createSoccer();
        stock = sportsFactory.createStock();
        tennis = sportsFactory.createTennis();
    }
    //    public enum CATEGORY
//    {
//        BJ("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=bj&kf=0"),
//        BY("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=by&kf=0"),
//        BB("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=bb&kf=0"),
//        HB("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=hb&kf=0"),
//        BQ("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=bq&kf=0"),
//        LQ("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=lq&kf=0"),
//        MZ("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=mz&kf=0"),
//        WQ("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=wq&kf=0"),
//        ZQ("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=zq&kf=0"),
//        OT("/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=ot&kf=0");
//        private final String url;
//        CATEGORY(String url) {
//            this.url = new TX_Website().findIndexPath()+url;
//        }
//    }
//    public String findPath(CATEGORY category) {
//        return category.url;
//    }
}
