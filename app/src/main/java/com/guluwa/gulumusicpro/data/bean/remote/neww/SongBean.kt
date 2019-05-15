package com.guluwa.gulumusicpro.data.bean.remote.neww

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
open class SongBean(
    val id: Int,
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
        val emptySong = SongBean(-1, "", -1, -1, -1, "", -1, -1, "", "", -1, "")
    }
}