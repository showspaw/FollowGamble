package com.heros.follow.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Albert on 2017/1/9.
 */
public class HandleJSONData{
    private final static Gson gson = new GsonBuilder().setExclusionStrategies(new MyExclusionStrategy()).serializeNulls().create();

    public <T> List<T> toObjectList(String data, Type tClass) throws Exception{
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().serializeNulls().create();
        List<T> objectList=new ArrayList<>();
        for (JsonElement jString : gson.fromJson(data, JsonArray.class)) {
            objectList.add(gson.fromJson(jString, tClass));
        }
        return objectList;
    }
    public <T> List<T> toList(String data,Type tClass) throws Exception{//, Type tClass
        Gson gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().serializeNulls().create();
        List<T> objectList=gson.fromJson(data, new TypeToken<T>() {
        }.getType());
        return objectList;
    }
    /**
     * 將資料自動轉成 josnobject or jsonarray
     * @param data - 資料
     * @return JsonElement - 需自行轉型
     */
    public final static JsonElement getElement(String data) {
        JsonElement jsonElement = gson.fromJson(data, JsonElement.class);
        try {
            if (jsonElement.isJsonArray()) {
                return jsonElement.getAsJsonArray();
            } else {
                return jsonElement.getAsJsonObject();
            }
        } catch (Exception e) {
            return null;
        }
    }
    /**
     * 將資料轉換成Json
     * @param obj - 要轉換的資料
     * @return 轉換好的json資料
     */
    public final static JsonElement JsonTree(Object obj) {
        return gson.toJsonTree(obj);
    }

}
