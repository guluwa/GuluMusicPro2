package com.guluwa.gulumusicpro.ui.player.normal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import androidx.fragment.app.Fragment
import com.guluwa.gulumusicpro.R
import kotlinx.android.synthetic.main.media_button.*

class PlayerPlaybackControlsFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_player_playback_controls, container, false)
    }

    fun show() {
        playPauseButton!!.animate()
                .scaleX(1f)
                .scaleY(1f)
                .rotation(360f)
                .setInterpolator(DecelerateInterpolator())
                .start()
    }

    fun hide() {
        if (playPauseButton != null) {
            playPauseButton!!.apply {
                scaleX = 0f
                scaleY = 0f
                rotation = 0f
            }
        }
    }
}