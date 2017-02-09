package com.heros.follow.Telegram;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.heros.follow.Telegram.api.getupdates.Chat;
import com.heros.follow.Telegram.api.getupdates.GetUpdates;
import com.heros.follow.Telegram.api.getupdates.Result;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.util.EntityUtils;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
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
    private String getUpdatesUrl;
    private String sendPhotoUrl;


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

    public String getSendMessageUrl(String chat_id, String message) {
        this.sendMessageUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chat_id + "&text=" + message;
        return sendMessageUrl;
    }

    public String getGetUpdatesUrl() {
        this.getUpdatesUrl = "https://api.telegram.org/bot" + botToken + "/getupdates";
        return getUpdatesUrl;
    }

    public String getSendPhotoUrl(String chat_id, String photo) {
        this.sendPhotoUrl = "https://api.telegram.org/bot" + botToken + "/sendPhoto?chat_id=" + chat_id + "&photo=" + photo;
        return sendPhotoUrl;
    }

    public String sendPhoto(Robot robot) {
        TelegramCenter telegramCenter = new TelegramCenter(robot.getBotToken());
        String result = null;
        try {
            result = getUrlContent(telegramCenter.getSendPhotoUrl(robot.getChatId(), robot.getPhoto()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String sendMsg(Robot robot) {
        TelegramCenter telegramCenter = new TelegramCenter(robot.getBotToken());
        String result = null;
        try {

            result = getUrlContent(telegramCenter.getSendMessageUrl(robot.getChatId(), URLEncoder.encode(robot.getMsg())));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getUpdates(Robot robot) {
        TelegramCenter telegramCenter = new TelegramCenter(robot.getBotToken());
        String result = null;
        try {
            result = getUrlContent(telegramCenter.getGetUpdatesUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void post(Robot robot) throws Exception {
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        String url = "https://api.telegram.org/bot352368801:AAFWKJEIn0ANZB32yEUYEqlNFsXGEYT75b0/sendPhoto";
        HttpPost httppost = new HttpPost(url);
        File file = new File(robot.getPhoto());

        MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file, "image/jpeg");
        mpEntity.addPart("photo", cbFile);
        ArrayList<NameValuePair> postParameters = new ArrayList<>();
        postParameters.add(new BasicNameValuePair("chat_id", "-219136787"));
        postParameters.add(new BasicNameValuePair("photo", robot.getPhoto()));
        httppost.setEntity(new UrlEncodedFormEntity(postParameters));
        System.out.println("executing request " + httppost.getRequestLine());
        HttpResponse response = httpclient.execute(httppost);
        HttpEntity resEntity = response.getEntity();

        System.out.println(response.getStatusLine());
        if (resEntity != null) {
            System.out.println(EntityUtils.toString(resEntity));
        }
        if (resEntity != null) {
            resEntity.consumeContent();
        }

        httpclient.getConnectionManager().shutdown();
    }

    public List<String> findChatIds(String getUpdatesResult) {
        List<String> chatIds = Lists.newArrayList();
        Gson gson = new Gson();
        GetUpdates getUpdates = gson.fromJson(getUpdatesResult, GetUpdates.class);
        for (Result result : getUpdates.getResult()) {
            if (result.getMessage() == null || result.getMessage().getChat() == null) {
                continue;
            }
            Chat chat = result.getMessage().getChat();
            chatIds.add(chat.toString());
        }
        return chatIds;
    }


    public static void main(String[] args) {
        Robot albertRobot = RobotFactory.createAlbertRobot();
        TelegramCenter telegramCenter = new TelegramCenter();
        try {
            telegramCenter.post(albertRobot.setPhoto(ImgurCenter.getImageLink("C:\\Users\\root\\Desktop\\photo_2017-02-09_11-36-11.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }

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
