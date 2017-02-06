package com.heros.follow.SocketServer.Command.collection;

import com.heros.follow.SocketServer.Command.collection.ACommand;

/**
 * Created by root on 2017/2/3.
 */
public class LogCommand extends ACommand {
    public LogCommand(String request,Name name) {
        super(request,name);
    }

    @Override
    public String execute() {
        return null;
    }
}
