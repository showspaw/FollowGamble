package com.heros.follow.utils;

import com.heros.follow.source.result.GameResult;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 2017/1/16.
 */
public class GenericMethod {
    public static String transZFOption(String Value){
        return getDSZFOption(Value);
    }
    public static String transDSOption(String Value){
        return getDSZFOption(Value);
    }

    private static String getDSZFOption(String Value) {
        String result = "";
        switch(Value)
        {
            case "0":
                result = "0";
                break;
            case "0/0.5":
                result = "1";
                break;
            case "0.5":
                result = "2";
                break;
            case "0.5/1":
                result = "3";
                break;
            case "1":
                result = "4";
                break;
            case "1/1.5":
                result = "5";
                break;
            case "1.5":
                result = "6";
                break;
            case "1.5/2":
                result = "7";
                break;
            case "2":
                result = "8";
                break;
            case "2/2.5":
                result = "9";
                break;
            case "2.5":
                result = "10";
                break;
            case "2.5/3":
                result = "11";
                break;
            case "3":
                result = "12";
                break;
            case "3/3.5":
                result = "13";
                break;
            case "3.5":
                result = "14";
                break;
            case "3.5/4":
                result = "15";
                break;
            case "4":
                result = "16";
                break;
            case "4/4.5":
                result = "17";
                break;
            case "4.5":
                result = "18";
                break;
            case "4.5/5":
                result = "19";
                break;
            case "5":
                result = "20";
                break;
            case "5/5.5":
                result = "21";
                break;
            case "5.5":
                result = "22";
                break;
            case "5.5/6":
                result = "23";
                break;
            case "6":
                result = "24";
                break;
            case "6/6.5":
                result = "25";
                break;
            case "6.5":
                result = "26";
                break;
            case "6.5/7":
                result = "27";
                break;
            case "7":
                result = "28";
                break;
            case "7/7.5":
                result = "29";
                break;
            case "7.5":
                result = "30";
                break;
            case "7.5/8":
                result = "31";
                break;
            case "8":
                result = "32";
                break;
            case "8/8.5":
                result = "33";
                break;
            case "8.5":
                result = "34";
                break;
            case "8.5/9":
                result = "35";
                break;
            case "9":
                result = "36";
                break;
            case "9/9.5":
                result = "37";
                break;
            case "9.5":
                result = "38";
                break;
            case "9.5/10":
                result = "39";
                break;
            case "10":
                result = "40";
                break;
        }
        return result;
    }

    public String CompareLine(GameResult Exrs, GameResult Nowrs) {
        if (Nowrs.getType() != Exrs.getType())
            return "賽事類型變更";
        else if (!Nowrs.getAwayTeam().equals(Exrs.getAwayTeam()))
            return "客隊名稱變更";
        else if (!Nowrs.getHomeTeam().equals(Exrs.getHomeTeam()))
            return "主隊名稱變更";
        else if (!Nowrs.getStartTime().equals(Exrs.getStartTime()))
            return "比賽時間變更";
        else if (!Nowrs.getLeagueName().equals(Exrs.getLeagueName()))
            return "聯盟名稱變更";
        else
            return"不明原因";
    }
    public static String getTransTime(int timetype, long time) {
        String TimePattern = "";
        switch(timetype) {
            case 1:
                TimePattern = "HH:mm:ss";
                break;
            case 2:
                TimePattern = "yyyy-MM-dd HH:mm:ss";
                break;
            default:
                TimePattern = "yyyy-MM-dd HH:mm:ss";
        }

        return (new SimpleDateFormat(TimePattern)).format(new Date(time));
    }
    public static String ComposeID(String GameID, String CompanyID, GenericEnum.LineType lineType, int LineNo) {
        return GameID + "_" + CompanyID + "_" + getGameType(lineType) + "_" + LineNo;
    }
    public static String getGameType(GenericEnum.LineType lineType) {
        switch (lineType) {
            case RunLine:
            case StandardLine:
            case DSB:
            case Goalorder:
            case DsGoal:
            case OtherPlay:
                return "0";
            case HalfRunLine:
            case HalfStandardLine:
            case HalfDSB:
                return "1";
            default:
                return "9";
        }
    }
    public static String getGameTypeTitle(int GameType){
        String result = "";
        switch (GameType) {
            case 0:
                result = "全場";
                break;
            case 1:
                result = "上半";
                break;
            case 2:
                result = "下半";
                break;
            case 3:
                result = "第一節";
                break;
            case 4:
                result = "第二節";
                break;
            case 5:
                result = "第三節";
                break;
            case 6:
                result = "第四節";
                break;
            case 7:
                result = "全場走地";
                break;
            case 8:
                result = "上半走地";
                break;
            case 9:
                result = "下半走地";
                break;
            case 10:
                result = "多種玩法";
                break;
            case 11:
                result = "";
                break;
            case 12:
                result = "單節最高分";
                break;
        }
        return result;
    }
    public static Object[] CompareLine(String oldVal, String newVal) {
        Object[] value = new Object[2];
        double old = Double.parseDouble(oldVal);
        double news = Double.parseDouble(newVal);
        value[0] = new Boolean(old != news);
        if(old > news) {
            value[1] = new Integer(2);
        } else if(old < news) {
            if(old == 0.0D) {
                value[1] = new Integer(0);
            } else {
                value[1] = new Integer(1);
            }
        } else {
            value[1] = new Integer(0);
        }

        return value;
    }
    /**
     * 判斷兩者是否相同
     * @param v1
     * @param v2
     * @return - true if both is same
     */
    public final static boolean bothSame(String v1, String v2) {
        return v1.equals(v2) || v1.indexOf(v2) != -1 || v2.indexOf(v1) != -1;
    }
    public static String trans_TS_SC_Option(String value) {
        String data[] = value.split("\\.");
        int a = Integer.parseInt(data[0]) * 4;
        if (data.length > 1) {
            switch (data[1]) {
                case "1":
                    a = a + 1;
                    break;
                case "5":
                    a = a + 2;
                    break;
                case "6":
                    a = a + 3;
                    break;
            }
        }
        return String.valueOf(a);
    }
    public static int Trans_TS_GameType(int scene,int kzdp){
        int result = 99;
        if ( scene == 11 ) {
            result = 3;
        } else if (scene == 12) {
            result = 4;
        } else if (scene == 13) {
            result = 5;
        } else if (scene == 14) {
            result = 6;
        } else if (kzdp == 2) {
            if (scene == 0) {
                result = 7;
            } else if (scene == 1) {
                result = 8;
            } else if (scene == 2) {
                result = 9;
            }
        } else if ( kzdp != 2 ) {
            if (scene == 0) {
                result = 0;
            } else if (scene == 1) {
                result = 1;
            } else if (scene == 2) {
                result = 2;
            } else if (scene == 8) {
                result = 10;
            }
        }
        return result;
    }
}
