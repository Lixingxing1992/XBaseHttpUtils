package com.qqkjbasepro.org.util

import android.content.Intent
import android.os.Bundle
import android.os.Message
import android.view.View
import com.app.org.base.BaseActivity
import com.app.org.http.BaseHttpConfig
import com.app.org.http.BaseHttpUtils
import com.app.org.http.Response
import com.app.org.http.httpservice.*
import com.app.org.http.interfaces.BaseHttpCallBack
import com.app.org.utils.BaseToastUtil
import com.app.org.view.dialog.BaseDialogLoading
import com.app_res.org.util.http.HttpHandlerCallBack
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import com.qqkjbasepro.org.R
import com.qqkjbasepro.org.config.AppConfig
import com.qqkjbasepro.org.model.EventModel
import com.qqkjbasepro.org.model.EventModel2
import kotlinx.android.synthetic.main.text_http.*


/**
 * Created by lixingxing on 2018/4/16.
 */
@ParallaxBack(edge = ParallaxBack.Edge.LEFT, layout = ParallaxBack.Layout.PARALLAX)
class TestHttp : BaseActivity() {
    override fun setRootView() {
    }

    override fun initView() {
    }

    override fun getData() {
    }

    override fun initDefaultData(intent: Intent?) {
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_http)
        setupTitleLayout(baseTitle, false)
        baseTitle.title = "测试"

        BaseHttpUtils.init(TDHttpService::class.java, JsonDataListener::class.java)

    }

    fun onBtnClick(view: View) {
        when (view.id) {
            R.id.btn_get -> {
                val baseDialogLoading = BaseDialogLoading(baseContext)
                        .setLoadingText("正在get请求中...")
                baseDialogLoading.show()

                BaseHttpUtils().initUrl(AppConfig.BaseUrl + "api/activity/getActivityList")
                        .initParams("cardCode", 10001, "page", 10, "size", 50)
                        .initResponseType(BaseHttpConfig.ResponseType.List)
                        .initHttpCallBack(object : BaseHttpCallBack<EventModel>(baseDialogLoading){
                            override fun onSuccess(response: Response?) {
                                var list:List<EventModel> = response!!.getListResponse()
                                BaseToastUtil.showShortToast(list[0].proName)
                            }
                            override fun onError(error: String?, errorType: BaseHttpConfig.ErrorType?) {
                            }
                        })
                        .requst()
            }
            R.id.btn_post -> {
                val baseDialogLoading = BaseDialogLoading(baseContext)
                        .showDialog("正在 post 请求中...")
                BaseHttpUtils().initUrl(AppConfig.BaseUrl + "api/activity/getActivityLists")
                        .initParams("cardCode", 10001, "page", 1, "size", 50)
                        .initResponseType(BaseHttpConfig.ResponseType.List)
                        .initHttpCallBack(object : BaseHttpCallBack<EventModel>(baseDialogLoading){
                            override fun onSuccess(response: Response?) {
                                var list:List<EventModel> = response!!.getListResponse()
                                BaseToastUtil.showShortToast(list[0].proName)
                            }
                            override fun onError(error: String?, errorType: BaseHttpConfig.ErrorType?) {
                                BaseToastUtil.showShortToast(error)
                            }

                        })
                        .requst()
            }
            R.id.btn_xml->{
                val baseDialogLoading = BaseDialogLoading(baseContext)
                        .showDialog("正在 xml 请求中...")
                BaseHttpUtils().initUrl("http://api.aquacity-nj.com:9821/aquacity/modilb/appFunction")
                        .initParams("<opType>getPromoteList</opType><page>10</page><size>10</size>")
                        .initIHttpService(XmlHttpService())
                        .initIDataListener(XmlDataListener())
                        .initResponseType(BaseHttpConfig.ResponseType.List)
                        .initHttpCallBack(object : BaseHttpCallBack<EventModel2>(baseDialogLoading) {
                            override fun onSuccess(response: Response?) {
                                var list:List<EventModel2> = response!!.getListResponse()
                                BaseToastUtil.showShortToast(list[0].promotionName)
                            }
                            override fun onError(error: String?, errorType: BaseHttpConfig.ErrorType?) {
                                BaseToastUtil.showShortToast(error)
                            }
                        })
                        .requst()
            }
            else -> {
            }
        }
    }
}
