package com.xhttp.lib.asyn;

/**
 * Created by lixingxing on 2019/6/23.
 */
public class AsynData {

    private Object realdata = null;
    private boolean ready = false;

    public synchronized void setResult(Object realdata) {
        if (ready) {
            return;   // 防止setRealData被调用两次以上。
        }
        this.realdata = realdata;
        this.ready = true;
        notifyAll();
    }

    public synchronized Object getResult(){
        while (!ready) {
            try {
                wait();
            } catch (InterruptedException e) {
            }
        }
        return realdata;
    }


}
