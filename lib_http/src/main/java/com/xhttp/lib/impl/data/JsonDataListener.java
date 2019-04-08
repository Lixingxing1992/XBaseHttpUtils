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
            result.resultAll = new String(baseResult.bytes);
            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ": 请求成功 返回值结果是: " + result.resultAll);
            }
            baseResult.result = result;
        }
    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return false;
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
