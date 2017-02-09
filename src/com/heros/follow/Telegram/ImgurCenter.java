package com.heros.follow.Telegram;

import com.google.gson.Gson;
import com.heros.follow.Telegram.api.getupdates.imgur.ImgurResult;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ContentBody;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by root on 2017/2/8.
 */
public class ImgurCenter {
    public static String executePost(String pathName) {
        HttpPost  httpPost = new HttpPost("https://api.imgur.com/3/image");
        ArrayList<NameValuePair> postParameters;
        postParameters = new ArrayList<NameValuePair>();
        postParameters.add(new BasicNameValuePair("Authorization", "Client-ID 45c782b483a0f7b"));
        MultipartEntity mpEntity = null;
        ContentBody cbFile = null;
        try {
            File f = new File(pathName);
            mpEntity = new MultipartEntity();
            cbFile = new FileBody(f, "image/jpeg");
            mpEntity.addPart("image", cbFile);
            httpPost.addHeader("Authorization","Client-ID 45c782b483a0f7b");
            httpPost.setEntity(mpEntity);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String responseString="";
        HttpClient httpclient = new DefaultHttpClient();
        try {
            HttpResponse response = httpclient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            responseString = EntityUtils.toString(entity, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return responseString;
    }

    public static String getImageLink(String pathName) {
        Gson gson = new Gson();
        ImgurResult imgurResult = gson.fromJson(ImgurCenter.executePost(pathName), ImgurResult.class);
        String link = imgurResult.getData().getLink();
        return link;
    }

    public static void main(String[] args) {
        String imageLink = ImgurCenter.getImageLink("C:\\Users\\root\\Desktop\\images.jpg");
        System.out.println(imageLink);
    }
}
