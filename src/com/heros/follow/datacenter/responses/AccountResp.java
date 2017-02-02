package com.heros.follow.datacenter.responses;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Albert on 2017/1/9.
 */
public class AccountResp {
    private String no;
//    @SerializedName(value="id", alternate={"Id", "identify"})
    private String account;
    private String password;
    private String where;
    private String water;

    public String getNo() {
        return no;
    }

    public void setNo(String no) {
        this.no = no;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWhere() {
        return where;
    }

    public void setWhere(String where) {
        this.where = where;
    }

    public String getWater() {
        return water;
    }

    public void setWater(String water) {
        this.water = water;
    }
}
