package com.app.org.http;

import com.app.org.http.httpservice.JsonDataListener;
import com.app.org.http.httpservice.JsonHttpService;
import com.app.org.http.interfaces.BaseHttpCallBack;
import com.app.org.http.interfaces.BaseHttpHandlerCallBack;
import com.app.org.http.interfaces.IDataListener;
import com.app.org.http.interfaces.IHttpService;
import com.app.org.utils.BaseNetworkUtil;
import com.app.org.utils.BaseThreadPoolUtil;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by lixingxing on 2018/6/1.
 */
public class BaseHttpUtils {

    public static Class iHttpServiceClass;
    public static Class iDataListenerClass;

    public boolean isLog = true;
    public String tag = "";

    /**
     * 初始化
     *
     * @param iHttpServiceClasss
     * @param iDataListenerClasss
     */
    public static void init(Class iHttpServiceClasss, Class iDataListenerClasss) {
        if (iHttpServiceClasss != null && iHttpServiceClasss.getSuperclass() == IHttpService.class) {
            iHttpServiceClass = iHttpServiceClasss;
        } else {
            // 默认
            iHttpServiceClass = JsonHttpService.class;
        }
        if (iDataListenerClasss != null && iDataListenerClasss.getSuperclass() == IDataListener.class) {
            iDataListenerClass = iDataListenerClasss;
        } else {
            // 默认
            iDataListenerClass = JsonDataListener.class;
        }
    }


    // 发网络请求处理类
    public IHttpService iHttpService;
    // 发送请求封装类
    public BaseHttpModel baseHttpModel;
    // 数据解析处理类
    public IDataListener iDataListener;
    // 前端调用页面的回调类
    public BaseHttpCallBack baseHttpCallBack;
    // 返回值解析类型
    public Map<BaseHttpConfig.ResponseType, Object[]> responseTypeMap = new HashMap();
    // 返回值解析类型
    BaseHttpConfig.ResponseType responseType = BaseHttpConfig.ResponseType.Object;

    public BaseHttpUtils() {
        this(new BaseHttpModel());
    }

    public BaseHttpUtils(BaseHttpModel baseHttpModel) {
        this.baseHttpModel = baseHttpModel;
    }

    public BaseHttpUtils initIHttpService(IHttpService iHttpService) {
        this.iHttpService = iHttpService;
        return this;
    }

    public BaseHttpUtils initIDataListener(IDataListener iDataListener) {
        this.iDataListener = iDataListener;
        return this;
    }

    // 设置请求路径
    public BaseHttpUtils initUrl(String url) {
        baseHttpModel.url = url;
        return this;
    }

    // 设置请求参数
    public BaseHttpUtils initParams(Object... objects) {
        baseHttpModel.params = objects;
        return this;
    }

    // 设置请求方式 type
    public BaseHttpUtils initRequestType(BaseHttpConfig.RequestType type) {
        baseHttpModel.request_type = type;
        return this;
    }

    public BaseHttpUtils initResponseType(BaseHttpConfig.ResponseType responseType) {
        this.responseType = responseType;
        return this;
    }

    // 设置请求回调
    public BaseHttpUtils initHttpCallBack(BaseHttpCallBack iHttpCallBack) {
        this.baseHttpCallBack = iHttpCallBack;
        return this;
    }

    // 设置返回值的类型
    public BaseHttpUtils initClass(Class tClass) {
        baseHttpModel.cls = tClass;
        return this;
    }

    // 设置需要获取的返回值 code
    public BaseHttpUtils initResultCode(String resultCode) {
        baseHttpModel.resultCode = resultCode;
        return this;
    }

    public BaseHttpUtils requst() {
        BaseThreadPoolUtil.execute(new Runnable() {
            @Override
            public void run() {
                if (tag.equals("")) {
                    tag = System.currentTimeMillis() + "";
                }
                if (iHttpService == null) {
                    try {
                        iHttpService = (IHttpService) iHttpServiceClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                iHttpService.setShowLog(isLog);
                iHttpService.setTag(tag);
                if (iDataListener == null) {
                    try {
                        iDataListener = (IDataListener) iDataListenerClass.newInstance();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                iDataListener.setShowLog(isLog);
                iDataListener.setTag(tag);
                iDataListener.initResponseType(responseType);

                if (!BaseNetworkUtil.isAvailableByPing()) {
                    if (baseHttpCallBack != null) {
                        Response response = new Response();
                        response.errorCode = BaseHttpConfig.ErrorCode.Error_HASNONEW;
                        iDataListener.sendMessage(response);
                    }
                } else {
                    Response baseHttpResultModel = null;
                    if (iHttpService != null) {
                        // 如果成员变量有值 --> 有特殊处理操作
                        baseHttpResultModel = iHttpService.request(baseHttpModel);
                    }
                    if (baseHttpCallBack != null) {
                        baseHttpCallBack.initIDataListener(iDataListener);
                        iDataListener.reponse(baseHttpResultModel, baseHttpCallBack);
                    }
                }
            }
        });
        return this;
    }

}
