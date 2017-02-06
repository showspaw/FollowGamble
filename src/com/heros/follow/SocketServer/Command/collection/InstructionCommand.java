package com.heros.follow.SocketServer.Command.collection;

/**
 * Created by root on 2017/2/3.
 */
public class InstructionCommand extends ACommand{

    public InstructionCommand(String request, Name name) {
        super(request, name);
    }

    @Override
    public String execute() {
        return null;
    }
}
