package com.xhttp.org

import android.content.Intent
import android.webkit.URLUtil

import com.xhttp.lib.BaseHttpUtils
import com.xhttp.lib.BaseResult
import com.xhttp.lib.callback.HttpResultCallBack
import com.xhttp.lib.impl.data.TDDataListener
import com.xhttp.lib.impl.service.TDHttpService
import com.xhttp.lib.impl.service.YGHttpService
import com.xhttp.lib.interfaces.IHttpService

import org.junit.Test

/**
 * Created by lixingxing on 2019/4/12.
 */
class ExampleTest {
    @Test
    fun testClass() {
        BaseHttpUtils.init(YGHttpService::class.java, TDDataListener::class.java)
        BaseHttpUtils()
                .initUrl("http://103.10.3.77:59527/yuyuan-store/activity/receiveActivityCoupon")
                .initParams("activityId", 0, "couponId", 0, "type", 1, "couponType", 1,
                        "userTel", "13213","memberId","123123")
                .initSuccessMsg("成功")
                .initShowSuccessMessage(true)
                .initHttpResultCallBack(object:HttpResultCallBack(){
                    override fun onSuccess(baseResult: BaseResult?) {
                        super.onSuccess(baseResult)
                    }
                })
        .post()
    }
}
