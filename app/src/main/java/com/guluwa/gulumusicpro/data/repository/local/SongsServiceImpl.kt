package com.guluwa.gulumusicpro.data.repository.local

import androidx.lifecycle.LiveData
import com.guluwa.gulumusicpro.data.bean.remote.Song
import com.guluwa.gulumusicpro.data.bean.remote.neww.SongBean
import com.guluwa.gulumusicpro.data.bean.remote.old.*
import com.guluwa.gulumusicpro.data.repository.local.database.DBHelper
import io.reactivex.Single

/**
 * Created by guluwa on 2018/1/12.
 */

class SongsServiceImpl : SongsService {

    /**
     * 本地数据库操作
     */
    private val songsDao = DBHelper.getInstance().guluMusicDataBase.songsDao

    /**
     * 查询热门歌曲
     *
     * @return
     */
    override fun queryNetCloudHotSong(): LiveData<List<SongBean>> {
        return songsDao.queryNetCloudHotSong()
    }

    /**
     * 查询本地歌曲
     *
     * @return
     */
    override fun queryLocalSong(): LiveData<List<LocalSongBean>> {
        return songsDao.queryLocalSong()
    }


    /**
     * 查询本地歌曲（单曲）
     *
     * @param id
     * @param name
     * @return
     */
    override fun queryLocalSong(id: String, name: String): LocalSongBean {
        return songsDao.queryLocalSong(id, name)
    }

    /**
     * 查询歌曲路径
     *
     * @param id
     * @return
     */
    override fun querySongPath(id: String): Single<List<SongPathBean>> {
        return songsDao.querySongPath(id)
    }

    /**
     * 查询歌曲歌词
     *
     * @param id
     * @return
     */
    override fun querySongWord(id: String): Single<List<SongWordBean>> {
        return songsDao.querySongWord(id)
    }

    /**
     * 添加歌曲到热门歌曲表
     *
     * @param songs
     */
    override fun addSongs(songs: List<SongBean>) {
        songsDao.addSongs(songs)
    }

    /**
     * 添加歌曲到本地歌曲表
     *
     * @param localSongBean
     */
    override fun addLocalSong(localSongBean: LocalSongBean) {
        songsDao.addLocalSong(localSongBean)
    }

    /**
     * 从本地歌曲表删除歌曲
     *
     * @param localSongBean
     */
    override fun deleteLocalSong(localSongBean: LocalSongBean) {
        songsDao.deleteLocalSong(localSongBean)
    }

    /**
     * 添加歌曲路径
     *
     * @param songPathBean
     */
    override fun addSongPath(songPathBean: SongPathBean) {
        songsDao.addSongPath(songPathBean)
    }

    /**
     * 添加歌曲歌词
     *
     * @param songWordBean
     */
    override fun addSongWord(songWordBean: SongWordBean) {
        songsDao.addSongWord(songWordBean)
    }

    /**
     * 添加新搜索记录
     *
     * @param searchHistoryBean
     */
    override fun addSearchHistory(searchHistoryBean: SearchHistoryBean) {
        songsDao.addSearchHistory(searchHistoryBean)
    }

    /**
     * 查询搜索记录
     *
     * @return
     */
    override fun querySearchRecord(): Single<List<SearchHistoryBean>> {
        return songsDao.querySearchRecord()
    }

    object SingletonHolder {
        //单例（静态内部类）
        val instance = SongsServiceImpl()
    }

    companion object {

        fun getInstance() = SingletonHolder.instance
    }
}
