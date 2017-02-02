package com.heros.follow.SocketServer.Command;

import com.heros.follow.SocketServer.Command.collections.Command;

/**
 * Created by root on 2017/1/26.
 */
public class CommandController {
    private Command command;

    public void setCommand(Command command) {
        this.command = command;
    }

    public void execute(String request) {
        command.execute(request);
    }
}
