package com.xhttp.lib.config;

/**
 * Created by lixingxing on 2019/3/26.
 */
public class BaseHttpParams {
    public String tags = "";

    public boolean openLog = true;
    // 请求地址
    public String url;
    // 参数
    public Object params;
    // 请求方式
    public BaseHttpConfig.RequestType request_type = BaseHttpConfig.RequestType.POST;

    // 连接超时时间
    public int timeout_connect = 12*1000;
    // 读取数据超时时间
    public int timeout_read = 12*1000;

    public boolean success;
}
