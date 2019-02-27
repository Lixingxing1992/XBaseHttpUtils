package com.app.org.db.interfaces;


import com.app.org.db.DataInfo;

/**
 * 转化器
 * Created by lixingxing on 2018/7/5.
 */
public interface Converter {

    public <T> String convert(String data, T value);

    public DataInfo reconver(String data);
}
