package com.guluwa.gulumusicpro.data.bean.remote

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize


/**
 * @author Karim Abou Zeid (kabouzeid)
 */
@Parcelize
open class Song(
    val id: Int,
    val title: String,
    val trackNumber: Int,
    val year: Int,
    val duration: Long,
    val data: String,
    val dateModified: Long,
    val albumId: Int,
    val albumName: String,
    val artistId: Int,
    val artistName: String,
    val composer: String?
) : Parcelable {

    companion object {
        @JvmStatic
        val emptySong = Song(-1, "", -1, -1, -1, "", -1, -1, "", -1, "", "")
    }
}