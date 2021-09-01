package com.xhttp.lib.impl.data;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.model.BaseResultData;
import com.xhttp.lib.model.result.JSONBaseResultData;
import com.xhttp.lib.params.BaseHttpParams;
import com.xhttp.lib.util.BaseJsonUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 示例 拓展的 DataListener
 * Created by lixingxing on 2019/6/5.
 */
public class BaseTempDataListener implements IDataListener {


    // 设置成功返回码
    public String successCode = "200";

    public BaseTempDataListener setSuccessCode(String successCode) {
        this.successCode = successCode;
        return this;
    }
    // 设置空判断返回码
    public String emptyCode = "10007";

    public BaseTempDataListener setEmptyCode(String emptyCode) {
        this.emptyCode = emptyCode;
        return this;
    }

    // 解析标识
    public String[] resultCode = new String[]{};

    public Map<String, BaseHttpConfig.DataParseType> resultCodes = new HashMap<>();

    public BaseTempDataListener setResultCode(String... resultCodes) {
        this.resultCode = resultCodes;
        return this;
    }

    public void setResultCodes(Map<String, BaseHttpConfig.DataParseType> resultCodes) {
        this.resultCodes = resultCodes;
    }

    protected String resCode;
    protected String resMsg;
    protected String dataResult;

    @NotNull
    @Override
    public BaseResultData parseResult(@NotNull com.xhttp.lib.params.BaseHttpParams baseHttpParams, @NotNull String result) {
        BaseResultData baseResultData = new JSONBaseResultData();
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(result);
            resCode = jsonObject.optInt("code") + "";
            resMsg = jsonObject.optString("msg");
            try {
                dataResult = jsonObject.optString("data");
                if ("null".equals(dataResult)) {
                    dataResult = "";
                }
            } catch (Exception e) {
                dataResult = result;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        baseResultData.setResultCode(resCode);
        baseResultData.setResultMsg(resMsg);
        baseResultData.setResultData(dataResult);

        return baseResultData;
    }

    @Override
    public String getString(@NotNull com.xhttp.lib.params.BaseHttpParams baseHttpParams, @NotNull BaseResultData result) {
        String resultObj = dataResult;
        if(resultCode != null && resultCode.length > 0 && dataResult !=null && !"".equals(dataResult) ){
            for (String s : resultCode) {
                try{
                    resultObj = new JSONObject(resultObj).optString(s);
                }catch (Exception e){
                    resultObj = "";
                    break;
                }
            }
        }
        return resultObj;
    }

    @Override
    public Object getObject(@NotNull com.xhttp.lib.params.BaseHttpParams baseHttpParams, @NotNull BaseResultData result) {
        String resultObj = dataResult;
        if(resultCode != null && resultCode.length > 0 && resultObj!=null && !"".equals(resultObj) ){
            for (String s : resultCode) {
                try{
                    resultObj = new JSONObject(resultObj).optString(s);
                }catch (Exception e){
                    resultObj = "";
                    break;
                }
            }
        }
        Object lists = null;
        try {
            lists = BaseJsonUtils.jsonToObject(resultObj, baseHttpParams.aClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    @Override
    public List getList(@NotNull com.xhttp.lib.params.BaseHttpParams baseHttpParams, @NotNull BaseResultData result) {
        String resultObj = dataResult;
        if(resultCode != null && resultCode.length > 0 && resultObj!=null && !"".equals(resultObj) ){
            for (String s : resultCode) {
                try{
                    resultObj = new JSONObject(resultObj).optString(s);
                }catch (Exception e){
                    resultObj = "";
                    break;
                }
            }
        }
        List<Object> lists = new ArrayList<>();
        try {
            lists = BaseJsonUtils.jsonToList(resultObj, baseHttpParams.aClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lists;
    }

    @Override
    public boolean isFailResult(@NotNull BaseHttpParams baseHttpParams,
                                @NotNull BaseResultData baseResultData,
                                @NotNull BaseResult.Result result) {
        return !successCode.equals(baseResultData.getResultCode());
    }

}
