package com.heros.follow.SocketServer.Command.collection;

/**
 * Created by Show on 2017/2/1.
 */
public class NoCommand extends ACommand {
    public NoCommand(String request) {
        super(request);
    }

    @Override
    public String execute() {
        System.out.println("No Command do nothing!");
        return "No Command find,so do nothing! Reason is request:"+getRequest();
    }
}
