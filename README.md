###扩展性极强的网络请求框架

方便后期拓展

###使用说明:

    compile ""
    
    1.在Application中初始化：
    
    // 打开全局的log日志开关。
    // 这个方法会被 使用BaseHttpUtils().initOpenLog(boolean openLog)方法的地方覆盖掉，但是不影响在其他地方调用时候的log打印
    static void init(boolean openLogs);
    // 设置全局的IHttpService 和 IDataListener, 默认是 DefaultHttpService 和 DefaultDataListener
    // 这个方法会被 使用BaseHttpUtils().initIHttpService(IHttpService iHttpService) 和 BaseHttpUtils().initIDataListener(IDataListener iDataListenerStatics)
    //      方法的地方覆盖掉，但是不影响在其他代码中的调用
    static void init(IHttpService iHttpServiceStatics, IDataListener iDataListenerStatics) 
    
###版本说明:

>1.0 