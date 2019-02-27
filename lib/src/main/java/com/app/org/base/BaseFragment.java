package com.app.org.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.app.org.utils.BaseUtils;
import com.app.org.view.BaseTitle;
import com.gyf.barlibrary.ImmersionBar;
import com.zhy.autolayout.utils.AutoUtils;


public abstract class BaseFragment extends Fragment {

    protected BaseActivity mActivity;
    protected View view = null;
    protected Context baseContext = null;
    protected int resId = 0;
    protected ImmersionBar mImmersionBar;

    protected BaseFragment(){
    }

    protected BaseFragment(@LayoutRes int resId){
        this.resId = resId;
    }

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T find(@IdRes int id) {
        if(view!=null){
            return (T) view.findViewById(id);
        }else{
            return null;
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        baseContext = context;
        mActivity = (BaseActivity)getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            hasBundle(args);
        }
    }
    protected void hasBundle(Bundle args){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if(null == view){
            view = inflater.inflate(resId,container,false);
            AutoUtils.autoSize(view);
        }
        return view;
    }

    protected abstract void firstInitViews(View view);

    protected boolean isFirstVisible = true;
    protected boolean isFirstInvisible = true;
    protected boolean isPrepared;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initPrepare();
    }

    private synchronized void initPrepare() {
        if (isPrepared) {

            mImmersionBar = ImmersionBar.with(this);
            changeBarDark();

            firstInitViews(view);
            onFirstUserVisible();
        } else {
            isPrepared = true;
        }
    }

    protected void changeBarDark(){
        mImmersionBar.statusBarDarkFont(true, 0.2f);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            if (isFirstVisible) {
                isFirstVisible = false;
                initPrepare();
            } else {
                onUserVisible();
            }
        } else {
//            onUserInvisible();
            if (isFirstInvisible) {
                isFirstInvisible = false;
//                onFirstUserInvisible();
            } else {
                onUserInvisible();
            }
        }
    }

    protected abstract void onFirstUserVisible();
    protected abstract void onUserVisible();
    //    protected void onFirstUserInvisible() { }
    protected abstract void onUserInvisible();


    public void reshow(){
        if(isPrepared && getUserVisibleHint()){
            onUserVisible();
        }
    }

    public void hide(){
        if(isPrepared && getUserVisibleHint()){
            onUserInvisible();
        }
    }

    @Override
    public void onDestroy() {
        DetoryViewAndThing();
        super.onDestroy();
    }
    protected abstract void DetoryViewAndThing();


    /**
     * Setup the BaseTitle.
     *
     * @param baseTitle
     * @param hideTitle 是否隐藏Title
     */
    protected void setupTitleLayout(BaseTitle baseTitle, boolean hideTitle){
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
     * 获取宿主Activity
     *
     * @return BaseActivity
     */
    protected BaseActivity getHoldingActivity() {
        return mActivity;
    }


    /**
     * 添加fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void addFragment(BaseFragment fragment, @IdRes int frameId) {
        BaseUtils.checkNotNull(fragment);
        getHoldingActivity().addFragment(fragment, frameId);

    }


    /**
     * 替换fragment
     *
     * @param fragment
     * @param frameId
     */
    protected void replaceFragment(BaseFragment fragment, @IdRes int frameId) {
        BaseUtils.checkNotNull(fragment);
        getHoldingActivity().replaceFragment(fragment, frameId);
    }


    /**
     * 隐藏fragment
     *
     * @param fragment
     */
    protected void hideFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getHoldingActivity().hideFragment(fragment);
    }


    /**
     * 显示fragment
     *
     * @param fragment
     */
    protected void showFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getHoldingActivity().showFragment(fragment);
    }


    /**
     * 移除Fragment
     *
     * @param fragment
     */
    protected void removeFragment(BaseFragment fragment) {
        BaseUtils.checkNotNull(fragment);
        getHoldingActivity().removeFragment(fragment);

    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        getHoldingActivity().popFragment();
    }

}
