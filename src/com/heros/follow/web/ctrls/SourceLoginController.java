package com.heros.follow.web.ctrls;

import com.heros.follow.datacenter.SendDataCenter;
import com.heros.follow.datacenter.responses.AccountResp;
import com.heros.follow.utils.HandleJSONData;
import com.heros.follow.web.po.SourceLogin;
import com.heros.follow.web.repository.SourceLoginRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * Created by root on 2017/1/4.
 */
@RestController
public class SourceLoginController {

    @Autowired
    private SourceLoginRepository sourceLoginRepository;

    @RequestMapping(path = "/sourcelogin/initial", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void initial() {
        sourceLoginRepository.deleteAll();
//        sourceLoginRepository.resetId();
        SourceLogin sourceLogin1 = new SourceLogin("","DD51126","aaa666","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin1 );
        SourceLogin sourceLogin2 = new SourceLogin("","DD51170","aa88","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin2 );
        SourceLogin sourceLogin3 = new SourceLogin("","KB67295","qaz123","TX_M","","96",new Date());
        sourceLoginRepository.save(sourceLogin3 );
        SourceLogin sourceLogin4 = new SourceLogin("","DD51167","aaa666","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin4 );
        SourceLogin sourceLogin5 = new SourceLogin("","9DH575","aa1111","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin5 );
        SourceLogin sourceLogin6 = new SourceLogin("","9DH511","aaa888","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin6 );
        SourceLogin sourceLogin7 = new SourceLogin("","DD51706","aa88","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin7 );
        SourceLogin sourceLogin8 = new SourceLogin("","DD51707","aa88","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin8 );
        SourceLogin sourceLogin9 = new SourceLogin("","DD51159","aaa666","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin9 );
        SourceLogin sourceLogin10 = new SourceLogin("","DD51759","aa88","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin10 );
        SourceLogin sourceLogin11 = new SourceLogin("","DD51761","aa88","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin11 );
        SourceLogin sourceLogin12 = new SourceLogin("","DD51762","aa88","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin12 );
        SourceLogin sourceLogin13 = new SourceLogin("","DD51763","aa88","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin13 );
        SourceLogin sourceLogin14 = new SourceLogin("","DD51765","aa88","TX_A","http://ag.td111.net","96",new Date());
        sourceLoginRepository.save(sourceLogin14 );
        SourceLogin sourceLogin15 = new SourceLogin("","avanet360","4321qaz","PHA","http://www.iwin168.us","null",new Date());
        sourceLoginRepository.save(sourceLogin15 );
        SourceLogin sourceLogin16 = new SourceLogin("","avanet720","4321qaz","PHA","http://www.iwin168.us","null",new Date());
        sourceLoginRepository.save(sourceLogin16 );
    }
    @RequestMapping(path = "/sourcelogin/getdata", method = RequestMethod.GET)
    @ResponseStatus(HttpStatus.OK)
    public void initialData() {
        for(SourceLogin sourceLogin:sourceLoginRepository.findAll()){
            System.out.println("SourceLogin sourceLogin"+sourceLogin.getId()+" = new SourceLogin(\"\",\""+sourceLogin.getAccount()+"\",\""+sourceLogin.getPassword()+"\",\""+sourceLogin.getWhere()+"\",\""+sourceLogin.getUrl()+"\",\""+sourceLogin.getWater()+"\",new Date());");
            System.out.println("sourceLoginRepository.save(sourceLogin"+sourceLogin.getId()+" );");
        }

    }
    @RequestMapping(path="/addSourceLogin",method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    @Transactional
    public void addFromCenter() {
        List<AccountResp> objects = null;
        try {
            objects = new HandleJSONData().toObjectList(new SendDataCenter().sendAccountApi(), AccountResp.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Date date = new Date();
        for (AccountResp accountResp : objects) {
            String url="";
            String name="";
            SourceLogin sourceLogin = new SourceLogin(name,accountResp.getAccount(),accountResp.getPassword(),accountResp.getWhere(),url,accountResp.getWater(),date);
            if(noDuplicate(sourceLogin)){
                sourceLoginRepository.save(sourceLogin);
            }
        }
    }

    private boolean noDuplicate(SourceLogin accountResp ) {
        for(SourceLogin sourceLogin:sourceLoginRepository.findAll()){
            if (sourceLogin.getRowValue().equals(accountResp.getRowValue())) {
                return false;
            }
        }
        return true;
    }
}
