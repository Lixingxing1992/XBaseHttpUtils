package com.app.org.broadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;

import com.app.org.utils.BaseUtils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 广播注册和取消注册
 * Created by lixingxing on 2018/4/17.
 */
public class BaseBroadcastUtil {

    Object tag = "";
    public Map<Object, Map<String, BroadcastReceiver>> broadcastReceiverMapAll = new HashMap<>();
    public Map<String, BroadcastReceiver> broadcastReceiverMap = new HashMap<>();
    public BaseBroadcastUtil(Object tag){
        this.tag = tag;
        broadcastReceiverMap = broadcastReceiverMapAll.get(tag);
        if(broadcastReceiverMap == null){
            broadcastReceiverMap = new HashMap<>();
            broadcastReceiverMapAll.put(tag,broadcastReceiverMap);
        }
    }

    public void addReceivers(String brName, BroadcastReceiver broadcastReceiver) {
        if (!broadcastReceiverMap.containsKey(brName)) {
            BaseUtils.getContext().registerReceiver(broadcastReceiver,new IntentFilter(brName));
            broadcastReceiverMap.put(brName, broadcastReceiver);
        }
    }

    public void clearReceiver() {
        Iterator it = broadcastReceiverMap.keySet().iterator();
        while (it.hasNext()) {
            String key = (String) it.next();
            BroadcastReceiver broadcastReceiver = broadcastReceiverMap.get(key);
            if (null != broadcastReceiver) {
                try {
                    BaseUtils.getContext().unregisterReceiver(broadcastReceiver);
                } catch (Exception e) {
                }
            }
        }
        broadcastReceiverMap.clear();
    }


}
