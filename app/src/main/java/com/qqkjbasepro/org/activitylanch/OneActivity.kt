package com.qqkjbasepro.org.activitylanch

import android.content.Intent
import android.os.Bundle
import android.view.View

import com.app.org.utils.BaseLogUtil
import com.qqkjbasepro.org.R
import com.zhy.autolayout.AutoLayoutActivity
import kotlinx.android.synthetic.main.text_activity.*

/**
 * Created by lixingxing on 2018/4/22.
 */
class OneActivity : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_activity)
        btn_go.text = "OneActivity->2"
        BaseLogUtil.e("activitytest", "OneActivity onCreate")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        BaseLogUtil.e("activitytest", "OneActivity onNewIntent")
    }

    fun onBtnClick(view: View) {
        if (view.id == R.id.btn_go) {
            startActivity(Intent(this, TwoActivity::class.java))
        }
    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        BaseLogUtil.e("activitytest", "OneActivity onSaveInstanceState")
    }
}
