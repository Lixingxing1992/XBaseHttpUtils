package com.xhttp.org;

import android.app.Application;
import android.content.Context;

import com.xhttp.lib.BaseHttpUtils;
import com.xhttp.lib.impl.data.TDDataListener;
import com.xhttp.lib.impl.service.TDHttpService;

import org.acra.ACRA;
import org.acra.collector.CrashReportData;
import org.acra.sender.EmailIntentSender;
import org.acra.sender.ReportSender;
import org.acra.sender.ReportSenderException;

/**
 * <p>这里仅需做一些初始化的工作</p>
 *
 * @name MyApplication
 */
// 发送崩溃日志  注意修改正确的邮箱地址
public class MyApplication extends Application {


    @Override
    public void onCreate() {
        super.onCreate();
        BaseHttpUtils.init(getApplicationContext(),true);
    }

}
