package com.xhttp.lib.params;

/**
 * 提示设置
 * @author Lixingxing
 */
public class BaseHttpMessageParams {

    // 是否提示(如果设置为true,则不管什么情况,一定会提示)
    private boolean isShowMessage = true;
    // 当成功的时候是否弹出提示
    private boolean isShowMessageWhenSuccess = true;
    // 当失败的是够是否弹出提示
    private boolean isShowMessageWhenFail = true;
}
