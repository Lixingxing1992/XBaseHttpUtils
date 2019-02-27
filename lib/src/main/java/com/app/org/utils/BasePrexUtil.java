package com.app.org.utils;

/**
 * Created by lixingxing on 2018/1/30.
 * 单位转换相关类
 *
 dp2px                       : dp转px
 dip2px                      : dip转px
 px2dp                       : px转dp
 px2dip                      : px转dip
 sp2px                       : sp转px
 px2sp                       : px转sp
 */

public class BasePrexUtil {
    /**
     * dip转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dip2px(float dpValue) {
        return dp2px(dpValue);
    }

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return px值
     */
    public static int dp2px(float dpValue) {
        final float scale = BaseUtils.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转dip
     *
     * @param pxValue px值
     * @return dip值
     */
    public static int px2dip(float pxValue) {
        return px2dp(pxValue);
    }

    /**
     * px转dp
     *
     * @param pxValue px值
     * @return dp值
     */
    public static int px2dp(float pxValue) {
        final float scale = BaseUtils.getContext().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转px
     *
     * @param spValue sp值
     * @return px值
     */
    public static int sp2px( float spValue) {
        final float fontScale = BaseUtils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * px转sp
     *
     * @param pxValue px值
     * @return sp值
     */
    public static int px2sp( float pxValue) {
        final float fontScale = BaseUtils.getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }
}
