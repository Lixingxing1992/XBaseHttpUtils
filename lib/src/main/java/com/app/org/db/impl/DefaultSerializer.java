package com.app.org.db.impl;

import com.app.org.db.DataInfo;
import com.app.org.db.interfaces.Serializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 序列化反序列化
 * Created by lixingxing on 2018/7/5.
 */
public class DefaultSerializer implements Serializer {
    Gson gson =  new Gson();

    @Override
    public <T> String serializer(T value) {

        String string = gson.toJson(value);
        return string;
    }

    @Override
    public <T> T deserializer(DataInfo dataInfo) {
        T t = null;
        switch (dataInfo.dataType) {
            case DataInfo.TYPE_LIST:
                t = toList(dataInfo);
                break;
            case DataInfo.TYPE_MAP:
                t = toList(dataInfo);
                break;
            case DataInfo.TYPE_OBJECT:
                t = (T)gson.fromJson(dataInfo.result,dataInfo.keyClass);
                break;
            default:
                break;
        }
        return t;
    }

    private <T> T toList(DataInfo dataInfo){
        //json
        String json = dataInfo.result;
        Class<?> itemClass = dataInfo.keyClass;
        List list = new Gson().fromJson(json,List.class);
        List listResult = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            String string = gson.toJson(list.get(i));
            Object o = gson.fromJson(string,itemClass);
            listResult.add(o);
        }
        return (T)listResult;
    }

    private <K,V,T> T toMap(DataInfo dataInfo){
        //json
        String json = dataInfo.result;
        Class<?> keyClass = dataInfo.keyClass;
        Class<?> valueClass = dataInfo.valueClass;

        Map<K,V> map = gson.fromJson(json,new TypeToken<Map<K, V>>() {}.getType());

        return (T)map;
    }
}
