package com.app.org.http.interfaces

import android.os.Handler
import android.os.Looper
import android.os.Message
import com.apkfuns.logutils.LogUtils
import com.app.org.http.BaseHttpConfig
import com.app.org.http.BaseHttpResultModel
import com.app.org.http.BaseHttpUtils
import com.app.org.http.Response
import com.app.org.utils.BaseClassUtil
import com.app.org.utils.BaseDataUtil
import com.app.org.utils.BaseStringUtil

/**
 * Created by lixingxing on 2018/5/10.
 */
abstract class IDataListener {
    var isShowLog = true
    var tag = "IDataListener"

    var response = Response()

    var httpHandler: Handler? = null
    var baseHttpCallBack: BaseHttpCallBack<*>? = null
    var responseType: BaseHttpConfig.ResponseType = BaseHttpConfig.ResponseType.String

    fun initResponseType(responseType: BaseHttpConfig.ResponseType) {
        this.responseType = responseType
    }

    open fun <T> reponse(baseHttpResultModel: Response,baseHttpCallBack: BaseHttpCallBack<T>) {
        this.response = baseHttpResultModel
            this.baseHttpCallBack = baseHttpCallBack

            // 如果成员变量有值 --> 有特殊处理操作
            if (baseHttpResultModel.responseCode == 200 && baseHttpResultModel.exception == null) {
                val bytes = BaseDataUtil.inputStream2Bytes(baseHttpResultModel.inputStream)
                val result = String(bytes)
                onSuccess(result)
            } else {
                onFail()
        }
    }

    open fun onSuccess(result: String) {
        if (isShowLog)
            LogUtils.tag(tag + "_IDataListener").e("返回值 = " + result)

        response.responseResult = result
        response.responseType = responseType

        httpHandler = Handler(Looper.getMainLooper(), baseHttpHandlerCallBack)
        parseResult(result, responseType, BaseClassUtil.getClassParameterizedType(baseHttpCallBack))
        sendMessage(response)
    }

    open fun onFail() {
        httpHandler = Handler(Looper.getMainLooper(), baseHttpHandlerCallBack)
        if (response!!.exception != null) {
            // 请求出现异常等等
            response?.errorCode = BaseHttpConfig.ErrorCode.Error_ResultException
            if (isShowLog)
                LogUtils.tag(tag + "_IDataListener").e(response!!.exception)
        } else {
            // 请求结果码错误等等
            response?.errorCode = BaseHttpConfig.ErrorCode.Error_ResultErrorCode
            if (isShowLog)
                LogUtils.tag(tag + "_IDataListener").e(response?.errorCode.toString() +
                        ",错误码为: ${response!!.responseCode}")
        }
        sendMessage(response)
    }


    open fun sendMessage(response: Response){
        httpHandler?.obtainMessage(0, response)?.sendToTarget()
    }

    abstract fun <T> getObject(resultStr: String, cls: Class<T>, vararg args: String): T?

    abstract fun <T> getObjectList(resultStr: String, cls: Class<T>, vararg args: String): List<T>?

    abstract fun <T> getResultData(resultStr: String, objects: String): T?


    //解析返回值
    abstract fun <T> parseResult(result: String, responseType: BaseHttpConfig.ResponseType, cls: Class<T>)

    abstract fun getResponseData(): String?
    abstract fun getErrorData(): String?

    //判断是否是空
    abstract fun isEmpty(): Boolean

    //判断是否是错误
    abstract fun isError(): Boolean


    var baseHttpHandlerCallBack = object :  Handler.Callback {
        override fun handleMessage(message: Message?): Boolean {
            var respone = message?.obj as Response
            if (respone.errorCode == BaseHttpConfig.ErrorCode.Error_Success) {
                // 请求成功 并且返回值不为空
                if (isEmpty()) {
                    baseHttpCallBack?.onEmpty()
                } else if (isError()) {
                    baseHttpCallBack?.onDataError(getErrorData())
                } else {
                    baseHttpCallBack?.onSuccess(respone)
                }
            } else {
                if( respone.errorCode == BaseHttpConfig.ErrorCode.Error_ResultErrorCode){
                    baseHttpCallBack?.onRequestError(respone.errorCode.toString() +
                            ",错误码为: ${respone!!.responseCode}")
                }else{
                    baseHttpCallBack?.onRequestError(respone.errorCode.toString())
                }
            }
            baseHttpCallBack?.onFinal()
            return false
        }
    }
}
