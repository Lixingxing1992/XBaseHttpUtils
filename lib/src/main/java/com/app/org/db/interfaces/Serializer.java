package com.app.org.db.interfaces;


import com.app.org.db.DataInfo;

/**
 * 序列化
 * Created by lixingxing on 2018/7/5.
 */
public interface Serializer {

    public <T> String serializer(T value);

    public <T> T deserializer(DataInfo dataInfo);
}
