package com.app.org.http.httpservice

import com.apkfuns.logutils.LogUtils
import com.app.org.http.*
import com.app.org.http.interfaces.IHttpService
import com.app.org.utils.BaseStringUtil
/**
 * Created by lixingxing on 2018/5/10.
 */
class XmlHttpService : IHttpService() {


    fun XmlHttpService(isShowLog:Boolean,tag:String){
        this.isShowLog = isShowLog
        this.tag = tag
    }

    override fun request(baseHttpModel: BaseHttpModel): Response {
        val url = baseHttpModel.url
        if(isShowLog)
            LogUtils.tag(tag ).e( "请求地址: $url")
        val params = init(baseHttpModel.params)
        return RequestUtil()
                .Request_ContentType(BaseHttpConfig.ParamType.XML)
                .Request_requestType(BaseHttpConfig.RequestType.POST)
                .request(params, url)
    }


    fun init(vararg param: Any):String{
        if(param.isNotEmpty()){
            var param = param[0] as Array<Any>
            if(param.size == 1 && param[0] is String){
                var params = param[0].toString()
                val sb = StringBuilder()
                sb.append("<request>")
                if (!BaseStringUtil.isEmpty(params)) {
                    sb.append(params)
                }
                sb.append("</request>")
                if(isShowLog)
                    LogUtils.tag(tag ).e("加密后请求参数: $sb")
                return sb.toString()
            }
        }
        return ""
    }

}
