package com.heros.follow.SocketServer;

import com.heros.follow.SocketServer.Command.collections.AdminCommand;

/**
 * Created by Show on 2017/1/23.
 */
public class App {
    public static void main(String[] args) throws InterruptedException {
        EchoServer echoServer = new EchoServer(2234);
        echoServer.run();
//        AdminCommand adminCommand=new AdminCommand();

    }
}
