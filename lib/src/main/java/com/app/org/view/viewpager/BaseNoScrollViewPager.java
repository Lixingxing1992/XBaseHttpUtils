package com.app.org.view.viewpager;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * <p>可以禁止滑动翻页的ViewPager </p>
 *
 * @author lixin 2017/9/27 10:10
 * @version V1.1
 * @name NoScrollViewPager
 */
public class BaseNoScrollViewPager extends ViewPager {

    private boolean isPagingEnabled = true;

    public BaseNoScrollViewPager(Context context) {
        super(context);
    }

    public BaseNoScrollViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onTouchEvent(event);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return this.isPagingEnabled && super.onInterceptTouchEvent(event);
    }

    public void setPagerEnabled(boolean b) {
        this.isPagingEnabled = b;
    }

    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, false);
    }

}
