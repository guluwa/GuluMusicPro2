package com.guluwa.gulumusicpro.ui.main.playlist

import android.os.Bundle
import com.guluwa.gulumusicpro.ui.main.library.LibraryFragment

class PlayListsFragment : LibraryFragment() {

    val libraryFragment: LibraryFragment get() = parentFragment as LibraryFragment

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setHasOptionsMenu(true)
    }

    companion object {

        fun newInstance(): PlayListsFragment {
            val args = Bundle()
            val fragment = PlayListsFragment()
            fragment.arguments = args
            return fragment
        }
    }
}