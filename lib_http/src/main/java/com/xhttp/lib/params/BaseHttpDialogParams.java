package com.xhttp.lib.params;

/**
 * dialog加载框设置
 * @author Lixingxing
 */
public class BaseHttpDialogParams {
    // 是否关闭dialog(如果设置为true,则不管什么情况,一定会关闭dialog)
    private boolean isDismissDialog = true;
    // 当成功的时候是否关闭dialog
    private boolean isDismissDialogWhenSuccess = true;
    // 当失败的是够是否关闭dialog
    private boolean isDismissDialogWhenFail = true;


    public boolean isDismissDialog() {
        return isDismissDialog;
    }

    public BaseHttpDialogParams setDismissDialog(boolean dismissDialog) {
        isDismissDialog = dismissDialog;
        return this;
    }

    public boolean isDismissDialogWhenSuccess() {
        return isDismissDialogWhenSuccess;
    }

    public BaseHttpDialogParams setDismissDialogWhenSuccess(boolean dismissDialogWhenSuccess) {
        isDismissDialogWhenSuccess = dismissDialogWhenSuccess;
        return this;
    }

    public boolean isDismissDialogWhenFail() {
        return isDismissDialogWhenFail;
    }

    public BaseHttpDialogParams setDismissDialogWhenFail(boolean dismissDialogWhenFail) {
        isDismissDialogWhenFail = dismissDialogWhenFail;
        return this;
    }
}
