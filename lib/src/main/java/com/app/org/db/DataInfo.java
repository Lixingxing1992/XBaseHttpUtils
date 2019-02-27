package com.app.org.db;

/**
 * Created by lixingxing on 2018/7/5.
 */
public class DataInfo {

    public static final int TYPE_LIST = 0;
    public static final int TYPE_MAP = 1;
    public static final int TYPE_OBJECT = 2;

    public int dataType = 2;
    public Class<?> keyClass = null;
    public Class<?> valueClass = null;

    // 存储的数据
    public String result;


    public DataInfo(int dataType, Class<?> keyClass, Class<?> valueClass, String result) {
        this.dataType = dataType;
        this.keyClass = keyClass;
        this.valueClass = valueClass;
        this.result = result;
    }
}
