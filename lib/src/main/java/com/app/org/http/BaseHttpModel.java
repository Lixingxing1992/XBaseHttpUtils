package com.app.org.http;

import com.app.org.http.interfaces.BaseHttpHandlerCallBack;

/**
 * Created by lixingxing on 2018/4/16.
 */
public class BaseHttpModel {
    public String url;
    public Object[] params = new Object[0];
    public BaseHttpConfig.RequestType request_type;
    public Class cls;
    public String resultCode;

    public BaseHttpHandlerCallBack baseHttpHandlerCallBack;
    //别的回调
    public Object baseOtherHttpHandlerCallBack;
}
