package com.app.org.http.httpservice

import com.app.org.http.BaseHttpConfig
import com.app.org.http.interfaces.IDataListener
import com.app.org.utils.BaseStringUtil
import org.json.JSONException
import org.json.JSONObject
import org.xmlpull.v1.XmlPullParser
import org.xmlpull.v1.XmlPullParserException
import org.xmlpull.v1.XmlPullParserFactory
import java.io.ByteArrayInputStream
import java.io.IOException
import java.lang.reflect.Field
import java.lang.reflect.InvocationTargetException
import java.util.ArrayList

/**
 * Created by lixingxing on 2018/6/5.
 */
class XmlDataListener : IDataListener() {
    override fun <T> getObject(resultStr: String, cls: Class<T>, vararg args: String): T? {
        return parseObject(resultStr.toByteArray(),cls)
    }

    override fun <T> getObjectList(resultStr: String, cls: Class<T>, vararg args: String): List<T>? {
        return parseObjectList(resultStr.toByteArray(),cls)
    }

    override fun <T> getResultData(resultStr: String, objects: String): T? {
        return parseStringByCode(resultStr.toByteArray(),objects) as T
    }

    override fun <T> parseResult(result: String, responseType: BaseHttpConfig.ResponseType, cls: Class<T>) {
        if (BaseStringUtil.isEmpty(result)) {
            response?.errorCode = BaseHttpConfig.ErrorCode.Error_Read
        } else {
            try {
                response.resultCode = "0"
                if (responseType == BaseHttpConfig.ResponseType.List) {
                    response.setListResponse(parseObjectList(result.toByteArray(), cls))
                } else if (responseType == BaseHttpConfig.ResponseType.Object) {
                    response.resultCode = "-100"
                    response.setObjectResponse(parseObject(result.toByteArray(), cls))
                    var obj = response.getObjectResponse<T>() as Any
                    var cls = obj.javaClass
                    var field: Field? = null
                    try {
                        field = cls.getDeclaredField("rt")
                        field.isAccessible = true
                        var value = field.get(obj) as String
                        field.isAccessible = false

                        response.resultCode = value
                    } catch (e: NoSuchFieldException) {
                        e.printStackTrace()
                    } catch (e: IllegalAccessException) {
                        e.printStackTrace()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } else {

                }
                response.responseDataResult = result
            } catch (exception: Exception) {
                response?.errorCode = BaseHttpConfig.ErrorCode.Error_Result_Parsr_error
                exception.printStackTrace()
            }
        }
    }

    override fun getResponseData(): String? {
        return ""
    }

    override fun getErrorData(): String? {
        if( response.resultCode == "-100"){
            return "返回值解析错误"
        }
        return ""
    }

    override fun isEmpty(): Boolean {
        if(responseType == BaseHttpConfig.ResponseType.List){
            return response.checkListIsEmpty()
        }else if(responseType == BaseHttpConfig.ResponseType.Object){
            return  response.checkObjectIsNone()
        }else{
            return false
        }
    }

    override fun isError(): Boolean {
        return response.resultCode != "0"
    }


    /**
     * 解析服务器返回的字符�?
     *
     * @param data
     * @return
     */
    fun parseString(data: ByteArray?): String? {
        if (null != data && data.size > 0) {
            val xmlParser = XmlPullParserFactory.newInstance()
                    .newPullParser()
            val bai = ByteArrayInputStream(data)
            xmlParser.setInput(bai, "UTF-8")
            var eventType = xmlParser.eventType
            var tagName: String? = null
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = xmlParser.name
                    if ("rt" == tagName) {
                        return xmlParser.nextText()
                    }
                }
                try {
                    eventType = xmlParser.next()
                } catch (e: Exception) {

                }

            }
        }
        return null
    }

    fun parseString(data: ByteArray?, code: String): Array<String?> {
        val strings = arrayOfNulls<String>(2)
        if (null != data && data.size > 0) {
            val xmlParser = XmlPullParserFactory.newInstance()
                    .newPullParser()
            val bai = ByteArrayInputStream(data)
            xmlParser.setInput(bai, "UTF-8")
            var eventType = xmlParser.eventType
            var tagName: String? = null
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = xmlParser.name
                    if ("rt" == tagName) {
                        strings[0] = xmlParser.nextText()
                    } else if (code == tagName) {
                        strings[1] = xmlParser.nextText()
                    }
                }
                try {
                    eventType = xmlParser.next()
                } catch (e: Exception) {

                }

            }
        }
        return strings
    }

    fun parseStringByCode(data: ByteArray?, code: String): String {
        var string = ""
        if (null != data && data.size > 0) {
            val xmlParser = XmlPullParserFactory.newInstance()
                    .newPullParser()
            val bai = ByteArrayInputStream(data)
            xmlParser.setInput(bai, "UTF-8")
            var eventType = xmlParser.eventType
            var tagName: String? = null
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = xmlParser.name
                    if (code.equals(tagName)) {
                        string = xmlParser.nextText()
                    }
                }
                try {
                    eventType = xmlParser.next()
                } catch (e: Exception) {

                }

            }
        }
        return string
    }

    /**
     * 解析服务器返回的对象
     *
     * @param data
     * @return
     * @throws InvocationTargetException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    @Throws(XmlPullParserException::class, IOException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, InstantiationException::class)
    fun <T> parseObject(data: ByteArray?, cls:Class<T> ): T? {
        // TODO Auto-generated method stub
        val returnObj =  cls.newInstance() as Any
        val fieldList = getField(returnObj)
        val methods = returnObj.javaClass.declaredMethods
        if (null != data && data.size > 0) {

            val xmlParser = XmlPullParserFactory.newInstance()
                    .newPullParser()
            val bai = ByteArrayInputStream(data)
            xmlParser.setInput(bai, "UTF-8")
            var eventType = xmlParser.eventType

            var tagName: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG) {
                    tagName = xmlParser.name

                    for (field in fieldList) {

                        if (field.toLowerCase() == tagName!!.toLowerCase()) {

                            val methodName = "set$field"

                            for (method in methods) {

                                if (method.name.toLowerCase() == methodName.toLowerCase()) {
                                    method.invoke(returnObj,
                                            xmlParser.nextText())
                                }

                            }

                        }

                    }

                }
                eventType = xmlParser.next()
            }
        }

        return returnObj as T
    }

    /**
     * 解析服务器返回的对象集合
     *
     * @param data
     * @return
     */
    @Throws(XmlPullParserException::class, IOException::class, IllegalAccessException::class, IllegalArgumentException::class, InvocationTargetException::class, InstantiationException::class)
    fun <T> parseObjectList(data: ByteArray?,cls:Class<T> ): List<T> {
        val returnObjList = ArrayList<Any>()
        var objectBase = cls.newInstance() as Any
        val fieldList = getField(objectBase)
        val methods = objectBase.javaClass.declaredMethods
        if (null != data && data.size > 0) {

            val xmlParser = XmlPullParserFactory.newInstance()
                    .newPullParser()
            val bai = ByteArrayInputStream(data)
            xmlParser.setInput(bai, "UTF-8")
            var eventType = xmlParser.eventType

            var tagName: String? = null

            while (eventType != XmlPullParser.END_DOCUMENT) {
                when (eventType) {
                    XmlPullParser.START_DOCUMENT -> {
                    }
                    XmlPullParser.START_TAG -> {
                        tagName = xmlParser.name
                        for (field in fieldList) {
                            if (field.toLowerCase() == tagName!!.toLowerCase()) {
                                val methodName = "set$field"
                                for (method in methods) {
                                    if (method.name.toLowerCase() == methodName.toLowerCase()) {
                                        method.invoke(objectBase,
                                                xmlParser.nextText())
                                        break
                                    }
                                }
                                break
                            }
                        }
                    }
                    XmlPullParser.END_TAG ->
                        //if (object.getClass().getName().equals(xmlParser.getName())) {
                        if ("item" == xmlParser.name.toLowerCase()) {
                            returnObjList.add(objectBase)
                            objectBase = cls.newInstance() as Any
                        }
                }
                eventType = xmlParser.next()// 循环解析下一个元�?
            }
        }

        return returnObjList as List<T>
    }


    /**
     * 获取某对象的�?��字段 用于xml解析封装 注意：这里的字段要和服务器返回的字段名称�?��
     *
     * @param object
     * @return
     */
    fun getField(`object`: Any): List<String> {
        // TODO Auto-generated method stub
        val fieldList = ArrayList<String>()
        val fields = `object`.javaClass
                .declaredFields
        for (f in fields) {
            fieldList.add(f.name)
        }
        return fieldList
    }
}
