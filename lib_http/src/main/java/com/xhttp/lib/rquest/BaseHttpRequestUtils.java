package com.xhttp.lib.rquest;

import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.interfaces.callback.IFileUploadListener;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;

import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 * @author Lixingxing
 */
public class BaseHttpRequestUtils {
    @NotNull
    private BaseHttpParams mBaseHttpParams;

    public BaseHttpRequestUtils(@NotNull final BaseHttpParams baseHttpParams){
        this.mBaseHttpParams = baseHttpParams;
    }

    /**
     * 请求方法
     */
    public BaseRequestResult request(Object paramsStr){
        BaseRequestResult baseRequestResult =
                new RequestUtil(mBaseHttpParams)
                        .Request_HeaderParams(mBaseHttpParams.headerParamsList)
                        .Request_ContentType(mBaseHttpParams.request_contentType.toString())
                        .Request_requestType(mBaseHttpParams.request_type)
                        .Reqeust_ConnectTimeOut(mBaseHttpParams.timeout_connect)
                        .Reqeust_ReadTimeOut(mBaseHttpParams.timeout_read)
                        .Request_OpenProxy(mBaseHttpParams.openProxy)
                        .request(paramsStr.toString(), mBaseHttpParams.url);
        return baseRequestResult;
    }

    public BaseRequestResult fileRequest(BaseHttpParams baseHttpParams,final IFileUploadListener fileUploadListener){
        BaseRequestResult request = new RequestUtil(baseHttpParams)
                .Reqeust_ConnectTimeOut(baseHttpParams.timeout_connect)
                .Request_HeaderParams(baseHttpParams.headerParamsList)
                .Reqeust_ReadTimeOut(baseHttpParams.timeout_read)
                .Request_ContentType(BaseHttpConfig.ParamType.FILE)
                .Request_requestType(baseHttpParams.request_type)
                .uploadFileByFiles(baseHttpParams.fileList,
                        baseHttpParams.fileKeys,
                        baseHttpParams.url,
                        new RequestUtil.RequestUtilFileListener() {
                            @Override
                            public void onFileProgress(int position, File file, long curlenth, long total) {
                                if(fileUploadListener != null){
                                    fileUploadListener.onFileProgress(position,file,curlenth,total);
                                }
                            }
                        });
        return request;
    }

}
