package com.app.org.base;

/**
 * <p>类说明</p>
 *
 * @author lixin 2017/9/20 22:23
 * @version V2.8.3
 * @name ApplicationDelegate
 */
public interface ApplicationDelegate {

    void onCreate();

    void onTerminate();

    void onLowMemory();

    void onTrimMemory(int level);

}
