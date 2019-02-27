package com.app.org.base;

import android.app.Application;

import com.app.org.broadcast.BaseBroadcastUtil;
import com.app.org.net.NetworkChangeReceiver;
import com.app.org.utils.BaseNetworkUtil;
import com.github.anzewei.parallaxbacklayout.ParallaxHelper;
import com.app.org.utils.BaseUtils;

import java.util.List;

import io.paperdb.Paper;

/**
 * 要想使用BaseApplication，必须在组件中实现自己的Application，并且继承BaseApplication；
 * 组件中实现的Application必须在debug包中的AndroidManifest.xml中注册，否则无法使用；
 * 组件的Application需置于java/debug文件夹中，不得放于主代码；
 * 组件中获取Context的方法必须为:Utils.getContext()，不允许其他写法；
 *
 * @author 2016/12/2 17:02
 * @version V1.0.0
 * @name BaseApplication
 */
public class BaseApplication extends Application {

//    public String ROOT_PACKAGE = "com.qqkjbasepro.org";

    private static BaseApplication sInstance;

    public static BaseApplication getIns() {
        return sInstance;
    }


    // 网络连接状态
    public BaseNetworkUtil.NetworkType newWorkType = BaseNetworkUtil.NetworkType.NETWORK_NO;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;

        BaseUtils.init(this);

        Paper.init(this);

        registerActivityLifecycleCallbacks(ParallaxHelper.getInstance());

        new BaseBroadcastUtil("BaseApplication")
                .addReceivers(BaseNetworkUtil.NET_BR_NAME,
                        new NetworkChangeReceiver(new NetworkChangeReceiver.NetChangeListener() {
            @Override
            public void onChangeListener(BaseNetworkUtil.NetworkType netWorkState) {
                newWorkType = netWorkState;
            }
        }));

//        mAppDelegateList = BaseClassUtil.getObjectsWithInterface(this, ApplicationDelegate.class, ROOT_PACKAGE);
//        for (ApplicationDelegate delegate : mAppDelegateList) {
//            delegate.onCreate();
//        }

    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }
}
