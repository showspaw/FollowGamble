package com.heros.follow.SocketServer.Command;

import com.heros.follow.SocketServer.Command.collections.AdminCommand;
import com.heros.follow.SocketServer.Command.collections.Command;
import com.heros.follow.SocketServer.Command.collections.InstructionCommand;

/**
 * Created by root on 2017/1/26.
 */
public class CommandLoader {


    private String request;
    public CommandLoader() {
    }

    public void setRequest(String request) {
        this.request = request.trim();
    }

    public void call() {
        CommandController commandController=new CommandController();

        if(request.startsWith(Command.adminPrefix)){
            AdminCommand adminCommand=new AdminCommand();
            commandController.setCommand(adminCommand);//AdminCommand::new
            commandController.execute(request);
        } else if (request.startsWith(Command.instructionPrefix)) {
            InstructionCommand instructionCommand=new InstructionCommand();
            commandController.setCommand(instructionCommand);
            commandController.execute(request);
        }
    }
}
