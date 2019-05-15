package com.guluwa.gulumusicpro.data.bean.remote.old

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * 本地歌曲类
 *
 * Created by guluwa on 2018/1/31.
 */

@Entity(tableName = "local_songs")
class LocalSongBean : BaseSongBean(), Serializable {

    @PrimaryKey(autoGenerate = true)
    var index: Int = 0

    var id: String = ""

    @Ignore
    var position = 0

    companion object {

        private const val serialVersionUID = 1377109839840074638L
    }
}
