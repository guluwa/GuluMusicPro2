package com.guluwa.gulumusicpro.manage

import android.app.Application
import android.content.Context
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.data.repository.local.database.DBHelper
import uk.co.chrisjenx.calligraphy.CalligraphyConfig

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        instance = this

        // 字体
        CalligraphyConfig.initDefault(CalligraphyConfig.Builder().setDefaultFont(R.font.circular_std_book).build())
        // 本地数据库
        initDataBase()
    }

    private fun initDataBase() {
        DBHelper.getInstance().initDataBase(this)
    }

    companion object {

        lateinit var instance: MyApplication private set

        val context: Context get() = instance.applicationContext
    }
}