package com.heros.follow.SocketServer.Command.collection;

import com.heros.follow.SocketServer.Command.collection.ACommand.*;
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
//        GroupType groupType = findCommandByCode(request).getAGroup();
        String response;
        Name command = findCommandByCode(request);
        ClassType classType = command.getAClass();
        CommandLoader commandLoader=new CommandLoader();
        switch (classType) {
            case 管理工具分類:
                commandLoader.setCommand(new MgrCommand(request,command));
                response=commandLoader.execute();
                break;
            case  無分類:
            default:
                commandLoader.setCommand(new NoCommand(request));
                response=commandLoader.execute();
                break;


        }
        return response;
    }
}
