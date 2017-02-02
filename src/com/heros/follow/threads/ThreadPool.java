package com.heros.follow.threads;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 2017/1/6.
 */
public class ThreadPool {
    private ExecutorService executorService;

    private ThreadPool() {

    }

    public ExecutorService create(int nThreads) {
        executorService = Executors.newFixedThreadPool(nThreads);
        return executorService;
    }

    public void shutDown() {
        if(!executorService.isShutdown())
            executorService.shutdown();
    }

    public static void main(String[] args) {
        ExecutorService executorService=new ThreadPool().create(5);
        TestThreads task = new TestThreads();
        Future future = executorService.submit(task);
        System.out.println(future.isDone());
    }
}
