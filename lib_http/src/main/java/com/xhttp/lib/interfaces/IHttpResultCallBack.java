package com.xhttp.lib.interfaces;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpParams;

/**
 * Created by lixingxing on 2019/3/26.
 */
public interface IHttpResultCallBack {
    // 请求发出之前
    void onBeforeRequest(BaseHttpParams baseHttpParams);
    // 调用方式失败
    void onFailUse(BaseErrorInfo errorInfo);


    // 请求开始处理
    void onRequest(BaseHttpParams baseHttpParams);
    // 请求成功
    void onSuccessRequest(BaseHttpParams baseHttpParams, BaseResult baseResult);
    void onFailRequest(BaseErrorInfo errorInfo);

    // 解析返回值之前(必须请求成功)
    void onBeforeDataParse(BaseHttpParams baseHttpParams, BaseResult baseResult);


    // 解析成功 -- 整个请求过程成功
    void onSuccess(BaseResult baseResult);

    // 空数据
    void onEmpty(BaseErrorInfo errorInfo);

    // 错误信息
    void onFail(BaseErrorInfo errorInfo);

    // 最终执行
    void onFinal(BaseResult baseResult);
}
