package com.xhttp.lib.model;

import android.text.TextUtils;

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


    public String errorStr = "";
    public void safeShow(String str){
        if(str == ""){
            str = "网络开小差了，请重试";
        }
        if(exception != null){
            errorStr = str;
        }
    }

    @Override
    public String toString() {
        if(!TextUtils.isEmpty(errorStr)){
            return errorStr;
        }
        String errorDesc = "";
        errorDesc = errorCode.toString();
        if(errorMsg != null && !"".equals(errorMsg)){
            if(!"".equals(errorDesc)){
                errorDesc += "\n";
            }
            errorDesc += errorMsg;
        }
        // 有异常信息
        if(exception!=null){
            errorDesc += "";
        }
        if("".equals(errorDesc.trim())){
            errorDesc = "网络开小差了，请重试";
        }
        return errorDesc;
    }

}
