package com.app.org.db.impl;

import com.app.org.db.DataInfo;
import com.app.org.db.interfaces.Converter;

import java.util.List;
import java.util.Map;

/**
 * Created by lixingxing on 2018/7/5.
 */
public class DefaultConverter implements Converter {

    String getClassName(Class<?> clazz){
        if(clazz != null){
            return clazz.getName();
        }else{
            return "";
        }
    }

    @Override
    public <T> String convert(String data, T value) {
        // 获得保存数据的类型
        Class<?> clazz = value.getClass();

        Class<?> keyClass = null;
        Class<?> valueClass = null;

        int dataType;
        if(List.class.isAssignableFrom(clazz)){
            List list = (List) value;
            if(!list.isEmpty()){
                keyClass = list.get(0).getClass();
            }
            dataType = DataInfo.TYPE_LIST;
        }else if(Map.class.isAssignableFrom(clazz)){
            Map<?,?> map = (Map<?,?>)value;
            for (Map.Entry<?,?> entry: map.entrySet()) {
                keyClass = entry.getKey().getClass();
                valueClass = entry.getValue().getClass();
            }
            dataType = DataInfo.TYPE_MAP;
        }else{
            dataType = DataInfo.TYPE_OBJECT;
            keyClass = clazz;
        }
        StringBuilder result = new StringBuilder();
        result.append(getClassName(keyClass))
        .append("#")
        .append(getClassName(valueClass))
        .append("#")
        .append(dataType)
        .append("#")
        .append(data);
        return result.toString();
    }



    @Override
    public DataInfo reconver(String data) {
        String[] infos = data.split("#");
        String keyClassName = infos[0];
        Class<?> keyClass = null;
        String valueClassName = infos[1];
        Class<?> valueClass = null;
        try {
            keyClass = Class.forName(keyClassName);
            valueClass = Class.forName(valueClassName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Integer dataType = Integer.valueOf(infos[2]);
        String result = infos[3];
        DataInfo dataInfo = new DataInfo(dataType,keyClass,valueClass,result);
        return null;
    }
}
