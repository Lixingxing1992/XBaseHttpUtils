package com.xhttp.lib.interfaces.data;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by lixingxing on 2019/4/29.
 */
public interface IDataListenerFilter extends Serializable {
    @NotNull
    IDataListener filterIDataListener(IDataListener iDataListener);
}
