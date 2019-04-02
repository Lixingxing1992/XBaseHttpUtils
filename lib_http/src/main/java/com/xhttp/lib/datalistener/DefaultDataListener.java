package com.xhttp.lib.datalistener;

import android.util.Log;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.IDataListener;

import static com.xhttp.lib.config.BaseHttpConfig.TAG;

/**
 * Created by lixingxing on 2019/3/27.
 */
public class DefaultDataListener implements IDataListener {
    @Override
    public BaseResult parse(BaseHttpParams baseHttpParams,BaseResult baseResult) {
        if(baseResult.isRequestSuccess){
            BaseResult.Result result = new BaseResult.Result();
            result.result_str = new String(baseResult.bytes);
            if(baseHttpParams.openLog){
                Log.e(TAG,baseHttpParams.tags +": 请求成功 返回值结果是: " +  result.result_str);
            }
            baseResult.result = result;
            baseResult.isResultParseSucess = true;
        }
        return baseResult;
    }
}
