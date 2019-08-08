package com.xhttp.lib.interfaces.data;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpParams;

import org.json.JSONException;

import java.util.List;
import java.util.Map;

/**
 * Created by lixingxing on 2019/3/26.
 */
public interface IDataListener {
    // 先处理一下返回值, 返回的内容应为接下来要解析的主体内容
    String parseResult(BaseHttpParams baseHttpParams,byte[] bytes) throws Exception;
    // 解析成列表
    List parseList(BaseHttpParams baseHttpParams, String resultObj) throws Exception ;
    // 解析成对象
    Object parseObject(BaseHttpParams baseHttpParams,String resultObj) throws Exception ;
    // 解析字符串
    String parseDefault(BaseHttpParams baseHttpParams,String resultObj) throws Exception ;
    // 解析组合模式返回值
    Map<String,Object> parseCombination(BaseHttpParams baseHttpParams, String resultObj) throws Exception ;

    // 检查数据是否失败  false 成功   true 解析失败
    boolean isFail(final BaseHttpParams baseHttpParams,final BaseResult baseResult);
    BaseErrorInfo getFailErrorInfo();

    // 检查是否为空数据  false 不为空  true空数据
    boolean isEmpty(final BaseHttpParams baseHttpParams,final BaseResult baseResult);
    BaseErrorInfo getEmptyErrorInfo();
}
