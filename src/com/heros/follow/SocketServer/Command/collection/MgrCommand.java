package com.heros.follow.SocketServer.Command.collection;

import com.heros.follow.gamble.threads.ThreadManager;

/**
 * Created by Show on 2017/2/1.
 */
public class MgrCommand extends ACommand {
    private Name name;

    MgrCommand(String request,Name name) {
        super(request);
        this.name=name;

    }

    @Override
    public String execute() {
        String response="";
        GroupType groupType=name.getAGroup();
        if(getRequest().contains("-")){
            String action=getRequest().substring(getRequest().lastIndexOf("-"), getRequest().length());
        }
        switch (groupType) {
            case 執行緒中心:
                if(name==Name.啟動執行緒){

                } else if (name == Name.查詢執行緒) {
                    response = queryAllThreads();
                }
                break;
            case 資料中心:
                break;

        }
        System.out.println("**********\n\rRequest Msg:"+getRequest()+"\n\r執行命令:"+name.name()+"["+name.getCode()+"]\n"+response+"**********" +
                "\n");
        return "執行命令:"+name.name()+"["+name.getCode()+"]\n\r"+response;

    }

    private String queryAllThreads() {
//        return "Find all threads.\n";
        return ThreadManager.getInstance().findAllThreads();
    }

    public class Action{


    }
}
