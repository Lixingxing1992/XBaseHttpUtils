package com.app.org.http.interfaces;

import android.app.Dialog;
import android.os.Handler;

import com.app.org.http.BaseHttpConfig;
import com.app.org.http.Response;
import com.app.org.utils.BaseClassUtil;

import java.util.List;

/**
 * Created by lixingxing on 2018/6/6.
 */
public abstract class BaseHttpCallBack<T> {

    public String emptyMsg = "数据获取为空";
    public BaseHttpCallBack initEmptyMsg(String emptyMsg){
        this.emptyMsg = emptyMsg;
        return this;
    }

    protected Dialog dialog;
    public BaseHttpCallBack(Dialog dialog){
        this.dialog = dialog;
    }

    IDataListener iDataListener;
    public void initIDataListener(IDataListener iDataListener){
        this.iDataListener = iDataListener;
    }

    public abstract void onSuccess(Response response);

    // 请求错误
    public void onRequestError(String error){
        // 请求时候的异常  如网络超时等
        onError(error, BaseHttpConfig.ErrorType.Error_Request);
    }
    // 返回值错误
    public void onDataError(String error){
        // 返回值提示的错误信息 如 false
        onError(error, BaseHttpConfig.ErrorType.Error_Data);
    }
    //空数据
    public void onEmpty(){
        onError(emptyMsg,  BaseHttpConfig.ErrorType.Error_Empty);
    }

    public abstract void onError(String error,BaseHttpConfig.ErrorType errorType);

    public void onFinal(){
        dismissDialog();
    }

    public void  dismissDialog() {
        if (null != dialog && dialog.isShowing()) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.dismiss();
                }
            }, 500);
        }
    }
}
