package com.guluwa.gulumusicpro.data.bean.local

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import java.io.Serializable

/**
 * Created by guluwa on 2018/4/2.
 * 页面状态0：加载 1：错误
 */
@Parcelize
class PageTipBean(var tip: String, var status: Int) : Parcelable {
    companion object {
        @JvmStatic
        val loadingItem = PageTipBean("", 0)
    }
}