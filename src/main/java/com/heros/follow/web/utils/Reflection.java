package com.heros.follow.web.utils;

import com.google.common.collect.Lists;
import sun.net.www.content.text.Generic;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by root on 2017/1/5.
 */
public class Reflection<E> {

//    public String findVariablesNameValue(Class clazz,T... anything){
//        ArrayList<HashMap<String, Object>> variablesNameValueList= Lists.newArrayList();
//        for (T t : anything) {
//            try {
//                HashMap<String, Object> map=new HashMap<>();
//
//                map.put("variableName", clazz.getDeclaredField(t.toString()).getName());
//                map.put("variableValue", t==null?"":t);
//                variablesNameValueList.add(map);
//            } catch (NoSuchFieldException e) {
//                e.printStackTrace();
//            }
//        }
//
//        StringBuilder sb=new StringBuilder();
//        for (HashMap<String, Object> map : variablesNameValueList) {
//            sb.append(map.get("variableName")).append("=").append(map.get("variableValue")).append(",");
//        }
//        return sb.toString();
//    }
}
