package com.heros.follow.Telegram;

import com.beust.jcommander.internal.Lists;
import com.google.gson.Gson;
import com.heros.follow.Telegram.api.getupdates.Chat;
import com.heros.follow.Telegram.api.getupdates.GetUpdates;
import com.heros.follow.Telegram.api.getupdates.Result;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by root on 2017/2/8.
 */
public class TelegramCenter {
    public TelegramCenter(String botToken) {
        this.botToken = botToken;
    }

    public TelegramCenter() {
    }

    private String botToken;
    private String chat_id;
    private String message;
    private String sendMessageUrl;
    private String findChatIdUrl;





    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getChat_id() {
        return chat_id;
    }

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSendMessageUrl(String chat_id,String message) {
        this.sendMessageUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chat_id + "&text=" + message;
        return sendMessageUrl;
    }
    public String getFindChatIdUrl() {
        this.findChatIdUrl = "https://api.telegram.org/bot" + botToken+"/getupdates";
        return findChatIdUrl;
    }

    public String sendMsg(Robot robot) {
        TelegramCenter telegramCenter = new TelegramCenter(robot.getBotToken());
        String result = null;
        try {
            result=getUrlContent(telegramCenter.getSendMessageUrl(robot.getChatId(), robot.getMsg()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getUpdates(Robot robot) {
        TelegramCenter telegramCenter = new TelegramCenter(robot.getBotToken());
        String result = null;
        try {
            result=getUrlContent(telegramCenter.getFindChatIdUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public List<String> findChatIds(String getUpdatesResult) {
        List<String> chatIds = Lists.newArrayList();
        Gson gson = new Gson();
        GetUpdates getUpdates=gson.fromJson(getUpdatesResult, GetUpdates.class);
        for(Result result:getUpdates.getResult()){
            Chat chat = result.getMessage().getChat();
            chatIds.add(chat.toString());
        }
        return chatIds;
    }
    public static void main(String[] args) {
        //BotToken from #Telegram BotFather.
        String jsBotToken = "376021850:AAF8LD5_UqFcmfM8AS-NTB2WuRH9GAbnwLQ";
        //find from #getupdates
        String jsJavaGroupChatId = "-196885208";
        String msg = "hehehehe";
        Robot jsRobot=new Robot(jsBotToken, jsJavaGroupChatId, msg);

        String albertToken = "352368801:AAFWKJEIn0ANZB32yEUYEqlNFsXGEYT75b0";
        String groupId = "-219136787";
        Robot albertRobot=new Robot(albertToken,groupId,"123");

        TelegramCenter telegramCenter = new TelegramCenter();
//        telegramCenter.sendMsg(new Robot(jsBotToken, jsJavaGroupChatId, msg));
        String x=telegramCenter.getUpdates(albertRobot);
        List<String> chatIds=telegramCenter.findChatIds(x);
        for (String s : chatIds) {
            System.out.println(s);
        }
//        String x= telegramCenter.sendMsg(albertRobot.setMsg("321"));
//        System.out.println(x);

    }

    public String getUrlContent(String sUrl) throws Exception {
        URL url = new URL(sUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setDoOutput(true);
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(5000);
        connection.connect();
        BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        String content = "", line;
        while ((line = rd.readLine()) != null) {
            content += line + "\n";
        }
        return content;

    }
}
