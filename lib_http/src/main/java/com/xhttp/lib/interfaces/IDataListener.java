package com.xhttp.lib.interfaces;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseErrorInfo;
import com.xhttp.lib.config.BaseHttpParams;

/**
 * Created by lixingxing on 2019/3/26.
 */
public interface IDataListener {
    // 处理返回值  解析存储等操作...
    void parse(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 检查数据是否失败  false 成功   true 解析失败
    boolean isFail(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 获取错误信息 返回值不能为空,  返回的 BaseErrorInfo里必须要有错误信息描述
    BaseErrorInfo getErrorInfo(BaseHttpParams baseHttpParams,BaseResult baseResult);
    // 检查是否为空数据  false 不为空  true空数据
    boolean isEmpty(BaseHttpParams baseHttpParams,BaseResult baseResult);
}
