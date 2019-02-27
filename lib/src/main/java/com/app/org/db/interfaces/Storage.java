package com.app.org.db.interfaces;

/**
 * Created by lixingxing on 2018/7/5.
 */
public interface Storage {

    public void put(String key, String value);

    public String get(String key);
}
