package com.heros.follow.SocketServer.Command.collection;

import com.heros.follow.Exceptions.JSException;
import com.heros.follow.SocketServer.Command.*;
import com.heros.follow.SocketServer.Command.collection.ACommand.*;
import com.oracle.webservices.internal.api.message.PropertySet;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

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
        String className=this.getClass().getPackage().getName() + "." + command.getAClass().getCommandClassName();
        ACommand aCommand = null;
        try {
            Class aClass = Class.forName(className);
            Constructor<ACommand> constructor = aClass.getConstructor(String.class, Name.class);
            aCommand=constructor.newInstance(request,command);
        } catch (Exception e) {
            System.out.println(JSException.Command(e.getMessage()).getMessage());
        }
        return aCommand;
    }
//    public String call(String request) {
////        GroupType groupType = findCommandByCode(request).getAGroup();
//        String response;
//        Name command = findCommandByCode(request);
//        ClassType classType = command.getAClass();
//        CommandLoader commandLoader=new CommandLoader();
//        switch (classType) {
//            case 管理工具分類:
//                commandLoader.setCommand(new MgrCommand(request,command));
//                break;
//            case 拋接資料分類:
//                commandLoader.setCommand(new TransceiversCommand(request,command));
//                break;
//            case 日誌紀錄分類:
//                commandLoader.setCommand(new LogCommand(request,command));
//                break;
//            case 查詢指令分類:
//                commandLoader.setCommand(new InstructionCommand(request,command));
//                break;
//            case  無分類:
//            default:
//                commandLoader.setCommand(new NoCommand(request,command));
//                break;
//        }
//        response=commandLoader.execute();
//        return response;
//    }
}
