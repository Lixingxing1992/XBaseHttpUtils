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
    public BaseHttpConfig.DataParseType dataParseType = BaseHttpConfig.DataParseType.String;


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
    public BaseErrorInfo errorInfo = new BaseErrorInfo();

    public Result result;
    public Result getResult(){
        return result;
    }
    public static class Result{
        // 全部返回值
        public String resultAll;

        // 解析出来需要处理的返回值
        private Object result_object;
        private List<Object> result_list = new ArrayList<>();
        private String result_str;

        public <T> T getResult_object() {
            if(result_object != null){
                return (T)result_object;
            }
            return null;
        }

        public void setResult_object(Object result_object) {
            this.result_object = result_object;
        }

        public <T> List<T> getResult_list() {
            if(result_list != null){
                return (List<T>)result_list;
            }
            return null;
        }

        public void setResult_list(List<Object> result_list) {
            this.result_list = result_list;
        }

        public String getResult_str() {
            return result_str;
        }

        public void setResult_str(String result_str) {
            this.result_str = result_str;
        }
    }
}


