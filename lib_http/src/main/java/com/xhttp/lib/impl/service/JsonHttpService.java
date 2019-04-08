package com.xhttp.lib.impl.service;

import android.util.Log;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.IHttpService;
import com.xhttp.lib.util.RequestUtil;
import com.xhttp.lib.util.encryption.BaseEncodeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * Json请求
 * Created by lixingxing on 2019/3/27.
 */
public class JsonHttpService implements IHttpService {

    @Override
    public BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return baseResult.errorInfo;
    }

    @Override
    public Object parseParams(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        String params = "";
        // 如果是post提交方式 处理参数
        if(baseHttpParams.request_type == BaseHttpConfig.RequestType.POST){
            if(baseHttpParams.params instanceof Object[]){
                Object[] p = (Object[]) baseHttpParams.params;
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < p.length; i+=2) {
                    try {
                        jsonObject.put((String) p[i],p[i+1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                params = jsonObject.toString();
            }else if(baseHttpParams.params instanceof Map){
                Map p = (Map) baseHttpParams.params;
                JSONObject jsonObject = new JSONObject(p);
                params = jsonObject.toString();
            }else{
                params = (String) baseHttpParams.params;
            }
            if (baseHttpParams.openLog) {
                Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                        baseHttpParams.request_type.toString() + "\n" +
                        baseResult.responseType.toString()+ "\n" +
                        baseHttpParams.url + "\n" +
                        params);
            }
        }else{
            if (baseHttpParams.openLog) {
                Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                        baseHttpParams.request_type.toString() + "\n" +
                        baseResult.responseType.toString()+ "\n" +
                        baseHttpParams.url);
            }
        }
        return params;
    }

    @Override
    public void request(BaseHttpParams baseHttpParams,BaseResult baseResult) {
        byte[] result = new RequestUtil(baseHttpParams, baseResult)
                .Reqeust_ConnectTimeOut(baseHttpParams.timeout_connect)
                .Reqeust_ReadTimeOut(baseHttpParams.timeout_read)
                .Request_ContentType(BaseHttpConfig.ParamType.JSON)
                .Request_requestType(baseHttpParams.request_type)
                .request((baseHttpParams.params == null ? "" : baseHttpParams.params.toString()), baseHttpParams.url);
    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return !baseResult.isRequestSuccess;
    }
}
