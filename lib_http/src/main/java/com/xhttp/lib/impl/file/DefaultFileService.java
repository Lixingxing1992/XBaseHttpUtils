package com.xhttp.lib.impl.file;

import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.interfaces.callback.IFileUploadListener;
import com.xhttp.lib.interfaces.file.IFileService;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;
import com.xhttp.lib.rquest.BaseHttpRequestUtils;

/**
 * Created by lixingxing on 2019/6/20.
 */
public class DefaultFileService implements IFileService {
    @Override
    public BaseRequestResult uploadFile(BaseHttpParams baseHttpParams, final IFileUploadListener fileUploadListener) {

        baseHttpParams.request_contentType = BaseHttpConfig.RequestContentType.FILE;
        BaseHttpRequestUtils baseHttpRequestUtils = new BaseHttpRequestUtils(
                baseHttpParams);
        BaseRequestResult baseRequestResult =
                baseHttpRequestUtils.fileRequest(baseHttpParams,
                            fileUploadListener
                        );
        return baseRequestResult;
    }
}
