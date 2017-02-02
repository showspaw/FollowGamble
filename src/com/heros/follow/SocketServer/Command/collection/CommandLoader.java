package com.heros.follow.SocketServer.Command.collection;

/**
 * Created by Show on 2017/2/1.
 */
public class CommandLoader {
    private ACommand aCommand;

    public void setCommand(ACommand aCommand) {
        this.aCommand = aCommand;

    }

    public String execute() {
        return aCommand.execute();
    }
}
