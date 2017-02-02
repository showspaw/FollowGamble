package com.heros.follow.gamble.threads;

import com.google.common.collect.Maps;
import com.heros.follow.gamble.datamanager.DataManager;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;

/**
 * Created by root on 2017/1/19.
 */
public class ThreadManager {
    private static ThreadManager threadManager;
    private static ScheduledExecutorService executor;
    private static Map<DataManager.Kinds,ScheduledFuture<?>> scheduledFutureMap;
    //法老爬蟲執行緒by GameType
    private static Map<DataManager.PHA,ScheduledFuture<?>> phaScheduledFutureMap;
    private ThreadManager() {
        scheduledFutureMap = Maps.newConcurrentMap();
        phaScheduledFutureMap = Maps.newConcurrentMap();
        executor = Executors.newScheduledThreadPool(20);
    }

    public static ScheduledExecutorService getExecutor() {
        return executor;
    }
    public static ThreadManager getInstance() {
        if(threadManager==null){
            synchronized(ThreadManager.class){
                threadManager = new ThreadManager();
            }
        }
        return threadManager;
    }

    public void startSchedules() {
        PHA_Task phaTask = new PHA_Task();
        ScheduledFuture<?> phaScheduledFuture = executor.scheduleAtFixedRate(phaTask, 0, 5, TimeUnit.SECONDS);
        TX_Task txTask=new TX_Task();
        ScheduledFuture<?> txScheduledFuture1 = executor.scheduleAtFixedRate(txTask, 0, 5, TimeUnit.SECONDS);
    }
    public void startSourceSchedule(DataManager.Kinds aKind, Runnable task) {
        if(scheduledFutureMap.get(aKind)!=null&&!scheduledFutureMap.get(aKind).isCancelled()){
                scheduledFutureMap.get(aKind).cancel(true);
        }
        ScheduledFuture<?> phaScheduledFuture = executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
        scheduledFutureMap.put(aKind,phaScheduledFuture);
    }
    public void startPHASchedule(DataManager.PHA aKind, Runnable task) {
        if(scheduledFutureMap.get(aKind)!=null&&!scheduledFutureMap.get(aKind).isCancelled()){
                scheduledFutureMap.get(aKind).cancel(true);
        }
        ScheduledFuture<?> phaScheduledFuture = executor.scheduleAtFixedRate(task, 0, 5, TimeUnit.SECONDS);
        phaScheduledFutureMap.put(aKind,phaScheduledFuture);
    }

    public static HashMap<Object,Object> checkScheduleAlive() {
        HashMap<Object,Object> result = new HashMap();
        if(scheduledFutureMap.size()>0){
            scheduledFutureMap.forEach((k,v)->{
                result.put(k.name(), !v.isCancelled());
            });
        }
        if(phaScheduledFutureMap.size()>0){
            phaScheduledFutureMap.forEach((k,v)->{
                result.put("PHA_"+k.name(), !v.isCancelled());
            });
        }
        return result;
    }

    public static String allThreads="";
    public static String findAllThreads() {
        allThreads = "";
        int size = checkScheduleAlive().size();

        checkScheduleAlive().forEach((k, v)->{
            allThreads += k.toString() + ":" + v.toString()+"\n\r";
        });
        allThreads = allThreads + "Total " + size + " threads.\n\r";
        return allThreads;
    }
}
