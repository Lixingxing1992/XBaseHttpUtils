package com.xhttp.lib;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.impl.data.DefaultDataListener;
import com.xhttp.lib.impl.file.DefaultFileService;
import com.xhttp.lib.impl.message.MessageManager;
import com.xhttp.lib.impl.service.DefaultHttpService;
import com.xhttp.lib.interfaces.callback.IFileUploadListener;
import com.xhttp.lib.interfaces.callback.IHttpFileResultCallBack;
import com.xhttp.lib.interfaces.callback.IHttpResultCallBack;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.interfaces.data.IDataListenerFilter;
import com.xhttp.lib.interfaces.file.IFileService;
import com.xhttp.lib.interfaces.http.IHttpService;
import com.xhttp.lib.interfaces.http.IHttpServiceFilter;
import com.xhttp.lib.interfaces.message.IMessageManager;
import com.xhttp.lib.interfaces.message.IMessageManagerFilter;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.util.BaseThreadPoolUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.xhttp.lib.config.BaseHttpConfig.TAG;

/**
 * Created by lixingxing on 2019/3/26.
 */
public final class BaseHttpUtils {
    // 唯一标识
    private String tags = "";
    private static Context contextStatic;
    // 是否打开Log日志
    private static boolean openLogStatic = true;

    public static void init(Context context, boolean openLogs) {
        contextStatic = context;
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
        if (contextStatic == null) {
            throw new RuntimeException("请在application中调用init(Context context,boolean openLogs)方法，并且context!=null");
        }
        this.baseHttpParams = baseHttpParams;
        this.baseResult = new BaseResult();

        tags = UUID.randomUUID().toString();
//        tags = System.currentTimeMillis() + "";
        this.baseHttpParams.tags = tags;
        if (iHttpServiceStatic == null) {
            // 默认
            iHttpServiceStatic = DefaultHttpService.class;
        }
        if (iDataListenerStatic == null) {
            // 默认
            iDataListenerStatic = DefaultDataListener.class;
        }
        if (iMessageManagerStatic == null) {
            // 默认
            iMessageManagerStatic = MessageManager.class;
        }
        if (iFileServiceStatic == null) {
            // 默认
            iFileServiceStatic = DefaultFileService.class;
        }
        if(iFileDataStatic == null){
            // 默认
            iFileDataStatic = DefaultFileService.class;
        }
    }

    /************************ 请求参数 ********************************/
    /**
     * 全局的 请求工具类 iHttpServiceStatic 和 数据解析类 iDataListenerStatic
     * 会被 initIHttpService 和 initIDataListener中设置的值覆盖掉
     */
    public static Class iHttpServiceStatic;
    public static Class iDataListenerStatic;
    public static Class iFileServiceStatic;
    public static Class iFileDataStatic;

    private IHttpService iHttpServiceCurr;
    private IDataListener iDataListenerCurr;
    private IFileService iFileServiceCurr;
    private IDataListener iFileDataListenerCurr;

    // 初始化设置 全局的 请求工具类 和 数据解析类
    public static void init(Class iHttpServiceStatics, Class iDataListenerStatics) {
        if (iHttpServiceStatics != null && IHttpService.class.isAssignableFrom(iHttpServiceStatics)) {
            iHttpServiceStatic = iHttpServiceStatics;
        } else if (iHttpServiceStatics != null) {
            throw new RuntimeException("初始化请求工具类和数据解析类时类型错误");
        }
        if (iDataListenerStatics != null && IDataListener.class.isAssignableFrom(iDataListenerStatics)) {
            iDataListenerStatic = iDataListenerStatics;
        } else if (iDataListenerStatics != null) {
            throw new RuntimeException("初始化请求工具类和数据解析类时类型错误");
        }
    }


    // 初始化设置 全局的 请求工具类 和 数据解析类(如果没有调用，则会使用默认的)
    public static void initFile(Class iHttpServiceStatics, Class iDataListenerStatics) {
        if (iHttpServiceStatics != null && IFileService.class.isAssignableFrom(iHttpServiceStatics)) {
            iFileServiceStatic = iHttpServiceStatics;
        } else if (iHttpServiceStatics != null) {
            throw new RuntimeException("初始化请求工具类和数据解析类时类型错误");
        }
        if (iDataListenerStatics != null && IDataListener.class.isAssignableFrom(iDataListenerStatics)) {
            iFileDataStatic = iDataListenerStatics;
        } else if (iDataListenerStatics != null) {
            throw new RuntimeException("初始化请求工具类和数据解析类时类型错误");
        }
    }

    /**
     * 针对 本次请求的 请求工具类 iHttpService 和 数据解析类 iDataListener
     * 会覆盖全局的 请求工具类 iHttpServiceStatic 和 数据解析类 iDataListenerStatic
     */
    // 网络请求工具类处理
    private IHttpServiceFilter iHttpServiceFilter;
    // 数据解析工具类处理
    private IDataListenerFilter iDataListenerFilter;

    // 设置 网络请求工具类
    public BaseHttpUtils initIHttpService(IHttpService iHttpService) {
        this.iHttpServiceCurr = iHttpService;
        return this;
    }

    // 对设置好的 IHttpService 进行处理
    public BaseHttpUtils initIHttpServiceFilter(IHttpServiceFilter iHttpServiceFilter) {
        this.iHttpServiceFilter = iHttpServiceFilter;
        return this;
    }

    // 设置 数据解析工具类
    public BaseHttpUtils initIDataListener(IDataListener iDataListener) {
        this.iDataListenerCurr = iDataListener;
        return this;
    }

    // 对设置好的  IDataListener 进行处理
    public BaseHttpUtils initIDataListenerFilter(IDataListenerFilter iDataListenerFilter) {
        this.iDataListenerFilter = iDataListenerFilter;
        return this;
    }

    /**
     * 设置请求路径 url
     *
     * @param url
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
     */
    public BaseHttpUtils initRequestType(BaseHttpConfig.RequestType type) {
        baseHttpParams.request_type = type;
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param timeOut
     */
    public BaseHttpUtils initConnectTimeOut(int timeOut) {
        baseHttpParams.timeout_connect = timeOut;
        return this;
    }

    public BaseHttpUtils initReadTimeOut(int timeOut) {
        baseHttpParams.timeout_read = timeOut;
        return this;
    }

    public BaseHttpUtils initTimeOut(int timeOut) {
        baseHttpParams.timeout_connect = timeOut;
        baseHttpParams.timeout_read = timeOut;
        return this;
    }


    /************************返回值********************************/

    /**
     * 设置返回值 解析模式
     *
     * @param dataParseType
     */
    public BaseHttpUtils initDataParseType(BaseHttpConfig.DataParseType dataParseType) {
        baseHttpParams.dataParseType = dataParseType;
        return this;
    }

    /**
     * 设置返回值 解析类型
     *
     * @param tClass
     */
    public BaseHttpUtils initClass(Class tClass) {
        baseHttpParams.aClass = tClass;
        return this;
    }


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
     * 请求回调提示语句工具类
     */
    static Class iMessageManagerStatic;

    public static void init(Class iMessageManagerStatics) {
        if (iMessageManagerStatics != null && IMessageManager.class.isAssignableFrom(iMessageManagerStatics)) {
            iMessageManagerStatic = iMessageManagerStatics;
        } else if (iMessageManagerStatics != null) {
            throw new RuntimeException("初始化提示语句工具类时类型错误");
        }
    }

    IMessageManager iMessageManager;
    IMessageManagerFilter iMessageManagerFilter;

    public BaseHttpUtils initIMessageManager(IMessageManager iMessageManager) {
        this.iMessageManager = iMessageManager;
        return this;
    }

    public BaseHttpUtils initIMessageManagerFilter(IMessageManagerFilter iMessageManagerFilter) {
        this.iMessageManagerFilter = iMessageManagerFilter;
        return this;
    }

    /**
     * 设置是否显示提示语句 默认显示
     */
    boolean isShowMessage = true;

    public BaseHttpUtils initShowMessage(boolean isShowMessage) {
        this.isShowMessage = isShowMessage;
        return this;
    }

    /**
     * 设置是否显示错误时提示语句 默认显示
     */
    boolean isShowErrorMessage = true;

    public BaseHttpUtils initShowErrorMessage(boolean isShowErrorMessage) {
        this.isShowErrorMessage = isShowErrorMessage;
        return this;
    }

    String errorMsg = "";

    public BaseHttpUtils initErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
        return this;
    }

    /**
     * 设置是否显示空数据时提示语句 默认null 列表模式下不显示
     * 如果列表模式下想设置显示 可调用此方法 initShowEmptyMessage(true)
     */
    Boolean isShowEmptyMessage = null;

    public BaseHttpUtils initShowEmptyMessage(Boolean isShowEmptyMessage) {
        this.isShowEmptyMessage = isShowEmptyMessage;
        return this;
    }

    String emptyMsg = "";

    public BaseHttpUtils initEmptyMsg(String emptyMsg) {
        this.emptyMsg = emptyMsg;
        return this;
    }

    /**
     * 设置是否显示正确时提示语句 默认不显示
     */
    boolean isShowSuccessMessage = false;

    public BaseHttpUtils initShowSuccessMessage(boolean isShowSuccessMessage) {
        this.isShowSuccessMessage = isShowSuccessMessage;
        return this;
    }

    String successMsg = "请求成功";

    public BaseHttpUtils initSuccessMsg(String successMsg) {
        this.successMsg = successMsg;
        return this;
    }

    /**
     * 设置加载提示框
     *
     * @param dialog
     */
    Dialog dialog;

    public BaseHttpUtils initDialog(Dialog dialog) {
        this.dialog = dialog;
        return this;
    }

    /**
     * 设置 dialog是否消失 默认最后必须消失
     *
     * @param isDialogDismiss
     */
    boolean isDialogDismiss = true;

    public BaseHttpUtils initDialogDismiss(boolean isDialogDismiss) {
        this.isDialogDismiss = isDialogDismiss;
        return this;
    }

    /**
     * 设置 dialog请求成功时是否消失 默认最后必须消失
     *
     * @param isDialogDismiss
     */
    boolean isDialogDismissWhenSuccess = true;

    public BaseHttpUtils initDialogDismissWhenSuccess(boolean isDialogDismissWhenSuccess) {
        this.isDialogDismissWhenSuccess = isDialogDismissWhenSuccess;
        return this;
    }

    /**
     * 设置 dialog请求结果是空数据是否消失 默认最后必须消失
     *
     * @param isDialogDismiss
     */
    boolean isDialogDismissWhenEmpty = true;

    public BaseHttpUtils initDialogDismissWhenEmpty(boolean isDialogDismissWhenEmpty) {
        this.isDialogDismissWhenEmpty = isDialogDismissWhenEmpty;
        return this;
    }

    /**
     * 设置 dialog请求失败时是否消失 默认最后必须消失
     *
     * @param isDialogDismiss
     */
    boolean isDialogDismissWhenFail = true;

    public BaseHttpUtils initDialogDismissWhenFail(boolean isDialogDismissWhenFail) {
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
    private final Handler mHandler = new Handler(Looper.getMainLooper());


    // post
    public void postList(Class clz) {
        baseHttpParams.aClass = clz;
        postList();
    }

    public void postList() {
        baseHttpParams.dataParseType = BaseHttpConfig.DataParseType.List;
        post();
    }

    public void postObject(Class clz) {
        baseHttpParams.aClass = clz;
        postObject();
    }

    public void postObject() {
        baseHttpParams.dataParseType = BaseHttpConfig.DataParseType.Object;
        post();
    }

    public void post() {
        baseHttpParams.request_type = BaseHttpConfig.RequestType.POST;
        request();
    }

    // get
    public void getList(Class clz) {
        baseHttpParams.aClass = clz;
        getList();
    }

    public void getList() {
        baseHttpParams.dataParseType = BaseHttpConfig.DataParseType.List;
        get();
    }

    public void getObject(Class clz) {
        baseHttpParams.aClass = clz;
        getObject();
    }

    public void getObject() {
        baseHttpParams.dataParseType = BaseHttpConfig.DataParseType.Object;
        get();
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

    // 请求方法
    private void requests() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (iResultCallBack != null) {
                    iResultCallBack.onBeforeRequest(baseHttpParams);
                }
            }
        });
        // log开关设置
        baseHttpParams.openLog = checkLog();
        // 设置空数据时提示语句是否显示, 如果调用过 initShowEmptyMessage方法,则该方法不调用
        settingEmptyMessage();
        // messageManager设置
        settingMessageManager();
        // 检查一些基本设置
        if (!settingDefaultParams()) {
            return;
        }
        if (!settingHttpServiceAndDataListener()) {
            return;
        }
        if (!callHttpRequest()) {
            return;
        }
        if (!callDataParse()) {
            return;
        }

        if (baseHttpParams.openLog) {
            Log.e(BaseHttpConfig.TAG, tags + ": 请求完成,返回成功");
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isDialogDismiss && isDialogDismissWhenSuccess) {
                    dismissDialog();
                }
                if (iMessageManager != null && isShowMessage && isShowSuccessMessage) {
                    iMessageManager.showMessages(contextStatic, "".equals(successMsg) ? baseResult.errorInfo.errorMsg : successMsg);
                }
                if (iResultCallBack != null) {
                    iResultCallBack.onSuccess(baseResult);
                    iResultCallBack.onFinal(baseResult);
                }
            }
        });
    }

    // 设置空数据时提示语句是否显示, 如果调用过 initShowEmptyMessage方法,则该方法不调用
    private void settingEmptyMessage() {
        if (isShowEmptyMessage == null) {
            // 默认模式 列表模式下不显示
            if (baseHttpParams.dataParseType == BaseHttpConfig.DataParseType.List) {
                isShowEmptyMessage = false;
            } else {
                isShowEmptyMessage = true;
            }
        }
    }

    // messageManager设置
    private void settingMessageManager() {
        try {
            iMessageManager = iMessageManager == null ? (IMessageManager) iMessageManagerStatic.newInstance() : iMessageManager;
            if (iMessageManagerFilter != null && iMessageManager != null) {
                iMessageManagerFilter.filterIMessageManager(iMessageManager);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
    }

    private boolean settingDefaultParams() {
        if (baseHttpParams.aClass == null && baseHttpParams.dataParseType != BaseHttpConfig.DataParseType.String) {
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ":错误描述_ 请设置好需要解析的对象类型");
            }
            baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
            baseResult.errorInfo.errorMsg = "请设置好需要解析的对象类型";
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onFailUse(baseResult.errorInfo);
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        return true;
    }


    // 根据传入的值 初始化 IHttpService 和 IDataListener
    private boolean settingHttpServiceAndDataListener() {
        /******************** 发送前检查 ***********************/
        baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
        try {
            // 获取当前的 数据请求工具类
            iHttpServiceCurr = iHttpServiceCurr == null ? (IHttpService) (iHttpServiceStatic == null ? null : iHttpServiceStatic.newInstance()) : iHttpServiceCurr;
            // 获取当前的 数据解析工具类
            iDataListenerCurr = iDataListenerCurr == null ? (IDataListener) (iDataListenerStatic == null ? null : iDataListenerStatic.newInstance()) : iDataListenerCurr;
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
            baseResult.errorInfo.errorMsg = "请先初始化设置好请求和解析工具类";
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onFailUse(baseResult.errorInfo);
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        } else {
            if (iHttpServiceFilter != null && iHttpServiceCurr != null) {
                iHttpServiceFilter.filterIHttpService(iHttpServiceCurr);
            }
            if (iDataListenerFilter != null && iDataListenerCurr != null) {
                iDataListenerFilter.filterIDataListener(iDataListenerCurr);
            }
            // filterIHttpService 和 filterIDataListener 以后 再做一次非空判断
            if ((iHttpServiceCurr == null || iDataListenerCurr == null)) {
                if (baseHttpParams.openLog) {
                    Log.e(BaseHttpConfig.TAG, tags + ": filterIHttpService 和 filterIDataListener 方法不能设置参数为null");
                }
                baseResult.errorInfo.errorMsg = "filterIHttpService 和 filterIDataListener 方法不能设置参数为null";
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isDialogDismiss && isDialogDismissWhenFail) {
                            dismissDialog();
                        }
                        if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                            iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                        }
                        if (iResultCallBack != null) {
                            iResultCallBack.onFailUse(baseResult.errorInfo);
                            iResultCallBack.onFail(baseResult.errorInfo);
                            iResultCallBack.onFinal(baseResult);
                        }
                    }
                });
                return false;
            }
            return true;
        }
    }

    // 发送请求
    private boolean callHttpRequest() {
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
        if ("".equals(baseHttpParams.url) || null == baseHttpParams.url) {
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ":错误描述_ url不能为空");
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    baseResult.errorInfo.errorMsg = "url不能为空";
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onFailUse(baseResult.errorInfo);
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        Object params = iHttpServiceCurr.parseParams(baseHttpParams);
        if (params == null) {
            params = "";
        }
        baseHttpParams.params = params;

        final BaseRequestResult baseRequestResult = iHttpServiceCurr.request(baseHttpParams);
        if (baseRequestResult == null || !baseRequestResult.checkResult()) {
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ":错误描述_ IHttpService的request方法中 返回值BaseRequestResult不符合规定");
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    BaseErrorInfo baseErrorInfos = new BaseErrorInfo();
                    baseErrorInfos.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
                    baseErrorInfos.errorMsg = "IHttpService的request方法中 返回值BaseRequestResult不符合规定";
                    baseResult.errorInfo = baseErrorInfos;
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onFailRequest(baseErrorInfos);
                        iResultCallBack.onFail(baseErrorInfos);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.baseRequestResult = baseRequestResult;
        if (!baseRequestResult.isSuccess) {
            baseResult.success = false;
            baseResult.errorInfo = baseRequestResult.errorInfo;
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onFailRequest(baseResult.errorInfo);
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.baseRequestResult.isSuccess = true;
        return true;
    }

    // 数据解析
    private boolean callDataParse() {
        /******************** 解析返回值 ***********************/
        if (baseHttpParams.openLog) {
            Log.e(BaseHttpConfig.TAG, tags + ": 开始解析返回值...");
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (iResultCallBack != null) {
                    iResultCallBack.onBeforeDataParse(baseHttpParams, baseResult);
                }
            }
        });
        byte[] bytes = baseResult.baseRequestResult.bytes;

        baseResult.result = new BaseResult.Result();
        baseResult.result.resultAll = new String(bytes);
        baseResult.result.resultData = baseResult.result.resultAll;
        if (baseHttpParams.openLog) {
            Log.e(TAG, baseHttpParams.tags + ":返回值的结果是: " + baseResult.result.resultAll);
        }
        String resultData = "";
        try {
            resultData = iDataListenerCurr.parseResult(baseHttpParams, bytes);
        } catch (Exception e) {
            resultData = "";

            baseResult.success = false;
            baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error;
            baseResult.errorInfo.exception = e;
            baseResult.errorInfo.errorMsg = "IDataListener parseResult方法出现异常,异常信息为:" + e;

            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ IDataListener parseResult方法出现异常,异常信息为:" + e);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onFailRequest(baseResult.errorInfo);
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.result.resultData = resultData;

        if (resultData != null && !"".equals(resultData) && !"null".equals(resultData)) {
            try {
                switch (baseHttpParams.dataParseType) {
                    case List:
                        baseResult.result.setResult_list(iDataListenerCurr.parseList(baseHttpParams, resultData));
                        break;
                    case Object:
                        baseResult.result.setResult_object(iDataListenerCurr.parseObject(baseHttpParams, resultData));
                        break;
                    case Combination:
                        baseResult.result.setResult_list_combination(iDataListenerCurr.parseCombination(baseHttpParams, resultData));
                        break;
                    case String:
                        baseResult.result.setResult_str(iDataListenerCurr.parseDefault(baseHttpParams, resultData));
                    default:
                        break;
                }
            } catch (Exception e) {
                baseResult.success = false;
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error;
                baseResult.errorInfo.exception = e;
                baseResult.errorInfo.errorMsg = "IDataListener 返回值解析异常,异常信息为:" + e;

                if (baseHttpParams.openLog) {
                    Log.e(TAG, baseHttpParams.tags + ":错误描述_ IDataListener 返回值解析异常,异常信息为:" + e);
                }
                // 请求结果出现异常
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isDialogDismiss && isDialogDismissWhenFail) {
                            dismissDialog();
                        }
                        if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                            iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                        }
                        if (iResultCallBack != null) {
                            iResultCallBack.onFailRequest(baseResult.errorInfo);
                            iResultCallBack.onFail(baseResult.errorInfo);
                            iResultCallBack.onFinal(baseResult);
                        }
                    }
                });
                return false;
            }
        }
        if (iDataListenerCurr.isFail(baseHttpParams, baseResult)) {
            baseResult.success = false;
            // 返回值提示错误
            BaseErrorInfo baseErrorInfo = iDataListenerCurr.getFailErrorInfo();
            if (baseErrorInfo == null) {
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error_default;
                baseResult.errorInfo.errorMsg = "请求失败,请稍后重试";
            } else {
                baseResult.errorInfo = baseErrorInfo;
            }

            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, "".equals(errorMsg) ? baseResult.errorInfo.errorMsg : errorMsg);
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onFailRequest(baseResult.errorInfo);
                        iResultCallBack.onFail(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        } else if (iDataListenerCurr.isEmpty(baseHttpParams, baseResult)) {
            baseResult.success = true;
            baseResult.isEmpty = true;
            // 返回值提示错误
            BaseErrorInfo baseErrorInfo = iDataListenerCurr.getEmptyErrorInfo();
            if (baseErrorInfo == null) {
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_none;
                baseResult.errorInfo.errorMsg = "未获取到数据";
            } else {
                baseResult.errorInfo = baseErrorInfo;
            }
            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenEmpty) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowEmptyMessage) {
                        iMessageManager.showEmptyMessages(contextStatic, "".equals(emptyMsg) ? baseResult.errorInfo.errorMsg : emptyMsg);
                    }
                    if (iResultCallBack != null) {
                        iResultCallBack.onEmpty(baseResult.errorInfo);
                        iResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        return true;
    }


    /************************ 上传文件设置 ********************************/
    // 是否同步上传 默认 是
    public boolean isSysn  = true;
    public BaseHttpUtils initUploadSysn(boolean isSysn){
        this.isSysn = isSysn;
        return this;
    }
    // 最大异步上传个数（最大是3）
    public int maxUploadCount = 3;
    public BaseHttpUtils initMaxUploadCount(int maxUploadCount){
        this.maxUploadCount = maxUploadCount;
        return this;
    }

    public List<File> listFiles = new ArrayList<>();
    public List<String> listFilesKey = new ArrayList<>();

    public BaseHttpUtils initFilePath(String filePath) {
        File file = new File(filePath);
        return initFile(file);
    }

    public BaseHttpUtils initFile(File file) {
        if (file != null) {
            listFiles.add(file);
        }
        return this;
    }
    public BaseHttpUtils initFilesList(List<File> fileList) {
        this.listFiles.addAll(fileList);
        return this;
    }

    public BaseHttpUtils initFileList(String... filePathList) {
        for (String s : filePathList) {
            initFilePath(s);
        }
        return this;
    }

    public BaseHttpUtils initFileList(List<String> filePathList) {
        for (String s : filePathList) {
            initFilePath(s);
        }
        return this;
    }


    public BaseHttpUtils initFileKeyList(String... filePathList) {
        for (String s : filePathList) {
            listFilesKey.add(s);
        }
        return this;
    }

    /**
     * key生成策略
     * isChange = true   keyFirst0 keyFirst1 keyFirst2 keyFirst3...
     * isChange = false   keyFirst keyFirst keyFirst keyFirst...
     *
     * @param keyFirst
     * @param isChange
     * @return
     */
    String keyFirst = "file";
    boolean isKeyChange = false;
    // key自动生成。
    boolean isKeyAuto = false;

    public BaseHttpUtils initFileKey(String keyFirst, boolean isKeyChange) {
        isKeyAuto = true;
        this.keyFirst = keyFirst;
        this.isKeyChange = isKeyChange;
        return this;
    }

    // 上传完成后需要不需要删除文件
    public BaseHttpUtils initFileDelete(boolean delete) {
        baseHttpParams.isDelete = delete;
        return this;
    }

    /**
     * 文件上传请求回调
     */
    IHttpFileResultCallBack iHttpFileResultCallBack;

    public BaseHttpUtils initHttpFileResultCallBack(IHttpFileResultCallBack iHttpFileResultCallBack) {
        this.iHttpFileResultCallBack = iHttpFileResultCallBack;
        return this;
    }

    public void upload() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            BaseThreadPoolUtil.execute(new Runnable() {
                @Override
                public void run() {
                    uploads();
                }
            });
        } else {
            uploads();
        }
    }

    private void uploads() { // log开关设置
        baseHttpParams.openLog = checkLog();
        settingMessageManager();

        if ("".equals(baseHttpParams.url) || null == baseHttpParams.url) {
            baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ":错误描述_ url不能为空");
            }
            deleteFile();
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    baseResult.errorInfo.errorMsg = "url不能为空";
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return;
        }

        if (baseHttpParams.openLog) {
            Log.e(TAG, baseHttpParams.tags + ": 开始上传文件");
            Log.e(TAG, baseHttpParams.tags + ": url = " + baseHttpParams.url);
            Log.e(TAG, baseHttpParams.tags + ": 上传文件个数:" + listFiles.size());
        }
        if (!settingFileServiceAndDataListener()) {
            deleteFile();
            return;
        }
        if (!checkFiles()) {
            deleteFile();
            return;
        }
        if(isSysn || listFiles.size() == 1){
            // 同步
            if (!callFileHttpRequest()) {
                deleteFile();
                return;
            }
            if (!callFileDataParse()) {
                deleteFile();
                return;
            }
        }else{
            boolean flag = false;
//            startUpload();
        }
        if (baseHttpParams.openLog) {
            Log.e(BaseHttpConfig.TAG, tags + ": 文件上传成功");
        }
        deleteFile();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (isDialogDismiss && isDialogDismissWhenSuccess) {
                    dismissDialog();
                }
                if (iMessageManager != null && isShowMessage && isShowSuccessMessage) {
                    iMessageManager.showMessages(contextStatic, "".equals(successMsg) ? baseResult.errorInfo.errorMsg : successMsg);
                }
                if (iHttpFileResultCallBack != null) {
                    iHttpFileResultCallBack.onSuccess(baseResult);
                    iHttpFileResultCallBack.onFinal(baseResult);
                }
            }
        });

    }

    // 根据传入的值 初始化 IFileService 和 IDataListener
    private boolean settingFileServiceAndDataListener() {
        /******************** 发送前检查 ***********************/
        baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
        try {
            // 获取当前的 数据请求工具类
            iFileServiceCurr = (iFileServiceCurr == null) ? (IFileService) (iFileServiceStatic == null ? null : iFileServiceStatic.newInstance()) : iFileServiceCurr;
            // 获取当前的 数据解析工具类
            iFileDataListenerCurr = iFileDataListenerCurr == null ? (IDataListener) (iFileDataStatic == null ? null : iFileDataStatic.newInstance()) : iFileDataListenerCurr;
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }
        // 检查调用方式是否正确
        if ((iFileServiceCurr == null || iFileDataListenerCurr == null)) {
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ": 请先初始化设置好文件处理工具和解析工具类");
            }
            baseResult.errorInfo.errorMsg = "请先初始化设置好文件处理工具和解析工具类";
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        } else {
//            if (iHttpServiceFilter != null && iHttpServiceCurr != null) {
//                iHttpServiceFilter.filterIHttpService(iHttpServiceCurr);
//            }
//            if (iDataListenerFilter != null && iDataListenerCurr != null) {
//                iDataListenerFilter.filterIDataListener(iDataListenerCurr);
//            }
//            // filterIHttpService 和 filterIDataListener 以后 再做一次非空判断
//            if ((iHttpServiceCurr == null || iDataListenerCurr == null)) {
//                if (baseHttpParams.openLog) {
//                    Log.e(BaseHttpConfig.TAG, tags + ": filterIHttpService 和 filterIDataListener 方法不能设置参数为null");
//                }
//                baseResult.errorInfo.errorMsg = "filterIHttpService 和 filterIDataListener 方法不能设置参数为null";
//                mHandler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        if (isDialogDismiss && isDialogDismissWhenFail) {
//                            dismissDialog();
//                        }
//                        if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
//                            iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
//                        }
//                        if (iHttpFileResultCallBack != null) {
//                            iHttpFileResultCallBack.onFail(baseResult.errorInfo);
//                            iHttpFileResultCallBack.onFinal(baseResult);
//                        }
//                    }
//                });
//                return false;
//            }
            return true;
        }
    }

    // 检查文件是否可用
    private boolean checkFiles() {
        baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_File_error;
        if (listFiles.isEmpty()) {
            baseResult.errorInfo.errorMsg = "上传文件为空";

            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, "".equals(errorMsg) ? baseResult.errorInfo.errorMsg : errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        } else {
            if (!isKeyAuto) {
                if (listFiles.size() != listFilesKey.size()) {
                    baseResult.errorInfo.errorMsg = "未设置上传文件的key值";
                    if (baseHttpParams.openLog) {
                        Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isDialogDismiss && isDialogDismissWhenFail) {
                                dismissDialog();
                            }
                            if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                                iMessageManager.showErrorMessages(contextStatic, "".equals(errorMsg) ? baseResult.errorInfo.errorMsg : errorMsg);
                            }
                            if (iHttpFileResultCallBack != null) {
                                iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                                iHttpFileResultCallBack.onFinal(baseResult);
                            }
                        }
                    });

                    return false;
                }
            } else {
                listFilesKey.clear();
            }
            boolean flag = true;
            for (int i = 0; i < listFiles.size(); i++) {
                File file = listFiles.get(i);
                if (!file.exists()) {
                    baseResult.errorInfo.errorMsg = "第" + (i + 1) + "个文件不存在: 路径为:" + file.getPath();
                    if (baseHttpParams.openLog) {
                        Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
                    }
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (isDialogDismiss && isDialogDismissWhenFail) {
                                dismissDialog();
                            }
                            if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                                iMessageManager.showErrorMessages(contextStatic, "".equals(errorMsg) ? baseResult.errorInfo.errorMsg : errorMsg);
                            }
                            if (iHttpFileResultCallBack != null) {
                                iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                                iHttpFileResultCallBack.onFinal(baseResult);
                            }
                        }
                    });
                    flag = false;
                    break;
                }
                if (isKeyAuto) {
                    listFilesKey.add(isKeyChange ? (keyFirst + i) : keyFirst);
                }
                if (baseHttpParams.openLog) {
                    Log.e(TAG, baseHttpParams.tags + ": 第" + (i + 1) + "个文件: 路径为:" + file.getPath() + ", key = " + listFilesKey.get(i));
                }
            }
            if (flag) {
                baseHttpParams.fileList = listFiles;
                baseHttpParams.fileKeys = listFilesKey;
            }
            return flag;
        }
    }

    // 删除文件
    private void deleteFile(){
        if(baseHttpParams.isDelete && !listFiles.isEmpty()){
            for (File listFile : listFiles) {
                if(listFile.exists()){
                    listFile.delete();
                }
            }
        }
    }

    // 文件上传
    private boolean callFileHttpRequest() {
        BaseRequestResult baseRequestResult = iFileServiceCurr.uploadFile(baseHttpParams, new IFileUploadListener() {
            @Override
            public void onFileProgress(final int position,final File file,final long curlenth,final long total) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (iHttpFileResultCallBack != null) {
                            iHttpFileResultCallBack.onFileProgress(position,file,curlenth,total);
                        }
                    }
                });
            }
        });
        if (baseRequestResult == null || !baseRequestResult.checkResult()) {
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ":错误描述_ IFileService的uploadFile方法中 返回值BaseRequestResult不符合规定");
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    BaseErrorInfo baseErrorInfos = new BaseErrorInfo();
                    baseErrorInfos.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
                    baseErrorInfos.errorMsg = "IFileService的uploadFile方法中 返回值BaseRequestResult不符合规定";
                    baseResult.errorInfo = baseErrorInfos;
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseErrorInfos);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.baseRequestResult = baseRequestResult;
        if (!baseRequestResult.isSuccess) {
            baseResult.success = false;
            baseResult.errorInfo = baseRequestResult.errorInfo;
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.baseRequestResult.isSuccess = true;
        return true;
    }

    // 文件上传后的数据解析
    private boolean callFileDataParse() {
        /******************** 解析返回值 ***********************/
        if (baseHttpParams.openLog) {
            Log.e(BaseHttpConfig.TAG, tags + ": 开始解析返回值...");
        }
        byte[] bytes = baseResult.baseRequestResult.bytes;

        baseResult.result = new BaseResult.Result();
        baseResult.result.resultAll = new String(bytes);
        baseResult.result.resultData = baseResult.result.resultAll;
        if (baseHttpParams.openLog) {
            Log.e(TAG, baseHttpParams.tags + ":返回值的结果是: " + baseResult.result.resultAll);
        }
        String resultData = "";
        try {
            resultData = iFileDataListenerCurr.parseResult(baseHttpParams, bytes);
        } catch (Exception e) {
            resultData = "";

            baseResult.success = false;
            baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error;
            baseResult.errorInfo.exception = e;
            baseResult.errorInfo.errorMsg = "IDataListener parseResult方法出现异常,异常信息为:" + e;

            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ IDataListener parseResult方法出现异常,异常信息为:" + e);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.result.resultData = resultData;

        if (resultData != null && !"".equals(resultData) && !"null".equals(resultData)) {
            try {
                switch (baseHttpParams.dataParseType) {
                    case List:
                        baseResult.result.setResult_list(iFileDataListenerCurr.parseList(baseHttpParams, resultData));
                        break;
                    case Object:
                        baseResult.result.setResult_object(iFileDataListenerCurr.parseObject(baseHttpParams, resultData));
                        break;
                    case Combination:
                        baseResult.result.setResult_list_combination(iFileDataListenerCurr.parseCombination(baseHttpParams, resultData));
                        break;
                    case String:
                        baseResult.result.setResult_str(iFileDataListenerCurr.parseDefault(baseHttpParams, resultData));
                    default:
                        break;
                }
            } catch (Exception e) {
                baseResult.success = false;
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error;
                baseResult.errorInfo.exception = e;
                baseResult.errorInfo.errorMsg = "IDataListener 返回值解析异常,异常信息为:" + e;

                if (baseHttpParams.openLog) {
                    Log.e(TAG, baseHttpParams.tags + ":错误描述_ IDataListener 返回值解析异常,异常信息为:" + e);
                }
                // 请求结果出现异常
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isDialogDismiss && isDialogDismissWhenFail) {
                            dismissDialog();
                        }
                        if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                            iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                        }
                        if (iHttpFileResultCallBack != null) {
                            iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                            iHttpFileResultCallBack.onFinal(baseResult);
                        }
                    }
                });
                return false;
            }
        }
        if (iFileDataListenerCurr.isFail(baseHttpParams, baseResult)) {
            baseResult.success = false;
            // 返回值提示错误
            BaseErrorInfo baseErrorInfo = iFileDataListenerCurr.getFailErrorInfo();
            if (baseErrorInfo == null) {
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error_default;
                baseResult.errorInfo.errorMsg = "请求失败,请稍后重试";
            } else {
                baseResult.errorInfo = baseErrorInfo;
            }

            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, "".equals(errorMsg) ? baseResult.errorInfo.errorMsg : errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        } else if (iFileDataListenerCurr.isEmpty(baseHttpParams, baseResult)) {
            baseResult.success = true;
            baseResult.isEmpty = true;
            // 返回值提示错误
            BaseErrorInfo baseErrorInfo = iFileDataListenerCurr.getEmptyErrorInfo();
            if (baseErrorInfo == null) {
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_none;
                baseResult.errorInfo.errorMsg = "未获取到数据";
            } else {
                baseResult.errorInfo = baseErrorInfo;
            }
            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenEmpty) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowEmptyMessage) {
                        iMessageManager.showEmptyMessages(contextStatic, "".equals(emptyMsg) ? baseResult.errorInfo.errorMsg : emptyMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onEmpty(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        return true;
    }

    // 已经完成上传的个数
    private int uploadedCount = 0;
    private void asynUpload(){
        if(listFiles.size() <= maxUploadCount){
            // 需要上传的文件没有达到最大上传数
            for (int i = 0; i < listFiles.size(); i++) {
                BaseThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        isAsynUploadFinish();
                    }
                });
            }
        }else{
            // 最开始启动 maxUploadCount个线程用于上传
            if(uploadedCount == 0){
                // 异步
                for (int i = 0; i < maxUploadCount; i++) {
                    BaseThreadPoolUtil.execute(new Runnable() {
                        @Override
                        public void run() {
                            isAsynUploadFinish();
                        }
                    });
                }
            }else{
                BaseThreadPoolUtil.execute(new Runnable() {
                    @Override
                    public void run() {
                        isAsynUploadFinish();
                    }
                });
            }
        }
    }
    // 是否全部上传完成
    private boolean isAsynUploadFinish(){
        uploadedCount+=1;
        if(uploadedCount == listFiles.size()){
            // 全部完成
            return true;
        }
        // 上传其他的
        asynUpload();
        return false;
    }
    // 异步上传
    private boolean callFileByAsyn(){
        BaseRequestResult baseRequestResult = iFileServiceCurr.uploadFile(baseHttpParams, new IFileUploadListener() {
            @Override
            public void onFileProgress(final int position,final File file,final long curlenth,final long total) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (iHttpFileResultCallBack != null) {
                            iHttpFileResultCallBack.onFileProgress(position,file,curlenth,total);
                        }
                    }
                });
            }
        });
        if (baseRequestResult == null || !baseRequestResult.checkResult()) {
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, tags + ":错误描述_ IFileService的uploadFile方法中 返回值BaseRequestResult不符合规定");
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    BaseErrorInfo baseErrorInfos = new BaseErrorInfo();
                    baseErrorInfos.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
                    baseErrorInfos.errorMsg = "IFileService的uploadFile方法中 返回值BaseRequestResult不符合规定";
                    baseResult.errorInfo = baseErrorInfos;
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseErrorInfos);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.baseRequestResult = baseRequestResult;
        if (!baseRequestResult.isSuccess) {
            baseResult.success = false;
            baseResult.errorInfo = baseRequestResult.errorInfo;
            if (baseHttpParams.openLog) {
                Log.e(BaseHttpConfig.TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.baseRequestResult.isSuccess = true;

        /******************** 解析返回值 ***********************/
        if (baseHttpParams.openLog) {
            Log.e(BaseHttpConfig.TAG, tags + ": 开始解析返回值...");
        }
        byte[] bytes = baseResult.baseRequestResult.bytes;

        baseResult.result = new BaseResult.Result();
        baseResult.result.resultAll = new String(bytes);
        baseResult.result.resultData = baseResult.result.resultAll;
        if (baseHttpParams.openLog) {
            Log.e(TAG, baseHttpParams.tags + ":返回值的结果是: " + baseResult.result.resultAll);
        }
        String resultData = "";
        try {
            resultData = iFileDataListenerCurr.parseResult(baseHttpParams, bytes);
        } catch (Exception e) {
            resultData = "";

            baseResult.success = false;
            baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error;
            baseResult.errorInfo.exception = e;
            baseResult.errorInfo.errorMsg = "IDataListener parseResult方法出现异常,异常信息为:" + e;

            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ IDataListener parseResult方法出现异常,异常信息为:" + e);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        baseResult.result.resultData = resultData;

        if (resultData != null && !"".equals(resultData) && !"null".equals(resultData)) {
            try {
                switch (baseHttpParams.dataParseType) {
                    case List:
                        baseResult.result.setResult_list(iFileDataListenerCurr.parseList(baseHttpParams, resultData));
                        break;
                    case Object:
                        baseResult.result.setResult_object(iFileDataListenerCurr.parseObject(baseHttpParams, resultData));
                        break;
                    case Combination:
                        baseResult.result.setResult_list_combination(iFileDataListenerCurr.parseCombination(baseHttpParams, resultData));
                        break;
                    case String:
                        baseResult.result.setResult_str(iFileDataListenerCurr.parseDefault(baseHttpParams, resultData));
                    default:
                        break;
                }
            } catch (Exception e) {
                baseResult.success = false;
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error;
                baseResult.errorInfo.exception = e;
                baseResult.errorInfo.errorMsg = "IDataListener 返回值解析异常,异常信息为:" + e;

                if (baseHttpParams.openLog) {
                    Log.e(TAG, baseHttpParams.tags + ":错误描述_ IDataListener 返回值解析异常,异常信息为:" + e);
                }
                // 请求结果出现异常
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (isDialogDismiss && isDialogDismissWhenFail) {
                            dismissDialog();
                        }
                        if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                            iMessageManager.showErrorMessages(contextStatic, baseResult.errorInfo.errorMsg);
                        }
                        if (iHttpFileResultCallBack != null) {
                            iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                            iHttpFileResultCallBack.onFinal(baseResult);
                        }
                    }
                });
                return false;
            }
        }
        if (iFileDataListenerCurr.isFail(baseHttpParams, baseResult)) {
            baseResult.success = false;
            // 返回值提示错误
            BaseErrorInfo baseErrorInfo = iFileDataListenerCurr.getFailErrorInfo();
            if (baseErrorInfo == null) {
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error_default;
                baseResult.errorInfo.errorMsg = "请求失败,请稍后重试";
            } else {
                baseResult.errorInfo = baseErrorInfo;
            }

            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenFail) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowErrorMessage) {
                        iMessageManager.showErrorMessages(contextStatic, "".equals(errorMsg) ? baseResult.errorInfo.errorMsg : errorMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onFail(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        } else if (iFileDataListenerCurr.isEmpty(baseHttpParams, baseResult)) {
            baseResult.success = true;
            baseResult.isEmpty = true;
            // 返回值提示错误
            BaseErrorInfo baseErrorInfo = iFileDataListenerCurr.getEmptyErrorInfo();
            if (baseErrorInfo == null) {
                baseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Result_none;
                baseResult.errorInfo.errorMsg = "未获取到数据";
            } else {
                baseResult.errorInfo = baseErrorInfo;
            }
            if (baseHttpParams.openLog) {
                Log.e(TAG, baseHttpParams.tags + ":错误描述_ " + baseResult.errorInfo.errorMsg);
            }
            // 请求结果出现异常
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (isDialogDismiss && isDialogDismissWhenEmpty) {
                        dismissDialog();
                    }
                    if (iMessageManager != null && isShowMessage && isShowEmptyMessage) {
                        iMessageManager.showEmptyMessages(contextStatic, "".equals(emptyMsg) ? baseResult.errorInfo.errorMsg : emptyMsg);
                    }
                    if (iHttpFileResultCallBack != null) {
                        iHttpFileResultCallBack.onEmpty(baseResult.errorInfo);
                        iHttpFileResultCallBack.onFinal(baseResult);
                    }
                }
            });
            return false;
        }
        return true;
    }


    class MyRunnable implements Runnable {
        int position;
        public MyRunnable(int position){
            this.position = position;
        }
        @Override
        public void run() {
            System.out.println(position+":当前执行第"+(position+1)+"个");
            try {
                Thread.sleep(500);
                uploaded(position);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static int listFileSize = 9;
    static int maxSize = 3;
    static int startedCount;
    static int uploadSuccessCount = 0;
    public void startUpload(){
        // 每次需要处理的个数
        int uploadCount = Math.min(maxSize, listFileSize);

        startedCount = uploadCount;

        for(int i = 0 ; i < uploadCount; i++){
            System.out.println(i+":开始执行第"+(i+1)+"个");
            new Thread(new MyRunnable(i)).start();
        }
    }
    public synchronized void uploaded(int position) {
        uploadSuccessCount+=1;
        System.out.println(position+":执行第"+(position+1)+"个成功,一共成功了"+uploadSuccessCount+"个");
        if(uploadSuccessCount == listFileSize){
            System.out.println("上传成功");
        }else if(startedCount < listFileSize){
            test2();
        }
    }
    public synchronized void test2(){
        startedCount+=1;
        if(startedCount == listFileSize){
            System.out.println("全部执行完毕");
        }
        System.out.println(startedCount+":开始执行第"+(startedCount)+"个");
        new Thread(new MyRunnable(startedCount-1)).start();
    }
}
