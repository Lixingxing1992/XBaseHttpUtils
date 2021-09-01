package com.xhttp.lib.model;

/**
 * 返回值第一层封装
 *
 * @author Lixingxing
 */
public interface BaseResultData {
    public String getResultCode();

    public void setResultCode(String resultCode);

    public String getResultMsg();

    public void setResultMsg(String resultMsg);

    public String getResultData();

    public void setResultData(String resultData);
}
