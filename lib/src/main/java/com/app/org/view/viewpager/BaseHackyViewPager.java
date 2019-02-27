package com.app.org.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <p> 解决图片缩放崩溃的问题</p>
 * @name HackyViewPager
 * @author lixin 2017/9/27 10:10
 * @version V1.1
 */
public class BaseHackyViewPager extends ViewPager {

    public BaseHackyViewPager(Context context) {
        super(context);
    }

    public BaseHackyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            return super.onInterceptTouchEvent(ev);
        } catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        return false;
    }
}
