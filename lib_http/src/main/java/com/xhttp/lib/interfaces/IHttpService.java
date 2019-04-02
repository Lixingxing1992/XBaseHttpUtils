package com.xhttp.lib.interfaces;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseHttpParams;

/**
 * 网络请求处理
 * Created by lixingxing on 2019/3/26.
 */
public interface IHttpService {
    /**
     * 重写方法需要 处理 参数中的baseResult,注意要符合下面的规则:
     *
     * 1.如果 请求成功,成功获取到返回值
     *      baseResult.isRequestSuccess = true
     *      baseResult.responseCode = 200
     *      baseResult.byte != null && baseResult.byte.length() != 0
     * 2.如果 请求成功,但返回值为空
     *      baseResult.isRequestSuccess = false
     *      baseResult.responseCode = -1
     *      baseResult.errorInfo != null
     * 3.如果 请求失败,有异常信息
     *      baseResult.isRequestSuccess = false
     *      baseResult.responseCode = -2
     *      baseResult.errorInfo != null  &&  baseResult.errorInfo.e != null
     * @param baseHttpParams
     * @param baseResult
     * @return
     */
    BaseResult request(BaseHttpParams baseHttpParams,BaseResult baseResult);
}
