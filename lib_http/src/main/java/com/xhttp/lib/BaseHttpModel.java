package com.xhttp.lib;


import com.xhttp.lib.config.BaseHttpConfig;

/**
 * Created by lixingxing on 2018/4/16.
 */
public class BaseHttpModel<T> {
    public String url;
    public Object[] params = new Object[0];
    public BaseHttpConfig.RequestType request_type;
    public T cls;
    public String resultCode;
//    public BaseHttpHandlerCallBack baseHttpHandlerCallBack;
    //别的回调
    public Object baseOtherHttpHandlerCallBack;
}
