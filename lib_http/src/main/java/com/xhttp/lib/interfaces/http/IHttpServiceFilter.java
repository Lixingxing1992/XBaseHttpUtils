package com.xhttp.lib.interfaces.http;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

/**
 * Created by lixingxing on 2019/4/29.
 */
public interface IHttpServiceFilter extends Serializable {
    @NotNull
    IHttpService filterIHttpService(IHttpService iHttpService);
}
