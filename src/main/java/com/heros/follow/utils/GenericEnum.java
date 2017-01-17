package com.heros.follow.utils;

/**
 * Created by root on 2017/1/16.
 */
public class GenericEnum {
    public enum SportCode{
        bj("美棒", "MLB","bj"),
        by( "日棒", "JPB","by"),
        bb( "台棒", "CPBL","bb"),
        hb( "韓棒",  "KBO","hb"),
        bq( "冰球", "NHL","bq"),
        lq( "籃球",  "NBA","lq"),
        cq("彩球",  "Lottery","cq"),
        mz( "美足", "MLS","mz"),
        wq("網球","Tennis","wq"),
        zq("足球",  "Soccer","zq"),
        zs( "指數", "Stock","zs"),
        ss( "賽馬", "Horse","ss"),
        ot("其它",  "Other","ot");
        private String name;
        private String className;
        private String sportPath;
        private final String prePath="http://ag.td111.net/manage/Page/SportsMessage/Ashx/SingleEntry_New_Ajax.ashx?v=%s&pageNum=1000&pageIndex=1&m_Ball=";
        private final String postPath = "&kf=0";
        private SportCode(String name,String className,String sportType){
            this.name = name;
            this.className = className;
            this.sportPath = prePath+sportType+postPath;
        }

        public String getName() {
            return name;
        }

        public String getClassName() {
            return className;
        }
        public String getSportPath() {
            return sportPath;
        }
    }
    public enum SiteCode {
        TX("TX"), PHA("PHA");

        private String code;
        private SiteCode(String code) {
            this.code = code;
        }
        public String getCode() {
            return code;
        }
    }
    public enum LineName {
        ZF("讓分"), DS("大小"), SD("單雙"), ESRE("一輸二贏"), DE("獨贏"), DrawDE("足球獨贏");

        private String name;
        private LineName(String name) {
            this.name = name();
        }
        public String getName() {
            return name;
        }
    }
    public static enum ParserType {
        MainListener("監視者", 2000L),
        League("聯盟接口", 21600000L),
        Team("隊伍接口", 21600000L),
        Game("賽事接口", 10000L),
        MultOdds("多盤口接口", 10000L),
        UpdateOddsIn30s("30秒主盤口更新接口", 1000L),
        UpdateMultOddsIn30s("30秒多盤口更新接口", 1000L),
        InPlayLine("走地更新接口", 20L),
        HalfStandardLine("半場歐賠接口", 1000L),
        Goalorder("先後進球盤接口", 2000L),
        DsGoal("單雙盤接口", 2000L),
        OtherPlay("其它盤接口", 2000L),
        ModifyRecord("比賽中斷資訊接口", 30000L),
        ClearDataCenter("清理者", 300000L);

        private long value;
        private String parsername;

        private ParserType(String parsername, long value) {
            this.value = value;
            this.parsername = parsername;
        }

        public long getValue() {
            return this.value;
        }

        public void setValue(long v) {
            this.value = v;
        }

        public String getParsername() {
            return this.parsername;
        }
    }
    public static enum LineType {
        RunLine,
        StandardLine,
        DSB,
        HalfRunLine,
        HalfDSB,
        HalfStandardLine,
        InPlayLine,
        Goalorder,
        DsGoal,
        OtherPlay;

        private LineType() {
        }
    }
}
