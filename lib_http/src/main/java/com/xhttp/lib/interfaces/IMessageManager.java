package com.xhttp.lib.interfaces;

import android.content.Context;

/**
 * 请求返回处理正确或者错误信息的工具类
 * Created by lixingxing on 2019/4/9.
 */
public interface IMessageManager {
    void showMessage(Context context,String msg);
}
