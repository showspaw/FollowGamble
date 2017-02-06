package com.heros.follow.SocketServer.Command.collection.mgrCmd;

import com.heros.follow.gamble.threads.ThreadManager;

/**
 * Created by root on 2017/2/6.
 */
public class ThreadCmdQuery implements ThreadCenterCmd {
    @Override
    public String exe(String req) {
        return ThreadManager.getInstance().findAllThreads();
    }
}
