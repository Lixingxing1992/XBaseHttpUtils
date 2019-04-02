package com.xhttp.org

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.xhttp.lib.BaseHttpUtils
import com.xhttp.lib.BaseResult
import com.xhttp.lib.callback.HttpResultCallBack
import com.xhttp.lib.config.BaseErrorInfo
import com.xhttp.lib.interfaces.IHttpResultCallBack
import com.xhttp.lib.service.JsonHttpService
import com.xhttp.lib.service.TDHttpService
import com.xhttp.org.config.AppConfig
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var httpTypeId = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        params.visibility = View.VISIBLE
        httpTypeId = R.id.radioButton
        url.setText(postHttpUrl)
        radio.setOnCheckedChangeListener { radioGroup, i ->
            httpTypeId = i
            when(i){
                R.id.radioButton->{
                    params.visibility = View.VISIBLE
                }
                R.id.radioButton2->{
                    params.visibility = View.GONE
                }
            }
        }

        http.setOnClickListener {
            when(httpTypeId){
                // Post
                R.id.radioButton->{
                    BaseHttpUtils().initUrl(url.text.toString())
                            .initIHttpService(TDHttpService())
                            .initParams("cardCode","17101309000670","page",1,"size",30)
                            .initHttpResultCallBack(object :HttpResultCallBack(){
                                override fun onSuccess(baseResult: BaseResult) {

                                }
                                override fun onFail(errorInfo: BaseErrorInfo) {
                                }
                            })
                            .post()
                }
                // Get
                R.id.radioButton2->{
                    BaseHttpUtils().initUrl("http://103.10.3.77:59527/yuyuan-resource/user/randPage/261/30")
                            .initHttpResultCallBack(object : HttpResultCallBack() {
                                override fun onSuccess(baseResult: BaseResult) {
                                    var resultStr = baseResult.getResult<String>().result_str
                                    result.text = resultStr
//                                    Toast.makeText(this@MainActivity,
//                                            baseResult.getResult<String>().result_str
//                                            ,Toast.LENGTH_SHORT).show()
                                }

                                override fun onFail(errorInfo: BaseErrorInfo) {
                                    Toast.makeText(this@MainActivity,errorInfo.errorMsg,Toast.LENGTH_SHORT).show()
                                }
                            })
                            .get()
                }
            }
        }
    }


    var postHttpUrl = "http://wechat.hlwmall.com:8080/huilinwan/api/activity/getActivityList"
    var paramsStr = ""
}
