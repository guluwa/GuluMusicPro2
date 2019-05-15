package com.guluwa.gulumusicpro.views

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.guluwa.gulumusicpro.R
import com.guluwa.gulumusicpro.utils.ATHUtil
import com.guluwa.gulumusicpro.utils.ColorUtil

class ColorIconsImageView : AppCompatImageView {

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        // Load the styled attributes and set their properties
        val attributes = context.obtainStyledAttributes(attrs, R.styleable.ColorIconsImageView, 0, 0)
        setIconBackgroundColor(attributes.getColor(R.styleable.ColorIconsImageView_iconBackgroundColor, Color.RED))
        attributes.recycle()
    }

    private fun setIconBackgroundColor(color: Int) {
        setBackgroundResource(R.drawable.color_circle_gradient)

        val alpha = if (ATHUtil.isWindowBackgroundDark(context)) {
            1.0f
        } else {
            0.12f
        }
        val filterColor = if (ATHUtil.isWindowBackgroundDark(context)) {
            ATHUtil.resolveColor(context, R.attr.colorPrimary, Color.parseColor("#455A64"))
        } else {
            color
        }

        backgroundTintList = ColorStateList.valueOf(ColorUtil.adjustAlpha(color, alpha))
        imageTintList = ColorStateList.valueOf(filterColor)
        requestLayout()
        invalidate()
    }
}
