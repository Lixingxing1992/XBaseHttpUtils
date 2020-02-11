package com.xhttp.lib.interfaces.message;

import android.content.Context;

/**
 * 展示提示的工具类
 * Created by lixingxing on 2019/4/9.
 */
public interface IMessageManager {
    void showMessages(Context context, String msg);
    void showErrorMessages(Context context, String msg);
    void showEmptyMessages(Context context, String msg);
}
