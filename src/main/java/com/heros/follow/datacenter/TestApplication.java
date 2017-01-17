package com.heros.follow.datacenter;

import com.google.gson.JsonObject;
import com.heros.follow.tools.HttpClientUtil;
import com.heros.follow.tools.HttpClientUtils;
import com.heros.follow.utils.HandleJSONData;
import com.heros.follow.datacenter.responses.AccountResp;

import java.util.List;

/**
 * Created by Albert on 2017/1/9.
 */
public class TestApplication {
    // 從英雄接收帳號資料
    public void getAccountResp() {
        try {
            List<AccountResp> objects = new HandleJSONData().toObjectList(new SendDataCenter().sendAccountApi(), AccountResp.class);
            objects.forEach( x-> {
                System.out.println(x.getAccount()+x.getPassword());
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public void getItemResp() {
//        new HandleJSONData().toObjectList(new SendDataCenter().sendItemApi(), AccountResp.class);
//    }
    public static void main(String[] args) {
//        new TestApplication().getAccountResp();
        new TestApplication().getAccountResp();
    }
}
