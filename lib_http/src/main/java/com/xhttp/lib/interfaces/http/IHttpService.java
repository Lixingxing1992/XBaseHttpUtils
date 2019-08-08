package com.xhttp.lib.interfaces.http;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.model.BaseRequestResult;

/**
 * 网络请求处理
 * Created by lixingxing on 2019/3/26.
 */
public interface IHttpService {
    // 处理参数  get请求下这个方法不使用
    Object parseParams(BaseHttpParams baseHttpParams);
    /**
     * 返回 BaseRequestResult必须符合下面的条件
     *
     * 1.如果 请求成功,成功获取到返回值
     *      BaseRequestResult.isSuccess = true
     *      BaseRequestResult.responseCode = 200
     *      BaseRequestResult.byte != null && BaseRequestResult.byte.length() != 0
     * 2.如果 请求失败,有异常信息
     *      BaseRequestResult.isSuccess = false
     *      BaseRequestResult.errorInfo != null
     * @param baseHttpParams
     */
     BaseRequestResult request(BaseHttpParams baseHttpParams);

}
