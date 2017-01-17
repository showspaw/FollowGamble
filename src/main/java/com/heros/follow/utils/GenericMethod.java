package com.heros.follow.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 2017/1/16.
 */
public class GenericMethod {
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
