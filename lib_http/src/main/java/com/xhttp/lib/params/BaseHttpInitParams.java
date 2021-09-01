package com.xhttp.lib.params;

import android.content.Context;

import com.xhttp.lib.impl.data.DefaultDataListener;
import com.xhttp.lib.impl.file.DefaultFileService;
import com.xhttp.lib.impl.service.DefaultHttpService;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.interfaces.data.IDataListenerFilter;
import com.xhttp.lib.interfaces.file.IFileService;
import com.xhttp.lib.interfaces.http.IHttpService;
import com.xhttp.lib.interfaces.http.IHttpServiceFilter;
import com.xhttp.lib.interfaces.message.IMessageManager;

/**
 * 全局设置的参数（在使用中会优先使用独立设置的参数）
 * @author Lixingxing
 */
public class BaseHttpInitParams {
     private BaseHttpInitParams(){

     }
    public BaseHttpInitParams(Context context){
        this(context,true);
    }
    public BaseHttpInitParams(Context context,boolean mOpenLog){
        this.mContext = context;
        this.mOpenLog = mOpenLog;
        mHttpService = new DefaultHttpService();
        mDataListener = new DefaultDataListener();
        mFileService = new DefaultFileService();
    }

    public BaseHttpInitParams(Context context,boolean mOpenLog,boolean isProxy){
        this.mContext = context;
        this.mOpenLog = mOpenLog;
        this.mOpenProxy = isProxy;
        mHttpService = new DefaultHttpService();
        mDataListener = new DefaultDataListener();
        mFileService = new DefaultFileService();
    }

    // 全局上下文
    public Context mContext;
    // 是否打开Log日志
    public boolean mOpenLog = true;
    // 是否打开代理
    public boolean mOpenProxy = true;

    // 请求超时时间
    public int connectTimeOut = 1 * 60 * 1000;

    // 读取超时时间
    public int readTimeOut = 15 * 1000;

    public IHttpService mHttpService;
    public IDataListener mDataListener;
    public IMessageManager mMessageManager;

    public IHttpServiceFilter mHttpServiceFilter;
    public IDataListenerFilter mDataListenerFilter;

    public IFileService mFileService;

    // 设置上下文
    public BaseHttpInitParams setContext(Context mContext){
        this.mContext = mContext;
        return this;
    }
    // 设置log开关
    public BaseHttpInitParams setOpenLog(boolean mOpenLog) {
        this.mOpenLog = mOpenLog;
        return this;
    }

    public void setConnectTimeOut(int connectTimeOut) {
        this.connectTimeOut = connectTimeOut;
    }

    public void setReadTimeOut(int readTimeOut) {
        this.readTimeOut = readTimeOut;
    }

    // 设置请求工具类
    public BaseHttpInitParams setIHttpService(IHttpService mHttpService) {
        this.mHttpService = mHttpService;
        return this;
    }
    // 设置数据解析工具类
    public BaseHttpInitParams setIDataListener(IDataListener mDataListener) {
        this.mDataListener = mDataListener;
        return this;
    }
    // 设置请求工具类 过滤器
    public BaseHttpInitParams setIHttpServiceFilter(IHttpServiceFilter mHttpServiceFilter) {
        this.mHttpServiceFilter = mHttpServiceFilter;
        return this;
    }
    // 设置数据解析工具类 过滤器
    public BaseHttpInitParams setIDataListenerFilter(IDataListenerFilter mDataListenerFilter) {
        this.mDataListenerFilter = mDataListenerFilter;
        return this;
    }
    // 设置返回值提示工具类
    public BaseHttpInitParams setIMessageManager(IMessageManager mMessageManager) {
        this.mMessageManager = mMessageManager;
        return this;
    }

    // 设置上传文件工具类
    public BaseHttpInitParams setIFileService(IFileService mFileService){
        this.mFileService = mFileService;
        return this;
    }
}
