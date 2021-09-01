package com.xhttp.lib.interfaces.data;


import com.xhttp.lib.BaseResult;
import com.xhttp.lib.model.BaseResultData;
import com.xhttp.lib.params.BaseHttpParams;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.List;

/**
 * 数据解析工具类
 * Created by lixingxing on 2019/3/26.
 */
public interface IDataListener<D extends BaseResultData> extends Serializable {
    // 先处理一下返回值,把返回值封装成约定好的参数实体类（如若没有，可以为string)
    // 实体类中要包含 resCode
    @NotNull
    D parseResult(@NotNull final BaseHttpParams baseHttpParams, @NotNull final String result);

    // 获取三种结果类型  根据 DataParseType 来调用不同的方法
    <T> List<T> getList(@NotNull final BaseHttpParams baseHttpParams, @NotNull final D result);
    <T> T getObject(@NotNull final BaseHttpParams baseHttpParams, @NotNull final D result);
    String getString(@NotNull final BaseHttpParams baseHttpParams, @NotNull final D result);


    // 根据解析结果判断是否失败
    boolean isFailResult(@NotNull final BaseHttpParams baseHttpParams, @NotNull D baseResultData, @NotNull BaseResult.Result result);


}
