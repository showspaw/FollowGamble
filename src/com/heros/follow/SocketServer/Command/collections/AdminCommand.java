package com.heros.follow.SocketServer.Command.collections;

/**
 * Created by root on 2017/1/26.
 */
public class AdminCommand implements Command {

    @Override
    public void execute(String request) {
        System.out.println("Admin Command!");
        if(request.startsWith(threadManagerClass)){
            System.out.println("about thread command.");
        } else if (request.startsWith(dataManagerClass)) {
            System.out.println("about data command");
        }
    }
}
