package com.app.org.db.impl;


import com.app.org.db.interfaces.Storage;

/**
 * Created by lixingxing on 2018/7/5.
 */
public class DefaultStorage implements Storage {
    @Override
    public void put(String key, String value) {

    }

    @Override
    public String get(String key) {
        return key;
    }
}
