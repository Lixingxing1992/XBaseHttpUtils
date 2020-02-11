package com.xhttp.lib.impl.service;

import android.util.Pair;

import com.xhttp.lib.interfaces.http.IHttpService;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;
import com.xhttp.lib.rquest.BaseHttpRequestUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Lixingxing
 */
public class JsonHttpService implements IHttpService {


    @Override
    public Object parseParams(List<Pair<String, Object>> params) {
        JSONObject jsonObject = new JSONObject();
        for (Pair<String, Object> param : params) {
            try {
                jsonObject.put(param.first,param.second);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return jsonObject.toString();
    }

    @Override
    public String getRequestParamsDesc(Object params) {
        return params.toString();
    }

    @Override
    public @NotNull
    BaseRequestResult request(final BaseHttpParams baseHttpParams) {
        BaseHttpRequestUtils baseHttpRequestUtils = new BaseHttpRequestUtils(
                baseHttpParams);
        BaseRequestResult baseRequestResult =
                baseHttpRequestUtils.request((baseHttpParams.params == null)?"":baseHttpParams.params.toString());
        return baseRequestResult;
    }
}
