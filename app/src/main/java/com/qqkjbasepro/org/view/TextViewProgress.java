package com.qqkjbasepro.org.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.app.org.base.BaseActivity;
import com.app.org.view.progressbar.BaseNumberProgressBar;
import com.app.org.view.progressbar.BaseProgressBar;
import com.app.org.view.progressbar.BaseRoundProgress;
import com.github.anzewei.parallaxbacklayout.ParallaxBack;
import com.qqkjbasepro.org.R;

/**
 * Created by lixingxing on 2018/3/8.
 */
@ParallaxBack(edge = ParallaxBack.Edge.LEFT,layout = ParallaxBack.Layout.PARALLAX)
public class TextViewProgress extends BaseActivity{
    BaseNumberProgressBar progressBar1;
    BaseProgressBar progressBar2;
    BaseRoundProgress progressBar3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.text_progress);
        progressBar1 = find(R.id.progress);
        progressBar2 = find(R.id.progress2);
        progressBar3 = find(R.id.progress3);

        handler.sendEmptyMessage(0);
    }

    @Override
    public void setRootView() {

    }

    @Override
    public void initView() {

    }

    @Override
    public void getData() {

    }

    @Override
    public void initDefaultData(Intent intent) {

    }

    int pro = 0;
    Handler handlers = new Handler();
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message message) {
            progressBar1.setProgress(pro);
            progressBar2.setProgress(pro);
            progressBar3.setProgress(pro);
            if(pro < 100){
                pro += 1;
                handlers.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler.sendEmptyMessage(0);
                    }
                },500);
            }
            return false;
        }
    });
}
