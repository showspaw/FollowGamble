package com.heros.follow.gamble.threads;

import java.util.concurrent.TimeUnit;

/**
 * Created by root on 2017/1/19.
 */
public class PHA_Task extends Task {
    public void run() {
        try {
            TimeUnit.SECONDS.sleep(2);
            System.out.println("PHA: " + System.nanoTime());
        }
        catch (InterruptedException e) {
            System.err.println("task interrupted");
        }
    }
}
