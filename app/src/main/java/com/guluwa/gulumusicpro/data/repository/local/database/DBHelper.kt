package com.guluwa.gulumusicpro.data.repository.local.database

import android.content.Context
import androidx.room.Room

/**
 * Created by guluwa on 2018/1/3.
 */

class DBHelper {

    lateinit var guluMusicDataBase: GuluMusicProDataBase

    /**
     * 数据库初始化
     *
     * @param context
     */
    fun initDataBase(context: Context) {
        guluMusicDataBase = Room.databaseBuilder(context, GuluMusicProDataBase::class.java, DATABASE_NAME).build()
    }

    object SingletonHolder {
        //单例（静态内部类）
        val instance = DBHelper()
    }

    companion object {

        fun getInstance() = SingletonHolder.instance

        private const val DATABASE_NAME = "gulu_music_pro_database"
    }
}
