package com.xhttp.lib.impl.service;

import android.util.Log;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.IDataListener;
import com.xhttp.lib.interfaces.IHttpService;
import com.xhttp.lib.util.RequestUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 豫园请求工具类
 * Created by lixingxing on 2019/4/8.
 */
public class YGHttpService implements IHttpService {
    @Override
    public BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return baseResult.errorInfo;
    }

    @Override
    public Object parseParams(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        JSONObject jsonObject = null;
        // 如果是post提交方式 处理参数
        if(baseHttpParams.request_type == BaseHttpConfig.RequestType.POST){
            if(baseHttpParams.params instanceof Object[]){
                Object[] p = (Object[]) baseHttpParams.params;
                jsonObject = new JSONObject();
                for (int i = 0; i < p.length; i+=2) {
                    try {
                        jsonObject.put((String) p[i],p[i+1]);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }else if(baseHttpParams.params instanceof Map){
                Map p = (Map) baseHttpParams.params;
                jsonObject = new JSONObject(p);
            }else{
                try {
                    jsonObject = new JSONObject(baseHttpParams.params.toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            try {
                jsonObject.put("pType","app");
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (baseHttpParams.openLog) {
                Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                        baseHttpParams.request_type.toString() + "\n" +
                        baseResult.dataParseType.toString()+ "\n" +
                        baseHttpParams.url + "\n" +
                        jsonObject == null ? "" : jsonObject.toString() + "\n"
                );
            }
        }else{
            if (baseHttpParams.openLog) {
                Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                        baseHttpParams.request_type.toString() + "\n" +
                        baseResult.dataParseType.toString()+ "\n" +
                        baseHttpParams.url);
            }
        }
        return jsonObject == null ? "" : jsonObject.toString();
    }

    @Override
    public void request(BaseHttpParams baseHttpParams,BaseResult baseResult) {
        new RequestUtil(baseHttpParams, baseResult)
                .Request_ContentType(BaseHttpConfig.ParamType.JSON)
                .Request_requestType(baseHttpParams.request_type)
                .Reqeust_ConnectTimeOut(baseHttpParams.timeout_connect)
                .Reqeust_ReadTimeOut(baseHttpParams.timeout_read)
                .request((baseHttpParams.params == null ? "" : baseHttpParams.params.toString()), baseHttpParams.url);
    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return !baseResult.isRequestSuccess;
    }
}
