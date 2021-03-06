package com.xhttp.lib;

import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.model.BaseRequestResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixingxing on 2019/3/26.
 */
public class BaseResult {
    public BaseResult(){}

    // 是否成功
    public boolean success = false;
    // 是否是空数据  空数据的时候 success 应为true
    public boolean isEmpty = false;

    public BaseRequestResult baseRequestResult;
    // 错误信息描述
    public BaseErrorInfo errorInfo = new BaseErrorInfo();

    public Result result;
    public Result getResult(){
        return result;
    }
    public static class Result{
        // 全部的返回值
        public String resultAll;
        // 需要解析的返回值  例如 返回值是 {"resCode":0,"resMsg":"aaa",data:[]} resultData就是data后面的值
        public String resultData;

        // 根据解析类型解析出来的返回值
        private Object result_object;
        private List<Object> result_list = new ArrayList<>();
        private String result_str;
        private Map<String,Object> result_list_combination = new HashMap<>();

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

        public Map<String, Object> getResult_list_combination() {
            return result_list_combination;
        }

        public void setResult_list_combination(Map<String, Object> result_list_combination) {
            this.result_list_combination = result_list_combination;
        }
    }
}


