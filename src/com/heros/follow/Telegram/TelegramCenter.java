package com.heros.follow.Telegram;

import com.beust.jcommander.internal.Lists;
import com.fasterxml.jackson.core.JsonProcessingException;
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
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreProtocolPNames;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.testng.reporters.Files;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;


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

    public String getSendMessageUrl(String chat_id,String message) {
        this.sendMessageUrl = "https://api.telegram.org/bot" + botToken + "/sendMessage?chat_id=" + chat_id + "&text=" + message;
        return sendMessageUrl;
    }
    public String getGetUpdatesUrl() {
        this.getUpdatesUrl = "https://api.telegram.org/bot" + botToken+"/getupdates";
        return getUpdatesUrl;
    }
    public String getSendPhotoUrl(String chat_id,String photo) {
        this.sendPhotoUrl = "https://api.telegram.org/bot"+ botToken+"/sendPhoto?chat_id="+chat_id+ "&photo="+photo;
        return sendPhotoUrl;
    }

    public String sendPhoto(Robot robot) {
        TelegramCenter telegramCenter = new TelegramCenter(robot.getBotToken());
        String result = null;
        try {
            result=getUrlContent(telegramCenter.getSendPhotoUrl(robot.getChatId(), robot.getPhoto()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public String sendMsg(Robot robot) {
        TelegramCenter telegramCenter = new TelegramCenter(robot.getBotToken());
        String result = null;
        try {

            result=getUrlContent(telegramCenter.getSendMessageUrl(robot.getChatId(),URLEncoder.encode(robot.getMsg()) ));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String getUpdates(Robot robot) {
        TelegramCenter telegramCenter = new TelegramCenter(robot.getBotToken());
        String result = null;
        try {
            result=getUrlContent(telegramCenter.getGetUpdatesUrl());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void post(Robot robot) throws  Exception{
        HttpClient httpclient = new DefaultHttpClient();
        httpclient.getParams().setParameter(CoreProtocolPNames.PROTOCOL_VERSION, HttpVersion.HTTP_1_1);
        String url = "https://api.telegram.org/bot352368801:AAFWKJEIn0ANZB32yEUYEqlNFsXGEYT75b0/sendPhoto";
        HttpPost httppost = new HttpPost(url);
        File file = new File(robot.getPhoto());

                MultipartEntity mpEntity = new MultipartEntity();
        ContentBody cbFile = new FileBody(file, "image/jpeg");
        mpEntity.addPart("photo", cbFile);
        ArrayList<NameValuePair> postParameters;
        postParameters = new ArrayList<NameValuePair>();

        postParameters.add(new BasicNameValuePair("chat_id", "-219136787"));
        postParameters.add(new BasicNameValuePair("photo", robot.getPhoto()));
        httppost.setEntity(new UrlEncodedFormEntity(postParameters));
//        httppost.setEntity(mpEntity);
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
        GetUpdates getUpdates=gson.fromJson(getUpdatesResult, GetUpdates.class);
        for(Result result:getUpdates.getResult()){
            Chat chat = result.getMessage().getChat();
            chatIds.add(chat.toString());
        }
        return chatIds;
    }
    private final ObjectMapper objectMapper = new ObjectMapper();
    public String httpPost(Robot robot) {
        CloseableHttpClient httpclient;
        httpclient = HttpClientBuilder.create()
                .setSSLHostnameVerifier(new NoopHostnameVerifier())
                .setConnectionTimeToLive(70, TimeUnit.SECONDS)
                .setMaxConnTotal(100)
                .build();
        String url="https://api.telegram.org/bot352368801:AAFWKJEIn0ANZB32yEUYEqlNFsXGEYT75b0/sendPhoto";
        //http client
        HttpPost httpPost = new HttpPost(url);
        httpPost.addHeader("charset", StandardCharsets.UTF_8.name());
        BasicHttpParams basicHttpParams = new BasicHttpParams();
        basicHttpParams.setParameter("chat_id",-219136787);
        basicHttpParams.setParameter("photo", "C:\\Users\\root\\Desktop\\download.jpg");
        httpPost.setParams(basicHttpParams);
//        httpPost.setConfig(requestConfig);
        try {
            httpPost.setEntity(new StringEntity(objectMapper.writeValueAsString(null), ContentType.APPLICATION_JSON));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        String responseContent="";
        try {
            CloseableHttpResponse response = httpclient.execute(httpPost);
            HttpEntity ht = response.getEntity();
            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            responseContent=EntityUtils.toString(buf, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseContent;
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
        String photo = "https://media.licdn.com/mpr/mpr/AAEAAQAAAAAAAAk-AAAAJDJiN2Q0MjFhLTVlOTAtNGJlMS1iZmFjLTA3Yzc4Yzk3YmQ5ZQ.png";
        Robot albertRobot=new Robot(albertToken,groupId,"123 gg cc",photo);
        TelegramCenter telegramCenter = new TelegramCenter();
        try {
            telegramCenter.post(albertRobot.setPhoto(ImgurCenter.getImageLink("C:\\Users\\root\\Desktop\\photo_2017-02-09_11-36-11.jpg")));
        } catch (Exception e) {
            e.printStackTrace();
        }
//        telegramCenter.httpPost(albertRobot.setPhoto(URLEncoder.encode("C:\\Users\\root\\Desktop\\download.jpg")));
//        telegramCenter.sendMsg(new Robot(jsBotToken, jsJavaGroupChatId, msg));
//        String x=telegramCenter.getUpdates(albertRobot);
//        File f = new File("C:\\Users\\root\\Desktop\\download (1).jpg");
//        try {
//            FileInputStream fileInputStream = new FileInputStream(f);
//            String content = Files.readFile(f);
//            EntityUtils.toString(new StringEntity(content));
//            telegramCenter.sendMsg(albertRobot.setPhoto(fileInputStream.toString()));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        String x=telegramCenter.sendPhoto(albertRobot);
//        List<String> chatIds=telegramCenter.findChatIds(x);
//        for (String s : chatIds) {
//            System.out.println(s);
//        }
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
