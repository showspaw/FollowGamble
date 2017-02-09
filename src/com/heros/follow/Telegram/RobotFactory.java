package com.heros.follow.Telegram;

/**
 * BotToken from #Telegram BotFather after create /newbot.
 * chat_id:find from #getupdates
 * Created by root on 2017/2/9.
 */
public class RobotFactory {
    public static final String jsBotToken = "376021850:AAF8LD5_UqFcmfM8AS-NTB2WuRH9GAbnwLQ";
    public static final String jsJavaGroupChatId = "-196885208";

    public static final String albertToken = "352368801:AAFWKJEIn0ANZB32yEUYEqlNFsXGEYT75b0";
    public static final String groupId = "-219136787";

    public static Robot createAlbertRobot() {
        Robot robot=new Robot(RobotFactory.albertToken, RobotFactory.groupId,"","");
        return robot;
    }
    public static Robot createJSJAVARobot() {
        Robot robot=new Robot(RobotFactory.jsBotToken, RobotFactory.jsJavaGroupChatId,"","");
        return robot;
    }
}
