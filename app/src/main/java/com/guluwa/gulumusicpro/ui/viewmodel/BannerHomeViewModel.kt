package com.guluwa.gulumusicpro.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.guluwa.gulumusicpro.data.bean.remote.neww.SongBean
import com.guluwa.gulumusicpro.data.bean.remote.old.FreshBean
import com.guluwa.gulumusicpro.data.bean.remote.old.ViewDataBean
import com.guluwa.gulumusicpro.data.repository.total.SongsRepository

class BannerHomeViewModel : ViewModel() {

    //热门歌曲刷新
    private var mHotSongListFresh: MutableLiveData<FreshBean>? = null

    //热门歌曲
    private var mHotSongs: LiveData<ViewDataBean<List<SongBean>>>? = null

    /**
     * 查询热门歌曲
     *
     * @return
     */
    fun queryNetCloudHotSong(): LiveData<ViewDataBean<List<SongBean>>>? {
        if (mHotSongs == null) {
            mHotSongListFresh = MutableLiveData()
            mHotSongs = Transformations.switchMap(mHotSongListFresh!!) { input ->
                if (input.isFresh) {
                    SongsRepository.getInstance().queryNetCloudHotSong(input.isFirstComing)
                } else {
                    null
                }
            }
        }
        return mHotSongs
    }

    /**
     * 刷新热门歌曲
     *
     * @param isFresh
     * @param isFirstComing
     */
    fun refreshNetCloudHotSong(isFresh: Boolean, isFirstComing: Boolean) {
        mHotSongListFresh!!.value = FreshBean(isFresh, isFirstComing)
    }
}