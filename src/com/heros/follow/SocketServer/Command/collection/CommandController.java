package com.heros.follow.SocketServer.Command.collection;

import com.heros.follow.Exceptions.JSException;
import com.heros.follow.SocketServer.Command.collection.ACommand.*;

import java.lang.reflect.Constructor;

/**
 * Created by Show on 2017/2/1.
 */
public class CommandController {
    private ACommand.Name findCommandByCode(String request) {
        String requestCode = "";
        if(request.trim().length()>=3){
            requestCode = request.trim().substring(0, 3);
        }
        for (Name name:Name.values()
                ) {
            if(name.getCode().equals(requestCode)){
                return name;
            }
        }
        return Name.錯誤或無命令;
    }

    public String call(String request) {
        Name command = findCommandByCode(request);
        CommandLoader commandLoader=new CommandLoader();
        commandLoader.setCommand(createACommandImpl(request, command));
        String response = commandLoader.execute();
        return response;
    }

    private ACommand createACommandImpl(String request, Name command) {
        String className=this.getClass().getPackage().getName() + "." + command.getAClass().getClassName();
        ACommand aCommand = null;
        try {
            Class aClass = Class.forName(className);
            Constructor<ACommand> constructor = aClass.getConstructor(String.class, Name.class);
            aCommand=constructor.newInstance(request,command);
        } catch (Exception e) {
            System.err.println(JSException.Command(e.getMessage()).getMessage());
        }
        return aCommand;
    }
}
