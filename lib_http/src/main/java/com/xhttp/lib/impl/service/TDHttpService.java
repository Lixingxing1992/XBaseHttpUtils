package com.xhttp.lib.impl.service;

import android.util.Log;

import com.google.gson.Gson;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.http.IHttpService;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.util.RequestUtil;
import com.xhttp.lib.util.encryption.BaseEncodeUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by lixingxing on 2019/5/5.
 */
public class TDHttpService implements IHttpService {
    @Override
    public Object parseParams(BaseHttpParams baseHttpParams) {
        String oldParams = "";
        String params = "";
        // 如果是post提交方式 处理参数
        if(baseHttpParams.request_type == BaseHttpConfig.RequestType.POST){
            if(baseHttpParams.params instanceof Object[]){
                Object[] p = (Object[]) baseHttpParams.params;
                JSONObject jsonObject = new JSONObject();
                for (int i = 0; i < p.length; i+=2) {
                    try {
                        String key = p[i].toString();
                        Object value = p[i + 1];
                        if(value instanceof List){
                            String string = new Gson().toJson(value);
                            JSONArray job = new JSONArray(string);
                            jsonObject.put(key, job);
                        }else{
                            jsonObject.put(key, value);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                oldParams = jsonObject.toString();
            }else if(baseHttpParams.params instanceof Map){
                Map p = (Map) baseHttpParams.params;
                JSONObject jsonObject = new JSONObject(p);
                oldParams = jsonObject.toString();
            }else{
                oldParams = (String) baseHttpParams.params;
            }
            //将请求参数数据向服务器端发送\
            JSONObject jsonParams = new JSONObject();
            try {
                jsonParams.put("userCode","qqkj");
                jsonParams.put("passwd","123456");
                if( oldParams != null && !oldParams.equals("")){
                    params = oldParams == null ? "" : BaseEncodeUtil.ooEncode(oldParams);
                    jsonParams.put("data",params);
                }
                params = jsonParams.toString();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (baseHttpParams.openLog) {
                Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                        baseHttpParams.request_type.toString() + "\n" +
                        baseHttpParams.dataParseType.toString()+ "\n" +
                        baseHttpParams.url + "\n" +
                        "加密前: "+ oldParams + "\n" +
                        "加密后: "+ params);
            }
        }else{
            if (baseHttpParams.openLog) {
                Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                        baseHttpParams.request_type.toString() + "\n" +
                        baseHttpParams.dataParseType.toString()+ "\n" +
                        baseHttpParams.url);
            }
        }
        return params;
    }

    @Override
    public BaseRequestResult request(BaseHttpParams baseHttpParams) {
        return new RequestUtil(baseHttpParams)
                .Request_ContentType(BaseHttpConfig.ParamType.JSON)
                .Request_requestType(baseHttpParams.request_type)
                .Reqeust_ConnectTimeOut(baseHttpParams.timeout_connect)
                .Reqeust_ReadTimeOut(baseHttpParams.timeout_read)
                .request((baseHttpParams.params == null ? "" : baseHttpParams.params.toString()), baseHttpParams.url);
    }
}
