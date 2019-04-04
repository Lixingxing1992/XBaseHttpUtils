package com.xhttp.lib.impl.data;

import android.util.Log;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.IDataListener;

import static com.xhttp.lib.config.BaseHttpConfig.TAG;

/**
 * Created by lixingxing on 2019/3/27.
 */
public class DefaultDataListener implements IDataListener {
    @Override
    public void parse(BaseHttpParams baseHttpParams,BaseResult baseResult) {
        if(baseResult.isRequestSuccess){
            BaseResult.Result result = new BaseResult.Result();
            result.resultAll = new String(baseResult.bytes);
            result.setResult_str(result.resultAll);
            if(baseHttpParams.openLog){
                Log.e(TAG,baseHttpParams.tags +": 请求成功 返回值结果是: " +  result.resultAll);
            }
            baseResult.result = result;
            baseResult.isResultParseSucess = true;
        }
    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return !baseResult.isRequestSuccess || !baseResult.isResultParseSucess;
    }

    @Override
    public BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return baseResult.errorInfo;
    }

    @Override
    public boolean isEmpty(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return false;
    }
}
