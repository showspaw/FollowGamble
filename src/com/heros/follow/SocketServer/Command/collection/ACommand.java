package com.heros.follow.SocketServer.Command.collection;

import com.heros.follow.SocketServer.Command.collection.mgrCmd.ThreadCmdQuery;
import com.heros.follow.SocketServer.Command.collection.mgrCmd.ThreadCmdStart;

/**
 * Created by Show on 2017/2/1.
 */
public abstract class ACommand {
    ACommand(String request,Name name) {
        this.request = request;
        this.name = name;
    }
    private String request;
    protected Name name;
    public String getRequest() {
        return this.request;
    }

    public enum ClassType {
        查詢指令分類("0**","InstructionCommand"),
        拋接資料分類("2**","TransceiversCommand"),
        日誌紀錄分類("6**","LogCommand"),
        管理工具分類("7**","MgrCommand"),
        無分類("9**","NoCommand");
//        InstructionCommand("0**","查詢指令分類"),
//        TransceiversCommand("2**","拋接資料分類"),
//        LogCommand("6**","日誌紀錄分類"),
//        MgrCommand("7**","管理工具分類"),
//        NoCommand("9**","無分類");
        private String code;
        private String className;
        private ClassType(String code,String className) {
            this.code = code;
            this.className = className;
        }

        public String getClassName() {
            return this.className;
        }

        public String getCode() {
            return this.code;
        }
    }
    public enum GroupType{
        //無分類
        無命令("99*",""),
        //拋接資料分類
        //日誌紀錄分類

        //管理工具分類
        執行緒中心("71*","ThreadCenter"),
        資料中心("72*","DataCenter");
        //查詢指令分類

        private String code;
        private String className;
        private GroupType(String code,String className) {
            this.code = code;
            this.className = className;
        }

        public String getClassName() {
            return className;
        }

        public String getCode() {
            return this.code;
        }

    }
    public enum Name{
        //無命令
        錯誤或無命令("999",ClassType.無分類,GroupType.無命令,NoCommand.class),
        //執行緒中心
        查詢執行緒("710", ClassType.管理工具分類, GroupType.執行緒中心, ThreadCmdQuery.class),
        啟動執行緒("711", ClassType.管理工具分類, GroupType.執行緒中心, ThreadCmdStart.class),
        ;

        private String code;
        private ClassType classType;
        private GroupType aGroup;
        private Class aClass;

        private Name(String code, ClassType classType, GroupType aGroup,Class aClass){
            this.code = code;
            this.classType = classType;
            this.aGroup = aGroup;
            this.aClass = aClass;
        }
        public ClassType getAClass() {
            return this.classType;
        }

        public ACommand.GroupType getAGroup() {
            return this.aGroup;
        }

        public String getCode() {
            return this.code;
        }

        public Class getaClass() {
            return aClass;
        }
    }
    public abstract String execute();
}
