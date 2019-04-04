package com.xhttp.lib.impl.data;

import android.util.Log;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.IDataListener;
import com.xhttp.lib.util.DataUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static com.xhttp.lib.config.BaseHttpConfig.TAG;

/**
 * Created by lixingxing on 2019/4/2.
 */
public class TDDataListener implements IDataListener {
    // 设置正确判断
    public String successCode = "0";

    public TDDataListener setSuccessCode(String successCode) {
        this.successCode = successCode;
        return this;
    }

    // 设置空判断
    public String emptyCode = "10007";

    public TDDataListener setEmptyCode(String emptyCode) {
        this.emptyCode = emptyCode;
        return this;
    }

    String resCode;
    String resMsg;
    String dataResult;

    @Override
    public void parse(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        baseResult.errorInfo.errorType = BaseHttpConfig.ErrorType.Error_Data;
        baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error;
        if (baseResult.isRequestSuccess) {
            BaseResult.Result result = new BaseResult.Result();
            result.resultAll = new String(baseResult.bytes);
            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ": 请求成功 返回值结果是: " + result.resultAll);
            }
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result.resultAll);
                resCode = jsonObject.optString("resCode");
                resMsg = jsonObject.optString("resMsg");
                dataResult = jsonObject.optString("data");
                if(successCode.equals(resCode)){
                    switch (baseResult.responseType) {
                        case List:{
                            if(baseResult.aClass == null){
                                baseResult.errorInfo.errorMsg = "没有设置返回值解析类型";
                            }else{
                                List<Object> lists = DataUtil.parseJsonToList(dataResult,baseResult.aClass);
                                if(lists == null){
                                    baseResult.errorInfo.errorMsg = "list数据解析错误,请检查返回值是否正确";
                                }else{
                                    baseResult.isResultParseSucess = true;
                                    result.setResult_list(lists);
                                }
                            }
                        }
                        case Object:{
                            if(baseResult.aClass == null){
                                baseResult.errorInfo.errorMsg = "没有设置返回值解析类型";
                            }else{
                                Object obj = DataUtil.parseJsonToObject(dataResult,baseResult.aClass);
                                if(obj == null){
                                    baseResult.errorInfo.errorMsg = "object数据解析错误,请检查返回值是否正确";
                                }else{
                                    baseResult.isResultParseSucess = true;
                                    result.setResult_object(obj);
                                }
                            }
                        }
                        case String:{
                            baseResult.isResultParseSucess = true;
                            result.setResult_str(dataResult);
                        }
                        break;
                    }
                }
            } catch (JSONException e) {
                baseResult.errorInfo.exception = e;
                e.printStackTrace();
            }
            baseResult.result = result;
        }
    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        if (baseResult.isRequestSuccess && baseResult.isResultParseSucess &&
                (successCode.equals(resCode) || emptyCode.equals(resCode))
                ) {
            return false;
        }
        return true;
    }

    @Override
    public BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return baseResult.errorInfo;
    }

    @Override
    public boolean isEmpty(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        if (baseResult.isRequestSuccess && baseResult.isResultParseSucess && emptyCode.equals(resCode)
                ) {
            baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_none;
            baseResult.errorInfo.errorMsg = "".equals(resMsg) ? "当前数据为空~~" : resMsg;
            return true;
        }else if (baseResult.isRequestSuccess && baseResult.isResultParseSucess && successCode.equals(resCode)
                ) {
            if(baseResult.responseType == BaseHttpConfig.ResponseType.List
                    && baseResult.result.getResult_list().isEmpty()){
                return true;
            }
        }
        return false;
    }
}
