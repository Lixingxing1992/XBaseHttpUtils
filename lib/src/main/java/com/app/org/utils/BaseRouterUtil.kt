package com.app.org.utils

import android.app.Activity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter

/**
 * 路由启动工具类
 * Created by lixingxing on 2018/4/23.
 */

fun startRouterActivity(activityName:String) {
    ARouter.getInstance().build(activityName).navigation()
}
fun startRouterActivity(activityName:String,bundle: Bundle) {
    ARouter.getInstance().build(activityName).with(bundle).navigation()
}

fun startRouterActivityResult(activity: Activity,requestCode:Int, activityName:String) {
    ARouter.getInstance().build(activityName).navigation(activity, requestCode)
}

fun createRouterFragment(fragmentName:String) : Fragment {
    return ARouter.getInstance().build(fragmentName).navigation() as Fragment
}

fun startRouterActivity(activityName:String,iInterceptor: IInterceptor) {
    ARouter.getInstance().build(activityName).navigation()
}





/*
// 构建标准的路由请求
ARouter.getInstance().build("/home/main").navigation();

// 构建标准的路由请求，并指定分组
ARouter.getInstance().build("/home/main", "ap").navigation();

// 构建标准的路由请求，通过Uri直接解析
Uri uri;
ARouter.getInstance().build(uri).navigation();

// 构建标准的路由请求，startActivityForResult
// navigation的第一个参数必须是Activity，第二个参数则是RequestCode
ARouter.getInstance().build("/home/main", "ap").navigation(this, 5);

// 直接传递Bundle
Bundle params = new Bundle();
ARouter.getInstance()
.build("/home/main")
.with(params)
.navigation();

// 指定Flag
ARouter.getInstance()
.build("/home/main")
.withFlags();
.navigation();

// 获取Fragment
Fragment fragment = (Fragment) ARouter.getInstance().build("/test/fragment").navigation();

// 对象传递
ARouter.getInstance()
.withObject("key", new TestObj("Jack", "Rose"))
.navigation();

// 觉得接口不够多，可以直接拿出Bundle赋值
ARouter.getInstance()
.build("/home/main")
.getExtra();

// 转场动画(常规方式)
ARouter.getInstance()
.build("/test/activity2")
.withTransition(R.anim.slide_in_bottom, R.anim.slide_out_bottom)
.navigation(this);

// 转场动画(API16+)
ActivityOptionsCompat compat = ActivityOptionsCompat.
makeScaleUpAnimation(v, v.getWidth() / 2, v.getHeight() / 2, 0, 0);

// ps. makeSceneTransitionAnimation 使用共享元素的时候，需要在navigation方法中传入当前Activity

ARouter.getInstance()
.build("/test/activity2")
.withOptionsCompat(compat)
.navigation();

// 使用绿色通道(跳过所有的拦截器)
ARouter.getInstance().build("/home/main").greenChannel().navigation();

// 使用自己的日志工具打印日志
ARouter.setLogger();
*/