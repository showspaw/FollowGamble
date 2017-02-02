package com.heros.follow.web.ctrls;

import com.google.common.base.Joiner;
import com.heros.follow.gamble.datamanager.DataManager;
import com.heros.follow.gamble.threads.ThreadManager;
import com.heros.follow.source.result.GameResult;
import com.heros.follow.web.po.User;
import com.heros.follow.web.repository.UserRepository;
import com.heros.follow.web.requests.UserRequest;
import com.heros.follow.utils.Cryptography;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by root on 2017/1/4.
 */
@RestController
public class UserController {
    @Autowired
    private UserRepository userRepository;
    private String string;

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String test() {
        String result = "";
        GameResult gameResult;
        Map<String, GameResult> gameResultMap = DataManager.getInstance().getGameResults().get(DataManager.Kinds.PHA_SOCCER);
        for (String key : gameResultMap.keySet()) {
            gameResult = gameResultMap.get(key);
            result += Joiner.on(",").join(gameResult.getFollowID(), gameResult.getStartTime(), gameResult.getAwayTeam(), gameResult.getHomeTeam()) + "<br>";
        }
        return result;
    }

    @RequestMapping(path = "/c", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public String serveron() {
        string = "";
        int size = ThreadManager.getInstance().checkScheduleAlive().size();
        ThreadManager.getInstance().checkScheduleAlive().forEach((k,v)->{
           string += k.toString() + ":" + v.toString()+"<br>";
        });
        string = string + "Total " + size + " threads.<br>";
        return string;
    }

    @RequestMapping(path = "/alert", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void alert() {
        Iterable<User> allUsers = userRepository.findAll();
        for (User user : allUsers) {
            System.out.println(user.toString());
        }
    }

    @RequestMapping(path = "/put", method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void insertOrUpdate(@RequestBody List<UserRequest> userReq) {
        Date date = new Date();
        for (UserRequest ur : userReq) {
            User user;
            if (userRepository.existsAccount(ur.getAccount())) {
                user = userRepository.findByAccount(ur.getAccount());
                String password = Cryptography.encodeSHA1(Joiner.on(";").join(ur.getAccount(), ur.getPassword(), user.getCreateDateTime()));
                user.update(password, ur.getName(), ur.getMail(), date, ur.getLoginIp());
            } else {
                String password = Cryptography.encodeSHA1(Joiner.on(";").join(ur.getAccount(), ur.getPassword(), date));
                user = new User(ur.getAccount(), password, ur.getName(), ur.getMail(), date, null, ur.getLoginIp());
                userRepository.save(user);
            }
        }
    }
}
