package com.heros.follow.threads;

import com.heros.follow.datacenter.SendDataCenter;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.concurrent.Callable;

/**
 * Created by root on 2017/1/6.
 */
public class TestThreads implements Callable {
    @PersistenceContext
    private EntityManager entityManager;
    @Override
    public Object call() throws Exception {
        return new SendDataCenter().sendAccountApi();
    }
}
