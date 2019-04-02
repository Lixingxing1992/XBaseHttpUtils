package com.xhttp.lib.interfaces;

import com.xhttp.lib.BaseResult;
import com.xhttp.lib.config.BaseHttpParams;

/**
 * Created by lixingxing on 2019/3/26.
 */
public interface IDataListener {
    public BaseResult parse(BaseHttpParams baseHttpParams,BaseResult baseResult);
}
