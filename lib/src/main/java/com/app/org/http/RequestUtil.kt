package com.app.org.http

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


/**
 * Created by Administrator on 2017/6/7.
 */

class RequestUtil {

    private var successMsg = ""
    private var timeOut = 10 * 1000
    private var connectTimeout = 8 * 1000

    private var requestType = ""
    private var contentType = ""
    //上传文件所需参数
    private val BOUNDARY = UUID.randomUUID().toString()
    private val PREFIX = "--"
    private val LINE_END = "\r\n"
    private val CONTENT_TYPE = "multipart/form-data"
    private val CHARSET = "utf-8"

    companion object {
        // 如果成员变量有值则用成员变量的值->对应个别的调用情况
        // 如果成员变量没有值则使用静态变量的值->对应
        private var requestTypeStatic = "POST"
        private var contentTypeStatic = "application/json"

        //提交方式
        fun Request_requestType(paramType: BaseHttpConfig.RequestType){
            requestTypeStatic =
                    when (paramType) {

                        BaseHttpConfig.RequestType.GET -> "GET"

                        BaseHttpConfig.RequestType.POST -> "POST"

                        BaseHttpConfig.RequestType.FILE -> "FILE"

                        else -> "POST"
                    }
        }

        fun Request_requestType(paramType: String){
            contentTypeStatic = paramType
        }

        //参数类型
        fun Request_ContentType(paramType: BaseHttpConfig.ParamType){
            contentTypeStatic =
                    when (paramType) {

                        BaseHttpConfig.ParamType.JSON -> "application/json"

                        BaseHttpConfig.ParamType.XML -> "text/html;charset=UTF-8"

                        BaseHttpConfig.ParamType.POST -> "application/x-www-form-urlencoded"

                        else -> "application/json"
                    }
        }

        fun Request_ContentType(paramType: String) {
            contentTypeStatic = paramType
        }
    }

    //提交方式
    fun Request_requestType(paramType: BaseHttpConfig.RequestType): RequestUtil {
        requestType =
                when (paramType) {

                    BaseHttpConfig.RequestType.GET -> "GET"

                    BaseHttpConfig.RequestType.POST -> "POST"

                    BaseHttpConfig.RequestType.FILE -> "FILE"

                    else -> "POST"
                }
        return this
    }

    fun Request_requestType(paramType: String): RequestUtil {
        requestType = paramType
        return this
    }

    //参数类型
    fun Request_ContentType(paramType: BaseHttpConfig.ParamType): RequestUtil {
        contentType =
                when (paramType) {

                    BaseHttpConfig.ParamType.JSON -> "application/json"

                    BaseHttpConfig.ParamType.XML -> "text/html;charset=UTF-8"

                    BaseHttpConfig.ParamType.POST -> "application/x-www-form-urlencoded"

                    else -> "application/json"
                }
        return this
    }

    fun Request_ContentType(paramType: String): RequestUtil {
        contentType = paramType
        return this
    }


    @Synchronized
    fun request(params: String?, urlPath: String): Response {
        var requestType = if(requestType == "") requestTypeStatic else requestType
        var contentType = if(contentType == "") contentTypeStatic else contentType

        var baseHttpResultModel = Response()

        if (requestType == "FILE") {
            baseHttpResultModel.exception = RuntimeException("请选择File上传方法")
            return baseHttpResultModel
        }

        var url: URL?
        var conn: HttpURLConnection?
        try {
            url = URL(urlPath)
            //打开连接
            conn = url.openConnection() as HttpURLConnection
            //设置提交方式
            conn.doOutput = true
            conn.doInput = true
            conn.requestMethod = requestType
            if (requestType == "POST") {
                //post方式不能使用缓存
                conn.useCaches = false
                conn.instanceFollowRedirects = true
            }
            //设置连接超时时间
            conn.connectTimeout = connectTimeout
            //设置读取超时时间
            conn.readTimeout = timeOut
            //配置本次连接的Content-Type
            conn.setRequestProperty("Content-Type", contentType) //"application/x-www-form-urlencoded")
            //维持长连接
            conn.setRequestProperty("Connection", "Keep-Alive")
            //设置浏览器编码
            conn.setRequestProperty("Charset", CHARSET)

            val dos = DataOutputStream(conn.outputStream)
            dos.write(params?.toByteArray())
            dos.flush()
            dos.close()

            var code = conn.responseCode
            baseHttpResultModel.responseCode = code

            if (code == 200) {
                //获得服务器端输出流
                baseHttpResultModel.inputStream = conn.inputStream
            }
        } catch (e: Exception) {
            baseHttpResultModel.exception = e
            e.printStackTrace()
        }

        return baseHttpResultModel
    }

    @Synchronized
    fun uploadFileByFiles(files: List<File>, loadUrl: String, fileKeys: Array<String>, isDelete: Boolean) {
        var result: String? = null
        var requestTime = System.currentTimeMillis()
        var responseTime: Long = 0
        try {
            val url = URL(loadUrl)
            val conn = url.openConnection() as HttpURLConnection
            conn.readTimeout = timeOut
            conn.connectTimeout = connectTimeout
            conn.doInput = true
            conn.doOutput = true
            conn.useCaches = false
            conn.requestMethod = "POST"
            conn.setRequestProperty("Charset", CHARSET)
            conn.setRequestProperty("connection", "keep-alive")
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)")
            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary="
                    + BOUNDARY)
            val dos = DataOutputStream(conn.outputStream)
            var sb: StringBuffer? = null
            var params: String? = ""

            for (i in files.indices) {
                sb = null
                sb = StringBuffer()
                params = null
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append(PREFIX).append(BOUNDARY).append(LINE_END)
                sb.append("Content-Disposition: form-data; name=\"" + fileKeys[i] + "\"; filename=\""
                        + files[i].name + "\"" + LINE_END)
                sb.append("Content-Type: application/octet-stream; charset=$CHARSET$LINE_END")
                sb.append(LINE_END)
                params = sb.toString()
                println(params)
                sb = null
                dos.write(params.toByteArray())
                /**上传文件 */
                /**上传文件 */
                val `is` = FileInputStream(files[i])
                //onUploadProcessListener.initUpload((int)file.length());
                val bytes = ByteArray(1024)
                var len = 0
                var curLen = 0
//                while ((len = `is`.read(bytes)) != -1) {
//                    curLen += len
//                    dos.write(bytes, 0, len)
//                }
                `is`.close()
                dos.write(LINE_END.toByteArray())
            }
            val end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).toByteArray()
            dos.write(end_data)
            dos.flush()
            //dos.write(tempOutputStream.toByteArray());
            //获取响应码 200=成功 当响应成功，获取响应的流
            val res = conn.responseCode
            responseTime = System.currentTimeMillis()
            requestTime = ((responseTime - requestTime) / 1000).toInt().toLong()

            if (isDelete) {
                for (i in files.indices) {
                    files[i].delete()
                }
            }
            if (res == 200) {
                val input = conn.inputStream
                val `in` = BufferedReader(InputStreamReader(input, "utf-8"))
                val sb1 = StringBuffer()
                var line = ""
//                while ((line = `in`.readLine()) != null) {
//                    sb1.append(line)
//                }
                result = sb1.toString()
            } else {

            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun setSuccessMsg(successMsg: String): RequestUtil {
        this.successMsg = successMsg
        return this
    }

    fun setTimeOut(timeOut: Int): RequestUtil {
        this.timeOut = timeOut
        return this
    }

    fun setConnectTimeout(connectTimeout: Int): RequestUtil {
        this.connectTimeout = connectTimeout
        return this
    }



}
