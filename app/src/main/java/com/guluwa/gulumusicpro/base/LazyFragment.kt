package com.guluwa.gulumusicpro.base


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.ui.main.MainActivity
import com.guluwa.gulumusicpro.utils.ATHUtil
import com.guluwa.gulumusicpro.utils.AppUtils
import com.guluwa.gulumusicpro.utils.ColorUtil
import com.guluwa.gulumusicpro.utils.interfaces.MusicServiceEventListener
import com.guluwa.gulumusicpro.utils.interfaces.OnActionListener
import com.hwangjr.rxbus.RxBus

abstract class LazyFragment : Fragment(), MusicServiceEventListener {

    var mViewDataBinding: ViewDataBinding? = null

    private var mIsMulti = false

    private var savedState: Bundle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RxBus.get().register(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        if (mViewDataBinding == null) {
            mViewDataBinding = DataBindingUtil.inflate(inflater, viewLayoutId, container, false)
        }
        val parent = mViewDataBinding!!.root.parent
        if (parent != null) {
            (parent as ViewGroup).removeView(mViewDataBinding!!.root)
        }
        return mViewDataBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews(view)
    }

    open fun initViewModel() {

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        // Restore State Here
        if (!restoreStateFromArguments()) {
            // First Time, Initialize something here
            onFirstTimeLaunched()
        }
        if (userVisibleHint && mViewDataBinding != null && !mIsMulti) {
            mIsMulti = true
            lazyLoad()
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        if (isVisibleToUser && isVisible && mViewDataBinding != null && !mIsMulti) {
            mIsMulti = true
            lazyLoad()
        } else {
            super.setUserVisibleHint(isVisibleToUser)
        }
    }

    /**
     * 绑定布局文件
     *
     * @return 布局文件ID
     */
    abstract val viewLayoutId: Int

    /**
     * 初始化视图控件
     */
    protected abstract fun initViews(view: View)

    /**
     * 当视图初始化并且对用户可见的时候去真正的加载数据
     */
    protected abstract fun lazyLoad()

    /**
     * 当视图已经对用户不可见并且加载过数据，如果需要在切换到其他页面时停止加载数据，可以覆写此方法
     */
    protected fun stopLoad() {

    }

    override fun onDestroy() {
        RxBus.get().unregister(this)
        super.onDestroy()
    }

    open fun onFirstTimeLaunched() {
        initViewModel()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save State Here
        saveStateToArguments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Save State Here
        saveStateToArguments()
    }

    private fun saveStateToArguments() {
        if (view != null)
            savedState = saveState()
        if (savedState != null) {
            arguments?.putBundle("internalSavedViewState8954201239547", savedState)
        }
    }

    private fun restoreStateFromArguments(): Boolean {
        savedState = arguments?.getBundle("internalSavedViewState8954201239547")
        if (savedState != null) {
            restoreState()
            return true
        }
        return false
    }

    private fun restoreState() {
        if (savedState != null) {
            onRestoreState(savedState!!)
        }
    }

    open fun onRestoreState(savedInstanceState: Bundle) {
        initViewModel()
    }

    private fun saveState(): Bundle {
        val state = Bundle()
        onSaveState(state)
        return state
    }

    open fun onSaveState(outState: Bundle) {

    }

    fun showSnackBar(msg: String) {
        val snackBar = Snackbar.make(mViewDataBinding!!.root, msg, Snackbar.LENGTH_SHORT)
        snackBar.view.setBackgroundColor(resources.getColor(R.color.green))
        snackBar.show()
    }

    fun showSnackBarWithAction(msg: String, action: String, listener: OnActionListener) {
        val snackBar = Snackbar.make(mViewDataBinding!!.root, msg, Snackbar.LENGTH_LONG)
        snackBar.view.setBackgroundColor(resources.getColor(R.color.green))
        snackBar.setAction(action) {
            listener.action()
        }
        snackBar.show()
    }

    override fun onPlayingMetaChanged() {

    }

    override fun onServiceConnected() {

    }

    override fun onServiceDisconnected() {

    }

    override fun onQueueChanged() {

    }

    override fun onPlayStateChanged() {

    }

    override fun onRepeatModeChanged() {

    }

    override fun onShuffleModeChanged() {

    }

    override fun onMediaStoreChanged() {

    }
}