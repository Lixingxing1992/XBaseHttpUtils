package com.xhttp.org

import android.app.ProgressDialog
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.xhttp.lib.BaseHttpUtils
import com.xhttp.lib.BaseResult
import com.xhttp.lib.callback.HttpResultCallBack
import com.xhttp.lib.config.BaseErrorInfo
import com.xhttp.lib.config.BaseHttpConfig
import com.xhttp.lib.impl.data.DefaultDataListener
import com.xhttp.lib.impl.data.JsonDataListener
import com.xhttp.lib.impl.data.TDDataListener
import com.xhttp.lib.impl.service.DefaultHttpService
import com.xhttp.lib.impl.service.JsonHttpService
import com.xhttp.lib.impl.service.TDHttpService
import com.xhttp.lib.interfaces.data.IDataListener
import com.xhttp.lib.interfaces.http.IHttpService
import com.xhttp.lib.util.BaseThreadPoolUtil
import com.xhttp.org.model.AchievnmentModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var httpTypeId = 0
    var listView = ArrayList<View>()

    var httpService: IHttpService = DefaultHttpService()
    var dataListener: IDataListener = DefaultDataListener()

    var isDialogDismiss = true
    var isDialogDismissWhenSuccess = true
    var isDialogDismissWhenEmpty = true
    var isDialogDismissWhenFail = true

    var isShow = true
    var isShowError = true
    var isShowEmpty = true
    var isShowSuccess = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val waitingDialog = ProgressDialog(this@MainActivity)
        waitingDialog.setTitle("我是一个Dialog")
        waitingDialog.setMessage("等待中...")
        waitingDialog.setIndeterminate(true)


        params.visibility = View.VISIBLE
        paramsBtn.visibility = View.VISIBLE

        httpTypeId = R.id.radioButton
        url.setText(postHttpUrl)

        radio.setOnCheckedChangeListener { radioGroup, i ->
            httpTypeId = i
            when (i) {
                R.id.radioButton -> {
                    url.setText(postHttpUrl)

                    for (entry in map) {
                        var view = layoutInflater.inflate(R.layout.item_add_param, null)
                        params.addView(view)
                        listView.add(view)
                        (view.findViewById(R.id.keys) as EditText).setText(entry.key)
                        (view.findViewById(R.id.values) as EditText).setText(entry.value.toString())
                    }

                    params.visibility = View.VISIBLE
                    paramsBtn.visibility = View.VISIBLE
                }
                R.id.radioButton2 -> {
                    url.setText(getHttpUrl)
                    params.removeAllViews()
                    params.visibility = View.GONE
                    paramsBtn.visibility = View.GONE
                }
            }
        }

        radioService.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.service1 -> {
                    httpService = DefaultHttpService()
                }
                R.id.service2 -> {
                    httpService = JsonHttpService()
                }
                R.id.service3 ->{
                    httpService = TDHttpService()
                }
            }
        }

        radioData.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.data1 -> {
                    dataListener = DefaultDataListener()
                }
                R.id.data2 -> {
                    dataListener = JsonDataListener()
                }
                R.id.data3 ->{
                    dataListener = TDDataListener()
                }
            }
        }

        radioDialog.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.dialog1 -> {
                    isDialogDismiss = true
                    isDialogDismissWhenSuccess = true
                    isDialogDismissWhenEmpty = true
                    isDialogDismissWhenFail = true
                }
                R.id.dialog2 -> {
                    isDialogDismiss = true
                    isDialogDismissWhenSuccess = false
                    isDialogDismissWhenEmpty = true
                    isDialogDismissWhenFail = true
                }
                R.id.dialog3 ->{
                    isDialogDismiss = true
                    isDialogDismissWhenSuccess = true
                    isDialogDismissWhenEmpty = false
                    isDialogDismissWhenFail = true
                }
                R.id.dialog4 ->{
                    isDialogDismiss = true
                    isDialogDismissWhenSuccess = true
                    isDialogDismissWhenEmpty = true
                    isDialogDismissWhenFail = false
                }
            }
        }

        radioShow.setOnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.show1 -> {
                    isShow = true
                    isShowError = true
                    isShowEmpty = true
                    isShowSuccess = false
                }
                R.id.show2 -> {
                    isShow = true
                    isShowError = false
                    isShowEmpty = true
                    isShowSuccess = false
                }
                R.id.show3 ->{
                    isShow = true
                    isShowError = true
                    isShowEmpty = false
                    isShowSuccess = false
                }
                R.id.show4 ->{
                    isShow = true
                    isShowError = true
                    isShowEmpty = true
                    isShowSuccess = true
                }
                R.id.show5 ->{
                    isShow = false
                    isShowError = true
                    isShowEmpty = true
                    isShowSuccess = true
                }
            }
        }


        http.setOnClickListener {
            waitingDialog.show()
            result.text = ""
            Handler().postDelayed({
                when (httpTypeId) {
                    // Post
                    R.id.radioButton -> {
                        var map = HashMap<String, String>()
                        for (view in listView) {
                            map.put((view.findViewById(R.id.keys) as EditText).text.toString(), (view.findViewById(R.id.values) as EditText).text.toString())
                        }
                        BaseHttpUtils(waitingDialog)
                                .initUrl(url.text.toString())
                                .initIHttpService(httpService)
                                .initIDataListener(dataListener)
                                .initParams(map)
                                .initClass(AchievnmentModel::class.java)
                                .initDataParseType(BaseHttpConfig.DataParseType.Combination)
                                .initDialogDismiss(isDialogDismiss)
                                .initDialogDismissWhenSuccess(isDialogDismissWhenSuccess)
                                .initDialogDismissWhenEmpty(isDialogDismissWhenEmpty)
                                .initDialogDismissWhenFail(isDialogDismissWhenFail)
                                .initShowMessage(isShow)
                                .initShowErrorMessage(isShowError)
                                .initShowEmptyMessage(isShowEmpty)
                                .initShowSuccessMessage(isShowSuccess)
                                .initIDataListenerFilter {
                                    (it as TDDataListener).setResultCodes(mapOf("count" to BaseHttpConfig.DataParseType.String,"list" to BaseHttpConfig.DataParseType.List))
                                }
                                .initIMessageManagerFilter {

                                }
                                .initHttpResultCallBack(object : HttpResultCallBack() {
                                    override fun onSuccess(baseResult: BaseResult) {
                                        result.text = baseResult.getResult().resultData
                                    }
                                    override fun onEmpty(errorInfo: BaseErrorInfo) {
                                        result.text = errorInfo.errorMsg
                                    }
                                    override fun onFail(errorInfo: BaseErrorInfo) {
                                        result.text = errorInfo.errorMsg
                                    }
                                })
                                .post()
                    }
                    // Get
                    R.id.radioButton2 -> {
                        BaseThreadPoolUtil.execute {
                            BaseHttpUtils(waitingDialog)
                                    .initUrl(url.text.toString())
                                    .initIHttpService(httpService)
                                    .initIDataListener(dataListener)
                                    .initDialogDismiss(isDialogDismiss)
                                    .initDialogDismissWhenSuccess(isDialogDismissWhenSuccess)
                                    .initDialogDismissWhenEmpty(isDialogDismissWhenEmpty)
                                    .initDialogDismissWhenFail(isDialogDismissWhenFail)
                                    .initShowMessage(isShow)
                                    .initShowErrorMessage(isShowError)
                                    .initShowEmptyMessage(isShowEmpty)
                                    .initShowSuccessMessage(isShowSuccess)
                                    .initHttpResultCallBack(object : HttpResultCallBack() {
                                        override fun onSuccess(baseResult: BaseResult) {
                                            var resultStr = baseResult.getResult().result_str
                                            result.text = resultStr
                                        }
                                        override fun onFail(errorInfo: BaseErrorInfo) {
                                            result.text = errorInfo.errorMsg
                                        }
                                    })
                                    .get()
                        }
                    }
                }
            },2000)
        }

        addParams.setOnClickListener {
            var view = layoutInflater.inflate(R.layout.item_add_param, null)
            params.addView(view)
            listView.add(view)
        }
        removeParams.setOnClickListener {
            if (!listView.isEmpty()) {
                var view = listView.last()
                params.removeView(view)
                listView.remove(view)
            }
        }

        for (entry in map) {
            var view = layoutInflater.inflate(R.layout.item_add_param, null)
            params.addView(view)
            listView.add(view)
            (view.findViewById(R.id.keys) as EditText).setText(entry.key)
            (view.findViewById(R.id.values) as EditText).setText(entry.value.toString())
        }


    }


    var postHttpUrl = "http://103.10.3.77:59527/yuyuan-interact/achievement/userAchievement"//"http://www.xiaooo.club:8080/tangdao/api/banner/getBannerByType"
    var getHttpUrl = "http://103.10.3.77:59527/yuyuan-resource/user/randPage/261/30"
    var paramsStr = ""
    var map = mapOf("mobile" to "18260031044")//mapOf("type" to "50")//
}
