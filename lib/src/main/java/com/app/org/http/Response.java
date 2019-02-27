package com.app.org.http;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static com.app.org.http.BaseHttpConfig.ErrorCode.Error_Success;

/**
 * Created by lixingxing on 2018/6/8.
 */
public class Response {
    public int responseCode = 200; //请求返回连接码
    public BaseHttpConfig.ErrorCode errorCode = Error_Success; // 结果正确/异常类型
    public String resultCode = "-100001" ; // 返回值标识码
    public String responseMsg = "解析错误,请联系系统管理员"; // 返回值信息
    public String responseResult;//返回值内容
    public String responseDataResult;//返回值主要内容区域
    public BaseHttpConfig.ResponseType responseType = BaseHttpConfig.ResponseType.String;
    public Exception exception; // 错误类型
    public InputStream inputStream; // 结果输入流

    private List<Object> listResponse = new ArrayList<>();
    private Object objectResponse;

    public <T>List<T> getListResponse() {
        if(responseType != BaseHttpConfig.ResponseType.List){
            throw new RuntimeException("请调用 initResponseType(BaseHttpConfig.ResponseType.List)");
        }
        return (List<T>)listResponse;
    }

    public <T> void setListResponse(List<T> listResponse) {
        this.listResponse = (List<Object>) listResponse;
    }

    public <T> T getObjectResponse() {
        if(responseType != BaseHttpConfig.ResponseType.Object){
            throw new RuntimeException("请调用 initResponseType(BaseHttpConfig.ResponseType.Object)");
        }
        return (T)objectResponse;
    }

    public <T> void setObjectResponse(T objectResponse) {
        this.objectResponse = (Object)objectResponse;
    }


    public boolean isSuccess(){
        return errorCode == Error_Success; // 结果正确/异常类型
    }

    public boolean checkListIsEmpty(){
        return  listResponse.isEmpty();
    }
    public boolean checkObjectIsNone(){
        return  objectResponse == null;
    }
}
