package com.app.org.http.httpservice

import com.apkfuns.logutils.LogUtils
import com.app.org.encryption.BaseEncodeUtil
import com.app.org.http.*
import com.app.org.http.interfaces.IHttpService
import com.app.org.utils.BaseLogUtil
import com.app.org.utils.BaseStringUtil
import com.google.gson.Gson
import org.json.JSONArray
import org.json.JSONObject

/**
 * Created by lixingxing on 2018/5/10.
 */
class TDHttpService : IHttpService() {


    fun TDHttpService(isShowLog: Boolean, tag: String) {
        this.isShowLog = isShowLog
        this.tag = tag
    }

    override fun request(baseHttpModel: BaseHttpModel): Response {
        val url = baseHttpModel.url
        if (isShowLog)
            LogUtils.tag(tag).e("请求地址: $url")
        val params = init(baseHttpModel.params)

        return RequestUtil()
                .Request_ContentType(BaseHttpConfig.ParamType.JSON)
                .Request_requestType(BaseHttpConfig.RequestType.POST)
                .request(params, url)
    }


    fun init(vararg param: Any): String {
        if (param.isNotEmpty()) {
            var param = param[0] as Array<Any>
            if (param.size > 1 && param.size % 2 == 0) {
                var params = JSONObject()
                var i = 0
                while (i < param.size - 1) {
                    val key = param[i].toString()
                    val value = param[i + 1]
                    if (value is List<*>) {
                        val string = Gson().toJson(value)
                        val job = JSONArray(string)
                        params!!.put(key, job)
                    } else {
                        params!!.put(key, value)
                    }
                    i += 2
                }
                if (isShowLog)
                    LogUtils.tag(tag ).e("加密前请求参数: $params")
                //将请求参数数据向服务器端发送\
                var jsonParams = JSONObject()
                jsonParams.put("userCode", "qqkj")
                jsonParams.put("passwd", "123456")
                if (!BaseStringUtil.isEmpty(params.toString())) {
                    var params = BaseEncodeUtil.ooEncode(params.toString())
                    jsonParams.put("data", params)
                }
                if (isShowLog)
                    LogUtils.tag(tag ).e("加密后请求参数: $jsonParams")
                return jsonParams.toString()
            }
        }
        return ""
    }

}
