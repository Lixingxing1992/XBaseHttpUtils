package com.xhttp.lib.service;

import android.util.Log;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.IHttpService;
import com.xhttp.lib.util.RequestUtil;
import com.xhttp.lib.util.encryption.BaseEncodeUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 唐道 --》需要给参数加密后再 json提交
 * Created by lixingxing on 2019/3/28.
 */
public class TDHttpService implements IHttpService {
    @Override
    public BaseResult request(BaseHttpParams baseHttpParams,BaseResult baseResult) {
        String oldParams = "";
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
                if(oldParams != null){
                    params = BaseEncodeUtil.ooEncode(oldParams);
                    jsonParams.put("data",params);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (baseHttpParams.openLog) {
                Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                        baseHttpParams.request_type.toString() + "\n" +
                        baseResult.responseType.toString()+ "\n" +
                        baseHttpParams.url + "\n" +
                        "加密前: "+ oldParams + "\n" +
                        "加密后: "+ params);
            }
        }else{
            if (baseHttpParams.openLog) {
                Log.e("BaseHttpUtils", baseHttpParams.tags + ":\n" +
                        baseHttpParams.request_type.toString() + "\n" +
                        baseResult.responseType.toString()+ "\n" +
                        baseHttpParams.url);
            }
        }
        byte[] result = new RequestUtil(baseHttpParams, baseResult)
                .Request_ContentType(BaseHttpConfig.ParamType.JSON)
                .Request_requestType(baseHttpParams.request_type)
                .request(params, baseHttpParams.url);
        return baseResult;
    }
}
