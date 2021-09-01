package com.xhttp.lib.impl.data;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.model.result.JSONBaseResultData;
import com.xhttp.lib.model.result.JSONBaseResultData;
import com.xhttp.lib.params.BaseHttpParams;
import com.xhttp.lib.util.BaseJsonUtils;
import com.xhttp.lib.util.BaseObjectUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * @author Lixingxing
 */
public class JsonDataListener implements IDataListener<JSONBaseResultData> {
    // json需要解析的key
    String[] resultCode = null;
    public void setResultCode(String[] resultCode){
        this.resultCode = resultCode;
    }
    @NotNull
    @Override
    public JSONBaseResultData parseResult(@NotNull BaseHttpParams baseHttpParams, @NotNull String result) {
        baseHttpParams.dataParseType = BaseHttpConfig.DataParseType.String;
        JSONBaseResultData defaultBaseResultData = new JSONBaseResultData();
        defaultBaseResultData.setResultCode("0");
        defaultBaseResultData.setResultMsg("");
        try {
            JSONObject jsonObject = new JSONObject(result);
            if(resultCode!=null){
                for (String s : resultCode) {
                    result = jsonObject.getString(s);
                    if(result == null || "".equals(result)){
                        break;
                    }
                }
            }
            defaultBaseResultData.setResultData(result);
        } catch (JSONException e) {
            defaultBaseResultData.setResultCode("-1");
            defaultBaseResultData.setResultMsg("解析出现异常");
            defaultBaseResultData = null;
            e.printStackTrace();
        }
        return defaultBaseResultData;
    }

    @Override
    public boolean isFailResult(@NotNull BaseHttpParams baseHttpParams,
                                @NotNull JSONBaseResultData baseResultData,
                                @NotNull BaseResult.Result result) {
        return false;
    }

    @Override
    public <T> List<T> getList(@NotNull BaseHttpParams baseHttpParams, @NotNull JSONBaseResultData result) {
        try {
            return BaseJsonUtils.jsonToList(result.getResultData(),baseHttpParams.aClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public <T> T getObject(@NotNull BaseHttpParams baseHttpParams, @NotNull JSONBaseResultData result) {
        try {
            return (T) BaseJsonUtils.jsonToObject(result.getResultData(),baseHttpParams.aClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getString(@NotNull BaseHttpParams baseHttpParams, @NotNull JSONBaseResultData result) {
        return result.getResultData();
    }
}
