package com.xhttp.lib.callback;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.interfaces.callback.IHttpResultCallBack;
import com.xhttp.lib.model.BaseErrorInfo;
import com.xhttp.lib.params.BaseHttpParams;

/**
 * 回调抽象类,使用此回调可以不强制重写 onEmpty 和 onFinal
 * Created by lixingxing on 2019/3/27.
 */
public abstract class HttpResultCallBack implements IHttpResultCallBack {

    public HttpResultCallBack(){
    }

    @Override
    public void onBeforeRequest(BaseHttpParams baseHttpParams) {

    }

    @Override
    public void onFinal(BaseResult baseResult) {
    }

    @Override
    public void onEmpty(BaseResult baseResult) {

    }

    @Override
    public boolean onFailForResult(BaseResult baseResult) {
        return false;
    }

    @Override
    public void onFail(BaseErrorInfo errorInfo) {

    }
}
