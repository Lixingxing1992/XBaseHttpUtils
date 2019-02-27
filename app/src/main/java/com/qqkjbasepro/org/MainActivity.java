package com.qqkjbasepro.org;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.org.base.BaseActivity;
import com.qqkjbasepro.org.activitylanch.OneActivity;
import com.qqkjbasepro.org.util.TestHttp;
import com.qqkjbasepro.org.view.TextViewDialog;
import com.qqkjbasepro.org.view.TextViewProgress;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        try {
            getClassLoader().loadClass("Test");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

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

    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.progress:
                startActivity(new Intent(baseContext, TextViewProgress.class));
                break;
            case R.id.dialog:
                startActivity(new Intent(baseContext, TextViewDialog.class));
                break;
            case R.id.http:
                startActivity(new Intent(baseContext, TestHttp.class));
                break;
            case R.id.activity:
                startActivity(new Intent(baseContext, OneActivity.class));
                break;
            default:
                break;
        }
    }

}
