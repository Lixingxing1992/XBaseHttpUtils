package com.app.org.http.httpservice

import com.app.org.http.BaseHttpConfig
import com.app.org.http.Response
import com.app.org.http.interfaces.IDataListener
import com.app.org.utils.BaseStringUtil
import com.google.gson.Gson
import com.google.gson.JsonParser
import org.json.JSONException
import org.json.JSONObject
import java.util.ArrayList

/**
 * Created by lixingxing on 2018/6/5.
 */
class JsonDataListener : IDataListener() {

    override fun isEmpty(): Boolean {
        return response.resultCode == "10007"
    }

    override fun isError(): Boolean {
        return response.resultCode != "0" && response.resultCode != "10007"
    }


    override fun <T> getObject(jsonString: String, cls: Class<T>, vararg args: String): T? {
        var t: T? = null
        var code = jsonString
        if (args.size > 0) {
            for (arg in args) {
                code = getResultData<String>(code, arg).toString()
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

    override fun <T> getObjectList(jsonString: String, cls: Class<T>, vararg args: String): List<T>? {
        val list = ArrayList<T>()
        var code = jsonString
        if (args.size > 0) {
            for (arg in args) {
                code = getResultData<String>(code, arg).toString()
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

    override fun <T> getResultData(jsonString: String, objects: String): T? {
        if (!BaseStringUtil.isEmpty(jsonString)) {
            try {
                val jsonObject = JSONObject(jsonString)
                return jsonObject.opt(objects) as T
            } catch (exception: JSONException) {

            }
        }
        return null
    }


    override fun <T> parseResult(result: String, responseType: BaseHttpConfig.ResponseType, cls: Class<T>) {
        if (BaseStringUtil.isEmpty(result)) {
            response?.errorCode = BaseHttpConfig.ErrorCode.Error_Read
        } else {
            try {
                val jsonObject = JSONObject(result)
                var resCode = jsonObject.optString("resCode")
                var resMsg = jsonObject.optString("resMsg")
                response.resultCode = resCode
                response.responseMsg = resMsg
                var dataResult = ""
                try {
                    dataResult = jsonObject.optString("data")
                } catch (exception: JSONException) {
                    dataResult = ""
                }
                if(resCode == "0"){
                    if (responseType == BaseHttpConfig.ResponseType.List) {
                        response.setListResponse(getObjectList(dataResult, cls))
                    } else if (responseType == BaseHttpConfig.ResponseType.Object) {
                        response.setObjectResponse(getObject(dataResult, cls))
                    } else {

                    }
                }
                response.responseDataResult = dataResult
            } catch (exception: Exception) {
                response?.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error
                exception.printStackTrace()
            }
        }
    }

    override fun getResponseData(): String? {
        return response.responseDataResult
    }

    override fun getErrorData(): String? {
        return response.responseMsg
    }


}
