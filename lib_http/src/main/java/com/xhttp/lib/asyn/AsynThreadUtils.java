package com.xhttp.lib.asyn;

/**
 * 异步处理类
 * Created by lixingxing on 2019/6/23.
 */
public class AsynThreadUtils {

    public AsynData request(){
        final AsynData asynData = new AsynData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                asynData.setResult(true);
            }
        }).start();
        return asynData;
    }

    public AsynData request(final Object result){
        final AsynData asynData = new AsynData();
        new Thread(new Runnable() {
            @Override
            public void run() {
                asynData.setResult(result);
            }
        }).start();
        return asynData;
    }

}
