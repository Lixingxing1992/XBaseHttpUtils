package com.app.org.http.httpservice

import com.app.org.http.*
import com.app.org.http.interfaces.IHttpService

/**
 * Created by lixingxing on 2018/5/10.
 */
class JsonHttpService : IHttpService() {
    override fun request(baseHttpModel: BaseHttpModel): Response {
        val params = baseHttpModel.params.toString()
        val url = baseHttpModel.url
        return RequestUtil()
                .Request_ContentType(BaseHttpConfig.ParamType.JSON)
                .Request_requestType(BaseHttpConfig.RequestType.POST)
                .request(params, url)
    }

}
