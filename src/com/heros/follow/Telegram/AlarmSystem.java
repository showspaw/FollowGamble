package com.heros.follow.Telegram;

import com.google.common.collect.Sets;

import java.util.List;
import java.util.Set;

/**
 * Created by root on 2017/2/9.
 */
public class AlarmSystem {
    public static Robot robot=RobotFactory.createAlbertRobot();
    private static TelegramCenter telegramCenter = new TelegramCenter();

    public static void sendMessage(String message) {
        telegramCenter.sendMsg(robot.setMsg(message));
    }
    public static void sendChatIds() {
        List<String> chatIds = telegramCenter.findChatIds(telegramCenter.getUpdates(robot));
        Set<String> chatIdSets = Sets.newHashSet();
        for (String s : chatIds) {
            chatIdSets.add(s);
        }
        chatIdSets.stream().forEach(message->telegramCenter.sendMsg(robot.setMsg(message)));
    }

    public static void sendPhoto(String pathName) {
        try {
            //extension jpg
            if(pathName.toUpperCase().contains(".JPG")){
                if(pathName.toUpperCase().startsWith("HTTP")){
                    //online file
                    telegramCenter.sendPhoto(robot.setPhoto(pathName));
                }else{
                    //local file
                    telegramCenter.post(robot.setPhoto(ImgurCenter.getImageLink(pathName)));
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        AlarmSystem.sendChatIds();
        AlarmSystem.sendMessage("fizz");
        AlarmSystem.sendPhoto("C:\\Users\\root\\Desktop\\123.jpg");
        AlarmSystem.sendPhoto("http://i.gbc.tw/2010/zone/lol/champion/skinview_thumb/Fizz_8.jpg");
    }
}
