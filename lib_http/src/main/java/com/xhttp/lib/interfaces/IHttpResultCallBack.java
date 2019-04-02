package com.xhttp.lib.interfaces;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;

/**
 * Created by lixingxing on 2019/3/26.
 */
public interface IHttpResultCallBack {
    public void onSuccess(BaseResult baseResult);
    public void onFail(BaseErrorInfo errorInfo);
    public void onFinal(BaseResult baseResult);
}
