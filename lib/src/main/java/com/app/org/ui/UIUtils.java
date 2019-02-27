package com.app.org.ui;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.gyf.barlibrary.ImmersionBar;

import java.lang.reflect.Field;

/**
 * 用来按美工的基准值生成真实设备上需要的宽高值
 * Created by lixingxing on 2018/5/28.
 */
public class UIUtils {
    //做成单例
    private static UIUtils ourInstance;
    public static UIUtils getInstance(Context context){
        return ourInstance;
    }

    //基准宽高
    public static int STANDARD_WIDTH = 720;
    public static int STANDARD_HEIGHT = 1280-48;

    private static final String DIMEN_CLASS = "com.android.internal.R$dimen";

    //实际设备分辨率
    public float displayMetricsWidth;
    public float displayMetircsHeight;

    //初始化
    private UIUtils(Context context){
        WindowManager windowManager = (WindowManager)context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        if(displayMetricsWidth == 0.0F || displayMetircsHeight == 0.0F ){
            windowManager.getDefaultDisplay().getMetrics(displayMetrics);
            int systemBarHight = getSystemBarHeight(context);

            // 处理真实的宽高
            if(displayMetrics.widthPixels > displayMetrics.heightPixels){ // 横
                this.displayMetricsWidth = displayMetrics.heightPixels;
                this.displayMetircsHeight = displayMetrics.widthPixels - systemBarHight;
            }else{  // 竖
                this.displayMetircsHeight = displayMetrics.heightPixels - systemBarHight;
                this.displayMetricsWidth = displayMetrics.widthPixels;
            }
        }
    }


    // 获取状态栏高度
    private int getSystemBarHeight(Context context){
        return getValue(context,"com.android.internal.R$dimen","system_bar_height",48);
    }


    /**
     *
     * @param context
     * @param attrGroupClass 安卓源码中找到的存放围度的类
     * @param attrName 状态看的信息
     * @param defValue 默认值
     * @return
     */
    private int getValue(Context context,String attrGroupClass,String attrName,int defValue){
        try {
            Class e = Class.forName(attrGroupClass);
            Object obj = e.newInstance();
            Field field = e.getField(attrName);
            //获取到一个ID
            int x = Integer.parseInt(field.get(obj).toString());

            return context.getResources().getDimensionPixelOffset(x);
        }catch (Exception e) {
            e.printStackTrace();
            return defValue;
        }
    }


    //开始获取缩放以后的结果
    public float getWidth(float width){
        return width*this.displayMetricsWidth/720.0F;
    }
    public float getHeight(float height){
        return height*this.displayMetircsHeight/1280.0F;
    }

    public int getWidth(int width){
        return (int)(width*this.displayMetricsWidth/720.0F);
    }
    public int getHeight(int height){
        return (int)(height*this.displayMetircsHeight/1280.0F);
    }


}
