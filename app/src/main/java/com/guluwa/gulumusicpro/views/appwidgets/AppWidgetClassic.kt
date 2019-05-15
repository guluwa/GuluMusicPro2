package com.guluwa.gulumusicpro.views.appwidgets

import android.content.Context
import android.view.View
import android.widget.RemoteViews
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.service.MusicService
import com.guluwa.gulumusicpro.utils.AppUtils

class AppWidgetClassic : BaseAppWidget() {

    /**
     * Initialize given widgets to default state, where we launch Music on default click and hide
     * actions if service not running.
     */
    override fun defaultAppWidget(context: Context, appWidgetIds: IntArray) {
        val appWidgetView = RemoteViews(context.packageName, R.layout.app_widget_classic)

        appWidgetView.setViewVisibility(R.id.media_titles, View.INVISIBLE)
        appWidgetView.setImageViewResource(R.id.image, R.drawable.default_album_art)
        appWidgetView.setImageViewBitmap(
            R.id.button_next,
            createBitmap(
                AppUtils.getTintedVectorDrawable(
                    context,
                    R.drawable.ic_skip_next_white_24dp,
                    AppUtils.getSecondaryTextColor(context, true)
                )!!, 1f
            )
        )
        appWidgetView.setImageViewBitmap(
            R.id.button_prev,
            createBitmap(
                AppUtils.getTintedVectorDrawable(
                    context,
                    R.drawable.ic_skip_previous_white_24dp,
                    AppUtils.getSecondaryTextColor(context, true)
                )!!, 1f
            )
        )
        appWidgetView.setImageViewBitmap(
            R.id.button_toggle_play_pause,
            createBitmap(
                AppUtils.getTintedVectorDrawable(
                    context,
                    R.drawable.ic_play_arrow_white_24dp,
                    AppUtils.getSecondaryTextColor(context, true)
                )!!, 1f
            )
        )

        pushUpdate(context, appWidgetIds, appWidgetView)
    }

    /**
     * Update all active widget instances by pushing changes
     */
    override fun performUpdate(service: MusicService, appWidgetIds: IntArray?) {

    }

    companion object {

        const val NAME = "app_widget_classic"

        private var mInstance: AppWidgetClassic? = null
        private var imageSize = 0
        private var cardRadius = 0f

        val instance: AppWidgetClassic
            @Synchronized get() {
                if (mInstance == null) {
                    mInstance = AppWidgetClassic()
                }
                return mInstance!!
            }
    }
}
