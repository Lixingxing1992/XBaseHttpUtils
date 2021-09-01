package com.xhttp.lib.model.result;

import com.xhttp.lib.model.BaseResultData;

/**
 * @author Lixingxing
 */
public class DefaultBaseResultData implements BaseResultData {

    protected String resultCode = "0"; // 默认
    protected String resultMsg = ""; // 错误信息
    protected String resultData = "";// 数据解析主体

    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }

    @Override
    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    @Override
    public String getResultData() {
        return resultData;
    }

    @Override
    public void setResultData(String resultData) {
        this.resultData = resultData;
    }
}
