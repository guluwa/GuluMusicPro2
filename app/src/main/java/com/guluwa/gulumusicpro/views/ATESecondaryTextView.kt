package com.guluwa.gulumusicpro.views

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatTextView
import com.guluwa.gulumusicpro.utils.ATHUtil

class ATESecondaryTextView : AppCompatTextView {

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    private fun init(context: Context) {
        setTextColor(ATHUtil.resolveColor(context, android.R.attr.textColorSecondary))
    }
}