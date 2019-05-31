package com.guluwa.gulumusicpro.ui.main.song

import android.os.Bundle
import com.guluwa.gulumusicpro.ui.main.library.LibraryFragment

class SongsFragment : LibraryFragment() {

    val libraryFragment: LibraryFragment get() = parentFragment as LibraryFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    companion object {

        fun newInstance(): SongsFragment {
            val args = Bundle()
            val fragment = SongsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}