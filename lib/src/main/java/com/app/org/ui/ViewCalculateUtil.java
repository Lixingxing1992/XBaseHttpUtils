package com.app.org.ui;

import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by lixingxing on 2018/5/28.
 */
public class ViewCalculateUtil {
    // 获取调用层传入的值进行设置
    public static void setViewLinearLayoutParam(View view, int width,int height,int topMargin,int bottomMargin,int leftMargin,int rightMargin){
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if(width!= LinearLayout.LayoutParams.MATCH_PARENT && width!=LinearLayout.LayoutParams.WRAP_CONTENT){
//            layoutParams.width = UIUtils.getInstance().getWidth(width);
        }
    }
}
