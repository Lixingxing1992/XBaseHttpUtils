package com.xhttp.lib.callback;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.IHttpResultCallBack;

/**
 * 回调抽象类,使用此回调可以不强制重写 onEmpty 和 onFinal
 * Created by lixingxing on 2019/3/27.
 */
public abstract class HttpResultCallBack implements IHttpResultCallBack {


    @Override
    public void onBeforeRequest(BaseHttpParams baseHttpParams) {

    }

    @Override
    public void onFailUse(BaseErrorInfo errorInfo) {

    }

    @Override
    public void onRequest(BaseHttpParams baseHttpParams) {

    }

    @Override
    public void onSuccessRequest(BaseHttpParams baseHttpParams, BaseResult baseResult) {

    }

    @Override
    public void onFailRequest(BaseErrorInfo errorInfo) {

    }

    @Override
    public void onBeforeDataParse(BaseHttpParams baseHttpParams, BaseResult baseResult) {

    }

    @Override
    public void onSuccess(BaseResult baseResult) {

    }

    @Override
    public void onEmpty(BaseErrorInfo errorInfo) {

    }

    @Override
    public void onFail(BaseErrorInfo errorInfo) {

    }

    @Override
    public void onFinal(BaseResult baseResult) {

    }
}
