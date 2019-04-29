package com.xhttp.org

import android.content.Intent
import android.test.mock.MockContext
import android.webkit.URLUtil

import com.xhttp.lib.BaseHttpUtils
import com.xhttp.lib.BaseResult
import com.xhttp.lib.callback.HttpResultCallBack
import com.xhttp.lib.config.BaseHttpConfig
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
        var context = MockContext()
        BaseHttpUtils.init(context, true)
        BaseHttpUtils.init(YGHttpService::class.java, TDDataListener::class.java)
    }
}
