package com.guluwa.gulumusicpro.utils

import android.annotation.SuppressLint
import android.annotation.TargetApi
import android.content.ContentUris
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.CheckResult
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.vectordrawable.graphics.drawable.VectorDrawableCompat
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.data.bean.remote.Song
import com.guluwa.gulumusicpro.data.bean.remote.old.LocalSongBean
import com.guluwa.gulumusicpro.data.bean.remote.old.TracksBean
import com.guluwa.gulumusicpro.manage.MyApplication
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

object AppUtils {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    fun dp2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    fun px2dip(context: Context, pxValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (pxValue / scale + 0.5f).toInt()
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     */
    fun sp2px(context: Context, spValue: Float): Int {
        val fontScale = context.resources.displayMetrics.scaledDensity
        return (spValue * fontScale + 0.5f).toInt()
    }

    /**
     * 检测网络是否连接
     */
    val isNetConnected: Boolean
        get() {
            val cm = MyApplication.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = cm.activeNetworkInfo
            if (networkInfo != null) {
                return true
            }
            return false
        }

    fun getTintedVectorDrawable(context: Context, @DrawableRes id: Int, @ColorInt color: Int): Drawable? {
        return createTintedDrawable(getVectorDrawable(context.resources, id, context.theme), color)
    }

    @CheckResult
    fun createTintedDrawable(drawable: Drawable?, @ColorInt color: Int): Drawable? {
        var drawable: Drawable? = drawable ?: return null
        drawable = DrawableCompat.wrap(drawable!!.mutate())
        DrawableCompat.setTintMode(drawable!!, PorterDuff.Mode.SRC_IN)
        DrawableCompat.setTint(drawable, color)
        return drawable
    }

    private fun getVectorDrawable(res: Resources, @DrawableRes resId: Int, theme: Resources.Theme?): Drawable? {
        return if (Build.VERSION.SDK_INT >= 21) {
            res.getDrawable(resId, theme)
        } else {
            VectorDrawableCompat.create(res, resId, theme)
        }
    }

    @SuppressLint("PrivateResource")
    @ColorInt
    fun getSecondaryTextColor(context: Context?, dark: Boolean): Int {
        return if (dark) {
            ContextCompat.getColor(context!!, R.color.secondary_text_default_material_light)
        } else {
            ContextCompat.getColor(context!!, R.color.secondary_text_default_material_dark)
        }
    }

    fun getSongFileUri(songId: Int): Uri {
        return ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, songId.toLong())
    }

    fun makeShuffleList(listToShuffle: MutableList<Song>, current: Int) {
        if (listToShuffle.isEmpty()) return
        if (current >= 0) {
            val song = listToShuffle.removeAt(current)
            listToShuffle.shuffle()
            listToShuffle.add(0, song)
        } else {
            listToShuffle.shuffle()
        }
    }

    fun convertDpToPixel(dp: Float, context: Context): Float {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun hasMarshmallow(): Boolean {
        return Build.VERSION.SDK_INT >= 23
    }

    fun hasLollipop(): Boolean {
        return Build.VERSION.SDK_INT >= 21
    }

    fun hasKitKat(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
    }

    fun getStrTimeYear(stampTime: Long): String {
        val sdf = SimpleDateFormat("yyyy")
        return sdf.format(Date(stampTime))
    }

    fun getCurrentDate(): Long {
        return System.currentTimeMillis() / 1000
    }

    private fun createFile(filename: String, type: Int): File {
        val file: File
        val file1: File
        if (type == 1) {
            file1 = File(Environment.getExternalStorageDirectory().absolutePath + "/gulu_music/song")
        } else {
            file1 = File(Environment.getExternalStorageDirectory().absolutePath + "/gulu_music/word")
        }
        if (!file1.exists())
            file1.mkdirs()
        file = File(file1.absolutePath + "/" + filename)
        return file
    }

    fun writeSong2Disk(responseBody: ResponseBody, filename: String): File? {

        val file = createFile(filename, 1)
        var outputStream: OutputStream? = null
        val inputStream = responseBody.byteStream()

        try {
            outputStream = FileOutputStream(file)
            var len: Int
            val buff = ByteArray(1024)
            while (true) {
                len = inputStream.read(buff)
                if (len != -1)
                    outputStream.write(buff, 0, len)
                else
                    break
            }
            return file
        } catch (e: IOException) {
            e.printStackTrace()
            return null
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
            try {
                inputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    fun getLocalSongBean(tracksBean: TracksBean): LocalSongBean {
        val localSongBean = LocalSongBean()
        localSongBean.id = tracksBean.id
        localSongBean.name = tracksBean.name
        localSongBean.al = tracksBean.al
        localSongBean.singer = tracksBean.singer
        localSongBean.tag = tracksBean.tag
        localSongBean.source = tracksBean.source
        return localSongBean
    }

    fun setAllowDrawUnderStatusBar(window: Window) {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    }

    @TargetApi(19)
    fun setStatusBarTranslucent(window: Window) {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
    }
}