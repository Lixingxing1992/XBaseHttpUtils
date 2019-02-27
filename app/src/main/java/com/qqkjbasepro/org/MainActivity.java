package com.qqkjbasepro.org;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.org.base.BaseActivity;
import com.qqkjbasepro.org.util.TestHttp;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
            case R.id.http:
                startActivity(new Intent(baseContext, TestHttp.class));
                break;
            default:
                break;
        }
    }

}
