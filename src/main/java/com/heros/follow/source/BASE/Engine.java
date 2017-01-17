package com.heros.follow.source.BASE;

/**
 * Created by Albert on 2017/1/12.
 */
public abstract class Engine implements Runnable{
    private LoginStatus loginStatus;
    public enum LoginStatus{
        FAILED, MAINTAIN, SUCCESS
    }
//    public abstract LoginStatus login(String ac,String pw);
//
//    public abstract LoginStatus relogin(String ac,String pw);

}
