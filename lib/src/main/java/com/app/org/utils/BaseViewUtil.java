package com.app.org.utils;

import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by lixingxing on 2018/4/11.
 */
public class BaseViewUtil {

    /**
     * 设置图片按钮获取焦点改变状态
     */
    public static void setViewFocusChangedBg(View inView, final int bg_pic, final int bg_down_pic) {
        inView.setBackgroundResource(bg_pic);
        inView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(bg_down_pic);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(bg_pic);
                }
                return false;
            }
        });
    }
    /**
     * 设置图片按钮获取焦点改变状态(ImageView类型)
     */
    public static void setImageViewSrcFocusChangedBg(final ImageView inView, final int bg_pic, final int bg_down_pic) {
        inView.setImageResource(bg_pic);
        inView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    inView.setImageResource(bg_down_pic);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    inView.setImageResource(bg_pic);
                }
                return false;
            }
        });
    }
}
