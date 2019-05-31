package com.guluwa.gulumusicpro.ui.main.library

import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.appbar.AppBarLayout
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.manage.Contacts.USER_PROFILE
import com.guluwa.gulumusicpro.ui.main.home.AbsBannerHomeFragment
import com.guluwa.gulumusicpro.ui.main.playlist.PlayListsFragment
import com.guluwa.gulumusicpro.ui.main.song.SongsFragment
import com.guluwa.gulumusicpro.utils.ATHUtil
import com.guluwa.gulumusicpro.utils.Compressor
import com.guluwa.gulumusicpro.utils.PreferenceUtil
import com.guluwa.gulumusicpro.utils.interfaces.MainActivityFragmentCallbacks
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_library.*
import java.io.File
import java.util.*

open class LibraryFragment : AbsBannerHomeFragment(), MainActivityFragmentCallbacks {

    private lateinit var disposable: CompositeDisposable

    override val viewLayoutId: Int
        get() = R.layout.fragment_library

    override fun initViews(view: View) {
        setStatusbarColorAuto(view)
        initToolbar()
        inflateFragment()
//        loadImageFromStorage()
    }

    private fun initToolbar() {
        bannerTitle.setTextColor(ATHUtil.resolveColor(context, android.R.attr.textColorPrimary))

        val primaryColor = ATHUtil.resolveColor(context, R.attr.colorPrimary, Color.parseColor("#455A64"))

        toolbar.setBackgroundColor(primaryColor)
        toolbar.setNavigationIcon(R.drawable.ic_search_white_24dp)
        appBarLayout.setBackgroundColor(primaryColor)
        appBarLayout.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { appBarLayout, verticalOffset -> mainActivity.setLightStatusbar(!ATHUtil.isWindowBackgroundDark(context!!)) })
        mainActivity.title = null
        mainActivity.setSupportActionBar(toolbar)
    }

    private fun inflateFragment() {
        if (arguments == null) {
            selectedFragment(SongsFragment.newInstance())
            return
        }
        when (arguments!!.getInt(CURRENT_TAB_ID)) {
            R.id.action_song -> selectedFragment(SongsFragment.newInstance())
            R.id.action_playlist -> selectedFragment(PlayListsFragment.newInstance())
            else -> selectedFragment(SongsFragment.newInstance())
        }
    }

    private fun loadImageFromStorage() {
        disposable = CompositeDisposable()
        disposable.add(Compressor(Objects.requireNonNull(context))
                .setMaxHeight(300)
                .setMaxWidth(300)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .compressToBitmapAsFlowable(File(PreferenceUtil.getInstance().profileImage, USER_PROFILE))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ bitmap -> userImage.setImageBitmap(bitmap) },
                        { throwable -> userImage.setImageDrawable(ContextCompat.getDrawable(context!!, R.drawable.ic_person_flat)) }))
    }

    private fun selectedFragment(fragment: Fragment) {
        val fragmentTransaction = childFragmentManager.beginTransaction()

        fragmentTransaction
                .replace(R.id.fragmentContainer, fragment, TAG)
                .commit()
    }

    override fun lazyLoad() {

    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.dispose()
    }

    override fun handleBackPress(): Boolean {
        return false
    }

    companion object {

        val TAG = "LibraryFragment"

        private val CURRENT_TAB_ID = "current_tab_id"

        fun newInstance(tab: Int): Fragment {
            val args = Bundle()
            args.putInt(CURRENT_TAB_ID, tab)
            val fragment = LibraryFragment()
            fragment.arguments = args
            return fragment
        }

        fun newInstance(): Fragment {
            return LibraryFragment()
        }
    }
}
