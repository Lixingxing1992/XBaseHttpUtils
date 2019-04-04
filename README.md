###扩展性极强的网络请求框架

方便后期拓展

###使用说明:

引用库:

    implementation 'com.github.Lixingxing1992:XHttpUtils:Tag'
    本库用到了GOSN,如果有冲突的话,可以尝试
    implementation ('com.github.Lixingxing1992:XHttpUtils:Tag'){
                exclude group: 'com.google.code.gson', module: 'gson'
            }

1.在Application中初始化：
    
        // 打开全局的log日志开关。
        // 这个方法会被 使用BaseHttpUtils().initOpenLog(boolean openLog)方法的地方覆盖掉，但是不影响在其他地方调用时候的log打印
        static void init(boolean openLogs);
        // 设置全局的IHttpService 和 IDataListener, 默认是 DefaultHttpService 和 DefaultDataListener
        // 这个方法会被 使用BaseHttpUtils().initIHttpService(IHttpService iHttpService) 和 BaseHttpUtils().initIDataListener(IDataListener iDataListenerStatics)
        //      方法的地方覆盖掉，但是不影响在其他代码中的调用
        static void init(IHttpService iHttpServiceStatics, IDataListener iDataListenerStatics) 
    
2.请求处理工具类: IHttpService 可以实现自己的IHttpService,只需要继承IHttpService并实现下面的方法
        
        // 处理参数方法  get请求下这个方法不使用
        Object parseParams(BaseHttpParams baseHttpParams,BaseResult baseResult);
        // 请求方法 会获取parseParams的参数进行请求
        void request(BaseHttpParams baseHttpParams,BaseResult baseResult);
        // 判断是否请求失败
        boolean isFail(BaseHttpParams baseHttpParams,BaseResult baseResult);
        // 获取错误信息 返回值不能为空,  返回的 BaseErrorInfo里必须要有错误信息描述
        BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams, BaseResult baseResult);
    
###版本说明:

>1.0-beta  