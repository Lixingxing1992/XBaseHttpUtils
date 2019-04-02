package com.xhttp.lib;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.datalistener.DefaultDataListener;
import com.xhttp.lib.interfaces.IDataListener;
import com.xhttp.lib.interfaces.IHttpService;
import com.xhttp.lib.interfaces.IHttpResultCallBack;
import com.xhttp.lib.service.DefaultHttpService;
import com.xhttp.lib.util.BaseThreadPoolUtil;

import java.util.Map;

/**
 * Created by lixingxing on 2019/3/26.
 */
public class BaseHttpUtils {

    // 唯一标识
    private String tags = "";

    // 是否打开Log日志
    private static boolean openLogStatic = true;
    public static void init(boolean openLogs){
        openLogStatic = openLogs;
    }
    private Boolean openLog = null;
    public BaseHttpUtils initOpenLog(boolean openLog){
        this.openLog = openLog;
        return this;
    }
    // 检查Log开关
    public boolean checkLog(){
        if(openLog == null){
            return openLogStatic;
        }
        return openLog;
    }

    // 发送请求封装类
    private BaseHttpParams baseHttpParams;
    // 返回值封装类
    private BaseResult baseResult;

    public BaseHttpUtils() {
        this(new BaseHttpParams());
    }

    public BaseHttpUtils(BaseHttpParams baseHttpParams) {
        this.baseHttpParams = baseHttpParams;
        this.baseResult = new BaseResult();

        tags = System.currentTimeMillis()+"";
        this.baseHttpParams.tags = tags;

        if(iHttpServiceStatic == null){
            // 默认
            iHttpServiceStatic = new DefaultHttpService();
        }
        if(iDataListenerStatic == null){
            // 默认
            iDataListenerStatic = new DefaultDataListener();
        }
    }

    /************************ 请求参数 ********************************/
    /**
     * 全局的 请求工具类 iHttpServiceStatic 和 数据解析类 iDataListenerStatic
     * 会被 initIHttpService 和 initIDataListener中设置的值覆盖掉
     */
    public static IHttpService iHttpServiceStatic;
    public static IDataListener iDataListenerStatic;

    // 初始化设置 全局的 请求工具类 和 数据解析类
    public static void init(IHttpService iHttpServiceStatics, IDataListener iDataListenerStatics) {
        iHttpServiceStatic = iHttpServiceStatics;
        iDataListenerStatic = iDataListenerStatics;
    }

    /**
     * 针对 本次请求的 请求工具类 iHttpService 和 数据解析类 iDataListener
     * 会覆盖全局的 请求工具类 iHttpServiceStatic 和 数据解析类 iDataListenerStatic
     */
    // 网络请求
    private IHttpService iHttpService;
    // 数据解析
    private IDataListener iDataListener;

    // 设置 网络请求工具类
    public BaseHttpUtils initIHttpService(IHttpService iHttpService) {
        this.iHttpService = iHttpService;
        return this;
    }

    // 设置 数据解析工具类
    public BaseHttpUtils initIDataListener(IDataListener iDataListener) {
        this.iDataListener = iDataListener;
        return this;
    }

    /**
     * 设置请求路径 url
     *
     * @param url
     * @return
     */
    public BaseHttpUtils initUrl(String url) {
        baseHttpParams.url = url;
        return this;
    }

    // initParams 只针对 post请求,get请求不处理params
    /**
     * 设置请求参数1 params
     * initParams(key,value,key,value....)
     * @param params
     * @return
     */
    public BaseHttpUtils initParams(Object... params) {
        baseHttpParams.params = params;
        return this;
    }
    /**
     * 设置请求参数2 params
     * key=value&key=value
     * @param params
     * @return
     */
    public BaseHttpUtils initParams(String params) {
        baseHttpParams.params = params;
        return this;
    }
    /**
     * 设置请求参数 params
     * Map参数
     * @param params
     * @return
     */
    public BaseHttpUtils initParams(Map params) {
        baseHttpParams.params = params;
        return this;
    }

    // 可以拓展
//    public BaseHttpUtils initJsonParams(String json){}

    /**
     * 设置请求方式 RequestType   post  get ...
     *
     * @param type
     * @return
     */
    public BaseHttpUtils initRequestType(BaseHttpConfig.RequestType type) {
        baseHttpParams.request_type = type;
        return this;
    }

    /************************返回值********************************/

    /**
     * 设置返回值 解析模式
     *
     * @param responseType
     * @return
     */
    public BaseHttpUtils initResponseType(BaseHttpConfig.ResponseType responseType) {
        baseResult.responseType = responseType;
        return this;
    }

    /**
     * 设置返回值 解析类型
     *
     * @param tClass
     * @return
     */
    public BaseHttpUtils initClass(Class tClass) {
        baseResult.aClass = tClass;
        return this;
    }

    // 设置需要获取的返回值 code
//    public BaseHttpUtils initResultCode(String resultCode) {
//        baseHttpParams.resultCode = resultCode;
//        return this;
//    }

    // 设置请求回调
//    public BaseHttpUtils initHttpCallBack(BaseHttpCallBack iHttpCallBack) {
//        this.baseHttpCallBack = iHttpCallBack;
//        return this;
//    }
    IHttpResultCallBack iResultCallBack;
    public BaseHttpUtils initHttpResultCallBack(IHttpResultCallBack iResultCallBack){
        this.iResultCallBack = iResultCallBack;
        return this;
    }

    /**
     * 发送请求
     */
//    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Handler mHandler = new Handler();
    public void post(){
        baseHttpParams.request_type = BaseHttpConfig.RequestType.POST;
        request();
    }

    public void get(){
        baseHttpParams.request_type = BaseHttpConfig.RequestType.GET;
        request();
    }

    // 网络请求要在线程中进行
    public void request() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            BaseThreadPoolUtil.execute(new Runnable(){
                @Override
                public void run() {
                    requests();
                }
            });
        } else {
            requests();
        }
    }

    public void requests(){
        baseHttpParams.openLog = checkLog();

        if( (iHttpServiceStatic == null && iHttpService == null) ||
                (iDataListenerStatic == null && iDataListener == null) ){
            if(baseHttpParams.openLog){
                Log.e("BaseHttpUtils",tags+":未设置请求和解析工具类");
            }
            return;
        }
        if(baseHttpParams.openLog){
            Log.e("BaseHttpUtils",tags+": 开始发送网络请求...");
        }
        if(iHttpService != null){
            iHttpService.request(baseHttpParams,baseResult);
        }else{
            iHttpServiceStatic.request(baseHttpParams,baseResult);
        }
        if(!baseResult.isRequestSuccess){
            if(baseHttpParams.openLog){
                Log.e("BaseHttpUtils",baseResult.errorInfo.errorMsg);
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(iResultCallBack != null){
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return;
        }
        if(iDataListener != null){
            iDataListener.parse(baseHttpParams,baseResult);
        }else{
           iDataListenerStatic.parse(baseHttpParams,baseResult);
        }
        if(!baseResult.isResultParseSucess){
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(iResultCallBack != null){
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return;
        }
        // 请求和解析都OK
        baseResult.success = baseResult.isRequestSuccess && baseResult.isResultParseSucess;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(iResultCallBack != null){
                    if(baseResult.success){
                        iResultCallBack.onSuccess(baseResult);
                    }else{
                        iResultCallBack.onFail(baseResult.errorInfo);
                    }
                    iResultCallBack.onFinal(baseResult);
                }
            }
        });
    }

}
