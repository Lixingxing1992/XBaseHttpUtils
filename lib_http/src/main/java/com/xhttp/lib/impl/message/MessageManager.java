package com.xhttp.lib.impl.message;

import android.content.Context;
import android.widget.Toast;

import com.xhttp.lib.interfaces.IMessageManager;

/**
 * 展示提示的工具类
 * Created by lixingxing on 2019/4/9.
 */
public class MessageManager implements IMessageManager {
    @Override
    public void showMessages(Context context,String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showErrorMessages(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showEmptyMessages(Context context, String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
