package com.guluwa.gulumusicpro.data.repository.remote

import androidx.lifecycle.LiveData
import com.guluwa.gulumusicpro.data.bean.remote.old.ViewDataBean

import io.reactivex.Observable

/**
 * Created by guluwa on 2018/1/4.
 */

object LiveDataObservableAdapter {

    fun <T> fromObservableViewData(observable: Observable<T>): LiveData<ViewDataBean<T>> {
        return ObservableViewLiveData(observable)
    }
}
