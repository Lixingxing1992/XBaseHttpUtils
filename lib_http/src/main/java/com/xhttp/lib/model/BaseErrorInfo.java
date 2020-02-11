package com.xhttp.lib.model;

import com.xhttp.lib.config.BaseHttpConfig;

/**
 * Created by lixingxing on 2019/3/27.
 */
public class BaseErrorInfo {

    public BaseErrorInfo() {
    }
    // 错误码
    public BaseHttpConfig.ErrorCode errorCode = BaseHttpConfig.ErrorCode.Error_UnknowError;
    // 异常
    public Exception exception;

    // 错误信息描述
    public String errorMsg = "";

    @Override
    public String toString() {
        String errorDesc = "";
        errorDesc = errorCode.toString();
        if(errorMsg != null && !"".equals(errorMsg)){
            errorDesc = errorDesc + "\n" + errorMsg;
        }
        // 有异常信息
        if(exception!=null){
            errorDesc =  errorDesc + "\n" + exception;
        }
        return errorDesc;
    }
}
