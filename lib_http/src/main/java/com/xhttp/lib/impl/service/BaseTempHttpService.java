package com.xhttp.lib.impl.service;

import android.util.Log;
import android.util.Pair;

import com.google.gson.Gson;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.interfaces.http.IHttpService;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;
import com.xhttp.lib.rquest.BaseHttpRequestUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by lixingxing on 2019/5/5.
 */
public class BaseTempHttpService extends JsonHttpService {
    @Override
    public Object parseParams(List<Pair<String, Object>> params) {
        if (params.isEmpty()) {
            return "";
        } else {
            JSONObject jsonObject = new JSONObject();
            for (Pair<String, Object> param : params) {
                try {
                    if (param.second instanceof List) {
                        String string = new Gson().toJson(param.second);
                        JSONArray job = new JSONArray(string);
                        jsonObject.put(param.first, job);
                    } else {
                        jsonObject.put(param.first, param.second);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return jsonObject.toString();
        }
    }

    @Override
    public String getRequestParamsDesc(Object params) {
        return null;
    }

    @Override
    public @NotNull
    BaseRequestResult request(final BaseHttpParams baseHttpParams) {
        baseHttpParams.request_contentType = BaseHttpConfig.RequestContentType.JSON;
        BaseHttpRequestUtils baseHttpRequestUtils = new BaseHttpRequestUtils(
                baseHttpParams);
        BaseRequestResult baseRequestResult =
                baseHttpRequestUtils.request((baseHttpParams.params == null) ? "" : baseHttpParams.params.toString());
        return baseRequestResult;
    }
}
