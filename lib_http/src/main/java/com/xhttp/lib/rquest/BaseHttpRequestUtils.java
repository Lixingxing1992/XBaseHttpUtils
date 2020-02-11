package com.xhttp.lib.rquest;

import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;

import org.jetbrains.annotations.NotNull;

/**
 * @author Lixingxing
 */
public class BaseHttpRequestUtils {
    @NotNull
    private BaseHttpParams mBaseHttpParams;

    public BaseHttpRequestUtils(@NotNull final BaseHttpParams baseHttpParams){
        this.mBaseHttpParams = baseHttpParams;
    }

    /**
     * 请求方法
     */
    public BaseRequestResult request(Object paramsStr){
        BaseRequestResult baseRequestResult =
                new RequestUtil(mBaseHttpParams)
                        .Request_ContentType(mBaseHttpParams.request_contentType.toString())
                        .Request_requestType(mBaseHttpParams.request_type)
                        .Reqeust_ConnectTimeOut(mBaseHttpParams.timeout_connect)
                        .Reqeust_ReadTimeOut(mBaseHttpParams.timeout_read)
                        .request(paramsStr.toString(), mBaseHttpParams.url);
        return baseRequestResult;
    }

}
