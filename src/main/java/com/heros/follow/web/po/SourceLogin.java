package com.heros.follow.web.po;

import com.google.common.base.Joiner;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by Albert on 2017/1/11.
 */
@Entity
public class SourceLogin {
    public SourceLogin(String name, String account, String password, String where, String url, String water,Date createTime) {
        this.name = name;
        this.account = account;
        this.password = password;
        this.where = where;
        this.url = url;
        this.water = water;
        this.createTime = createTime;
    }
    public SourceLogin(){

    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String name;
    private String account;
    private String password;
//    @Enumerated(value =EnumType.STRING)
    @Column(name = "source")
    private String where;
    private String url;
    private String water;
    @Column(name = "create_time")
    private Date createTime;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getRowValue() {
        return Joiner.on(",").join(name, account, password, water, where, url);
    }
}
