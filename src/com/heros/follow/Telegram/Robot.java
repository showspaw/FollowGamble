package com.heros.follow.Telegram;

/**
 * Created by root on 2017/2/8.
 */
public class Robot {
    public Robot(String botToken) {
        this.botToken = botToken;
    }

    public Robot(String botToken, String chatId, String msg) {
        this.botToken = botToken;
        this.chatId = chatId;
        this.msg = msg;
    }

    public Robot(String botToken, String chatId, String msg, String photo) {
        this.botToken = botToken;
        this.chatId = chatId;
        this.msg = msg;
        this.photo = photo;
    }

    String botToken;
    String chatId;
    String msg;
    String photo;

    public String getPhoto() {
        return photo;
    }

    public Robot setPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    public String getBotToken() {
        return botToken;
    }

    public void setBotToken(String botToken) {
        this.botToken = botToken;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public String getMsg() {
        return msg;
    }

    public Robot setMsg(String msg) {
        this.msg = msg;
        return this;
    }
}
