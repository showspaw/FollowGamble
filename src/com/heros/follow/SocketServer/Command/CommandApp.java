package com.heros.follow.SocketServer.Command;

/**
 * Created by Show on 2017/1/26.
 */
public class CommandApp {
    public static void main(String[] args) {
        CommandLoader commandLoader = new CommandLoader();
        commandLoader.setRequest("720");
        commandLoader.call();
        commandLoader.setRequest("710");
        commandLoader.call();
        commandLoader.setRequest("7200");
        commandLoader.call();
    }

}
