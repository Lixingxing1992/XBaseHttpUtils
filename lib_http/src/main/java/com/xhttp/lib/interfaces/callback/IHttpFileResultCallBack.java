package com.xhttp.lib.interfaces.callback;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.model.BaseErrorInfo;

import java.io.File;

/**
 * 文件上传回调
 * Created by lixingxing on 2019/3/26.
 */
public interface IHttpFileResultCallBack {
    // 整个请求过程成功
    void onSuccess(BaseResult baseResult);
    // 错误信息
    void onFail(BaseErrorInfo errorInfo);
    // 空集合
    void onEmpty(BaseErrorInfo errorInfo);
    // 最终执行
    void onFinal(BaseResult baseResult);

    void onFileProgress(int position, File file, long curlenth, long total);
}
