package com.guluwa.gulumusicpro.ui.main.home

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.base.LazyFragment
import com.guluwa.gulumusicpro.ui.main.MainActivity
import com.guluwa.gulumusicpro.utils.ATHUtil
import com.guluwa.gulumusicpro.utils.AppUtils
import com.guluwa.gulumusicpro.utils.ColorUtil

abstract class AbsBannerHomeFragment : LazyFragment() {

    val mainActivity: MainActivity get() = activity as MainActivity

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)


        mainActivity.hideStatusBar()
    }

    fun setStatusbarColorAuto(view: View) {
        // we don't want to use statusbar color because we are doing the color darkening on our own to support KitKat
        if (AppUtils.hasMarshmallow()) {
            setStatusbarColor(view, ATHUtil.resolveColor(context, R.attr.colorPrimary, Color.parseColor("#455A64")))
        } else {
            setStatusbarColor(view, ColorUtil.darkenColor(ATHUtil.resolveColor(context, R.attr.colorPrimary, Color.parseColor("#455A64"))))
        }
    }

    private fun setStatusbarColor(view: View, color: Int) {
        val statusBar = view.findViewById<View>(R.id.status_bar)
        if (statusBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBar.setBackgroundColor(color)
                mainActivity.setLightStatusbarAuto(color)
            } else {
                statusBar.setBackgroundColor(color)
            }
        }
    }
}