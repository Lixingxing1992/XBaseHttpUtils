package com.xhttp.lib.util;

import android.os.Parcelable;
import android.util.Pair;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * @author Lixingxing
 */
public class BaseJsonUtils {

    public static <T> T clone(T obj){
        T cloneObj = null;
        if(obj != null){
            try {
                return jsonToObject(getJson(obj), (Class<T>)obj.getClass());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return cloneObj;
    }

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
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(
                        new TypeToken<HashMap<String, Object>>(){}.getType(),
                        new JsonDeserializer<JsonObject>() {
                            @Override
                            public JsonObject deserialize(
                                    JsonElement json, Type typeOfT,
                                    JsonDeserializationContext context) throws JsonParseException {

                                JsonObject jsonObject = json.getAsJsonObject();
                                return jsonObject;
                            }
                        }).create();
        try{
            t = gson.fromJson(jsonString, clz);
        }catch (Exception e){
            throw e;
        }
        return t;
    }
}
