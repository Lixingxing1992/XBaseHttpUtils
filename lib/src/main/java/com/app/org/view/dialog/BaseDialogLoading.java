package com.app.org.view.dialog;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.app.org.R;
import com.app.org.view.BaseToast;
import com.app.org.view.progressbar.progressing.SpinKitView;

/**
 * Created by lixingxing on 2017/3/16.
 */

public class BaseDialogLoading extends BaseDialog {

    private SpinKitView mLoadingView;
    private View mDialogContentView;
    private TextView mTextView;

    public BaseDialogLoading(Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    public BaseDialogLoading(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        initView(context);
    }

    public BaseDialogLoading(Context context) {
        super(context);
        initView(context);
    }

    public BaseDialogLoading(Activity context) {
        super(context);
        initView(context);
    }

    public BaseDialogLoading(Context context, float alpha, int gravity) {
        super(context, alpha, gravity);
        initView(context);
    }

    private void initView(Context context) {
        mDialogContentView = LayoutInflater.from(context).inflate(R.layout.dialog_loading_spinkit, null);
        mLoadingView = (SpinKitView) mDialogContentView.findViewById(R.id.spin_kit);
        mTextView = (TextView) mDialogContentView.findViewById(R.id.name);
        setContentView(mDialogContentView);
    }

    public BaseDialogLoading setLoadingText(CharSequence charSequence) {
        mTextView.setText(charSequence);
        return this;
    }

    public BaseDialogLoading setLoadingColor(int color){
        mLoadingView.setColor(color);
        return this;
    }

    public void cancel(BaseCancelType code, String str) {
        cancel();
        switch (code) {
            case normal:
                BaseToast.normal(str);
                break;
            case error:
                BaseToast.error(str);
                break;
            case success:
                BaseToast.success(str);
                break;
            case info:
                BaseToast.info(str);
                break;
            default:
                BaseToast.normal(str);
                break;
        }
    }

    public SpinKitView getLoadingView() {
        return mLoadingView;
    }

    public View getDialogContentView() {
        return mDialogContentView;
    }

    public TextView getTextView() {
        return mTextView;
    }

    public enum BaseCancelType {normal, error, success, info}


    public BaseDialogLoading showDialog(){
        show();
        return this;
    }

    public BaseDialogLoading showDialog(String text){
        setLoadingText(text).show();
        return this;
    }
}
