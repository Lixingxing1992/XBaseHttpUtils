package com.app.org.http;

/**
 * Created by lixingxing on 2018/4/11.
 */
public class BaseHttpConfig {
    public static final int HTTP_TYPE_POST = 0;
    public static final int HTTP_TYPE_GET = 1;
    public static final int HTTP_TYPE_POST_JSON = 2;
    public static final int HTTP_TYPE_GET_JSON = 3;
    public static final int HTTP_TYPE_POST_TANGDAO = 4;


    //错误代码
    public enum ErrorCode {
        Error_Success("请求成功"),
        Error_Params("因为程序原因导致参数传递错误"),
        Error_Unknow("未知错误导致请求失败"),
        Error_ResultErrorCode("服务器请求失败"),
        Error_ResultException("服务器请求出现异常"),
        Error_HASNONEW("网络连接异常,请先检查您的网络配置"),
        Error_Read("服务器返回结果为空,请联系系统管理员"),
        Error_Result_Parsr_error("后台程序返回值不符合格式要求"),
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

    //错误类型
    public enum ErrorType {
        Error_Request("服务器请求失败"),
        Error_Data("返回值判断失败"),
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
        POST("POST"),
        JSON("JSON"),
        XML("XML");

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
    public enum ResponseType {
        List("List"),
        Object("Object"),
        String("String");
        private String msg = "";
        ResponseType(String msg){
            this.msg = msg;
        }
        @Override
        public String toString() {
            return msg;
        }
    }
}
