package com.heros.follow.SocketServer.Command.collections;

/**
 * Created by Show on 2017/1/26.
 */
public class InstructionCommand implements Command {
    public void execute(String request) {
        System.out.println("Instruction Command!");
    }
}
