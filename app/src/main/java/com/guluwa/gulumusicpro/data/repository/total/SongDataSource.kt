package com.guluwa.gulumusicpro.data.repository.total

import com.guluwa.gulumusicpro.data.bean.remote.old.SongPathBean
import com.guluwa.gulumusicpro.data.bean.remote.old.SongWordBean
import com.guluwa.gulumusicpro.data.bean.remote.old.TracksBean
import com.guluwa.gulumusicpro.utils.interfaces.OnResultListener

/**
 * Created by guluwa on 2018/1/12.
 */

interface SongDataSource {

    /**
     * 查询热门歌曲
     *
     * @return
     */
//    fun queryNetCloudHotSong(): LiveData<ViewDataBean<List<SongBean>>>

    /**
     * 查询歌曲路径(首页)
     *
     * @param song
     * @param listener
     * @return
     */
    fun querySongPath(song: TracksBean, listener: OnResultListener<SongPathBean>)

    /**
     * 查询歌曲歌词(首页)
     *
     * @param song
     * @param listener
     * @return
     */
    fun querySongWord(song: TracksBean, listener: OnResultListener<SongWordBean>)
}
