package com.app.org.http.interfaces;

import android.os.Handler;
import android.os.Message;

import com.app.org.http.BaseHttpConfig;

/**
 * Created by lixingxing on 2018/4/16.
 */
public class BaseHttpHandlerCallBack implements Handler.Callback{
    public BaseHttpConfig.ErrorCode errorCode  = BaseHttpConfig.ErrorCode.Error_Success;
    public boolean isSuccess;
    public String errorMsg;
    @Override
    public boolean handleMessage(Message message) {

        return false;
    }
}
