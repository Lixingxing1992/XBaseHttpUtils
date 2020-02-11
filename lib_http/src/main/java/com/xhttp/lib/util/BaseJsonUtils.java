package com.xhttp.lib.util;

import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Lixingxing
 */
public class BaseJsonUtils {

    public static String getJson(Object object){
        if(object == null){
            return "";
        }
        Gson gson = new Gson();
        return gson.toJson(object);
    }


    /**
     * json解析成 List<T>列表对象,解析错误的话 为null
     * @param jsonString
     * @param clz
     * @param <T>
     */
    public static <T> List<T> jsonToList(String jsonString, Class<T> clz) throws Exception{
        List<T> list = new ArrayList<>();
        JsonParser parser = new JsonParser();
        try {
            JsonArray Jarray = (JsonArray) parser.parse(jsonString);
            for (JsonElement jsonElement : Jarray) {
                T t = jsonToObject(jsonElement.toString(),clz);
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
    public static <T> T jsonToObject(String jsonString, Class<T> clz) throws Exception{
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
