package com.guluwa.gulumusicpro.data.repository.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.guluwa.gulumusicpro.data.bean.remote.neww.SongBean
import com.guluwa.gulumusicpro.data.bean.remote.old.*
import com.guluwa.gulumusicpro.data.repository.local.dao.SongsDao

/**
 * Created by guluwa on 2018/1/12.
 */

@Database(entities = [(SongBean::class), (SongWordBean::class),
    (SongPathBean::class), (LocalSongBean::class), (SearchHistoryBean::class)], version = 1, exportSchema = false)
abstract class GuluMusicProDataBase : RoomDatabase() {

    abstract val songsDao: SongsDao
}
