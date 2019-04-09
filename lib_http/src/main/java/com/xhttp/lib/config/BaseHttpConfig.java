package com.xhttp.lib.config;

/**
 * Created by lixingxing on 2018/4/11.
 */
public class BaseHttpConfig {
    public static final String TAG = "BaseHttpUtils";

    //错误类型
    public enum ErrorType {
        Error_Use("框架使用错误"),
        Error_Request("服务器请求失败"),
        Error_Data("返回值解析失败"),
        Error_Empty ("未获取到数据");

        private String msg = "";
        ErrorType(String msg){
            this.msg = msg;
        }
        @Override
        public String toString() {
            return msg;
        }
    }

    //错误代码
    public enum ErrorCode {
        Error_Success("请求成功"),
        Error_Use("调用方式错误,请仔细阅读使用说明"),
        Error_Params("因为程序原因导致参数传递错误"),
        Error_Unknow("未知错误导致请求失败"),
        Error_ResultErrorCode("网络请求返回码为 %s"),
        Error_ResultException("请求过程中出现异常"),
        Error_ResultExceptionTimeOut("服务器连接超时"),
        Error_ResultResponseNone("服务器返回结果为空,请联系系统管理员"),
        Error_HASNONEW("网络连接异常,请先检查您的网络配置"),
        Error_Result_Parsr_error("后台返回值解析错误"),
        Error_Result_Parsr_error1("后台返回值解析错误"),
        Error_Result_none("未获取到数据"),
        Error_Result_error("获取数据失败");

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


    //请求结果解析类型
//    public enum ResponseType {
//        List("List"),
//        Object("Object"),
//        String("String");
//        private String msg = "";
//        ResponseType(String msg){
//            this.msg = msg;
//        }
//        @Override
//        public String toString() {
//            return msg;
//        }
//    }
    public enum DataParseType {
        List("List"),
        Object("Object"),
        String("String");
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
