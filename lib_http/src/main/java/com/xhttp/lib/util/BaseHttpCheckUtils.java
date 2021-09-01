package com.xhttp.lib.util;

import com.xhttp.lib.exceptions.BaseHttpUtilsError;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.interfaces.file.IFileService;
import com.xhttp.lib.interfaces.http.IHttpService;
import com.xhttp.lib.params.BaseHttpInitParams;
import com.xhttp.lib.params.BaseHttpParams;

/**
 * 检查用法是否正确
 * @author Lixingxing
 */
public class BaseHttpCheckUtils {

    /**
     * 检查初始化方法有没有实现
     * @param mBaseHttpInitParams
     */
    public static void checkInit(BaseHttpInitParams mBaseHttpInitParams) {
        if(mBaseHttpInitParams == null){
            throw new BaseHttpUtilsError("init(BaseHttpInitConfig baseHttpInitConfig) 方法未调用");
        }
        if(mBaseHttpInitParams.mContext == null){
            throw new BaseHttpUtilsError("BaseHttpInitConfig中 Context 不能为空");
        }
    }


    /**
     * 请求开始前,检查符不符合调用要求
     * @param baseHttpParams
     */
    public static void checkRequest(BaseHttpParams baseHttpParams) {
        if(baseHttpParams == null){
            throw new BaseHttpUtilsError("BaseHttpParams 不能为空");
        }
        if(baseHttpParams.url == null){
            throw new BaseHttpUtilsError("BaseHttpParams url 不能为空");
        }
    }

    /**
     * 上传文件请求开始前,检查符不符合调用要求
     * @param baseHttpParams
     */
    public static void checkFileRequest(BaseHttpParams baseHttpParams) {
        if(baseHttpParams == null){
            throw new BaseHttpUtilsError("BaseHttpParams 不能为空");
        }
        if(baseHttpParams.url == null){
            throw new BaseHttpUtilsError("BaseHttpParams url 不能为空");
        }

        if(!baseHttpParams.isCanFileEmpty){
            if(baseHttpParams.fileList.isEmpty()){
                throw new BaseHttpUtilsError("BaseHttpParams 上传文件不能为空");
            }
            if(baseHttpParams.fileList.size() != baseHttpParams.fileKeys.size()){
                throw new BaseHttpUtilsError("BaseHttpParams 上传文件名称不能为空");
            }
        }
    }


    public static void checkServiceAndDataparse(IHttpService httpService, IDataListener iDataListener){
        if(httpService == null){
            throw new BaseHttpUtilsError("请求工具类 IHttpService 不能为空");
        }
        if(iDataListener == null){
            throw new BaseHttpUtilsError("解析工具类 DataListener 不能为空");
        }
    }

    public static void checkFileServiceAndDataparse(IFileService httpService, IDataListener iDataListener){
        if(httpService == null){
            throw new BaseHttpUtilsError("请求工具类 IFileService 不能为空");
        }
        if(iDataListener == null){
            throw new BaseHttpUtilsError("解析工具类 DataListener 不能为空");
        }
    }
}
