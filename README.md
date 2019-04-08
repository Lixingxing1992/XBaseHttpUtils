# 扩展性极强的网络请求框架





## 使用说明:

引用库:

```groovy
implementation 'com.github.Lixingxing1992:XHttpUtils:Tag'
//本库用到了GOSN,如果有冲突的话,可以尝试
implementation ('com.github.Lixingxing1992:XHttpUtils:Tag'){
            exclude group: 'com.google.code.gson', module: 'gson'
        }
```

 1.在Application中初始化：
    
```java
    // 打开全局的log日志开关。
    // 这个方法会被 使用BaseHttpUtils().initOpenLog(boolean openLog)方法的地方覆盖掉，但是不影响在其他地方调用时候的log打印
    public static void init(boolean openLogs);
    // 设置全局的IHttpService 和 IDataListener, 默认是 DefaultHttpService 和 DefaultDataListener
    // 这个方法会被 使用BaseHttpUtils().initIHttpService(IHttpService iHttpService) 和 BaseHttpUtils().initIDataListener(IDataListener iDataListenerStatics)
    //      方法的地方覆盖掉，但是不影响在其他代码中的调用
    public static void init(Class iHttpServiceStatics, Class iDataListenerStatics);
```

 2.请求处理工具类: IHttpService 可以实现自己的IHttpService,只需要继承IHttpService并实现下面的方法
        
```java
    // 处理参数方法  get请求下这个方法不使用
    Object parseParams(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 请求方法 会获取parseParams的参数进行请求
    void request(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 判断是否请求失败
    boolean isFail(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 获取错误信息 返回值不能为空,  返回的 BaseErrorInfo里必须要有错误信息描述
    BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams, BaseResult baseResult);
```

 3.解析返回值工具类:IDataListener 可以实现自己的IDataListener,需要实现下面的方面:

```java
    // 处理返回值  解析存储等操作...
    void parse(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 检查数据是否失败  false 成功   true 解析失败
    boolean isFail(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 获取错误信息 返回值不能为空,  返回的 BaseErrorInfo里必须要有错误信息描述
    BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 检查是否为空数据  false 不为空  true空数据
    boolean isEmpty(BaseHttpParams baseHttpParams,BaseResult baseResult);
```

 4.调用方式(偷个懒，DEMO里是用kotlin写的)

​	a.post请求​       

```kotlin
BaseHttpUtils(waitingDialog)
        .initUrl(url.text.toString())
        .initIHttpService(httpService)
        .initIDataListener(dataListener)
        .initParams(map)
        .initClass(EventModel::class.java)
        .initResponseType(BaseHttpConfig.ResponseType.List)
        .initDialogDismiss(isDialogDismiss)
        .initDialogDismissWhenSuccess(isDialogDismissWhenSuccess)
        .initDialogDismissWhenEmpty(isDialogDismissWhenEmpty)
        .initDialogDismissWhenFail(isDialogDismissWhenFail)
        .initHttpResultCallBack(object : HttpResultCallBack() {
            override fun onSuccess(baseResult: BaseResult) {
                result.text = baseResult.getResult().result_str
            }
            override fun onEmpty(errorInfo: BaseErrorInfo) {
                result.text = errorInfo.errorMsg
            }
            override fun onFail(errorInfo: BaseErrorInfo) {
                result.text = errorInfo.errorMsg
            }
        })
        .post()
```

​	b.get请求

```kotlin
BaseHttpUtils(waitingDialog)
        .initUrl(url.text.toString())
        .initIHttpService(httpService)
        .initIDataListener(dataListener)
        .initDialogDismiss(isDialogDismiss)
        .initDialogDismissWhenSuccess(isDialogDismissWhenSuccess)
        .initDialogDismissWhenEmpty(isDialogDismissWhenEmpty)
        .initDialogDismissWhenFail(isDialogDismissWhenFail)
        .initHttpResultCallBack(object : HttpResultCallBack() {
            override fun onSuccess(baseResult: BaseResult) {
                var resultStr = baseResult.getResult().result_str
                result.text = resultStr
            }
            override fun onFail(errorInfo: BaseErrorInfo) {
                result.text = errorInfo.errorMsg
            }
        })
        .get()
```



## 版本内容说明:

>1.0-beta4    请求工具类 实现了DefaultHttpService,JsonHttpService,TDHttpService；解析工具类实现了DefaultDataListener,JsonDataListener,TDDataListener.
>
>DefaultHttpService，DefaultDataListener 适用于普通post提交和get提交，200且返回值有值的时候直接获取到返回值。
>
>JsonHttpService，JsonDataListener适用于JSON提交和JSON解析
>
>TDHttpService，TDDataListener 是试用于 我们公司项目 唐道中的请求方式和解析方式（加密后提交，返回值解析需要和后台接口规则相对应，学习如何拓展的同学可以参考这两个类。