package com.xhttp.lib.callback;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.interfaces.callback.IHttpFileResultCallBack;

import java.io.File;

/**
 * Created by lixingxing on 2019/6/20.
 */
public abstract class HttpFileResultCallBack implements IHttpFileResultCallBack {
    @Override
    public void onFail(BaseErrorInfo errorInfo) {

    }

    @Override
    public void onEmpty(BaseErrorInfo errorInfo) {

    }

    @Override
    public void onFinal(BaseResult baseResult) {

    }

    @Override
    public void onFileProgress(int position, File file, long curlenth, long total) {

    }
}
