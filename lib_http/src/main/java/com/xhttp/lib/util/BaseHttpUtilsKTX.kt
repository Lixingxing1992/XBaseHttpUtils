package com.xhttp.lib.util

import android.app.Dialog
import androidx.lifecycle.LifecycleOwner
import com.xhttp.lib.BaseHttpUtils
import com.xhttp.lib.config.BaseHttpConfig

/**
 * Desc:
 * Author:lixingxing
 * Time:2021/8/25
 * ***************************
 * edit:
 */
class BaseHttpUtilsKTX{

//    BaseHttpUtils.create(lifecycleOwner, dialog)
//    .initUrl(URLUtils.getBaseUrl() + "/business/org/pay/payData")
//    .initParams("orgId",orgId,"month",month,"year",year,"type",type)
//    .initTest(false)
//    .initTestResult("""
//                {
//                	"code":200,
//                  "msg":"",
//                  "data":{
//                    	"orgName": "哈哈哈哈哈哈组织",
//                    	"orgHead":"xxxx",
//                   		"totalAmount":2200.00,
//                      "unpaidAmount":1100.00,
//                      "paidAmount":1000.00
//                  }
//                }
//            """.trimIndent())
//    .initHttpResultCallBack(object : GlodAntsHttpResultCallBack() {
//        override fun onSuccess(baseResult: BaseResult) {
////                    onGetResultSuccess?.invoke(true)
//            var model = baseResult.getResult().getResult_object<PayMainModel>()
//            onGetResultSuccess?.invoke(model)
//        }
//        override fun onFail(errorInfo: BaseErrorInfo) {
//            super.onFail(errorInfo)
//            ProBaseToastUtils.toast(errorInfo.toString())
//            onGetResultSuccess?.invoke(null)
//        }
//    })
//    .postObject(PayMainModel::class.java)
    var lifecycleOwner:LifecycleOwner?=null
    var dialog: Dialog? = null
    var url:String? = null
    var params:Array<Any> = arrayOf()
    var method:String = BaseHttpConfig.RequestType.POST.toString()

    internal var _success: (Any) -> Unit = { }

    internal var _fail: (Throwable) -> Unit = {}

    fun create(_lifecycleOwner:LifecycleOwner,_dialog: Dialog?=null){
        this.lifecycleOwner = _lifecycleOwner
        this.dialog = _dialog
    }

    fun onSuccess(onSuccess: (Any) -> Unit) {
        _success = onSuccess
    }

    fun onFail(onError: (Throwable) -> Unit) {
        _fail = onError
    }


}


fun baseHttpUtils(block:BaseHttpUtilsKTX.()->Unit):BaseHttpUtils{
    var BaseHttpUtilsKTX = BaseHttpUtilsKTX()
    BaseHttpUtilsKTX.block()
    return getBaseHttpUtils(null,BaseHttpUtilsKTX)
}

fun BaseHttpUtils?.baseHttpUtils(block:BaseHttpUtilsKTX.()->Unit):BaseHttpUtils{
    var BaseHttpUtilsKTX = BaseHttpUtilsKTX()
    BaseHttpUtilsKTX.block()
    return getBaseHttpUtils(this, BaseHttpUtilsKTX)
}

private fun getBaseHttpUtils(baseHttpUtilss:BaseHttpUtils?=null,BaseHttpUtilsKTX: BaseHttpUtilsKTX):BaseHttpUtils{
    var baseHttpUtils : BaseHttpUtils? = null
    if(baseHttpUtilss == null){
       baseHttpUtils = BaseHttpUtils.create()
    }
    else{
        baseHttpUtils = baseHttpUtilss
    }
    baseHttpUtils?.apply {
        initUrl(BaseHttpUtilsKTX.url)
        initRequestType(when(BaseHttpUtilsKTX.method.orEmpty().toLowerCase()){
            "post" ->{ BaseHttpConfig.RequestType.POST}
            "get" ->{ BaseHttpConfig.RequestType.POST}
            "put" ->{ BaseHttpConfig.RequestType.PUT}

            else->{ BaseHttpConfig.RequestType.POST}
        })
    }
    return baseHttpUtils!!
}



fun test(){
    baseHttpUtils {
//        create(this@PayMainFragment, httpDialog)
        url = "asdasd"
        params = arrayOf("name", "lixingxing")
        method = "post"
        onSuccess{
        }
        onFail{
        }
    }
    BaseHttpUtils.create(null, null)
        .baseHttpUtils {
            url = "asdasd"
            params = arrayOf("name", "lixingxing")
            method = "post"
            onSuccess{
            }
            onFail{
            }
        }
}