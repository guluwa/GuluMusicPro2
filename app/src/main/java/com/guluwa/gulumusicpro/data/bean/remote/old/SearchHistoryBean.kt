package com.guluwa.gulumusicpro.data.bean.remote.old

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Created by guluwa on 2018/3/3.
 */

@Entity(tableName = "search_history")
data class SearchHistoryBean(val date: Long, @PrimaryKey var text: String)