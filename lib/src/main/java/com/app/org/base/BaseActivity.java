package com.app.org.base;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.app.org.R;
import com.app.org.broadcast.BaseBroadcastUtil;
import com.app.org.utils.BaseUtils;
import com.app.org.view.BaseTitle;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * <p>Activity基类 </p>
 *
 * @name BaseActivity
 */
public abstract class BaseActivity extends AutoLayoutActivity{

    protected Context baseContext;
    protected BaseActivity activity;
    protected Bundle savedInstanceState;
    public BaseBroadcastUtil baseBroadcastUtil;
    // 状态栏处理工具类
    protected ImmersionBar mImmersionBar;

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T find(@IdRes int id) {
        return (T) super.findViewById(id);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseContext = this;
        activity = this;
        BaseViewManager.getInstance().addActivity(this);
        baseBroadcastUtil = new BaseBroadcastUtil(System.currentTimeMillis());
        this.savedInstanceState = savedInstanceState;

        mImmersionBar = ImmersionBar.with(this);
        changeBarDark();

        if(null!=savedInstanceState){
            hasBundle(savedInstanceState);
        }else{
            initActivity();
        }
    }

    /**
     * 改变状态栏字体颜色为深色
     */
    public void changeBarDark(){
        //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
        mImmersionBar.statusBarDarkFont(true, 0.2f)
                .init();
    }

    /**
     * 如果有 savedInstanceState 执行的方法 。  一般Activity被回收之后 在onSaveInstanceState方法中保存Bundle 信息，
     * 然后在onCreate方法中savedInstanceState就会接收这个bundle
     * @param savedInstanceState
     * @return
     */
    protected void hasBundle(Bundle savedInstanceState){
        initActivity();
    }

    /**
     * 当 activity设置
     * 1.launchmode="singleTask"，并且activity已经存在；
     * 2."singleTop" 时，并且activity已经存在栈顶；
     * @param intent
     */
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);//must store the new intent unless getIntent() will return the old one
//        initActivity();
    }

    protected void initActivity(){
        this.onCreateBefore(savedInstanceState);
        this.initDefaultData(getIntent()); // 获取参数
        this.setRootView(); // 设置布局文件
        this.initView();   // 初始化控件
        this.getData(); // 获取数据
        this.registerReceivers(); // 注册广播
        this.doAction(); // 其他的操作
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        baseContext = null;
        activity = null;
        BaseViewManager.getInstance().finishActivity(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    /**
     *  onCreate方法
     */
    public void onCreateBefore(Bundle savedInstanceState){

    }

    /**
     * 设置布局文件
     */
    public abstract void setRootView();

    /**
     * 初始化控件
     */
    public abstract void initView();

    /**
     * 获取网络数据
     */
    public abstract void getData();

    /**
     * 获取参数
     */
    public abstract void initDefaultData(Intent intent);

    /**
     * 注册广播
     */
    public void registerReceivers(){}


    /**
     * 其他的操作
     */
    public void doAction(){}


    @Override
    public void finish() {
        baseBroadcastUtil.clearReceiver();
        super.finish();
    }

    /**
     * Setup the toolbar.
     *
     * @param toolbar   toolbar
     * @param hideTitle 是否隐藏Title
     */
    protected void setupToolBar(Toolbar toolbar, boolean hideTitle) {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.mipmap.ic_arrow_back);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowHomeEnabled(true);
            if (hideTitle) {
                //隐藏Title
                actionBar.setDisplayShowTitleEnabled(false);
            }
        }
    }

    /**
     * Setup the BaseTitle.
     *
     * @param baseTitle
     * @param hideTitle 是否隐藏Title
     */
    protected void setupTitleLayout(BaseTitle baseTitle,boolean hideTitle){
        if (baseTitle != null) {
            if (hideTitle) {
                //隐藏Title
                baseTitle.setVisibility(View.GONE);
            }else{
                baseTitle.setVisibility(View.VISIBLE);
            }
        }
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .add(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 替换fragment
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .replace(frameId, fragment, fragment.getClass().getSimpleName())
                .addToBackStack(fragment.getClass().getSimpleName())
                .commitAllowingStateLoss();

    }


    /**
     * 隐藏fragment
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .hide(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 显示fragment
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .show(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 移除fragment
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getSupportFragmentManager().beginTransaction()
                .remove(fragment)
                .commitAllowingStateLoss();

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }


}
