package com.guluwa.gulumusicpro.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.data.bean.local.PageTipBean
import com.guluwa.gulumusicpro.data.bean.remote.neww.SongBean
import com.guluwa.gulumusicpro.manage.MyApplication
import java.util.*

class BannerHomeAdapter(dataSet: ArrayList<Any>) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var dataSet: MutableList<Any> = dataSet

    fun swapData(finalList: List<Any>) {
        dataSet.clear()
        dataSet.add(1)
        dataSet.addAll(finalList)
        notifyDataSetChanged()
    }

    fun swapErrorMsg(msg: String) {
        dataSet.clear()
        dataSet.add(PageTipBean(msg, 1))
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            1 -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list, parent, false)
                SongViewHolder(view)
            }
            2 -> PageStatusViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.page_status_item_view, parent, false))
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(R.layout.abs_playlists, parent, false)
                TopViewHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun getItemViewType(position: Int): Int {
        return if (dataSet[position] is SongBean) 1 else if (dataSet[position] is PageTipBean) 2 else 3
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == 1) {
            val song = dataSet[position] as SongBean

            if ((holder as SongViewHolder).adapterPosition == itemCount - 1) {
                if (holder.shortSeparator != null) {
                    holder.shortSeparator!!.visibility = View.GONE
                }
            } else {
                if (holder.shortSeparator != null) {
                    holder.shortSeparator!!.visibility = View.GONE
                }
            }

            if (holder.title != null) {
                holder.title!!.text = song.title
            }
            if (holder.text != null) {
                holder.text!!.text = song.artistName
            }

            if (holder.image != null) {
                Glide.with(MyApplication.context)
                        .load(song.albumPic)
                        .apply(RequestOptions().placeholder(R.mipmap.ic_launcher).error(R.mipmap.ic_launcher))
                        .into(holder.image!!)
            }
        } else if (getItemViewType(position) == 2) {
            (holder as PageStatusViewHolder).mItemBinding.pageTipBean = dataSet[position] as PageTipBean
        }
    }

    inner class SongViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        var image: ImageView? = null
        var title: TextView? = null
        var text: TextView? = null
        var imageContainer: ViewGroup? = null
        var menu: View? = null
        var separator: View? = null
        var shortSeparator: View? = null
        var paletteColorContainer: View? = null
        var imageTextContainer: CardView? = null

        init {
            title = view.findViewById(R.id.title)
            text = view.findViewById(R.id.text)
            image = view.findViewById(R.id.image)
            imageContainer = view.findViewById(R.id.image_container)
            imageTextContainer = view.findViewById(R.id.image_text_container)
            menu = view.findViewById(R.id.menu)
            separator = view.findViewById(R.id.separator)
            shortSeparator = view.findViewById(R.id.short_separator)
            paletteColorContainer = view.findViewById(R.id.palette_color_container)
        }
    }

    inner class TopViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    }

    companion object {

        val TAG: String = BannerHomeAdapter::class.java.simpleName
    }
}