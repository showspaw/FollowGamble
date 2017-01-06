package com.heros.follow.web.ctrls;

import com.google.common.base.Joiner;
import com.heros.follow.web.po.User;
import com.heros.follow.web.repository.UserRepository;
import com.heros.follow.web.requests.UserRequest;
import com.heros.follow.web.utils.Crytography;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * Created by root on 2017/1/4.
 */
@RestController
public class TestController {
    @Autowired
    private UserRepository userRepository;
    @RequestMapping(path="/alert",method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void alert() {
        Iterable<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            System.out.println(user.toString());
        }
    }
    @RequestMapping(path="/put",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void insertOrUpdate(@RequestBody List<UserRequest> userReq) {
        Date date = new Date();
        for (UserRequest ur : userReq) {
            User user;
            if(userRepository.existsAccount(ur.getAccount())){
                user=userRepository.findByAccount(ur.getAccount());
                String password=Crytography.encodeSHA1(Joiner.on(";").join(ur.getAccount(),ur.getPassword(),user.getCreateDateTime()));
                user.update(password, ur.getName(), ur.getMail(), date, ur.getLoginIp());
            }else {
                String password= Crytography.encodeSHA1(Joiner.on(";").join(ur.getAccount(),ur.getPassword(),date));
                user = new User(ur.getAccount(),password,ur.getName(),ur.getMail(),date,null,ur.getLoginIp());
                userRepository.save(user);
            }
        }
    }
}
