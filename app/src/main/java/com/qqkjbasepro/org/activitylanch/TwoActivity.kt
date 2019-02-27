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
class TwoActivity : AutoLayoutActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.text_activity)
        btn_go.text = "TwoActivity->3"
        BaseLogUtil.e("activitytest", "TwoActivity onCreate")
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        BaseLogUtil.e("activitytest", "TwoActivity onNewIntent")
    }

    fun onBtnClick(view: View) {
        if (view.id == R.id.btn_go) {
            startActivity(Intent(this, ThreeActivity::class.java))
        }
    }
}
