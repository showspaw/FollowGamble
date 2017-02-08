package com.heros.follow.Telegram.api.getupdates;

/**
 * Created by root on 2017/2/8.
 */
public class
NewChatMember {
    private long id;
    private String first_name;
    private String username;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
