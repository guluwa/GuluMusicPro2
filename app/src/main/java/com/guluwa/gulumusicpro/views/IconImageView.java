package com.guluwa.gulumusicpro.views;


import android.content.Context;
import android.graphics.PorterDuff;
import android.util.AttributeSet;

import com.guluwa.gulumusicpro.R;
import com.guluwa.gulumusicpro.utils.ATHUtil;

public class IconImageView extends androidx.appcompat.widget.AppCompatImageView {

    public IconImageView(Context context) {
        super(context);
        init(context);
    }

    public IconImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public IconImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        if (context == null) return;
        setColorFilter(ATHUtil.INSTANCE.resolveColor(context, R.attr.iconColor), PorterDuff.Mode.SRC_IN);
    }
}