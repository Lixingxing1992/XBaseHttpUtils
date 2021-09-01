package com.xhttp.lib.params;

import android.content.Context;
import android.util.Pair;

import com.xhttp.lib.config.BaseHttpConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 请求参数封装
 * Created by lixingxing
 */
public class BaseHttpParams {

    public Context mContext;

    public String tags = "";

    // Log日志开关
    public boolean openLog = true;
    // 代理开关
    public boolean openProxy = true;
    // 请求地址
    public String url;

    public Object params = "";
    // 参数
    public List<Pair<String,Object>> paramsList = new ArrayList<>();

    public String getParamsDesc(){
        StringBuilder stringBuilder = new StringBuilder();
        if(paramsList.size() > 0){
            for (Pair<String, Object> stringObjectPair : paramsList) {
                stringBuilder.append(stringObjectPair.first + "=" + stringObjectPair.second + "&");
            }
        }
        return stringBuilder.toString();
    }

    // head参数
    public List<Pair<String,Object>> headerParamsList = new ArrayList<>();

    // 请求方式 默认 post提交
    public BaseHttpConfig.RequestType request_type = BaseHttpConfig.RequestType.POST;
    // 请求头 Content-Type
    public BaseHttpConfig.RequestContentType request_contentType = BaseHttpConfig.RequestContentType.FORM;

    // 连接超时时间
    public int timeout_connect = 1*60*1000;
    // 读取数据超时时间
    public int timeout_read = 1*60*1000;

    // 返回值解析类型
    public Class aClass;

    // 返回值解析模式 默认是String
    public BaseHttpConfig.DataParseType dataParseType = BaseHttpConfig.DataParseType.String;


    /**** file 上传相关 ****/
    // 文件是否可以为空（空的话就不上传）
    public boolean isCanFileEmpty = false;
    public List<File> fileList = new ArrayList<>();
    public List<String>  fileKeys = new ArrayList<>();
    // 上传结束后是否删除
    public boolean isDelete = false;



    public BaseHttpParams(BaseHttpInitParams baseHttpInitParams){
        this.mContext = baseHttpInitParams.mContext;
        this.openLog = baseHttpInitParams.mOpenLog;
        this.openProxy = baseHttpInitParams.mOpenProxy;
        this.timeout_connect = baseHttpInitParams.connectTimeOut;
        this.timeout_read = baseHttpInitParams.readTimeOut;
    }
}
