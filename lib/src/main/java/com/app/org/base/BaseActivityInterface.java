package com.app.org.base;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by lixingxing on 2018/4/17.
 */
public interface BaseActivityInterface {
    /**
     *  onCreate方法
     */
    public void onCreateBefore(Bundle savedInstanceState);

    /**
     * 设置布局文件
     */
    public void setRootView();

    /**
     * 初始化控件
     */
    public void initView();

    /**
     * 获取网络数据
     */
    public void getData();

    /**
     * 获取参数
     */
    public void initDefaultData(Intent intent);

    /**
     * 注册广播
     */
    public void registerReceivers();


    /**
     * 其他的操作
     */
    public void doAction();

}
