package com.guluwa.gulumusicpro.ui.home

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.data.bean.remote.neww.SongBean
import com.guluwa.gulumusicpro.data.bean.remote.old.PageStatus
import com.guluwa.gulumusicpro.manage.Contacts.USER_BANNER
import com.guluwa.gulumusicpro.manage.Contacts.USER_PROFILE
import com.guluwa.gulumusicpro.service.MusicPlayerRemote
import com.guluwa.gulumusicpro.ui.main.MainActivity
import com.guluwa.gulumusicpro.ui.viewmodel.BannerHomeViewModel
import com.guluwa.gulumusicpro.utils.*
import com.guluwa.gulumusicpro.utils.interfaces.MainActivityFragmentCallbacks
import com.guluwa.gulumusicpro.utils.interfaces.MusicServiceEventListener
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.abs_playlists.*
import kotlinx.android.synthetic.main.fragment_banner_home.*
import java.io.File
import java.util.*
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.guluwa.gulumusicpro.adapters.BannerHomeAdapter

class BannerHomeFragment : Fragment(), MainActivityFragmentCallbacks, MusicServiceEventListener {

    private lateinit var mViewModel: BannerHomeViewModel

    private lateinit var disposable: CompositeDisposable

    val mainActivity: MainActivity get() = activity as MainActivity

    /**
     * 是否第一次进入
     */
    private var isFirstComing: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, viewGroup: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(
            if (PreferenceUtil.getInstance().isHomeBanner) R.layout.fragment_banner_home else R.layout.fragment_home,
            viewGroup,
            false
        )
    }

    private fun getTimeOfTheDay() {
        val c = Calendar.getInstance()
        val timeOfDay = c.get(Calendar.HOUR_OF_DAY)

        var images = arrayOf<String>()
        when (timeOfDay) {
            in 0..5 -> images = resources.getStringArray(R.array.night)
            in 6..11 -> images = resources.getStringArray(R.array.morning)
            in 12..15 -> images = resources.getStringArray(R.array.after_noon)
            in 16..19 -> images = resources.getStringArray(R.array.evening)
            in 20..23 -> images = resources.getStringArray(R.array.night)
        }

        val day = images[Random().nextInt(images.size)]
        loadTimeImage(day)
    }


    private fun loadTimeImage(day: String) {
        if (bannerImage != null) {
            if (PreferenceUtil.getInstance().bannerImage.isEmpty()) {
                Glide.with(activity!!)
                    .load(day)
                    .apply(
                        RequestOptions().placeholder(R.drawable.material_design_default).diskCacheStrategy(
                            DiskCacheStrategy.ALL
                        )
                    )
                    .into(bannerImage!!)
            } else {
                disposable.add(Compressor(context!!)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.WEBP)
                    .compressToBitmapAsFlowable(File(PreferenceUtil.getInstance().bannerImage, USER_BANNER))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { bannerImage!!.setImageBitmap(it) })
            }
        }
    }

    private fun loadImageFromStorage(imageView: ImageView) {
        disposable.add(Compressor(context!!)
            .setMaxHeight(300)
            .setMaxWidth(300)
            .setQuality(75)
            .setCompressFormat(Bitmap.CompressFormat.WEBP)
            .compressToBitmapAsFlowable(File(PreferenceUtil.getInstance().profileImage, USER_PROFILE))
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({
                if (it != null) {
                    imageView.setImageBitmap(it)
                } else {
                    imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_person_flat))
                }
            }) {
                imageView.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_person_flat))
            })
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isFirstComing = true
        mViewModel = ViewModelProviders.of(this).get(BannerHomeViewModel::class.java)
        mViewModel.queryNetCloudHotSong()!!.observe(this, Observer { data ->
            if (data == null) {
                mainActivity.showSnackBar(getString(R.string.data_error))
            }
            when (data.status) {
                PageStatus.Loading -> {
                    print("loading")
                }
                PageStatus.Error -> {
                    mViewModel.refreshNetCloudHotSong(false, isFirstComing)
                    mainActivity.showSnackBar(data.throwable!!.message!!)
                }
                PageStatus.Empty -> {
                    mViewModel.refreshNetCloudHotSong(false, isFirstComing)
                    mainActivity.showSnackBar(getString(R.string.load_error))
                }
                PageStatus.Content -> {
                    isFirstComing = false
                    mViewModel.refreshNetCloudHotSong(false, isFirstComing)
                    showData(data.data)
                }
            }
        })
        mViewModel.refreshNetCloudHotSong(true, isFirstComing)
    }

    private fun showData(data: List<SongBean>?) {
        if (data != null) {
            mainActivity.showSnackBar("success")
            homeAdapter.swapData(data)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (!PreferenceUtil.getInstance().isHomeBanner) {
            setStatusbarColorAuto(view)
        }

        userImage.setOnClickListener {

        }

        setupToolbar()

        homeAdapter = BannerHomeAdapter(arrayListOf(1))
        recyclerView.apply {
            layoutManager = LinearLayoutManager(mainActivity)
            adapter = homeAdapter
        }
        checkPadding()
    }

    private fun checkPadding() {
        val marginSpan = when {
            MusicPlayerRemote.playingQueue.isEmpty() -> AppUtils.convertDpToPixel(52f, context!!).toInt()
            else -> AppUtils.convertDpToPixel(0f, context!!).toInt()
        }

        (recyclerView.layoutParams as ViewGroup.MarginLayoutParams).bottomMargin = (marginSpan * 2.3f).toInt()
    }

    fun setStatusbarColorAuto(view: View) {
        // we don't want to use statusbar color because we are doing the color darkening on our own to support KitKat
        if (AppUtils.hasMarshmallow()) {
            setStatusbarColor(view, ATHUtil.resolveColor(context, R.attr.colorPrimary, Color.parseColor("#455A64")))
        } else {
            setStatusbarColor(
                view,
                ColorUtil.darkenColor(ATHUtil.resolveColor(context, R.attr.colorPrimary, Color.parseColor("#455A64")))
            )
        }
    }

    private fun setStatusbarColor(view: View, color: Int) {
        val statusBar = view.findViewById<View>(R.id.status_bar)
        if (statusBar != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                statusBar.setBackgroundColor(color)
                mainActivity.setLightStatusbarAuto(color)
            } else {
                statusBar.setBackgroundColor(color)
            }
        }
    }

    private fun setupToolbar() {
        mainActivity.title = null
        toolbar.apply {
            navigationIcon = AppUtils.createTintedDrawable(
                ContextCompat.getDrawable(context!!, R.drawable.ic_search_white_24dp),
                ATHUtil.resolveColor(context, android.R.attr.textColorSecondary)
            )
            setBackgroundColor(Color.TRANSPARENT)
            setNavigationOnClickListener {

            }
        }
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    override fun onResume() {
        super.onResume()
        disposable = CompositeDisposable()
        loadImageFromStorage(userImage)
        getTimeOfTheDay()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    override fun onServiceConnected() {
        checkPadding()
    }

    override fun onQueueChanged() {
        checkPadding()
    }

    override fun onServiceDisconnected() {

    }

    override fun onPlayingMetaChanged() {

    }

    override fun onPlayStateChanged() {

    }

    override fun onRepeatModeChanged() {

    }

    override fun onShuffleModeChanged() {

    }

    override fun onMediaStoreChanged() {

    }

    private lateinit var homeAdapter: BannerHomeAdapter

    companion object {

        const val TAG: String = "BannerHomeFragment"

        fun newInstance(): BannerHomeFragment {
            val args = Bundle()
            val fragment = BannerHomeFragment()
            fragment.arguments = args
            return fragment
        }
    }
}