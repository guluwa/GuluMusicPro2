package com.guluwa.gulumusicpro.ui.player.normal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.guluwa.gulumusicpro.R

class PlayerFragment : Fragment() {

    private lateinit var playbackControlsFragment: PlayerPlaybackControlsFragment

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpSubFragments()
    }

    private fun setUpSubFragments() {
        playbackControlsFragment = childFragmentManager.findFragmentById(R.id.playbackControlsFragment) as PlayerPlaybackControlsFragment
    }

    fun onShow() {
        playbackControlsFragment.show()
    }

    fun onHide() {
        playbackControlsFragment.hide()
    }
}
