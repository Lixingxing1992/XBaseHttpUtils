package com.app.org.http.interfaces

import com.app.org.http.BaseHttpModel
import com.app.org.http.Response

/**
 * Created by lixingxing on 2018/5/10.
 */
abstract class IHttpService {
    var isShowLog = true
    var tag = "IHttpService"
    abstract fun request(baseHttpModel: BaseHttpModel): Response

}
