package com.xhttp.lib.impl.data;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.util.DataUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by lixingxing on 2019/6/5.
 */
public class TDDataListener implements IDataListener {


    // 设置成功返回码
    public String successCode = "0";

    public TDDataListener setSuccessCode(String successCode) {
        this.successCode = successCode;
        return this;
    }

    // 设置空判断返回码
    public String emptyCode = "10007";

    public TDDataListener setEmptyCode(String emptyCode) {
        this.emptyCode = emptyCode;
        return this;
    }

    // 解析标识
    public String[] resultCode = new String[]{};

    public Map<String,BaseHttpConfig.DataParseType> resultCodes = new HashMap<>();

    public TDDataListener setResultCode(String... resultCodes) {
        this.resultCode = resultCodes;
        return this;
    }

    public void setResultCodes(Map<String, BaseHttpConfig.DataParseType> resultCodes) {
        this.resultCodes = resultCodes;
    }

    protected String resCode;
    protected String resMsg;
    protected String dataResult;

    @Override
    public String parseResult(BaseHttpParams baseHttpParams, byte[] bytes) throws Exception {
        String results = new String(bytes);
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(results);
            resCode = jsonObject.optString("resCode");
            resMsg = jsonObject.optString("resMsg");
            dataResult = jsonObject.optString("data");
            try {
                dataResult = jsonObject.optString("data");
                if ("null".equals(dataResult)) {
                    dataResult = "";
                }
            } catch (Exception e) {
                dataResult = "";
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return dataResult;
    }


    @Override
    public List parseList(BaseHttpParams baseHttpParams, String resultObj) throws Exception {
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
        return resultObj;
    }

    @Override
    public Map<String, Object> parseCombination(BaseHttpParams baseHttpParams, String resultObj) throws Exception {
        Map<String, Object> mapResult = new HashMap<>();
        // 组合模式下  resultCode都是同级的key
        if(resultCodes != null && resultCodes.size() > 0 && resultObj!=null && !"".equals(resultObj) ){
            JSONObject jsonObject =  new JSONObject(resultObj);
            String objs = "";
            for (String key : resultCodes.keySet()) {
                objs = jsonObject.optString(key);
                try{
                    BaseHttpConfig.DataParseType dataParseType = resultCodes.get(key);
                    if(dataParseType == BaseHttpConfig.DataParseType.List){
                        mapResult.put(key,parseList(baseHttpParams,objs));
                    }else if(dataParseType == BaseHttpConfig.DataParseType.Object){
                        mapResult.put(key,parseObject(baseHttpParams,objs));
                    }else{
                        mapResult.put(key,parseDefault(baseHttpParams,objs));
                    }
                }catch (Exception e){
                    break;
                }
            }
        }
        return mapResult;
    }

    @Override
    public boolean isFail(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return !successCode.equals(resCode) && !emptyCode.equals(resCode);
    }

    @Override
    public BaseErrorInfo getFailErrorInfo() {
        BaseErrorInfo baseErrorInfo = new BaseErrorInfo
                (BaseHttpConfig.ErrorCode.Error_Result_Parsr_error_default, "".equals(resMsg) ? BaseHttpConfig.ErrorCode.Error_Result_Parsr_error_default.toString() : resMsg);
        return baseErrorInfo;
    }

    @Override
    public boolean isEmpty(BaseHttpParams baseHttpParams, BaseResult baseResult) {
        return emptyCode.equals(resCode) ||
                (successCode.equals(resCode) &&
                        baseHttpParams.dataParseType == BaseHttpConfig.DataParseType.List &&
                        ( baseResult.getResult().getResult_list() == null || baseResult.getResult().getResult_list().isEmpty()) );
    }

    @Override
    public BaseErrorInfo getEmptyErrorInfo() {
        BaseErrorInfo baseErrorInfo = new BaseErrorInfo
                (BaseHttpConfig.ErrorCode.Error_Result_none, "".equals(resMsg) ? BaseHttpConfig.ErrorCode.Error_Result_none.toString() : resMsg);
        return baseErrorInfo;
    }
}
