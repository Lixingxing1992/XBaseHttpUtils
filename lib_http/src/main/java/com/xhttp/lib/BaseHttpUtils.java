package com.xhttp.lib;

import android.app.Dialog;
import android.app.Fragment;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.LifecycleOwner;

import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.interfaces.callback.IFileUploadListener;
import com.xhttp.lib.interfaces.callback.IHttpResultCallBack;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.interfaces.data.IDataListenerFilter;
import com.xhttp.lib.interfaces.file.IFileService;
import com.xhttp.lib.interfaces.http.IHttpService;
import com.xhttp.lib.interfaces.http.IHttpServiceFilter;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.model.BaseResultData;
import com.xhttp.lib.params.BaseHttpDialogParams;
import com.xhttp.lib.params.BaseHttpInitParams;
import com.xhttp.lib.params.BaseHttpMessageParams;
import com.xhttp.lib.params.BaseHttpParams;
import com.xhttp.lib.util.BaseHttpCheckUtils;
import com.xhttp.lib.util.BaseJsonUtils;
import com.xhttp.lib.util.BaseLogUtils;
import com.xhttp.lib.util.BaseObjectUtils;
import com.xhttp.lib.util.BaseThreadPoolUtils;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * 网络工具类
 *
 * @author Lixingxing
 */
public class BaseHttpUtils {
    private static Handler mHandler;

    static {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mHandler = new Handler(Looper.getMainLooper());
        } else {
            mHandler = new Handler();
        }
    }

    // 唯一标识
    private String tags = "";
    // 全局参数
    private static BaseHttpInitParams mBaseHttpInitParams;
    // 请求参数
    private BaseHttpParams mBaseHttpParams;
    // 返回值
    private BaseResult mBaseResult;

    // 测试数据
    private boolean isTestOpen = false;
    private String testResult = "";


    /**
     * 获取工具类实例(新实例)
     */
    public static BaseHttpUtils create() {
        return new BaseHttpUtils();
    }
    public static BaseHttpUtils create(Dialog dialog) {
        return new BaseHttpUtils(dialog);
    }
    public static BaseHttpUtils create(LifecycleOwner lifecycleOwner,Dialog dialog) {
        return new BaseHttpUtils(lifecycleOwner,dialog);
    }

    private void createTag() {
        tags = UUID.randomUUID().toString();
    }

    private BaseHttpUtils(Dialog dialog) {
        this();
        this.mDialog = dialog;
    }

    private BaseHttpUtils(LifecycleOwner lifecycleOwner,Dialog dialog) {
        this();
        this.lifecycleOwner = lifecycleOwner;
        this.mDialog = dialog;

        if(lifecycleOwner != null){
            lifecycleOwner.getLifecycle().addObserver(observer);
        }
    }

    private DefaultLifecycleObserver observer = new DefaultLifecycleObserver(){
        @Override
        public void onDestroy(@NonNull LifecycleOwner owner) {
            BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                    "调用者页面关闭");
            if(mDialog != null){
                mDialog.dismiss();
            }
            breakOff = true;
            owner.getLifecycle().removeObserver(observer);
        }
    };

    private BaseHttpUtils() {
        BaseHttpCheckUtils.checkInit(mBaseHttpInitParams);
        createTag();
        mBaseHttpParams = new BaseHttpParams(mBaseHttpInitParams);
        mBaseHttpParams.tags = BaseHttpConfig.TAGS + "_" + tags;
        BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                "此次请求开始");
    }

    /*********************************** 初始化参数 ************************************/
    /**
     * 初始化方法
     * 建议放在 Application中
     */
    public static void init(BaseHttpInitParams baseHttpInitParams) {
        BaseHttpCheckUtils.checkInit(baseHttpInitParams);
        mBaseHttpInitParams = baseHttpInitParams;
    }

    /*********************************** 设置生命周期观察者 ************************************/
    private LifecycleOwner lifecycleOwner;
    private boolean breakOff = false;

    /*********************************** 设置控制参数 **********************************/
    /**
     * 设置log开关
     *
     * @param openLog
     * @return
     */
    public BaseHttpUtils initOpenLog(boolean openLog) {
        mBaseHttpParams.openLog = openLog;
        return this;
    }

    /**
     * 设置log标签
     *
     * @param tag
     * @return
     */
    public BaseHttpUtils initTags(String tag) {
        mBaseHttpParams.tags = tag;
        return this;
    }

    /**
     *  设置超时时间
     * @param time
     * @return
     */
    public BaseHttpUtils initReadTimeOut(int time){
        mBaseHttpParams.timeout_read = time;
        return this;
    }
    /**
     *  设置超时时间
     * @param time
     * @return
     */
    public BaseHttpUtils initTimeOutConect(int time){
        mBaseHttpParams.timeout_connect = time;
        return this;
    }



    /*********************************** 设置加载框 ************************************/
    private Dialog mDialog;
    private BaseHttpDialogParams mBaseHttpDialogParams = new BaseHttpDialogParams();
    public BaseHttpUtils initDialog(Dialog dialog){
        this.mDialog = dialog;
        return this;
    }
    // 隐藏加载框
    private void dismissDialog(int type){
        if(type == BaseHttpConfig.TYPE_SUCCESS){
            // 成功
            if(!mBaseHttpDialogParams.isDismissDialog() || !mBaseHttpDialogParams.isDismissDialogWhenSuccess()){
                return;
            }
        }else if(type == BaseHttpConfig.TYPE_FAIL){
            // 失败
            if(!mBaseHttpDialogParams.isDismissDialog() || !mBaseHttpDialogParams.isDismissDialogWhenFail()){
                return;
            }
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(mDialog!=null && mDialog.isShowing()) {
                    mDialog.dismiss();
                }
            }
        });
    }

    /**
     * 设置dialog处理设置
     *
     * @param baseHttpDialogParams
     * @return
     */
    public BaseHttpUtils initDialogDismiss(@NotNull BaseHttpDialogParams baseHttpDialogParams){
        if(baseHttpDialogParams != null){
            this.mBaseHttpDialogParams = baseHttpDialogParams;
        }
        return this;
    }

    /**
     * 最后是否隐藏加载框
     * @param dismissDialog
     * @return
     */
    public BaseHttpUtils initDismissDialog(boolean dismissDialog) {
        mBaseHttpDialogParams.setDismissDialog(dismissDialog);
        return this;
    }
    /**
     * 请求成功的时候是否隐藏加载框
     * @param dismissDialogWhenSuccess
     * @return
     */
    public BaseHttpUtils initDismissDialogWhenSuccess(boolean dismissDialogWhenSuccess) {
        mBaseHttpDialogParams.setDismissDialogWhenSuccess(dismissDialogWhenSuccess);
        return this;
    }
    /**
     * 请求失败的时候是否隐藏加载框
     * @param dismissDialogWhenFail
     * @return
     */
    public BaseHttpUtils initDismissDialogWhenFail(boolean dismissDialogWhenFail) {
        mBaseHttpDialogParams.setDismissDialogWhenFail(dismissDialogWhenFail);
        return this;
    }


    /*********************************** 设置弹窗提示 **************************/
    private BaseHttpMessageParams mBaseHttpMessageParams = new BaseHttpMessageParams();
    public BaseHttpUtils initShowMessage(@NotNull BaseHttpMessageParams baseHttpMessageParams){
        if(baseHttpMessageParams != null){
            this.mBaseHttpMessageParams = baseHttpMessageParams;
        }
        return this;
    }
    public BaseHttpUtils initShowEmptyMessage(){
        return this;
    }


    /*********************************** 设置请求和解析工具类 **************************/
    /**
     * 针对 本次请求的 请求工具类 IHttpService 和 数据解析类 IDataListener
     * 会覆盖全局的 请求工具类 IHttpService 和 数据解析类 IDataListener
     */
    // 请求工具类处理
    private IHttpService mHttpService;
    private IDataListener mDataListener;
    private IFileService mFileService;

    // 解析工具类处理
    private IHttpServiceFilter mHttpServiceFilter;
    private IDataListenerFilter mDataListenerFilter;

    // 设置 网络请求工具类
    public BaseHttpUtils initIHttpService(IHttpService iHttpService) {
        this.mHttpService = iHttpService;
        return this;
    }

    // 对设置好的 IHttpService 进行处理
    public BaseHttpUtils initIHttpServiceFilter(IHttpServiceFilter iHttpServiceFilter) {
        this.mHttpServiceFilter = iHttpServiceFilter;
        return this;
    }

    // 设置 数据解析工具类
    public BaseHttpUtils initIDataListener(IDataListener iDataListener) {
        this.mDataListener = iDataListener;
        return this;
    }

    // 对设置好的  IDataListener 进行处理
    public BaseHttpUtils initIDataListenerFilter(IDataListenerFilter iDataListenerFilter) {
        this.mDataListenerFilter = iDataListenerFilter;
        return this;
    }

    public BaseHttpUtils initIFileService(IFileService mFileService){
        this.mFileService = mFileService;
        return this;
    }


    /*********************************** 设置参数 ************************************/
    /**
     * 设置请求路径 url
     *
     * @param url
     */
    public BaseHttpUtils initUrl(String url) {
        mBaseHttpParams.url = url;
        return this;
    }


    /**
     * 设置 header请求参数
     * initHeaderParams(key,value,key,value....)
     * @param params
     */
    public BaseHttpUtils initHeaderParams(Object... params) {
        mBaseHttpParams.headerParamsList.clear();
        if (params.length > 1) {
            for (int i = 0; i < params.length; i += 2) {
                mBaseHttpParams.headerParamsList.add(new Pair<String, Object>((String) params[i], params[i + 1]));
            }
        }
        return this;
    }

    /**
     * 设置请求参数1 params
     * initParams(key,value,key,value....)
     * initParams 只针对 post请求,get请求不处理params
     *
     * @param params
     */
    public BaseHttpUtils initParams(Object... params) {
        mBaseHttpParams.paramsList.clear();
        if (params.length > 1) {
            for (int i = 0; i < params.length; i += 2) {
                mBaseHttpParams.paramsList.add(new Pair<String, Object>((String) params[i], params[i + 1]));
            }
        }
        return this;
    }

    /**
     * 设置请求参数2 params
     * Map参数
     *
     * @param params
     */
    public BaseHttpUtils initMapParams(Map<String, Object> params) {
        if (params != null) {
            mBaseHttpParams.paramsList.clear();
            for (String s : params.keySet()) {
                mBaseHttpParams.paramsList.add(new Pair<String, Object>((String) s, params.get(s)));
            }
        }
        return this;
    }

    /**
     * 设置请求参数3 params
     * List<Pair>参数
     *
     * @param params
     */
    public BaseHttpUtils initListParams(List<Pair<String, Object>> params) {
        if (params != null) {
            mBaseHttpParams.paramsList.clear();
            mBaseHttpParams.paramsList.addAll(params);
        }
        return this;
    }

    /**
     * 设置请求参数3 params
     * List<Pair>参数
     *
     * @param params
     */
    public BaseHttpUtils initJsonParams(String params) {
        if (params != null) {
            try {
                JSONObject jsonObject = new JSONObject(params);
                mBaseHttpParams.paramsList.clear();
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = jsonObject.get(key);
                    mBaseHttpParams.paramsList.add(new Pair(key, value));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * 设置请求参数4 params
     * List<Pair>参数
     *
     * @param params
     */
    public BaseHttpUtils initObjectParams(Object params) {
        if (params != null) {
            try {
                JSONObject jsonObject =  new JSONObject(BaseJsonUtils.getJson(params));
                mBaseHttpParams.paramsList.clear();
                Iterator<String> keys = jsonObject.keys();
                while (keys.hasNext()) {
                    String key = keys.next();
                    Object value = jsonObject.get(key);
                    mBaseHttpParams.paramsList.add(new Pair(key, value));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    /**
     * 添加参数  注意 调用了这个方法以后,不可以再调用其他的initParams方法,不然此方法设置的参数将无效
     *
     * @param key
     * @param value
     * @return
     */
    public BaseHttpUtils addParams(String key, Object value) {
        mBaseHttpParams.paramsList.add(new Pair<String, Object>(key, value));
        return this;
    }

    /**
     * 设置 返回值解析类型
     *
     * @param dataParseType
     * @return
     */
    public BaseHttpUtils initDataParseType(BaseHttpConfig.DataParseType dataParseType) {
        mBaseHttpParams.dataParseType = dataParseType;
        return this;
    }

    /**
     * 请求提交类型
     *
     * @param requestType
     * @return
     */
    public BaseHttpUtils initRequestType(BaseHttpConfig.RequestType requestType) {
        mBaseHttpParams.request_type = requestType;
        return this;
    }

    /**
     * 返回值编码类型
     *
     * @param request_contentType
     * @return
     */
    public BaseHttpUtils initRequestDataType(BaseHttpConfig.RequestContentType request_contentType) {
        mBaseHttpParams.request_contentType = request_contentType;
        return this;
    }


    /**
     * 返回值 class
     * @param clz
     * @return
     */
    public BaseHttpUtils initClass(Class clz){
        mBaseHttpParams.aClass = clz;
        return this;
    }

    /**
     * 设置是否使用测试数据
     * @param isOpenTest
     * @return
     */
    public BaseHttpUtils initTest(boolean isOpenTest){
        this.isTestOpen = isOpenTest;
        return this;
    }
    /**
     * 设置测试数据
     * @param object
     * @return
     */
    public BaseHttpUtils initTestResult(String object){
        this.testResult = object;
        return this;
    }


    /*********************************** 设置回调 ************************************/
    private IHttpResultCallBack mHttpResultCallBack;
    private IFileUploadListener mFileUploadListener;

    /**
     * 设置 网络请求回调
     *
     * @param mHttpResultCallBack
     * @return
     */
    public BaseHttpUtils initHttpResultCallBack(IHttpResultCallBack mHttpResultCallBack) {
        this.mHttpResultCallBack = mHttpResultCallBack;
        return this;
    }
    /**
     * 设置 文件上传回调
     *
     * @param mFileUploadListener
     * @return
     */
    public BaseHttpUtils initIFileUploadListener(IFileUploadListener mFileUploadListener){
        this.mFileUploadListener = mFileUploadListener;
        return this;
    }



    /*********************************** 开始请求 ************************************/
    /**
     * 请求方法
     */
    public void get() {
        mBaseHttpParams.request_type = BaseHttpConfig.RequestType.GET;
        request();
    }
    public void getList() {
        mBaseHttpParams.dataParseType = BaseHttpConfig.DataParseType.List;
        get();
    }
    public void getObject() {
        mBaseHttpParams.dataParseType = BaseHttpConfig.DataParseType.Object;
        get();
    }
    public void getList(Class clz) {
        mBaseHttpParams.aClass = clz;
        mBaseHttpParams.dataParseType = BaseHttpConfig.DataParseType.List;
        get();
    }
    public void getObject(Class clz) {
        mBaseHttpParams.aClass = clz;
        mBaseHttpParams.dataParseType = BaseHttpConfig.DataParseType.Object;
        get();
    }

    public void postList(){
        mBaseHttpParams.dataParseType = BaseHttpConfig.DataParseType.List;
        post();
    }
    public void postObject(){
        mBaseHttpParams.dataParseType = BaseHttpConfig.DataParseType.Object;
        post();
    }
    public void postList(Class clz) {
        mBaseHttpParams.aClass = clz;
        mBaseHttpParams.dataParseType = BaseHttpConfig.DataParseType.List;
        post();
    }
    public void postObject(Class clz) {
        mBaseHttpParams.aClass = clz;
        mBaseHttpParams.dataParseType = BaseHttpConfig.DataParseType.Object;
        post();
    }
    public void post() {
        mBaseHttpParams.request_type = BaseHttpConfig.RequestType.POST;
        request();
    }

    public void put() {
        mBaseHttpParams.request_type = BaseHttpConfig.RequestType.PUT;
        request();
    }

    /**
     * 请求方法
     */
    public void request() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            BaseThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    requests();
                }
            });
        } else {
            requests();
        }
    }

    public void requests() {
        mBaseResult = new BaseResult();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mHttpResultCallBack != null) {
                    mHttpResultCallBack.onBeforeRequest(mBaseHttpParams);
                }
            }
        });
        /*
         * 检查和初始化
         */
        try {
            // 1.检查请求参数是否设置正确
            BaseHttpCheckUtils.checkRequest(mBaseHttpParams);

            // 2.检查和初始化 请求工具类
            if (mHttpService == null && mBaseHttpInitParams.mHttpService != null) {
                mHttpService = BaseObjectUtils.clone(mBaseHttpInitParams.mHttpService);
            }
            // 3.检查和初始化 解析工具类
            if (mDataListener == null && mBaseHttpInitParams.mDataListener != null) {
                mDataListener = BaseObjectUtils.clone(mBaseHttpInitParams.mDataListener);
            }

            // 4.检查和初始化 请求工具类 过滤器
            if (mHttpServiceFilter == null && mBaseHttpInitParams.mHttpServiceFilter != null) {
                mHttpServiceFilter = BaseObjectUtils.clone(mBaseHttpInitParams.mHttpServiceFilter);
            }
            // 4.1 过滤设置 请求工具类
            if (mHttpServiceFilter != null && mHttpService != null) {
                mHttpService = mHttpServiceFilter.filterIHttpService(mHttpService);
            }

            // 5.检查和初始化 解析工具类  过滤器
            if (mDataListenerFilter == null && mBaseHttpInitParams.mDataListenerFilter != null) {
                mDataListenerFilter = BaseObjectUtils.clone(mBaseHttpInitParams.mDataListenerFilter);
            }
            // 5.1 过滤设置 解析工具类
            if (mDataListenerFilter != null && mDataListener != null) {
                mDataListener = mDataListenerFilter.filterIDataListener(mDataListener);
            }
            BaseHttpCheckUtils.checkServiceAndDataparse(mHttpService, mDataListener);
        } catch (Exception e) {
            e.printStackTrace();

            mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
            mBaseResult.errorInfo.exception = e;
            onFail();
            return;
        }

        /*
         * 开始请求
         */
        //1.解析参数
        mBaseHttpParams.params = mHttpService.parseParams(mBaseHttpParams.paramsList);
        // 打印log
        BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                "提交方式   :" + mBaseHttpParams.request_type.toString() + "\n" +
                        "返回值类型 :" + mBaseHttpParams.dataParseType.toString() + "\n" +
                        "url        :" + mBaseHttpParams.url + "\n" +
                        "params     :" + mHttpService.getRequestParamsDesc(mBaseHttpParams.params));
        //2.执行请求
        String requestResult = "";
        if(isTestOpen){
            // 2.1 使用测试数据
            requestResult = testResult;
            BaseLogUtils.logE(mBaseHttpParams.tags,mBaseHttpParams.openLog,"来自测试数据");
        }
        else{
            // 2.2 正常请求
            BaseRequestResult baseRequestResult = mHttpService.request(mBaseHttpParams);
            // 判断请求结果
            if (baseRequestResult == null) {
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpFail;
                mBaseResult.errorInfo.errorMsg = "";
                onFail();
                return;
            }
            if (!baseRequestResult.isSuccess) {
                mBaseResult.errorInfo = baseRequestResult.errorInfo;
                onFail();
                return;
            }
            if (baseRequestResult.bytes == null || baseRequestResult.bytes.length == 0){
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpFail;
                mBaseResult.errorInfo.errorMsg = "返回值为空,请联系后台管理员";
                onFail();
                return;
            }
            requestResult = new String(baseRequestResult.bytes);
        }

        BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                "成功获取到返回值, result = " + requestResult);
        /*
         * 开始解析参数
         */
        try {
            mBaseResult.result.resultAll = requestResult;

            BaseResultData baseResultData = mDataListener.parseResult(mBaseHttpParams,requestResult);
            mBaseResult.baseResultData = baseResultData;
            if(baseResultData == null){
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultFail;
                mBaseResult.errorInfo.errorMsg = "IDataListener中 parseResult方法返回值不能为空";
                onFail();
                return;
            }
            try{
                switch (mBaseHttpParams.dataParseType) {
                    case List: {
                        mBaseResult.result.setResult_list(mDataListener.getList(mBaseHttpParams, baseResultData));
                    }
                    break;
                    case Object: {
                        mBaseResult.result.setResult_object(mDataListener.getObject(mBaseHttpParams, baseResultData));
                    }
                    break;
                    case String:
                    default: {
                        mBaseResult.result.setResult_str(mDataListener.getString(mBaseHttpParams, baseResultData));
                    }
                    break;
                }
            }catch (Exception e){
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultFail;
                mBaseResult.errorInfo.exception = e;
                onFail();
                return;
            }

            if(mDataListener.isFailResult(mBaseHttpParams,baseResultData,mBaseResult.result)){
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_FailFromResult;
                mBaseResult.errorInfo.errorMsg = baseResultData.getResultMsg();
                onFail();
                return;
            }else{
                mBaseResult.success = true;
                onSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultFail;
            mBaseResult.errorInfo.exception = e;
            onFail();
        }
    }
    // 成功
    private void onSuccess() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(breakOff){
                    BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                            "页面关闭，此次请求结束");
                   return;
                }
                mBaseResult.success = true;
                BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                        "此次请求结束");
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Success;
                mBaseResult.errorInfo.errorMsg = "";
                mBaseResult.errorInfo.exception = null;
                dismissDialog(BaseHttpConfig.TYPE_SUCCESS);
                if (mHttpResultCallBack != null) {
                    mHttpResultCallBack.onSuccess(mBaseResult);
                    mHttpResultCallBack.onFinal(mBaseResult);
                }
            }
        });
    }
    // 失败
    private void onFail() {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(breakOff){
                    BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                            "页面关闭，此次请求结束");
                    return;
                }
                mBaseResult.success = false;
                BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                        mBaseResult.errorInfo.toString());
                dismissDialog(BaseHttpConfig.TYPE_FAIL);
                if (mHttpResultCallBack != null) {
                    if(mBaseResult.errorInfo.errorCode == BaseHttpConfig.ErrorCode.Error_FailFromResult){
                        if(!mHttpResultCallBack.onFailForResult(mBaseResult)){
                            mHttpResultCallBack.onFail(mBaseResult.errorInfo);
                        }
                    }else{
                        mHttpResultCallBack.onFail(mBaseResult.errorInfo);
                    }
                    mHttpResultCallBack.onFinal(mBaseResult);
                }
            }
        });
    }



    /******************************************** 上传文件 *********************************/
    /**
     * 设置要上传的文件
     * @param fileList
     * @return
     */
    public BaseHttpUtils initFiles(List<File> fileList){
        mBaseHttpParams.fileList = fileList;
        return this;
    }

    /**
     * 设置上传文件的name
     * @param fileKeys
     * @return
     */
    public BaseHttpUtils initFileKeys(String fileKeys){
        mBaseHttpParams.fileKeys.clear();
        mBaseHttpParams.fileKeys.add(fileKeys);
        return this;
    }
    public BaseHttpUtils initFileKeys(List<String> fileKeys){
        mBaseHttpParams.fileKeys = fileKeys;
        return this;
    }

    /**
     * 设置是否可以不上传文件
     * @param canEmpty
     * @return
     */
    public BaseHttpUtils initCanEmptyFile(boolean canEmpty){
        mBaseHttpParams.isCanFileEmpty = canEmpty;
        return this;
    }


    /**
     * 上传文件
     */
    public void upload(){
        if (Looper.myLooper() == Looper.getMainLooper()) {
            BaseThreadPoolUtils.execute(new Runnable() {
                @Override
                public void run() {
                    uploads();
                }
            });
        } else {
            uploads();
        }
    }

    private void uploads() {
        mBaseResult = new BaseResult();
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (mHttpResultCallBack != null) {
                    mHttpResultCallBack.onBeforeRequest(mBaseHttpParams);
                }
            }
        });
        /*
         * 检查和初始化
         */
        try {
            // 1.检查请求参数是否设置正确
            BaseHttpCheckUtils.checkFileRequest(mBaseHttpParams);

            // 2.检查和初始化 请求工具类
            if (mFileService == null && mBaseHttpInitParams.mHttpService != null) {
                mFileService = BaseObjectUtils.clone(mBaseHttpInitParams.mFileService);
            }
            // 3.检查和初始化 解析工具类
            if (mDataListener == null && mBaseHttpInitParams.mDataListener != null) {
                mDataListener = BaseObjectUtils.clone(mBaseHttpInitParams.mDataListener);
            }

//            // 4.检查和初始化 请求工具类 过滤器
//            if (mHttpServiceFilter == null && mBaseHttpInitParams.mHttpServiceFilter != null) {
//                mHttpServiceFilter = BaseObjectUtils.clone(mBaseHttpInitParams.mHttpServiceFilter);
//            }
//            // 4.1 过滤设置 请求工具类
//            if (mHttpServiceFilter != null && mHttpService != null) {
//                mHttpService = mHttpServiceFilter.filterIHttpService(mHttpService);
//            }

            // 5.检查和初始化 解析工具类  过滤器
            if (mDataListenerFilter == null && mBaseHttpInitParams.mDataListenerFilter != null) {
                mDataListenerFilter = BaseObjectUtils.clone(mBaseHttpInitParams.mDataListenerFilter);
            }
            // 5.1 过滤设置 解析工具类
            if (mDataListenerFilter != null && mDataListener != null) {
                mDataListener = mDataListenerFilter.filterIDataListener(mDataListener);
            }
            BaseHttpCheckUtils.checkFileServiceAndDataparse(mFileService, mDataListener);
        } catch (Exception e) {
            e.printStackTrace();

            mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_Use;
            mBaseResult.errorInfo.exception = e;
            onFail();
            return;
        }

        /*
         * 开始请求
         */
        //1.解析参数
//        mBaseHttpParams.params = mFileService.uploadFile(mBaseHttpParams.paramsList);
        // 打印log
        BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                  "提交方式     :" + mBaseHttpParams.request_type.toString() + "\n" +
                        "返回值类型   :" + mBaseHttpParams.dataParseType.toString() + "\n" +
                        "url        :" + mBaseHttpParams.url + "\n" +
                        "params     :" + mBaseHttpParams.getParamsDesc() + "\n"+
                        "files      :" + mBaseHttpParams.fileList.size() + "个文件");
        //2.执行请求
        BaseRequestResult baseRequestResult = mFileService.uploadFile(mBaseHttpParams, mFileUploadListener);
        // 判断请求结果
        if (baseRequestResult == null) {
            mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpFail;
            mBaseResult.errorInfo.errorMsg = "";
            onFail();
            return;
        }
        if (!baseRequestResult.isSuccess) {
            mBaseResult.errorInfo = baseRequestResult.errorInfo;
            onFail();
            return;
        }
        if (baseRequestResult.bytes == null || baseRequestResult.bytes.length == 0){
            mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpFail;
            mBaseResult.errorInfo.errorMsg = "返回值为空,请联系后台管理员";
            onFail();
            return;
        }
        String requestResult = new String(baseRequestResult.bytes);
        BaseLogUtils.logE(mBaseHttpParams.tags, mBaseHttpParams.openLog,
                "成功获取到返回值, result = " + requestResult);
        /*
         * 开始解析参数
         */
        try {
            mBaseResult.result.resultAll = requestResult;

            BaseResultData baseResultData = mDataListener.parseResult(mBaseHttpParams,requestResult);
            mBaseResult.baseResultData = baseResultData;
            if(baseResultData == null){
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultFail;
                mBaseResult.errorInfo.errorMsg = "IDataListener中 parseResult方法返回值不能为空";
                onFail();
                return;
            }
            try{
                switch (mBaseHttpParams.dataParseType) {
                    case List: {
                        mBaseResult.result.setResult_list(mDataListener.getList(mBaseHttpParams, baseResultData));
                    }
                    break;
                    case Object: {
                        mBaseResult.result.setResult_object(mDataListener.getObject(mBaseHttpParams, baseResultData));
                    }
                    break;
                    case String:
                    default: {
                        mBaseResult.result.setResult_str(mDataListener.getString(mBaseHttpParams, baseResultData));
                    }
                    break;
                }
            }catch (Exception e){
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultFail;
                mBaseResult.errorInfo.exception = e;
                onFail();
                return;
            }

            if(mDataListener.isFailResult(mBaseHttpParams,baseResultData,mBaseResult.result)){
                mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_FailFromResult;
                mBaseResult.errorInfo.errorMsg = baseResultData.getResultMsg();
                onFail();
                return;
            }else{
                mBaseResult.success = true;
                onSuccess();
            }
        } catch (Exception e) {
            e.printStackTrace();
            mBaseResult.errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultFail;
            mBaseResult.errorInfo.exception = e;
            onFail();
        }
    }
}
