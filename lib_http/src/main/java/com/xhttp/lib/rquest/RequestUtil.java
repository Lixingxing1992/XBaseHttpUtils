package com.xhttp.lib.rquest;

import android.util.Pair;

import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.exceptions.BaseHttpUtilsError;
import com.xhttp.lib.model.BaseErrorInfo;
import com.xhttp.lib.model.BaseRequestResult;
import com.xhttp.lib.params.BaseHttpParams;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 用于网络请求的类
 * Created by lixingxing on 2019/3/27.
 */
public class RequestUtil {
    private String successMsg = "";
    public int timeOut = 12 * 1000;
    public int connectTimeout = 12 * 1000;
    private String requestType = "";
    private String contentType = "";
    private final String BOUNDARY = UUID.randomUUID().toString();
    private final String PREFIX = "--";
    private final String LINE_END = "\r\n";
    private final String CHARSET = "utf-8";

    private List<Pair<String,Object>> headerParamsList = new ArrayList<>();


    public Proxy proxy;
    public boolean openTest = true;

    BaseHttpParams baseHttpParams;

    public RequestUtil(BaseHttpParams baseHttpParams) {
        this.baseHttpParams = baseHttpParams;
    }

    /**
     * 设置请求提交方式
     *
     * @param paramType
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
     *
     * @param paramType
     */
    public final RequestUtil Request_ContentType(BaseHttpConfig.ParamType paramType) {
        this.contentType = paramType.toString();
        return this;
    }

    public final RequestUtil Request_ContentType(String paramType) {
        this.contentType = paramType;
        return this;
    }

    public final RequestUtil Request_HeaderParams(List<Pair<String,Object>> headerParamsList){
        this.headerParamsList = headerParamsList;
        return this;
    }

    /**
     * 设置超时时间
     *
     * @param timeOut
     */
    public final RequestUtil Reqeust_ConnectTimeOut(int timeOut) {
        this.connectTimeout = timeOut;
        return this;
    }

    public final RequestUtil Reqeust_ReadTimeOut(int timeOut) {
        this.timeOut = timeOut;
        return this;
    }

    /**
     *
     * @param proxy
     */
    public void Request_setProxy(Proxy proxy) {
        this.proxy = proxy;
    }
    public RequestUtil Request_OpenProxy(boolean openTest) {
        this.openTest = openTest;
        return this;
    }

    /**
     * 设置是否允许代理
     * @param params
     * @param urlPath
     * @return
     */


    @NotNull
    public final synchronized BaseRequestResult request(String params, String urlPath) {
        BaseRequestResult baseRequestResult = new BaseRequestResult();
        BaseErrorInfo errorInfo = baseRequestResult.errorInfo;
        try {
            URL url = new URL(urlPath);
            URLConnection connection = openTest ? url.openConnection() : url.openConnection(Proxy.NO_PROXY);
            if (connection == null) {
                throw new BaseHttpUtilsError("null cannot be cast to non-null type java.net.HttpURLConnection");
            }
            HttpURLConnection conn = (HttpURLConnection) connection;
            conn.setRequestMethod(requestType);
            if (requestType.equals("POST") || requestType.equals("PUT")) {
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

            if(!headerParamsList.isEmpty()){
                for (Pair<String, Object> stringObjectPair : headerParamsList) {
                    conn.addRequestProperty(stringObjectPair.first,stringObjectPair.second.toString());
                }
            }


            byte[] resultBytes;

            if (params != null && !"".equals(params) && requestType.equals("POST")) {
                DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
                dos.write(params.getBytes());
                dos.flush();
                dos.close();
            }
            int code = conn.getResponseCode();
            baseRequestResult.responseCode = code;
            if (code == 200) {
                //获得服务器端输出流
                InputStream inputStream = conn.getInputStream();
                resultBytes = stream2bytes(inputStream);
                if (resultBytes == null || resultBytes.length == 0) {
                    baseRequestResult.isSuccess = false;
                    baseRequestResult.responseCode = 200;
                    errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpFail;
                    errorInfo.errorMsg = "状态码200,但返回值为空";
                    baseRequestResult.errorInfo = errorInfo;
                } else {
                    baseRequestResult.isSuccess = true;
                    baseRequestResult.bytes = resultBytes;
                }
            } else {
                baseRequestResult.isSuccess = false;
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpErrorCode;
                errorInfo.errorMsg = baseRequestResult.responseCode + "";
                baseRequestResult.errorInfo = errorInfo;
            }
        } catch (Exception e) {
            baseRequestResult.isSuccess = false;
            if(e instanceof SocketTimeoutException){
                baseRequestResult.responseCode = BaseHttpConfig.REQUEST_CODE_ERROR;
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpTimeOut;
            }
            else{
                baseRequestResult.responseCode = BaseHttpConfig.REQUEST_CODE_ERROR;
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpException;
                errorInfo.exception = e;
            }
            e.printStackTrace();
        }
        return baseRequestResult;
    }

    // 同步上传多张文件
    public final synchronized BaseRequestResult uploadFileByFiles(final List<File> files, final List<String> fileKeys, final String loadUrl, RequestUtilFileListener requestUtilFileListener) {
        if(requestUtilFileListener != null) {
            this.requestUtilFileListener = requestUtilFileListener;
        }
        BaseRequestResult baseRequestResult = new BaseRequestResult();
        BaseErrorInfo errorInfo = baseRequestResult.errorInfo;
        errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_File_error;
        try {
            URL url = new URL(loadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(this.timeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", contentType + ";boundary="
                    + BOUNDARY);


            if(!headerParamsList.isEmpty()){
                for (Pair<String, Object> stringObjectPair : headerParamsList) {
                    conn.addRequestProperty(stringObjectPair.first,stringObjectPair.second.toString());
                }
            }

            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;

            sb = new StringBuffer();
            for (Pair<String, Object> entry : baseHttpParams.paramsList) {

                sb.append(PREFIX).append(BOUNDARY).append(LINE_END)
                        .append("Content-Disposition: form-data; name=\"" + entry.first + "\"" + LINE_END)
                        .append("Content-Type: text/plain; charset=" + CHARSET + LINE_END)
                        .append("Content-Transfer-Encoding: 8bit" + LINE_END)
                        .append(LINE_END).append(entry.second).append(LINE_END);

            }
            if(sb.length() > 0){
                dos.write(sb.toString().getBytes());
            }

            byte[] resultBytes;
            String params = "";
            for (int i = 0; i < files.size(); i++) {
                sb = new StringBuffer();
                /**
                 * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
                 * filename是文件的名字，包含后缀名的 比如:abc.png
                 */
                sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
                sb.append("Content-Disposition: form-data; name=\"" + fileKeys.get(i) + "\"; filename=\""
                        + files.get(i).getName() + "\"" + LINE_END);
                sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
                sb.append(LINE_END);
                params = sb.toString();
                dos.write(params.getBytes());
                /**上传文件*/
                InputStream is = new FileInputStream(files.get(i));
                long total = is.available();
                System.out.println("文件大小:"+total);
                byte[] bytes = new byte[1024];
                int len = 0;
                int curLen = 0;
                while ((len = is.read(bytes)) != -1) {
                    curLen += len;
                    System.out.println("已上传:"+curLen);
                    dos.write(bytes, 0, len);
                    if(requestUtilFileListener != null){
                        requestUtilFileListener.onFileProgress(i,files.get(i),curLen,total);
                    }
                }
                is.close();
                dos.write(LINE_END.getBytes());
            }
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            int code = conn.getResponseCode();
            baseRequestResult.responseCode = code;
            if (code == 200) {
                //获得服务器端输出流
                InputStream inputStream = conn.getInputStream();
                resultBytes = stream2bytes(inputStream);
                if (resultBytes == null || resultBytes.length == 0) {
                    baseRequestResult.isSuccess = false;
                    baseRequestResult.responseCode = 200;
                    errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpFail;
                    errorInfo.errorMsg = errorInfo.errorCode.toString();
                    baseRequestResult.errorInfo = errorInfo;
                } else {
                    baseRequestResult.isSuccess = true;
                    baseRequestResult.bytes = resultBytes;
                }
            } else {
                baseRequestResult.isSuccess = false;
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpErrorCode;
                errorInfo.errorMsg = String.format(errorInfo.errorCode.toString(), baseRequestResult.responseCode);
                baseRequestResult.errorInfo = errorInfo;
            }
        } catch (Exception e) {
            baseRequestResult.isSuccess = false;
            if(e instanceof SocketTimeoutException){
                baseRequestResult.responseCode = BaseHttpConfig.REQUEST_CODE_ERROR;
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpTimeOut;
            }
            else{
                baseRequestResult.responseCode = BaseHttpConfig.REQUEST_CODE_ERROR;
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpException;
                errorInfo.exception = e;
            }
            e.printStackTrace();
        }
        return baseRequestResult;
    }

    // 上传单个文件  用于异步上传
    public final synchronized BaseRequestResult uploadFileByFile(final int postion, final File file, final String fileKey, final String loadUrl, RequestUtilFileListener requestUtilFileListener) {
        if(requestUtilFileListener != null) {
            this.requestUtilFileListener = requestUtilFileListener;
        }
        BaseRequestResult baseRequestResult = new BaseRequestResult();
        BaseErrorInfo errorInfo = baseRequestResult.errorInfo;
        errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_File_error;
        try {
            URL url = new URL(loadUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(this.timeOut);
            conn.setConnectTimeout(connectTimeout);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Charset", CHARSET);
            conn.setRequestProperty("connection", "keep-alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
            conn.setRequestProperty("Content-Type", contentType + ";boundary="
                    + BOUNDARY);
            DataOutputStream dos = new DataOutputStream(conn.getOutputStream());
            StringBuffer sb = null;
            String params = "";
            byte[] resultBytes;
            sb = new StringBuffer();
            /**
             * 这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件
             * filename是文件的名字，包含后缀名的 比如:abc.png
             */
            sb.append(PREFIX).append(BOUNDARY).append(LINE_END);
            sb.append("Content-Disposition: form-data; name=\"" + fileKey + "\"; filename=\""
                    + file.getName() + "\"" + LINE_END);
            sb.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END);
            sb.append(LINE_END);
            params = sb.toString();
            System.out.println(params);
            dos.write(params.getBytes());
            /**上传文件*/
            InputStream is = new FileInputStream(file);
            // 总大小
            long total = is.available();
            System.out.println("文件大小:"+total);
            byte[] bytes = new byte[1024];
            int len = 0;
            int curLen = 0;
            while ((len = is.read(bytes)) != -1) {
                curLen += len;
                dos.write(bytes, 0, len);
                if(requestUtilFileListener != null){
                    requestUtilFileListener.onFileProgress(postion,file,curLen,total);
                }
            }
            is.close();
            dos.write(LINE_END.getBytes());
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
            dos.write(end_data);
            dos.flush();
            int code = conn.getResponseCode();
            baseRequestResult.responseCode = code;
            if (code == 200) {
                //获得服务器端输出流
                InputStream inputStream = conn.getInputStream();
                resultBytes = stream2bytes(inputStream);
                if (resultBytes == null || resultBytes.length == 0) {
                    baseRequestResult.isSuccess = false;
                    baseRequestResult.responseCode = 200;
                    errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpFail;
                    errorInfo.errorMsg = errorInfo.errorCode.toString();
                    baseRequestResult.errorInfo = errorInfo;
                } else {
                    baseRequestResult.isSuccess = true;
                    baseRequestResult.bytes = resultBytes;
                }
            } else {
                baseRequestResult.isSuccess = false;
                errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_HttpErrorCode;
                errorInfo.errorMsg = String.format(errorInfo.errorCode.toString(), baseRequestResult.responseCode);
                baseRequestResult.errorInfo = errorInfo;
            }
        } catch (Exception e) {
            baseRequestResult.isSuccess = false;
            baseRequestResult.responseCode = BaseHttpConfig.REQUEST_CODE_ERROR;
            // TODO 根据不同的异常异常返回不同的结果
            errorInfo.errorCode = BaseHttpConfig.ErrorCode.Error_File_error;
            errorInfo.exception = e;
            e.printStackTrace();
        }
        return baseRequestResult;
    }

    /**
     * 从流中读取数据到byte[]..
     *
     * @param inStream the in stream
     * @return the byte[]
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

    RequestUtilFileListener requestUtilFileListener;

    public RequestUtil setRequestUtilFileListener(RequestUtilFileListener requestUtilFileListener) {
        this.requestUtilFileListener = requestUtilFileListener;
        return this;
    }

    public interface RequestUtilFileListener{
        void onFileProgress(int position, File file, long curlenth, long total);
    }
}
