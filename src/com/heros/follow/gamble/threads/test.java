package com.heros.follow.gamble.threads;

import com.heros.follow.gamble.datamanager.DataManager;

/**
 * Created by root on 2017/1/19.
 */
public class test {
    public static void main(String[] args) {
        PHA_Task phaTask = new PHA_Task();
        TX_Task txTask=new TX_Task();
        ThreadManager.getInstance().startSourceSchedule(DataManager.Kinds.PHA_NBA,phaTask);

        ThreadManager.getInstance().startSourceSchedule(DataManager.Kinds.PHA_NBA,txTask);

        try {
            System.out.println("s1");
            Thread.sleep(10000);
            System.out.println("s2");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        ThreadManager.getInstance().startSourceSchedule(DataManager.Kinds.PHA_NBA,phaTask);
//        ThreadManager.getInstance().getExecutor().
//        ScheduledExecutorService executor = ThreadManager.getInstance().getExecutor();
//        PHA_Task phaTask = new PHA_Task();
//        TX_Task txTask=new TX_Task();
//        ScheduledFuture<?> scheduledFuture1 = executor.scheduleAtFixedRate(phaTask, 0, 1, TimeUnit.SECONDS);
//        ScheduledFuture<?> scheduledFuture2 = executor.scheduleAtFixedRate(txTask, 0, 1, TimeUnit.SECONDS);
//        try {
//            Thread.sleep(10);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        if(!scheduledFuture1.isCancelled()){
//            System.out.println("*****");
//            scheduledFuture1.cancel(true);
//        }
//        System.out.println(scheduledFuture1.isCancelled());
    }
}
