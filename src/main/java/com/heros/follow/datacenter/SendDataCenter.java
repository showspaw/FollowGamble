package com.heros.follow.datacenter;

import com.google.gson.JsonObject;
import com.heros.follow.utils.Cryptography;
import com.heros.follow.tools.HttpClientUtils;

/**
 * Created by Albert on 2017/1/9.
 * 負責與【PHP控端】進行溝通的類別。
 */
public class SendDataCenter {
    // 加密的key 需對應API
    private final String sKey = "aa668899aa668899";
    private static String Account = "javafollow";
    private static String PassWord = "4321qaz";
    // 基本發送API統一的格式
    private JsonObject getSendJsonObject() {
        JsonObject jsonObjectMary = new JsonObject();
        String sTime = String.valueOf(System.currentTimeMillis());
        jsonObjectMary.addProperty("SendTime", sTime);
        jsonObjectMary.addProperty("Encryption", Cryptography.encodeAES(sKey, sTime));
        jsonObjectMary.addProperty("Ciphertext", Cryptography.encodeMD5(Account + PassWord +sTime));
        return jsonObjectMary;
    }

    // 從英雄接收帳號資料
    public String sendAccountApi() {
        JsonObject jsonObjectMary = getSendJsonObject();
        jsonObjectMary.addProperty("table", "Account_View");
        return HttpClientUtils.post(HttpClientUtils.HttpWeb.SUPER, APIRequest.DATA_API, jsonObjectMary.toString(), null, null);
    }
    // 從英雄接收一般資料
    public String sendItemApi() {
        JsonObject jsonObjectMary = getSendJsonObject();
        jsonObjectMary.addProperty("table", "Item_View");
        return HttpClientUtils.post(HttpClientUtils.HttpWeb.SUPER, APIRequest.DATA_API, jsonObjectMary.toString(), null, null);
    }

}
