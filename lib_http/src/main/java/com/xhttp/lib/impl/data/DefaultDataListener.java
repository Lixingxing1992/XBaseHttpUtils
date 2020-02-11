package com.xhttp.lib.impl.data;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseHttpConfig;
import com.xhttp.lib.interfaces.data.IDataListener;
import com.xhttp.lib.model.result.DefaultBaseResultData;
import com.xhttp.lib.params.BaseHttpParams;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lixingxing on 2019/3/27.
 */
public class DefaultDataListener implements IDataListener<DefaultBaseResultData> {

    @NotNull
    @Override
    public DefaultBaseResultData parseResult(@NotNull BaseHttpParams baseHttpParams, @NotNull String result) {
        // DefaultDataListener 只能解析到 string为止
        baseHttpParams.dataParseType = BaseHttpConfig.DataParseType.String;
        DefaultBaseResultData defaultBaseResultData = new DefaultBaseResultData();
        defaultBaseResultData.setResultCode("0");
        defaultBaseResultData.setResultMsg("");
        defaultBaseResultData.setResultData(result);
        return defaultBaseResultData;
    }

    @Override
    public boolean isFailResult(@NotNull BaseHttpParams baseHttpParams,
                                @NotNull DefaultBaseResultData baseResultData,
                                @NotNull BaseResult.Result result) {
        return false;
    }

    @Override
    public <T> List<T> getList(@NotNull BaseHttpParams baseHttpParams, @NotNull DefaultBaseResultData result) {
        return new ArrayList<>();
    }

    @Override
    public <T> T getObject(@NotNull BaseHttpParams baseHttpParams, @NotNull DefaultBaseResultData result) {
        return null;
    }

    @Override
    public String getString(@NotNull BaseHttpParams baseHttpParams, @NotNull DefaultBaseResultData result) {
        return result.getResultData();
    }
}
