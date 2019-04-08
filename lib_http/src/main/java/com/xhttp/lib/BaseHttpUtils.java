package com.xhttp.lib;

import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.exceptions.BaseHttpUtilsError;
import com.xhttp.lib.impl.data.DefaultDataListener;
import com.xhttp.lib.interfaces.IDataListener;
import com.xhttp.lib.interfaces.IHttpService;
import com.xhttp.lib.interfaces.IHttpResultCallBack;
import com.xhttp.lib.impl.service.DefaultHttpService;
import com.xhttp.lib.util.BaseThreadPoolUtil;

import java.util.Map;

/**
 * Created by lixingxing on 2019/3/26.
 */
public final class BaseHttpUtils {
    // 唯一标识
    private String tags = "";

    // 是否打开Log日志
    private static boolean openLogStatic = true;

    public static void init(boolean openLogs) {
        openLogStatic = openLogs;
    }

    private Boolean openLog = null;

    public BaseHttpUtils initOpenLog(boolean openLog) {
        this.openLog = openLog;
        return this;
    }

    // 检查Log开关
    public boolean checkLog() {
        if (openLog == null) {
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

    public BaseHttpUtils(Dialog dialog) {
        this(new BaseHttpParams());
        this.dialog = dialog;
    }

    private BaseHttpUtils(BaseHttpParams baseHttpParams) {
        this.baseHttpParams = baseHttpParams;
        this.baseResult = new BaseResult();

        tags = System.currentTimeMillis() + "";
        this.baseHttpParams.tags = tags;

        if (iHttpServiceStatic == null) {
            // 默认
            iHttpServiceStatic = DefaultHttpService.class;
        }
        if (iDataListenerStatic == null) {
            // 默认
            iDataListenerStatic = DefaultDataListener.class;
        }
    }

    /************************ 请求参数 ********************************/
    /**
     * 全局的 请求工具类 iHttpServiceStatic 和 数据解析类 iDataListenerStatic
     * 会被 initIHttpService 和 initIDataListener中设置的值覆盖掉
     */
    public static Class iHttpServiceStatic;
    public static Class iDataListenerStatic;

    private IHttpService iHttpServiceCurr;
    private IDataListener iDataListenerCurr;

    // 初始化设置 全局的 请求工具类 和 数据解析类
    public static void init(Class iHttpServiceStatics, Class iDataListenerStatics) {
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
     *
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
     *
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
     *
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

    /**
     * 设置超时时间
     * @param timeOut
     * @return
     */
    public BaseHttpUtils initConnectTimeOut(int timeOut){
        baseHttpParams.timeout_connect = timeOut;
        return this;
    }
    public BaseHttpUtils initReadTimeOut(int timeOut){
        baseHttpParams.timeout_read = timeOut;
        return this;
    }
    public BaseHttpUtils initTimeOut(int timeOut){
        baseHttpParams.timeout_connect = timeOut;
        baseHttpParams.timeout_read = timeOut;
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

    /************************ 其他设置 ********************************/

    /**
     * 请求回调
     */
    IHttpResultCallBack iResultCallBack;
    public BaseHttpUtils initHttpResultCallBack(IHttpResultCallBack iResultCallBack) {
        this.iResultCallBack = iResultCallBack;
        return this;
    }

    /**
     * 设置加载提示框
     * @param dialog
     * @return
     */
    Dialog dialog;
    public BaseHttpUtils initDialog(Dialog dialog){
        this.dialog = dialog;
        return this;
    }

    /**
     * 设置 dialog是否消失 默认最后必须消失
     * @param isDialogDismiss
     * @return
     */
    boolean isDialogDismiss = true;
    public BaseHttpUtils initDialogDismiss(boolean isDialogDismiss){
        this.isDialogDismiss = isDialogDismiss;
        return this;
    }
    /**
     * 设置 dialog请求成功时是否消失 默认最后必须消失
     * @param isDialogDismiss
     * @return
     */
    boolean isDialogDismissWhenSuccess = true;
    public BaseHttpUtils initDialogDismissWhenSuccess(boolean isDialogDismissWhenSuccess){
        this.isDialogDismissWhenSuccess = isDialogDismissWhenSuccess;
        return this;
    }
    /**
     * 设置 dialog请求结果是空数据是否消失 默认最后必须消失
     * @param isDialogDismiss
     * @return
     */
    boolean isDialogDismissWhenEmpty = true;
    public BaseHttpUtils initDialogDismissWhenEmpty(boolean isDialogDismissWhenEmpty){
        this.isDialogDismissWhenEmpty = isDialogDismissWhenEmpty;
        return this;
    }
    /**
     * 设置 dialog请求失败时是否消失 默认最后必须消失
     * @param isDialogDismiss
     * @return
     */
    boolean isDialogDismissWhenFail = true;
    public BaseHttpUtils initDialogDismissWhenFail(boolean isDialogDismissWhenFail){
        this.isDialogDismissWhenFail = isDialogDismissWhenFail;
        return this;
    }

    public BaseHttpUtils dismissDialog() {
        if (null != dialog && dialog.isShowing()) {
            dialog.dismiss();
        }
        return this;
    }


    /**
     * 发送请求
     */
//    private final Handler mHandler = new Handler(Looper.getMainLooper());
    private final Handler mHandler = new Handler();

    public void post() {
        baseHttpParams.request_type = BaseHttpConfig.RequestType.POST;
        request();
    }

    public void get() {
        baseHttpParams.request_type = BaseHttpConfig.RequestType.GET;
        request();
    }

    // 网络请求要在线程中进行
    public void request() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            BaseThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    requests();
                }
            });
        } else {
            requests();
        }
    }

    private void requests() {
        baseHttpParams.openLog = checkLog();

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (iResultCallBack != null) {
                    iResultCallBack.onBeforeRequest(baseHttpParams);
                }
            }
        });

        /******************** 发送前检查 ***********************/

        baseResult.errorInfo.errorType = BaseHttpConfig.ErrorType.Error_Use;
        baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Use;

        try {
            // 获取当前的 数据请求工具类
            iHttpServiceCurr = iHttpService == null ? (IHttpService) (iHttpServiceStatic == null ? null : iHttpServiceStatic.newInstance()) : iHttpService;
            // 获取当前的 数据解析工具类
            iDataListenerCurr = iDataListener == null ? (IDataListener) (iDataListenerStatic == null ? null : iDataListenerStatic.newInstance())  : iDataListener;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        // 检查调用方式是否正确
        if ((iHttpServiceCurr == null || iDataListenerCurr == null)) {
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ": 请先初始化设置好请求和解析工具类");
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(isDialogDismiss && isDialogDismissWhenFail){
                        dismissDialog();
                    }
                    if (iResultCallBack != null) {
                        baseResult.errorInfo.errorMsg = "请先初始化设置好请求和解析工具类";
                        iResultCallBack.onFailUse(baseResult.errorInfo);
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return;
        } else if ("".equals(baseHttpParams.url) || null == baseHttpParams.url) {
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ": url不能为空");
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(isDialogDismiss && isDialogDismissWhenFail){
                        dismissDialog();
                    }
                    if (iResultCallBack != null) {
                        baseResult.errorInfo.errorMsg = "url不能为空";
                        iResultCallBack.onFailUse(baseResult.errorInfo);
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return;
        }

        /******************** 发送请求 ***********************/

        if (baseHttpParams.openLog) {
            Log.e(BaseHttpConfig.TAG, tags + ": 开始发送网络请求...");
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (iResultCallBack != null) {
                    iResultCallBack.onRequest(baseHttpParams);
                }
            }
        });
        Object params = iHttpServiceCurr.parseParams(baseHttpParams,baseResult);
        if(params == null){
            params = "";
        }
        baseHttpParams.params = params;
        iHttpServiceCurr.request(baseHttpParams, baseResult);
        if(iHttpServiceCurr.isFail(baseHttpParams,baseResult)){
            baseResult.isResultParseSucess = false;
            final BaseErrorInfo baseErrorInfo = iHttpServiceCurr.getErrorInfo(baseHttpParams, baseResult);
            if (baseErrorInfo == null) {
                if (baseHttpParams.openLog) {
                    Log.e(BaseHttpConfig.TAG, tags + ": getErrorInfo方法中 BaseErrorInfo不能为空");
                }
            } else {
                if (baseHttpParams.openLog) {
                    Log.e(BaseHttpConfig.TAG, baseHttpParams.tags + ": " + baseResult.errorInfo.errorMsg);
                }
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(isDialogDismiss && isDialogDismissWhenFail){
                        dismissDialog();
                    }
                    if (iResultCallBack != null) {
                        BaseErrorInfo baseErrorInfos = baseErrorInfo;
                        if(baseErrorInfos == null){
                            baseErrorInfos = new BaseErrorInfo();
                            baseErrorInfos.errorType = BaseHttpConfig.ErrorType.Error_Use;
                            baseErrorInfos.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
                            baseErrorInfos.errorMsg = "getErrorInfo方法中 BaseErrorInfo不能为空";
                        }
                        baseResult.errorInfo = baseErrorInfos;
                        iResultCallBack.onFailRequest(baseErrorInfos);
                        iResultCallBack.onFail(baseErrorInfos);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return;
        }
        baseResult.isResultParseSucess = true;

        /******************** 解析返回值 ***********************/
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (iResultCallBack != null) {
                    iResultCallBack.onSuccessRequest(baseHttpParams,baseResult);
                    iResultCallBack.onBeforeDataParse(baseHttpParams,baseResult);
                }
            }
        });

        //请求成功,解析返回值
        iDataListenerCurr.parse(baseHttpParams, baseResult);
        if (iDataListenerCurr.isFail(baseHttpParams, baseResult)) {
            baseResult.isResultParseSucess = false;
            final BaseErrorInfo baseErrorInfo = iDataListenerCurr.getErrorInfo(baseHttpParams, baseResult);
            if (baseErrorInfo == null) {
                if (baseHttpParams.openLog) {
                    Log.e(BaseHttpConfig.TAG, tags + ": getErrorInfo方法中 BaseErrorInfo不能为空");
                }
            } else {
                if (baseHttpParams.openLog) {
                    Log.e(BaseHttpConfig.TAG, baseHttpParams.tags + ": " + baseResult.errorInfo.errorMsg);
                }
            }
            // 解析结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(isDialogDismiss && isDialogDismissWhenFail){
                        dismissDialog();
                    }
                    if (iResultCallBack != null) {
                        BaseErrorInfo baseErrorInfos = baseErrorInfo;
                        if(baseErrorInfos == null){
                            baseErrorInfos = new BaseErrorInfo();
                            baseErrorInfos.errorType = BaseHttpConfig.ErrorType.Error_Use;
                            baseErrorInfos.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
                            baseErrorInfos.errorMsg = "getErrorInfo方法中 BaseErrorInfo不能为空";
                        }
                        iResultCallBack.onFail(baseErrorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return;
        } else if (iDataListenerCurr.isEmpty(baseHttpParams, baseResult)) {
            baseResult.isResultParseSucess = false;
            final BaseErrorInfo baseErrorInfo = iDataListenerCurr.getErrorInfo(baseHttpParams, baseResult);
            if (baseErrorInfo == null) {
                if (baseHttpParams.openLog) {
                    Log.e(BaseHttpConfig.TAG, tags + ": getErrorInfo方法中 BaseErrorInfo不能为空");
                }
            } else {
                if (baseHttpParams.openLog) {
                    Log.e(BaseHttpConfig.TAG, baseHttpParams.tags + ": " + baseResult.errorInfo.errorMsg);
                }
            }
            // 解析结果标识为 查询结果为空数据
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if(isDialogDismiss && isDialogDismissWhenEmpty){
                        dismissDialog();
                    }
                    if (iResultCallBack != null) {
                        BaseErrorInfo baseErrorInfos = baseErrorInfo;
                        if(baseErrorInfos == null){
                            baseErrorInfos = new BaseErrorInfo();
                            baseErrorInfos.errorType = BaseHttpConfig.ErrorType.Error_Use;
                            baseErrorInfos.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
                            baseErrorInfos.errorMsg = "getErrorInfo方法中 BaseErrorInfo不能为空";
                        }
                        iResultCallBack.onEmpty(baseErrorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return;
        } else {
            baseResult.isResultParseSucess = true;
        }
        baseResult.success = true;
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (baseResult.success) {
                    if(isDialogDismiss && isDialogDismissWhenSuccess){
                        dismissDialog();
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onSuccess(baseResult);
                    }
                } else {
                    if(isDialogDismiss && isDialogDismissWhenFail){
                        dismissDialog();
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onFail(baseResult.errorInfo);
                    }
                }
                if (iResultCallBack != null) {
                    iResultCallBack.onFinal(baseResult);
                }
            }
        });
    }

}
