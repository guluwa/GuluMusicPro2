package com.guluwa.gulumusicpro.ui.main

import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.base.BaseActivity
import com.guluwa.gulumusicpro.manage.Contacts.MEDIA_STORE_CHANGED
import com.guluwa.gulumusicpro.manage.Contacts.META_CHANGED
import com.guluwa.gulumusicpro.manage.Contacts.PLAY_STATE_CHANGED
import com.guluwa.gulumusicpro.manage.Contacts.QUEUE_CHANGED
import com.guluwa.gulumusicpro.manage.Contacts.REPEAT_MODE_CHANGED
import com.guluwa.gulumusicpro.manage.Contacts.SHUFFLE_MODE_CHANGED
import com.guluwa.gulumusicpro.service.MusicPlayerRemote
import com.guluwa.gulumusicpro.ui.main.home.BannerHomeFragment
import com.guluwa.gulumusicpro.ui.main.library.LibraryFragment
import com.guluwa.gulumusicpro.ui.player.MiniPlayerFragment
import com.guluwa.gulumusicpro.ui.player.normal.PlayerFragment
import com.guluwa.gulumusicpro.utils.interfaces.MainActivityFragmentCallbacks
import com.guluwa.gulumusicpro.utils.interfaces.MusicServiceEventListener
import com.sothree.slidinguppanel.SlidingUpPanelLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.sliding_music_panel_layout.*
import java.lang.ref.WeakReference
import java.util.ArrayList

class MainActivity : BaseActivity(), SlidingUpPanelLayout.PanelSlideListener, MusicServiceEventListener {

    override val viewLayoutId: Int get() = R.layout.activity_main

    private var miniPlayerFragment: MiniPlayerFragment? = null

    private var playerFragment: PlayerFragment? = null

    private val mMusicServiceEventListeners = ArrayList<MusicServiceEventListener>()

    private var serviceToken: MusicPlayerRemote.ServiceToken? = null

    private var musicStateReceiver: MusicStateReceiver? = null

    private var receiverRegistered: Boolean = false

    private lateinit var currentFragment: MainActivityFragmentCallbacks

    val panelState: SlidingUpPanelLayout.PanelState? get() = slidingLayout.panelState

    /**
     * 是否第一次进入
     */
    var isFirstComing: Boolean = true

    override fun initViews(savedInstanceState: Bundle?) {
        initService()
        initContentFragment()
        setupPlayerFragment()
        setupSlidingUpPanel()
        initSubFragments(savedInstanceState)
    }

    private fun initService() {
        serviceToken = MusicPlayerRemote.bindToService(this, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                this@MainActivity.onServiceConnected()
            }

            override fun onServiceDisconnected(name: ComponentName) {
                this@MainActivity.onServiceDisconnected()
            }
        })
    }

    private fun initContentFragment() {
        drawerLayout.addView(wrapSlidingMusicPanel(R.layout.activity_main_content))
    }

    private fun wrapSlidingMusicPanel(@LayoutRes resId: Int): View {
        val slidingMusicPanelLayout = layoutInflater.inflate(R.layout.sliding_music_panel_layout, null)
        val contentContainer = slidingMusicPanelLayout.findViewById<ViewGroup>(R.id.mainContentFrame)
        layoutInflater.inflate(resId, contentContainer)
        return slidingMusicPanelLayout
    }

    private fun setupPlayerFragment() {
        val fragment: Fragment = PlayerFragment()
        supportFragmentManager.beginTransaction().replace(R.id.playerFragmentContainer, fragment).commit()
        supportFragmentManager.executePendingTransactions()

        playerFragment = supportFragmentManager.findFragmentById(R.id.playerFragmentContainer) as PlayerFragment
        miniPlayerFragment = supportFragmentManager.findFragmentById(R.id.miniPlayerFragment) as MiniPlayerFragment
        miniPlayerFragment!!.view!!.setOnClickListener { expandPanel() }
    }

    private fun setupSlidingUpPanel() {
        slidingLayout.viewTreeObserver
            .addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    slidingLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)

                    if (panelState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                        onPanelSlide(slidingLayout, 1f)
                        onPanelExpanded()
                    } else if (panelState == SlidingUpPanelLayout.PanelState.COLLAPSED) {
                        onPanelCollapsed()
                    } else {
                        playerFragment!!.onHide()
                    }
                }
            })

        slidingLayout.addPanelSlideListener(this)
    }

    override fun onPanelSlide(panel: View?, slideOffset: Float) {
        setMiniPlayerAlphaProgress(slideOffset)
    }

    private fun setMiniPlayerAlphaProgress(progress: Float) {
        if (miniPlayerFragment!!.view == null) return
        val alpha = 1 - progress
        miniPlayerFragment!!.view!!.alpha = alpha
        // necessary to make the views below clickable
        miniPlayerFragment!!.view!!.visibility = if (alpha == 0f) View.GONE else View.VISIBLE

        bottomNavigationView.translationY = progress * 500
        bottomNavigationView.alpha = alpha
    }

    override fun onPanelStateChanged(
        panel: View,
        previousState: SlidingUpPanelLayout.PanelState,
        newState: SlidingUpPanelLayout.PanelState
    ) {
        when (newState) {
            SlidingUpPanelLayout.PanelState.COLLAPSED -> onPanelCollapsed()
            SlidingUpPanelLayout.PanelState.EXPANDED -> onPanelExpanded()
            SlidingUpPanelLayout.PanelState.ANCHORED -> collapsePanel() // this fixes a bug where the panel would get stuck for some reason
            else -> {
            }
        }
    }

    fun onPanelCollapsed() {
        playerFragment!!.setMenuVisibility(false)
        playerFragment!!.userVisibleHint = false
        playerFragment!!.onHide()
    }

    fun onPanelExpanded() {
        playerFragment!!.setMenuVisibility(true)
        playerFragment!!.userVisibleHint = true
        playerFragment!!.onShow()
    }

    private fun collapsePanel() {
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.COLLAPSED
    }

    fun expandPanel() {
        slidingLayout.panelState = SlidingUpPanelLayout.PanelState.EXPANDED
    }

    private fun initSubFragments(savedInstanceState: Bundle?) {
        bottomNavigationView.setOnNavigationItemSelectedListener {
            selectedFragment(it.itemId)
            true
        }
        if (savedInstanceState == null) {
            selectedFragment(R.id.action_home)
        } else {
            restoreCurrentFragment()
        }
    }

    private fun selectedFragment(itemId: Int) {
        when (itemId) {
            R.id.action_playlist,
            R.id.action_song -> setCurrentFragment(BannerHomeFragment.newInstance(), false)
            R.id.action_home -> setCurrentFragment(BannerHomeFragment.newInstance(), false)
            else -> {
                setCurrentFragment(BannerHomeFragment.newInstance(), false)
            }
        }
    }

    fun setCurrentFragment(fragment: Fragment, b: Boolean) {
        val trans = supportFragmentManager.beginTransaction()
        trans.replace(R.id.fragment_container, fragment, null)
        if (b) {
            trans.addToBackStack(null)
        }
        trans.commit()
        currentFragment = fragment as MainActivityFragmentCallbacks
    }

    private fun restoreCurrentFragment() {
        currentFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as MainActivityFragmentCallbacks
    }

    override fun initViewModel() {

    }

    private class MusicStateReceiver(activity: MainActivity) : BroadcastReceiver() {

        private val reference: WeakReference<MainActivity> = WeakReference(activity)

        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            val activity = reference.get()
            if (activity != null && action != null) {
                when (action) {
                    META_CHANGED -> activity.onPlayingMetaChanged()
                    QUEUE_CHANGED -> activity.onQueueChanged()
                    PLAY_STATE_CHANGED -> activity.onPlayStateChanged()
                    REPEAT_MODE_CHANGED -> activity.onRepeatModeChanged()
                    SHUFFLE_MODE_CHANGED -> activity.onShuffleModeChanged()
                    MEDIA_STORE_CHANGED -> activity.onMediaStoreChanged()
                }
            }
        }
    }

    fun addMusicServiceEventListener(listener: MusicServiceEventListener?) {
        if (listener != null) {
            mMusicServiceEventListeners.add(listener)
        }
    }

    fun removeMusicServiceEventListener(listener: MusicServiceEventListener?) {
        if (listener != null) {
            mMusicServiceEventListeners.remove(listener)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MusicPlayerRemote.unbindFromService(serviceToken)
        if (receiverRegistered) {
            unregisterReceiver(musicStateReceiver)
            receiverRegistered = false
        }
    }

    override fun onServiceConnected() {
        if (!receiverRegistered) {
            musicStateReceiver = MusicStateReceiver(this)

            val filter = IntentFilter()
            filter.addAction(PLAY_STATE_CHANGED)
            filter.addAction(SHUFFLE_MODE_CHANGED)
            filter.addAction(REPEAT_MODE_CHANGED)
            filter.addAction(META_CHANGED)
            filter.addAction(QUEUE_CHANGED)
            filter.addAction(MEDIA_STORE_CHANGED)

            registerReceiver(musicStateReceiver, filter)

            receiverRegistered = true
        }

        for (listener in mMusicServiceEventListeners) {
            listener.onServiceConnected()
        }

        if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
            slidingLayout.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    slidingLayout.viewTreeObserver.removeOnGlobalLayoutListener(this)
                    hideBottomBar(false)
                }
            })
        } // don't call hideBottomBar(true) here as it causes a bug with the SlidingUpPanelLayout
    }

    override fun onServiceDisconnected() {
        if (receiverRegistered) {
            unregisterReceiver(musicStateReceiver)
            receiverRegistered = false
        }

        for (listener in mMusicServiceEventListeners) {
            listener.onServiceDisconnected()
        }
    }

    override fun onQueueChanged() {
        for (listener in mMusicServiceEventListeners) {
            listener.onQueueChanged()
        }
        hideBottomBar(MusicPlayerRemote.playingQueue.isEmpty())
    }

    override fun onPlayingMetaChanged() {
        for (listener in mMusicServiceEventListeners) {
            listener.onPlayingMetaChanged()
        }
    }

    override fun onPlayStateChanged() {
        for (listener in mMusicServiceEventListeners) {
            listener.onPlayStateChanged()
        }
    }

    override fun onRepeatModeChanged() {
        for (listener in mMusicServiceEventListeners) {
            listener.onRepeatModeChanged()
        }
    }

    override fun onShuffleModeChanged() {
        for (listener in mMusicServiceEventListeners) {
            listener.onShuffleModeChanged()
        }
    }

    override fun onMediaStoreChanged() {
        for (listener in mMusicServiceEventListeners) {
            listener.onMediaStoreChanged()
        }
    }

    fun hideBottomBar(hide: Boolean) {
        val heightOfBar = resources.getDimensionPixelSize(R.dimen.mini_player_height)
        val heightOfBarWithTabs = resources.getDimensionPixelSize(R.dimen.mini_player_height_expanded)

        if (hide) {
            slidingLayout.panelHeight = 0
            collapsePanel()
        } else {
            if (MusicPlayerRemote.playingQueue.isNotEmpty()) {
                slidingLayout.panelHeight =
                    if (bottomNavigationView.visibility == View.VISIBLE) heightOfBarWithTabs else heightOfBar
            }
        }
    }
}
