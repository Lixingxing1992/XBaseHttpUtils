package com.xhttp.org

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.xhttp.lib.BaseHttpUtils
import com.xhttp.lib.BaseResult
import com.xhttp.lib.callback.HttpResultCallBack
import com.xhttp.lib.config.BaseErrorInfo
import com.xhttp.lib.config.BaseHttpConfig
import com.xhttp.lib.impl.data.TDDataListener
import com.xhttp.lib.impl.service.TDHttpService
import com.xhttp.org.model.EventModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var httpTypeId = 0
    var listView = ArrayList<View>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        params.visibility = View.VISIBLE
        paramsBtn.visibility = View.VISIBLE

        httpTypeId = R.id.radioButton
        url.setText(postHttpUrl)
        radio.setOnCheckedChangeListener { radioGroup, i ->
            httpTypeId = i
            when (i) {
                R.id.radioButton -> {
                    params.visibility = View.VISIBLE
                    paramsBtn.visibility = View.VISIBLE
                }
                R.id.radioButton2 -> {
                    params.removeAllViews()
                    params.visibility = View.GONE
                    paramsBtn.visibility = View.GONE
                }
            }
        }

        http.setOnClickListener {
            when (httpTypeId) {
                // Post
                R.id.radioButton -> {
                    var map = HashMap<String, String>()
                    for (view in listView) {
                        map.put((view.findViewById(R.id.keys) as EditText).text.toString(), (view.findViewById(R.id.values) as EditText).text.toString())
                    }
                    BaseHttpUtils().initUrl(url.text.toString())
                            .initIHttpService(TDHttpService())
                            .initIDataListener(TDDataListener())
                            .initParams(map)
                            .initClass(EventModel::class.java)
                            .initResponseType(BaseHttpConfig.ResponseType.List)
                            .initHttpResultCallBack(object : HttpResultCallBack() {
                                override fun onSuccess(baseResult: BaseResult) {
                                    var list = baseResult.getResult().getResult_list<EventModel>()
                                    var resultStr = ""
                                    for (eventModel in list) {
                                        resultStr +=  eventModel.toString() + "\n"
                                    }
                                    result.text = resultStr
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
                    BaseHttpUtils().initUrl("http://103.10.3.77:59527/yuyuan-resource/user/randPage/261/30")
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


    var postHttpUrl = "http://wechat.hlwmall.com:8080/huilinwan/api/activity/getActivityList"
    var paramsStr = ""
    var map = mapOf("cardCode" to "17101309000670", "page" to 1, "size" to 30)
}
