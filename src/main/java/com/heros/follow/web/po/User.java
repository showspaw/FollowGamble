package com.heros.follow.web.po;

import com.google.common.base.Joiner;
import com.heros.follow.web.utils.Reflection;
import org.springframework.data.util.ReflectionUtils;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by root on 2017/1/5.
 */
@Entity
public class User {
    public User(String account, String password, String name, String mail, Date createDateTime, Date updateDateTime, String loginIp) {
        this.account = account;
        this.password = password;
        this.name = name;
        this.mail = mail;
        this.createDateTime = createDateTime;
        this.updateDateTime = updateDateTime;
        this.loginIp = loginIp;
    }
    public User update(String password, String name, String mail, Date updateDateTime, String loginIp) {
        this.password = password;
        this.name = name;
        this.mail = mail;
        this.updateDateTime = updateDateTime;
        this.loginIp = loginIp;
        return this;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    @Column(unique = true)
    private String account;
    private String password;
    private String name;
    private String mail;
    @Column(name="create_datetime")
    private Date createDateTime;
    @Column(name="update_datetime")
    private Date updateDateTime;
    @Column(name="login_ip")
    private String loginIp;
    public User() {
    }

    @Override
    public String toString() {
        return Joiner.on(",").useForNull("null").join(id,name,account,password,mail);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getCreateDateTime() {
        return createDateTime;
    }

    public void setCreateDateTime(Date createDateTime) {
        this.createDateTime = createDateTime;
    }

    public Date getUpdateDateTime() {
        return updateDateTime;
    }

    public void setUpdateDateTime(Date updateDateTime) {
        this.updateDateTime = updateDateTime;
    }

    public String getLoginIp() {
        return loginIp;
    }

    public void setLoginIp(String loginIp) {
        this.loginIp = loginIp;
    }
}

