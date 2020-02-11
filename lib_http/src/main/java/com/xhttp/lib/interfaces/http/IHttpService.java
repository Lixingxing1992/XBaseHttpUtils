package com.xhttp.lib.interfaces.http;

import android.util.Pair;

import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * 网络请求处理
 * Created by lixingxing on 2019/3/26.
 */
public interface IHttpService extends Serializable {
    // 处理参数  get请求下这个方法不使用
    Object parseParams(List<Pair<String, Object>> params);

    // 返回请求参数的描述(用于log日志中参数的显示）
    String getRequestParamsDesc(Object params);
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
     @NotNull
     BaseRequestResult request(BaseHttpParams baseHttpParams);

}
