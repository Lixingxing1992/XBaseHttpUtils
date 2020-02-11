package com.xhttp.lib.impl.service;

import android.util.Pair;

import com.xhttp.lib.interfaces.http.IHttpService;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;
import com.xhttp.lib.rquest.BaseHttpRequestUtils;

import org.jetbrains.annotations.NotNull;

import java.util.List;

/**
 * Created by lixingxing on 2019/3/27.
 */
public class DefaultHttpService implements IHttpService {

    protected String paramsStr = "";

    @Override
    public Object parseParams(List<Pair<String, Object>> params) {
        for (Pair<String, Object> param : params) {
            paramsStr +=  param.first + "=" + param.second + "&";
        }
        return paramsStr;
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
        BaseRequestResult baseRequestResult = baseHttpRequestUtils.request(paramsStr);
        return baseRequestResult;
    }

}
