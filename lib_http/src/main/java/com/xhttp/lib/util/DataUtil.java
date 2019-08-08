package com.xhttp.lib.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * 解析返回值
 * Created by lixingxing on 2019/4/4.
 */
public class DataUtil {
    /**
     * json解析成 List<T>列表对象,解析错误的话 为null
     * @param jsonString
     * @param clz
     * @param <T>
     */
    public static <T> List<T> parseJsonToList(String jsonString, Class<T> clz) throws Exception{
        List<T> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        try {
            JsonArray Jarray = (JsonArray) parser.parse(jsonString);
            for (JsonElement jsonElement : Jarray) {
                T t = parseJsonToObject(jsonElement.toString(),clz);
                if(t == null){
                    list = null;
                    break;
                }else{
                    list.add(t);
                }
            }
        }catch (Exception e){
            list = null;
            throw e;
        }
        return list;
    }
    /**
     * json解析成 T 对象,解析错误的话 为null
     * @param jsonString
     * @param clz
     * @param <T>
     */
    public static <T> T parseJsonToObject(String jsonString, Class<T> clz) throws Exception{
        T t = null;
        Gson gson = new Gson();
        try{
            t = gson.fromJson(jsonString, clz);
        }catch (Exception e){
            throw e;
        }
        return t;
    }
}
