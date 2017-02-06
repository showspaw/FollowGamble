package com.heros.follow.SocketServer.Command.collection;

import com.heros.follow.SocketServer.Command.collection.mgrCmd.ThreadCenterCmd;
import com.heros.follow.gamble.threads.ThreadManager;

/**
 * Created by Show on 2017/2/1.
 */
public class MgrCommand extends ACommand {

    public MgrCommand(String request,Name name) {
        super(request,name);

    }

    public abstract class Manager{

    }
    public class ThreadCenter extends Manager{
        private String queryAllThreads() {
//        return "Find all threads.\n";
            return ThreadManager.getInstance().findAllThreads();
        }
    }
    public class DataCenter extends Manager{

    }
    public Object invoker() {
        Object o = null;
        try {
              o= Class.forName(name.getaClass().getName()).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return o;
    }
    public String execute() {
        String response="";
        GroupType groupType=name.getAGroup();
        if(getRequest().contains("-")){
            String action=getRequest().substring(getRequest().lastIndexOf("-"), getRequest().length());
        }
        switch (groupType) {
            case 執行緒中心:
                ThreadCenterCmd threadCenterCmd =(ThreadCenterCmd) invoker();
                response=threadCenterCmd.exe(getRequest());
                break;
            case 資料中心:
                DataCenter dataCenter=new DataCenter();
                break;

        }
        return "執行命令:"+name.name()+"["+name.getCode()+"]\n\r"+response;
    }
//    @Override
//    public String execute() {
//        String response="";
//        GroupType groupType=name.getAGroup();
//        if(getRequest().contains("-")){
//            String action=getRequest().substring(getRequest().lastIndexOf("-"), getRequest().length());
//        }
//        switch (groupType) {
//            case 執行緒中心:
//                ThreadCenter threadCenter = new ThreadCenter();
//                switch(name){
//                    case 啟動執行緒:
//                        break;
//                    case 查詢執行緒:
//                        response = threadCenter.queryAllThreads();
//                        break;
//                }
//                break;
//            case 資料中心:
//                DataCenter dataCenter=new DataCenter();
//                break;
//
//        }
//        System.out.println("**********\n\rRequest Msg:"+getRequest()+"\n\r執行命令:"+name.name()+"["+name.getCode()+"]\n"+response+"**********" +
//                "\n");
//        return "執行命令:"+name.name()+"["+name.getCode()+"]\n\r"+response;
//
//    }



    public class Action{


    }
}
