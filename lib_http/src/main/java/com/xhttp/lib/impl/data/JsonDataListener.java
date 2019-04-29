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
 * Created by lixingxing on 2019/4/8.
 */
public class JsonDataListener implements IDataListener {

    @Override
    public void parse(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        baseResult.errorInfo.errorType = BaseHttpConfig.ErrorType.Error_Data;
        baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error;
        if (baseResult.isRequestSuccess) {
            BaseResult.Result result = new BaseResult.Result();
            String dataResult = new String(baseResult.bytes);
            result.resultAll = dataResult;
            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ": 请求成功 返回值结果是: " + result.resultAll);
            }
            result.setResult_str(result.resultAll);
            switch (baseResult.dataParseType) {
                case List: {
                    if (baseResult.aClass == null) {
                        baseResult.errorInfo.errorMsg = "没有设置返回值解析类型";
                    } else {
                        List<Object> lists = DataUtil.parseJsonToList(dataResult, baseResult.aClass);
                        if (lists == null) {
                            baseResult.errorInfo.errorMsg = "list数据解析错误,请检查返回值是否正确";
                        } else {
                            baseResult.isResultParseSucess = true;
                            result.setResult_list(lists);
                        }
                    }
                }
                case Object: {
                    if (baseResult.aClass == null) {
                        baseResult.errorInfo.errorMsg = "没有设置返回值解析类型";
                    } else {
                        Object obj = DataUtil.parseJsonToObject(dataResult, baseResult.aClass);
                        if (obj == null) {
                            baseResult.errorInfo.errorMsg = "object数据解析错误,请检查返回值是否正确";
                        } else {
                            baseResult.isResultParseSucess = true;
                            result.setResult_object(obj);
                        }
                    }
                }
                case String: {
                    baseResult.isResultParseSucess = true;
                    result.setResult_str(dataResult);
                }
                break;
            }
            baseResult.result = result;
        }

    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return !baseResult.isResultParseSucess;
    }

    @Override
    public BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return baseResult.errorInfo;
    }

    @Override
    public boolean isEmpty(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return (baseResult.isRequestSuccess && baseResult.isResultParseSucess &&
                baseResult.dataParseType == BaseHttpConfig.DataParseType.List &&
                baseResult.result.getResult_list().isEmpty());
    }
}
