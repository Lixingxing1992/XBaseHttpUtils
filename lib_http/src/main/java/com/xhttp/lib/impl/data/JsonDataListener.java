package com.xhttp.lib.impl.data;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.util.DataUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by lixingxing on 2019/4/8.
 */
public class JsonDataListener implements IDataListener {
    @Override
    public String parseResult(BaseHttpParams baseHttpParams, byte[] bytes) throws Exception {
        String results = new String(bytes);
        return results;
    }

    @Override
    public List parseList(BaseHttpParams baseHttpParams, String resultObj) throws Exception {
        List<Object> lists;
        try {
            lists = DataUtil.parseJsonToList(resultObj, baseHttpParams.aClass);
        } catch (Exception e) {
            throw e;
        }
        return lists;
    }

    @Override
    public Object parseObject(BaseHttpParams baseHttpParams, String resultObj) throws Exception {
        Object object = null;
        try {
            object = DataUtil.parseJsonToObject(resultObj, baseHttpParams.aClass);
        } catch (Exception e) {
            throw e;
        }
        return object;
    }

    @Override
    public String parseDefault(BaseHttpParams baseHttpParams, String resultObj) throws Exception {
        return resultObj;
    }

    @Override
    public Map<String, Object> parseCombination(BaseHttpParams baseHttpParams, String resultObj) throws Exception {
        return null;
    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return false;
    }

    @Override
    public BaseErrorInfo getFailErrorInfo() {
        return null;
    }

    @Override
    public boolean isEmpty(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return false;
    }

    @Override
    public BaseErrorInfo getEmptyErrorInfo() {
        return null;
    }
}
