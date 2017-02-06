package com.heros.follow.Exceptions;

/**
 * Created by root on 2017/2/6.
 */
public class JSException extends Exception {
    private JSException(String message) {
        super(message);
    }

    public JSException() {

    }

    /***
     * 命令相關問題
     * @param message
     * @return
     */
    public static Exception Command(String message) {
        return new JSException("JS1000:"+message);
    }
}
