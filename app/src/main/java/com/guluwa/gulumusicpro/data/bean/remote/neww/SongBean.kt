package com.guluwa.gulumusicpro.data.bean.remote.neww

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "net_cloud_hot_songs")
open class SongBean(
        @PrimaryKey
        val id: Int,
        val index: Int,
        val title: String,
        val trackNumber: Int,
        val year: Int,
        val duration: Long,
        val data: String,
        val dateModified: Long,
        val albumId: Int,
        val albumName: String,
        val albumPic: String,
        val artistId: Int,
        val artistName: String
) : Parcelable {

    companion object {
        @JvmStatic
        val emptySong = SongBean(-1, -1,"", -1, -1, -1, "", -1, -1, "", "", -1, "")
    }
}