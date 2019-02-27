package com.app.org.db.interfaces;

/**
 * 加密、解密
 * Created by lixingxing on 2018/7/5.
 */
public interface Encryption {
    public String encrypt(String data);
    public String descrypt(String data);
}
