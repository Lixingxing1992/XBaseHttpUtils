package com.xhttp.lib.config;

/**
 * Created by lixingxing on 2019/3/27.
 */
public class BaseErrorInfo {

    public BaseErrorInfo() {
    }

    public BaseErrorInfo( BaseHttpConfig.ErrorCode errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    // 错误类型
//    public BaseHttpConfig.ErrorType errorType = BaseHttpConfig.ErrorType.Error_Request;
    // 错误码
    public BaseHttpConfig.ErrorCode errorCode = BaseHttpConfig.ErrorCode.Error_UnknowHttpError;
    // 错误信息描述
    public String errorMsg = "这里有错误信息";
    // 异常
    public Exception exception;

    public String getErrorMsg(){
        // 有异常信息
        if(exception!=null){
            return errorCode.toString() + "\n" + exception;
        }
        return errorCode.toString();
    }
}
