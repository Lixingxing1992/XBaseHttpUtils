package com.app.org.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.util.Log;

import com.app.org.utils.BaseNetworkUtil;

/**
 * Created by lixingxing on 2018/5/24.
 */
public class NetworkChangeReceiver extends BroadcastReceiver {

    public NetChangeListener listener;

    public NetworkChangeReceiver(NetChangeListener listener){
        this.listener = listener;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO Auto-generated method stub
        // 如果相等的话就说明网络状态发生了变化
        if (intent.getAction().equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            BaseNetworkUtil.NetworkType netWorkState = BaseNetworkUtil.getNetworkType();
            // 当网络发生变化，判断当前网络状态，并通过NetEvent回调当前网络状态
            if (listener != null) {
                listener.onChangeListener(netWorkState);
            }
        }
    }

    // 自定义接口
    public interface NetChangeListener {
        void onChangeListener(BaseNetworkUtil.NetworkType netWorkState);
    }

}
