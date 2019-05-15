package com.guluwa.gulumusicpro.data.repository.remote

import androidx.lifecycle.LiveData
import com.guluwa.gulumusicpro.data.bean.remote.neww.SongBean
import com.guluwa.gulumusicpro.data.bean.remote.old.*
import com.guluwa.gulumusicpro.data.repository.local.LocalSongsDataSource
import com.guluwa.gulumusicpro.data.repository.remote.LiveDataObservableAdapter.fromObservableViewData
import com.guluwa.gulumusicpro.data.repository.remote.retrofit.RetrofitWorker
import com.guluwa.gulumusicpro.data.repository.total.SongDataSource
import com.guluwa.gulumusicpro.manage.Contacts
import com.guluwa.gulumusicpro.utils.AppUtils
import com.guluwa.gulumusicpro.utils.interfaces.OnResultListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.ResponseBody
import java.io.File
import java.util.*

/**
 * Created by guluwa on 2018/1/12.
 */

class RemoteSongsDataSource : SongDataSource {

    fun queryNetCloudHotSong(): LiveData<ViewDataBean<List<SongBean>>> {
        val map = HashMap<String, String>()
        map["types"] = "playlist"
        map["id"] = Contacts.NET_CLOUD_HOT_ID
        return fromObservableViewData(
            RetrofitWorker.retrofitWorker
                .obtainNetCloudHot(Contacts.SONG_CALLBACK, map)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map<List<SongBean>> { result ->
                    val list = mutableListOf<SongBean>()
                    if (result.playlist != null && result.playlist!!.tracks != null) {
                        for (item in result.playlist!!.tracks!!) {
                            val year = AppUtils.getStrTimeYear(item.publishTime).toInt()
                            var albumId = 0
                            var albumName = ""
                            var albumPic = ""
                            if (item.al != null) {
                                albumId = item.al!!.id
                                albumName = item.al!!.name ?: ""
                                albumPic = item.al!!.picUrl
                            }
                            var artistId = 0
                            var artistName = ""
                            if (item.ar != null && item.ar!!.isNotEmpty()) {
                                artistId = item.ar!![0].id
                                artistName = item.ar!![0].name ?: ""
                            }
                            list.add(
                                SongBean(
                                    item.id.toInt(), item.name, -1, year, -1,
                                    "", item.publishTime, albumId, albumName, albumPic, artistId, artistName
                                )
                            )
                        }
                    }
                    list
                }
                .observeOn(AndroidSchedulers.mainThread())
        )
    }

    fun searchSongByKeyWord(freshBean: FreshBean): LiveData<ViewDataBean<List<SearchResultSongBean>>> {
        val map = HashMap<String, String>()
        map["types"] = "search"
        map["count"] = "20"
        map["source"] = "tencent"
        map["pages"] = freshBean.page.toString()
        map["name"] = freshBean.key
        return fromObservableViewData(
            RetrofitWorker.retrofitWorker
                .searchSongByKeyWord(Contacts.SONG_CALLBACK, map)
                .subscribeOn(Schedulers.io())
                .observeOn(Schedulers.io())
                .map { searchResultSongBeans ->
                    for (i in searchResultSongBeans.indices) {
                        val queryLocalSong = LocalSongsDataSource.getInstance().queryLocalSong(
                            searchResultSongBeans[i].id, searchResultSongBeans[i].name
                        )
                        searchResultSongBeans[i].isDownLoad = queryLocalSong != null
                    }
                    LocalSongsDataSource.getInstance()
                        .addSearchHistory(SearchHistoryBean(AppUtils.getCurrentDate(), freshBean.key))
                    searchResultSongBeans
                }
                .observeOn(AndroidSchedulers.mainThread())
        )
    }

    /**
     * 查询歌曲路径(首页)
     *
     * @param song
     * @return
     */
    override fun querySongPath(song: TracksBean, listener: OnResultListener<SongPathBean>) {
        val map = HashMap<String, String>()
        map["types"] = "url"
        map["id"] = if ("" == song.url_id) song.id else song.url_id
        map["source"] = song.source
        RetrofitWorker.retrofitWorker
            .obtainSongPath(Contacts.SONG_CALLBACK, map)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { songPathBean ->
                songPathBean.song = song
                songPathBean.id = song.id
                LocalSongsDataSource.getInstance().addSongPath(songPathBean)
                songPathBean
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ listener.success(it) }, { listener.failed(it.message!!) })
    }

    /**
     * 查询歌曲歌词(首页)
     *
     * @param song
     * @return
     */
    override fun querySongWord(song: TracksBean, listener: OnResultListener<SongWordBean>) {
        val map = HashMap<String, String>()
        map["types"] = "lyric"
        map["id"] = if ("" == song.lyric_id) song.id else song.lyric_id
        map["source"] = song.source
        RetrofitWorker.retrofitWorker
            .obtainSongWord(Contacts.SONG_CALLBACK, map)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { songWordBean ->
                songWordBean.song = song
                songWordBean.id = song.id
                LocalSongsDataSource.getInstance().addSongWord(songWordBean)
                songWordBean
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ listener.success(it) }, { listener.failed(it.message!!) })
    }

    /**
     * 查询歌曲封面图(搜索)
     *
     * @param song
     * @param listener
     */
    fun querySearchSongPic(song: TracksBean, listener: OnResultListener<SongPathBean>) {
        val map = HashMap<String, String>()
        map["types"] = "pic"
        map["id"] = if ("" == song.pic_id) song.id else song.pic_id
        map["source"] = song.source
        RetrofitWorker.retrofitWorker
            .obtainSongPath(Contacts.SONG_CALLBACK, map)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map { songPathBean ->
                if (LocalSongsDataSource.getInstance().queryLocalSong(song.id, song.name) == null) {
                    song.al!!.picUrl = songPathBean.url
                    LocalSongsDataSource.getInstance().addLocalSong(AppUtils.getLocalSongBean(song))
                } else {
                    println("歌曲已存在")
                }
                songPathBean
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ listener.success(it) }, { listener.failed(it.message!!) })
    }

    /**
     * 歌曲下载
     *
     * @param songPathBean
     * @param songName
     * @param listener
     */
    fun downloadSongFile(songPathBean: SongPathBean, songName: String, listener: OnResultListener<File>) {
        RetrofitWorker.retrofitWorker
            .downloadSongFile(songPathBean.url)
            .subscribeOn(Schedulers.io())
            .observeOn(Schedulers.io())
            .map<ResponseBody> { responseBody ->
                if (LocalSongsDataSource.getInstance().queryLocalSong(
                        songPathBean.id,
                        songPathBean.song!!.name
                    ) == null
                ) {
                    LocalSongsDataSource.getInstance().addLocalSong(AppUtils.getLocalSongBean(songPathBean.song!!))
                } else {
                    println("歌曲已存在")
                }
                responseBody
            }
            .observeOn(Schedulers.io())
            .map { responseBody -> AppUtils.writeSong2Disk(responseBody, songName) }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ listener.success(it!!) }, {
                println(it.message)
                listener.failed("歌曲下载失败")
            })
    }

    object SingletonHolder {
        //单例（静态内部类）
        val instance = RemoteSongsDataSource()
    }

    companion object {

        fun getInstance() = SingletonHolder.instance
    }
}
