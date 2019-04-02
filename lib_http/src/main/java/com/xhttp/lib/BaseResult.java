package com.xhttp.lib;

import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseErrorInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixingxing on 2019/3/26.
 */
public class BaseResult {
    // 返回值 数组
    public byte[] bytes;
    // 返回值解析类型
    public Class aClass;
    // 返回值解析模式 默认是String
    public BaseHttpConfig.ResponseType responseType = BaseHttpConfig.ResponseType.String;

    // 错误类型:  请求失败  解析失败  返回值提示失败
//    public BaseHttpConfig.ErrorType errorType = BaseHttpConfig.ErrorType.Error_Request;
    // 错误码
//    public BaseHttpConfig.ErrorCode errorCode = BaseHttpConfig.ErrorCode.Error_ResultErrorCode;

    // 是否成功
    public boolean success = false;
    // 请求是否成功
    public boolean isRequestSuccess = false;
    // 网络连接码   200 成功  -1 结果为空 -2有异常信息
    public int responseCode = 200; //请求返回连接码

    // 返回值解析是否成功
    public boolean isResultParseSucess = false;

    // 错误信息描述
    public BaseErrorInfo errorInfo;

    public Result result;
    public <T>Result getResult(){
        return result;
    }
    public static class Result<T>{
        public String result_str;
        public T result_object;
        public List<T> result_list = new ArrayList<>();
    }
}


