package com.xhttp.lib.config;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixingxing on 2019/3/26.
 */
public class BaseHttpParams {
    public String tags = "";

    public boolean openLog = true;
    // 请求地址
    public String url;
    // 参数
    public Object params;
    // 请求方式
    public BaseHttpConfig.RequestType request_type = BaseHttpConfig.RequestType.POST;
    // 连接超时时间
    public int timeout_connect = 12*1000;
    // 读取数据超时时间
    public int timeout_read = 12*1000;

    // 返回值解析类型
    public Class aClass;

    // 返回值解析模式 默认是String
    public BaseHttpConfig.DataParseType dataParseType = BaseHttpConfig.DataParseType.String;


    /**** file 上传相关 ****/
    public List<File> fileList = new ArrayList<>();
    public List<String>  fileKeys = new ArrayList<>();
    // 上传结束后是否删除
    public boolean isDelete = false;
}
