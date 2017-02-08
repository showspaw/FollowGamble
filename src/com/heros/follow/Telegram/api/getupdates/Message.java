package com.heros.follow.Telegram.api.getupdates;

/**
 * Created by root on 2017/2/8.
 */
public class Message {
    private long message_id;
    private From from;
    private Chat chat;
    private long date;
    private String text;
    private NewChatParticipant new_chat_participant;
    private NewChatMember new_chat_member;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public long getMessage_id() {
        return message_id;
    }

    public void setMessage_id(long message_id) {
        this.message_id = message_id;
    }

    public From getFrom() {
        return from;
    }

    public void setFrom(From from) {
        this.from = from;
    }

    public Chat getChat() {
        return chat;
    }

    public void setChat(Chat chat) {
        chat = chat;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }

    public NewChatParticipant getNew_chat_participant() {
        return new_chat_participant;
    }

    public void setNew_chat_participant(NewChatParticipant new_chat_participant) {
        this.new_chat_participant = new_chat_participant;
    }

    public NewChatMember getNew_chat_member() {
        return new_chat_member;
    }

    public void setNew_chat_member(NewChatMember new_chat_member) {
        this.new_chat_member = new_chat_member;
    }
}
