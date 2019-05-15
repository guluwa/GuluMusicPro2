package com.guluwa.gulumusicpro.data.repository.total

import androidx.lifecycle.LiveData
import com.guluwa.gulumusicpro.data.bean.remote.neww.SongBean
import com.guluwa.gulumusicpro.data.bean.remote.old.*
import com.guluwa.gulumusicpro.data.repository.local.LocalSongsDataSource
import com.guluwa.gulumusicpro.data.repository.remote.RemoteSongsDataSource
import com.guluwa.gulumusicpro.utils.AppUtils
import com.guluwa.gulumusicpro.utils.interfaces.OnResultListener

import java.io.File

/**
 * Created by guluwa on 2018/1/12.
 */

class SongsRepository {

    /**
     * 服务端数据
     */
    private val remoteSongsDataSource = RemoteSongsDataSource.getInstance()

    /**
     * 本地数据
     */
    private val localSongsDataSource = LocalSongsDataSource.getInstance()

    /**
     * 查询热门歌曲
     *
     * @param isFirstComing
     * @return
     */
    fun queryNetCloudHotSong(): LiveData<ViewDataBean<List<SongBean>>> {
        return remoteSongsDataSource.queryNetCloudHotSong()
    }

    /**
     * 查询本地歌曲
     *
     * @return
     */
    fun queryLocalSong(): LiveData<ViewDataBean<List<LocalSongBean>>> {
        return localSongsDataSource.queryLocalSong()
    }

    /**
     * 歌曲搜索
     *
     * @return
     */
    fun searchSongByKeyWord(freshBean: FreshBean): LiveData<ViewDataBean<List<SearchResultSongBean>>> {
        return remoteSongsDataSource.searchSongByKeyWord(freshBean)
    }

    /**
     * 查询歌曲路径(首页)
     *
     * @param song
     * @return
     */
    fun querySongPath(song: TracksBean, listener: OnResultListener<SongPathBean>) {
        if (AppUtils.isNetConnected) {
            remoteSongsDataSource.querySongPath(song, listener)
        } else {
            localSongsDataSource.querySongPath(song, listener)
        }
    }

    /**
     * 查询歌曲歌词(首页)
     *
     * @param song
     * @return
     */
    fun querySongWord(song: TracksBean, listener: OnResultListener<SongWordBean>) {
        if (AppUtils.isNetConnected) {
            remoteSongsDataSource.querySongWord(song, listener)
        } else {
            localSongsDataSource.querySongWord(song, listener)
        }
    }

    /**
     * 查询歌曲封面图（搜索）
     */
    fun querySongPic(song: TracksBean, listener: OnResultListener<SongPathBean>) {
        remoteSongsDataSource.querySearchSongPic(song, listener)
    }

    /**
     * 歌曲下载
     *
     * @param songPathBean
     * @param songName
     * @param listener
     */
    fun downloadSongFile(songPathBean: SongPathBean, songName: String, listener: OnResultListener<File>) {
        remoteSongsDataSource.downloadSongFile(songPathBean, songName, listener)
    }

    object SingletonHolder {
        //单例（静态内部类）
        val instance = SongsRepository()
    }

    companion object {

        fun getInstance() = SingletonHolder.instance
    }
}
