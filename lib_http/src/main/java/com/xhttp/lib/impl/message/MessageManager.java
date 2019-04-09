package com.xhttp.lib.impl.message;

import android.content.Context;
import android.widget.Toast;

import com.xhttp.lib.interfaces.IMessageManager;

/**
 * Created by lixingxing on 2019/4/9.
 */
public class MessageManager implements IMessageManager {
    @Override
    public void showMessage(Context context,String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
