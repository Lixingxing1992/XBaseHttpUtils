package com.qqkjbasepro.org.view

import android.content.Intent
import android.os.Bundle
import android.view.View

import com.app.org.base.BaseActivity
import com.app.org.view.dialog.BaseDialogLoading
import com.github.anzewei.parallaxbacklayout.ParallaxBack
import com.qqkjbasepro.org.R
import kotlinx.android.synthetic.main.text_dialog.*

/**
 * Created by lixingxing on 2018/3/8.
 */
@ParallaxBack(edge = ParallaxBack.Edge.LEFT, layout = ParallaxBack.Layout.PARALLAX)
class TextViewDialog : BaseActivity() {
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
        setContentView(R.layout.text_dialog)
        setupTitleLayout(baseTitle,false)
        baseTitle.title = "测试"
    }

    fun onBtnClick(view: View) {
        when (view.id) {
            R.id.BaseLoadingDialog -> {
                val baseDialogLoading = BaseDialogLoading(baseContext)
                baseDialogLoading.show()
            }
            else -> {
            }
        }
    }
}
