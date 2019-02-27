package com.app.org.db.impl;


import com.app.org.db.interfaces.Encryption;

/**
 * 加密、解密
 * Created by lixingxing on 2018/7/5.
 */
public class DefaultEncryption implements Encryption {
    @Override
    public String encrypt(String data) {
        return data;
    }

    @Override
    public String descrypt(String data) {
        return data;
    }
}
