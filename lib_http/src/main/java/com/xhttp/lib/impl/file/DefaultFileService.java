package com.xhttp.lib.impl.file;

import com.xhttp.lib.interfaces.callback.IFileUploadListener;
import com.xhttp.lib.interfaces.file.IFileService;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;

/**
 * Created by lixingxing on 2019/6/20.
 */
public class DefaultFileService implements IFileService {
    @Override
    public BaseRequestResult uploadFile(BaseHttpParams baseHttpParams, final IFileUploadListener fileUploadListener) {
//        BaseRequestResult request = new RequestUtil(baseHttpParams)
//                .Reqeust_ConnectTimeOut(baseHttpParams.timeout_connect)
//                .Reqeust_ReadTimeOut(baseHttpParams.timeout_read)
//                .Request_ContentType(BaseHttpConfig.ParamType.FILE)
//                .Request_requestType(baseHttpParams.request_type)
//                .uploadFileByFiles(baseHttpParams.fileList,
//                        baseHttpParams.fileKeys,
//                        baseHttpParams.url,
//                        new RequestUtil.RequestUtilFileListener() {
//                            @Override
//                            public void onFileProgress(int position, File file, long curlenth, long total) {
//                                if(fileUploadListener != null){
//                                    fileUploadListener.onFileProgress(position,file,curlenth,total);
//                                }
//                            }
//                        });
        return null;
    }
}
