package com.heros.follow.web.utils;

import com.heros.follow.web.po.SourceLogin;
import com.heros.follow.web.repository.SourceLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * Created by Albert on 2017/1/12.
 */
public class TableData {
    @Autowired
    private EntityManager entityManager;
    @Autowired
    private SourceLoginRepository sourceLoginRepository;
    public void saveNewOne() {

    }
    @Transactional
    public void initial() {
    }
}
