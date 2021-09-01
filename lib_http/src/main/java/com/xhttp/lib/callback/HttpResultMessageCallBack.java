package com.xhttp.lib.callback;

import android.app.Activity;
import android.widget.Toast;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.interfaces.callback.IHttpResultCallBack;
import com.xhttp.lib.model.BaseErrorInfo;
import com.xhttp.lib.params.BaseHttpParams;

import java.lang.ref.WeakReference;

/**
 * 回调抽象类 可以把错误提示toast出来
 * Created by lixingxing on 2019/3/27.
 */
public abstract class HttpResultMessageCallBack implements IHttpResultCallBack {
    private WeakReference<Activity> mContext;
    public HttpResultMessageCallBack(Activity context){
        this.mContext = new WeakReference<>(context);
    }
    @Override
    public void onBeforeRequest(BaseHttpParams baseHttpParams) {

    }
    @Override
    public void onFail(BaseErrorInfo errorInfo) {
        Toast.makeText(mContext.get(),errorInfo.toString(),Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onFinal(BaseResult baseResult) {
    }
}
