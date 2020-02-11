package com.xhttp.lib.util;

import android.util.Log;

/**
 * 日志记录方法
 * @author Lixingxing
 */
public class BaseLogUtils {

    public static void logD(String TAGS,boolean openLog,String msg){
        if(openLog){
            Log.d(TAGS,msg);
        }
    }
    public static void logE(String TAGS,boolean openLog,String msg){
        if(openLog){
            Log.e(TAGS,msg);
        }
    }
}
