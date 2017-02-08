package com.heros.follow.Telegram.api.getupdates;

import com.google.common.base.Joiner;

/**
 * Created by root on 2017/2/8.
 */
public class Chat {
    private long id;
    private String title;
    private String type;
    private boolean all_members_are_administrators;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isAll_members_are_administrators() {
        return all_members_are_administrators;
    }

    public void setAll_members_are_administrators(boolean all_members_are_administrators) {
        this.all_members_are_administrators = all_members_are_administrators;
    }

    @Override
    public String toString() {
        return Joiner.on(",").join("type:"+type, "title:"+title,"id:"+ id);
    }


}
