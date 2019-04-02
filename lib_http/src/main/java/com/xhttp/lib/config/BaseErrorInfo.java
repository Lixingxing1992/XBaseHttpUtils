package com.xhttp.lib.config;

/**
 * Created by lixingxing on 2019/3/27.
 */
public class BaseErrorInfo {
    // 错误类型
    public BaseHttpConfig.ErrorType errorType = BaseHttpConfig.ErrorType.Error_Request;
    // 错误码
    public BaseHttpConfig.ErrorCode errorCode = BaseHttpConfig.ErrorCode.Error_ResultErrorCode;
    // 错误信息描述
    public String errorMsg = "这里有错误信息";

    // 异常
    public Exception requestException;

    public String getErrorMsg(){
        // 有异常信息
        if(requestException!=null){
            return errorType.toString() + "," +errorCode.toString() + "\n" + requestException;
        }
        return errorType.toString() + "," +errorCode.toString();
    }
}
