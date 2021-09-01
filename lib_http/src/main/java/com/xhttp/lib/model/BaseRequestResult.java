package com.xhttp.lib.model;

/**
 * 网络请求的返回值封装
 * Created by lixingxing on 2019/6/4.
 */
public class BaseRequestResult {
    // 是否成功
    public boolean isSuccess = false;

    // 返回值 数组
    public byte[] bytes = new byte[]{};

    // 网络连接码   200 成功  -1 结果为空 -2有异常信息
    //请求返回连接码
    public int responseCode = 200;

    public BaseErrorInfo errorInfo = new BaseErrorInfo();

    // 检查返回值是否符合条件
    public boolean checkResult() {
        // 成功的时候 必须 bytes 有值 且 responseCode == 200
        if (isSuccess && bytes != null && responseCode == 200 && bytes.length != 0) {
            return true;
        }
        // 失败的时候 必须有失败信息描述
        if (!isSuccess && errorInfo != null) {
            return true;
        }
        return false;
    }

}
