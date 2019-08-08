package com.xhttp.lib.interfaces.file;

import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.interfaces.callback.IFileUploadListener;
import com.xhttp.lib.model.BaseRequestResult;

/**
 * 网络请求处理
 * Created by lixingxing on 2019/3/26.
 */
public interface IFileService {
     // 上传file
     BaseRequestResult uploadFile(BaseHttpParams baseHttpParams,final IFileUploadListener fileUploadListener);
}
