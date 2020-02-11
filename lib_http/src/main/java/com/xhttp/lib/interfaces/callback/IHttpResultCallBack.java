package com.xhttp.lib.interfaces.callback;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.model.BaseErrorInfo;
import com.xhttp.lib.params.BaseHttpParams;

/**
 * Created by lixingxing on 2019/3/26.
 */
public interface IHttpResultCallBack {
    // 请求发出之前
    void onBeforeRequest(BaseHttpParams baseHttpParams);

    // 解析成功 -- 整个请求过程成功
    void onSuccess(BaseResult baseResult);

    // 空数据  一般对list返回值有效
    void onEmpty(BaseResult baseResult);

    // 错误信息
    void onFail(BaseErrorInfo errorInfo);

    // 最终执行
    void onFinal(BaseResult baseResult);
}
