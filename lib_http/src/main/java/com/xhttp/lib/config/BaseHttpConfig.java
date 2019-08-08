package com.xhttp.lib.config;

/**
 * Created by lixingxing on 2018/4/11.
 */
public class BaseHttpConfig {
    public static final String TAG = "BaseHttpUtils";

    public static final int REQUEST_CODE_SUCCESS = 0;
    public static final int REQUEST_CODE_EMPTY = -1;
    public static final int REQUEST_CODE_ERROR = -2;
    //错误类型
//    public enum ErrorType {
//        Error_Use("框架使用错误"),
//        Error_Request("服务器请求失败"),
//        Error_Data("返回值解析失败"),
//        Error_Empty ("未获取到数据");
//
//        private String msg = "";
//        ErrorType(String msg){
//            this.msg = msg;
//        }
//        @Override
//        public String toString() {
//            return msg;
//        }
//    }

    //错误代码
    public enum ErrorCode {
        Error_Success("请求成功"),
        Error_Use("调用方式错误,请仔细阅读使用说明"),
        Error_Params("因为程序原因导致参数传递错误"),
        Error_UnknowHttpError("未知错误导致请求失败"),
        Error_HttpErrorCode("网络请求返回码为 %s"),
        Error_HttpException("请求过程中出现异常"),
        Error_HttpExceptionTimeOut("服务器连接超时"),
        Error_HttpResponseNone("服务器返回结果为空,请联系系统管理员"),
        Error_HASNONEW("网络连接异常,请先检查您的网络配置"),
        Error_Result_Parsr_error("后台返回值解析异常"),
        Error_Result_Parsr_error_default("后台返回值提示错误"),
        Error_Result_none("未获取到数据"),
        Error_Result_error("获取数据失败"),
        Error_File_error("文件上传失败");

        private String msg = "";
        ErrorCode(String msg){
            this.msg = msg;
        }
        @Override
        public String toString() {
            return msg;
        }
    }


    //请求方式类型
    public enum RequestType {
        POST("POST"),
        GET("GET"),
        FILE("FILE");
        private String msg = "";
        RequestType(String msg){
            this.msg = msg;
        }
        @Override
        public String toString() {
            return msg;
        }
    }
    //请求参数类型
    public enum ParamType {
        DEFAULT("text/html;charset=UTF-8"),
        JSON("application/json"),
        XML("XML"),
        FILE("multipart/form-data"),
        String("text/html;charset=UTF-8");

        private String msg = "";
        ParamType(String msg){
            this.msg = msg;
        }
        @Override
        public String toString() {
            return msg;
        }
    }
    //参数解析对象
    public enum DataParseType {
        List("List"),
        Object("Object"),
        String("String"),
        // 组合模式: 比如 多个字段+列表+对象等等
        Combination("Combination");
        private String msg = "";
        DataParseType(String msg){
            this.msg = msg;
        }
        @Override
        public String toString() {
            return msg;
        }
    }
}
