package com.xhttp.lib.impl.service;

import android.util.Log;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.IHttpService;
import com.xhttp.lib.util.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

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
        // 如果是post提交方式
        if(baseHttpParams.request_type == BaseHttpConfig.RequestType.POST){
            if(baseHttpParams.params instanceof Object[]){
                Object[] p = (Object[]) baseHttpParams.params;
                for (int i = 0; i < p.length; i+=2) {
                    params =  p[i]+"="+p[i+1];
                }
            }else{
                params = (String) baseHttpParams.params;
            }
        }
        if (baseHttpParams.openLog) {
            Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                    baseHttpParams.request_type.toString() + "\n" +
                    baseResult.responseType.toString()+ "\n" +
                    baseHttpParams.url + "\n" +
                    baseHttpParams.params);
        }
        return params;
    }

    @Override
    public void request(BaseHttpParams baseHttpParams,BaseResult baseResult) {
        byte[] result = new RequestUtil(baseHttpParams, baseResult)
                .Request_ContentType(BaseHttpConfig.ParamType.JSON)
                .Request_requestType(baseHttpParams.request_type)
                .request((baseHttpParams.params == null ? "" : baseHttpParams.params.toString()), baseHttpParams.url);
    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return !baseResult.isRequestSuccess;
    }
}
