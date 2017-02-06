package com.heros.follow.SocketServer.Command.collection;

/**
 * Created by Show on 2017/2/1.
 */
public class NoCommand extends ACommand {
    public NoCommand(String request,Name name) {
        super(request,name);
    }

    @Override
    public String execute() {
        System.out.println("No Command do nothing!");
        return "執行命令:" + name.name() + "[" + name.getCode() + "]\n\r The reason is Request:" + getRequest();
    }
}
