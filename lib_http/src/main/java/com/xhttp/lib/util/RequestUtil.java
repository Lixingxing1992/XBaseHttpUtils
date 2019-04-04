package com.xhttp.lib.util;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.config.BaseHttpParams;
import com.xhttp.lib.config.BaseErrorInfo;

import org.apache.http.conn.ConnectTimeoutException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

/**
 * 用于网络请求的类
 * Created by lixingxing on 2019/3/27.
 */
public class RequestUtil {
    private String successMsg = "";
    private int timeOut = 10000;
    private int connectTimeout = 8000;
    private String requestType = "";
    private String contentType = "";
    private final String BOUNDARY = UUID.randomUUID().toString();
    private final String PREFIX = "--";
    private final String LINE_END = "\r\n";
    private final String CONTENT_TYPE = "multipart/form-data";
    private final String CHARSET = "utf-8";
    private static String requestTypeStatic = "POST";
    private static String contentTypeStatic = "application/json";

    BaseHttpParams baseHttpParams;
    BaseResult baseResult;
    public RequestUtil(BaseHttpParams baseHttpParams, BaseResult baseResult){
        this.baseHttpParams = baseHttpParams;
        this.baseResult = baseResult;
    }

    /**
     * 设置请求提交方式
     * @param paramType
     * @return
     */
    public final RequestUtil Request_requestType(BaseHttpConfig.RequestType paramType) {
        this.requestType = paramType.toString();
        return this;
    }
    public final RequestUtil Request_requestType(String paramType) {
        this.requestType = paramType;
        return this;
    }

    /**
     * 设置参数类型
     * @param paramType
     * @return
     */
    public final RequestUtil Request_ContentType(BaseHttpConfig.ParamType paramType) {
        this.contentType = paramType.toString();
        return this;
    }
    public final RequestUtil Request_ContentType(String paramType) {
        this.contentType = paramType;
        return this;
    }


    public final synchronized byte[] request(String params, String urlPath) {
        BaseErrorInfo errorInfo = this.baseResult.errorInfo;
        try {
            URL url = new URL(urlPath);
            URLConnection connection = url.openConnection();
            if (connection == null) {
                throw new RuntimeException("null cannot be cast to non-null type java.net.HttpURLConnection");
            }
            HttpURLConnection conn = (HttpURLConnection)connection;
            conn.setRequestMethod(requestType);
            if (requestType.equals("POST")) {
                conn.setUseCaches(false);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setInstanceFollowRedirects(true);
            }

            conn.setConnectTimeout(this.connectTimeout);
            conn.setReadTimeout(this.timeOut);
            conn.setRequestProperty("Content-Type", contentType);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", this.CHARSET);

            byte[] resultBytes;

            if(!requestType.equals("GET") && params!=null){
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.write(params.getBytes());
                dos.flush();
                dos.close();
            }
            int code = conn.getResponseCode();
            baseResult.responseCode = code;
            if (code == 200) {
                //获得服务器端输出流
                InputStream inputStream = conn.getInputStream();
                resultBytes = stream2bytes(inputStream);
                if(resultBytes == null || resultBytes.length == 0){
                    baseResult.isRequestSuccess = false;
                    baseResult.responseCode = -1;
                    errorInfo.errorType = BaseHttpConfig.ErrorType.Error_Request;
                    errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultResponseNone;
                    errorInfo.errorMsg = errorInfo.errorCode.toString();
                    baseResult.errorInfo = errorInfo;
                }else{
                    baseResult.isRequestSuccess = true;
                    baseResult.bytes = resultBytes;
                }
            }else{
                baseResult.isRequestSuccess = false;
                errorInfo.errorType = BaseHttpConfig.ErrorType.Error_Request;
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultErrorCode;
                errorInfo.errorMsg = errorInfo.errorType + ","+
                        String.format(errorInfo.errorCode.toString(),baseResult.responseCode);
                baseResult.errorInfo = errorInfo;
            }
        } catch (Exception e) {
            baseResult.isRequestSuccess = false;
            baseResult.responseCode = -2;

            errorInfo.errorType = BaseHttpConfig.ErrorType.Error_Request;
            // TODO 根据不同的异常异常返回不同的结果
            errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultException;
            if(e instanceof ConnectTimeoutException){
                // 网络请求超时
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_ResultExceptionTimeOut;
            }
            else{

            }
            errorInfo.exception = e;
            errorInfo.errorMsg = errorInfo.getErrorMsg();
            e.printStackTrace();
        }
        return null;
    }

//    public final synchronized void uploadFileByFiles(@NotNull List files, @NotNull String loadUrl, @NotNull String[] fileKeys, boolean isDelete) {
//        Intrinsics.checkParameterIsNotNull(files, "files");
//        Intrinsics.checkParameterIsNotNull(loadUrl, "loadUrl");
//        Intrinsics.checkParameterIsNotNull(fileKeys, "fileKeys");
//        String result = (String)null;
//        long requestTime = System.currentTimeMillis();
//        long responseTime = 0L;
//
//        try {
//            URL url = new URL(loadUrl);
//            URLConnection var10000 = url.openConnection();
//            if (var10000 == null) {
//                throw new TypeCastException("null cannot be cast to non-null type java.net.HttpURLConnection");
//            }
//
//            HttpURLConnection conn = (HttpURLConnection)var10000;
//            conn.setReadTimeout(this.timeOut);
//            conn.setConnectTimeout(this.connectTimeout);
//            conn.setDoInput(true);
//            conn.setDoOutput(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Charset", this.CHARSET);
//            conn.setRequestProperty("connection", "keep-alive");
//            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//            conn.setRequestProperty("Content-Type", this.CONTENT_TYPE + ";boundary=" + this.BOUNDARY);
//            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
//            StringBuffer sb = (StringBuffer)null;
//            String params = "";
//            int i = 0;
//
//            int res;
//            byte[] var28;
//            for(res = ((Collection)files).size(); i < res; ++i) {
//                sb = (StringBuffer)null;
//                sb = new StringBuffer();
//                params = (String)null;
//                sb.append(this.PREFIX).append(this.BOUNDARY).append(this.LINE_END);
//                sb.append("Content-Disposition: form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + ((File)files.get(i)).getName() + "\"" + this.LINE_END);
//                sb.append("Content-Type: application/octet-stream; charset=" + this.CHARSET + this.LINE_END);
//                sb.append(this.LINE_END);
//                params = sb.toString();
//                System.out.println(params);
//                sb = (StringBuffer)null;
//                Charset var18 = Charsets.UTF_8;
//                if (params == null) {
//                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
//                }
//
//                var28 = params.getBytes(var18);
//                Intrinsics.checkExpressionValueIsNotNull(var28, "(this as java.lang.String).getBytes(charset)");
//                byte[] var24 = var28;
//                dos.write(var24);
//                FileInputStream is = new FileInputStream((File)files.get(i));
//                byte[] bytes = new byte[1024];
//                int len = false;
//                int curLen = false;
//                is.close();
//                String var21 = this.LINE_END;
//                Charset var22 = Charsets.UTF_8;
//                if (var21 == null) {
//                    throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
//                }
//
//                var28 = var21.getBytes(var22);
//                Intrinsics.checkExpressionValueIsNotNull(var28, "(this as java.lang.String).getBytes(charset)");
//                var24 = var28;
//                dos.write(var24);
//            }
//
//            String var27 = this.PREFIX + this.BOUNDARY + this.PREFIX + this.LINE_END;
//            Charset var29 = Charsets.UTF_8;
//            if (var27 == null) {
//                throw new TypeCastException("null cannot be cast to non-null type java.lang.String");
//            }
//
//            var28 = var27.getBytes(var29);
//            Intrinsics.checkExpressionValueIsNotNull(var28, "(this as java.lang.String).getBytes(charset)");
//            byte[] end_data = var28;
//            dos.write(end_data);
//            dos.flush();
//            res = conn.getResponseCode();
//            responseTime = System.currentTimeMillis();
//            requestTime = (long)((int)((responseTime - requestTime) / (long)1000));
//            if (isDelete) {
//                int i = 0;
//
//                for(int var33 = ((Collection)files).size(); i < var33; ++i) {
//                    ((File)files.get(i)).delete();
//                }
//            }
//
//            if (res == 200) {
//                InputStream input = conn.getInputStream();
//                new BufferedReader((Reader)(new InputStreamReader(input, "utf-8")));
//                StringBuffer sb1 = new StringBuffer();
//                String line = "";
//                result = sb1.toString();
//            }
//        } catch (IOException var25) {
//            var25.printStackTrace();
//        }
//
//    }

    /**
     * 从流中读取数据到byte[]..
     *
     * @param inStream the in stream
     * @return the byte[]
     * @throws IOException Signals that an I/O exception has occurred.
     */
    public synchronized byte[] stream2bytes(InputStream inStream) throws IOException {
        byte[] buff = new byte[1024];
        byte[] data = null;
        try {
            ByteArrayOutputStream swapStream = new ByteArrayOutputStream();
            int read = 0;
            while ((read = inStream.read(buff, 0, 100)) > 0) {
                swapStream.write(buff, 0, read);
            }
            data = swapStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }
}
