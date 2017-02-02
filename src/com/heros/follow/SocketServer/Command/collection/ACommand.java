package com.heros.follow.SocketServer.Command.collection;

/**
 * Created by Show on 2017/2/1.
 */
public abstract class ACommand {
    ACommand(String request) {
        this.request = request;
    }
    private String request;

    public String getRequest() {
        return this.request;
    }

    public enum ClassType {
        無分類("9**"),拋接資料分類("2**"), 日誌紀錄分類("6**"),管理工具分類("7**"),查詢指令分類("0**");
        private String code;
        private ClassType(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }
    }
    public enum GroupType{
        //無分類
        無命令("99*"),
        //拋接資料分類
        //日誌紀錄分類

        //管理工具分類
        執行緒中心("71*"),
        資料中心("72*");
        //查詢指令分類

        private String code;
        private GroupType(String code) {
            this.code = code;
        }

        public String getCode() {
            return this.code;
        }

    }
    public enum Name{
        //無命令
        錯誤或無命令("999",ClassType.無分類,GroupType.無命令),
        //執行緒中心
        查詢執行緒("710", ClassType.管理工具分類, GroupType.執行緒中心),
        啟動執行緒("711", ClassType.管理工具分類, GroupType.執行緒中心),
        ;

        private String code;
        private ClassType aClass;
        private GroupType aGroup;

        private Name(String code,ClassType aClass,GroupType aGroup){
            this.code = code;
            this.aClass = aClass;
            this.aGroup = aGroup;
        }
        public ClassType getAClass() {
            return this.aClass;
        }

        public ACommand.GroupType getAGroup() {
            return this.aGroup;
        }

        public String getCode() {
            return this.code;
        }
    }
    public abstract String execute();
}
