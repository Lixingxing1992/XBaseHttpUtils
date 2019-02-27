package com.app_res.org.util.http

import android.app.Dialog
import android.os.Handler
import android.os.Message
import com.app.org.http.BaseHttpConfig
import com.app.org.http.interfaces.BaseHttpHandlerCallBack
import com.app.org.utils.BaseStringUtil
import com.app.org.utils.BaseToastUtil
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by lixingxing on 2018/3/15.
 */

abstract class HttpHandlerCallBack(private var dialog: Dialog? = null, var `objects`: Array<Any>? = null) : BaseHttpHandlerCallBack() {

    private var `object`: Array<Any>? = null

    var success = false

    //请求成功不关闭
    var isSuccessDisMiss: Boolean = true
    //请求失败之后关闭dialog
    var isFailDisMiss: Boolean = true

    var isShowError: Boolean = true
    var isShowHttpError: Boolean = true
    var isShowLocalError: Boolean = true
    var isShowNoDataError: Boolean = true

    var isShowSuccessMsg = false
    var successMsg = ""

    var successResCode = 0
    var noDataResCode = 10007

    var arg1: Int = 0
    var arg2: Int = 0
    var arg3: Int = 0

    companion object {
        internal val Result_success = 0        // 请求成功
        internal val Result_error = -1        // 后台返回的错误
        internal val Result_error_local = -2 // 程序自身造成的错误
    }

    constructor() : this(null)

    init {
        success = false
        this.`object` = `objects`
    }


    fun setSuccessMsg(isShowSuccessMsg: Boolean = false, successMsg: String = ""): HttpHandlerCallBack {
        this.isShowSuccessMsg = isShowSuccessMsg
        this.successMsg = successMsg
        return this
    }

    fun setIsSuccessDisMiss(isDisMiss: Boolean = true): HttpHandlerCallBack {
        this.isSuccessDisMiss = isDisMiss
        return this
    }

    fun setIsFailDisMiss(isDisMiss: Boolean = true): HttpHandlerCallBack {
        this.isFailDisMiss = isDisMiss
        return this
    }


    fun setIsShowError(isShowError: Boolean = true): HttpHandlerCallBack {
        this.isShowError = isShowError
        return this
    }

    fun setIsShowHttpError(isShowError: Boolean = true): HttpHandlerCallBack {
        this.isShowHttpError = isShowError
        return this
    }

    fun setIsShowLocalError(isShowError: Boolean = true): HttpHandlerCallBack {
        this.isShowLocalError = isShowError
        return this
    }

    fun setShowNoDataError(isShowNoDataError: Boolean = true): HttpHandlerCallBack {
        this.isShowNoDataError = isShowNoDataError
        return this
    }

    fun setNoDataCode(resCode: Int = 10007): HttpHandlerCallBack {
        this.noDataResCode = resCode
        return this
    }

    fun setArgs(`object`: Array<Any>): HttpHandlerCallBack {
        this.`object` = `object`
        return this
    }

    fun <T : Any> getArgs(position: Int): T? {
        if (`object` != null && position < `object`!!.size) {
            return `object`!![position] as T
        } else {
            return null
        }
    }

    fun setDialog(dialog: Dialog?): HttpHandlerCallBack {
        this.dialog = dialog
        return this
    }

    fun dismissDialog() {
        if (null != dialog && dialog!!.isShowing) {
            Handler().postDelayed({
                dialog!!.dismiss()
            }, 800)
        }
    }


    fun catchError(msg: Message) {
        if (isFailDisMiss) {
            dismissDialog()
        }
        if (isShowError) {
            if (isShowHttpError) {
                if(msg.obj != null && !BaseStringUtil.isEmpty(msg.obj.toString())){
                    BaseToastUtil.showShortToast(msg.obj.toString())
                }
            }
        }
        hasError()
        hasError(msg)//当对不同的错误有不同的操作的时候调用
    }

    // 如果有异常,会调用下面的方法
    abstract fun hasError()

    open fun hasError(msg: Message) {
    }

    // 如果没有获取到数据，会调用下面的方法
    abstract fun hasNoData()
    // 如果有网络,会调用下面的方法
    abstract fun dealMessage(msg: Message)

    open fun finalMessage(msg: Message) {
    }


    fun <T> getObject(jsonString: String, cls: Class<T>, vararg args: String): T? {
        var t: T? = null
        var code = jsonString
        if (args.size > 0) {
            for (arg in args) {
                code = getJsonData<String>(code, arg).toString()
            }
        }
        try {
            val gson = Gson()
            t = gson.fromJson(code, cls)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return t
    }

    fun <T> getObjectList(jsonString: String, cls: Class<T>, vararg args: String): ArrayList<T> {
        val list = ArrayList<T>()
        var code = jsonString
        if (args.size > 0) {
            for (arg in args) {
                code = getJsonData<String>(code, arg).toString()
            }
        }
        val gson = Gson()
        val parser = JsonParser()
        val Jarray = parser.parse(code).asJsonArray
        for (obj in Jarray) {
            var t: T = gson.fromJson(obj, cls)
            list.add(t)
        }
        return list
    }

    fun <T> getJsonData(jsonString: String, objects: String): T? {
        if (!BaseStringUtil.isEmpty(jsonString)) {
            try {
                val jsonObject = JSONObject(jsonString)
                return jsonObject.opt(objects) as T
            } catch (exception: JSONException) {

            }
        }
        return null
    }

    /**
     * 处理结果
     */
    var resCode = 0
    var resMsg = ""
    var dataResult  : Any? = null
    fun initResult(msg: Message){
        try {
            val jsonObject = JSONObject(msg.obj.toString())
            resCode = jsonObject.optInt("resCode")
            resMsg = jsonObject.optString("resMsg")
            try {
                dataResult = jsonObject.opt("data")
            }catch (exception: JSONException) {
                dataResult = ""
            }
            when (resCode) {
                successResCode -> {
                    success = true
                    if (isShowSuccessMsg && !BaseStringUtil.isEmpty(successMsg)) {
                        BaseToastUtil.showShortToast(successMsg)
                    }
                    if (isSuccessDisMiss) {
                        dismissDialog()
                    }
                    dealMessage(msg)
                }
                noDataResCode -> {
                    // 未获取到数据
                    if (isShowError && isShowNoDataError) {
                        if(!BaseStringUtil.isEmpty(resMsg)){
                            BaseToastUtil.showShortToast(resMsg)
                        }
                    }
                    if (isFailDisMiss) {
                        dismissDialog()
                    }
                    hasNoData()
                }
                else -> {
                    // 有错误
                    msg.obj = resMsg
                    catchError(msg)
                }
            }
        } catch (exception: JSONException) {
            msg.obj = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error.toString()
            catchError(msg)
        }
    }

    override fun handleMessage(msg: Message): Boolean {
        success = false

        when (errorCode) {
            BaseHttpConfig.ErrorCode.Error_Success -> {
                initResult(msg)
            }
            else -> {
                catchError(msg)
            }
        }
        finalMessage(msg)
        return false
    }
}
