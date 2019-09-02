package com.cnsunrun.commonui.widget.ratbar;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.RatingBar;

import com.cnsunrun.commonui.R;


public class YellowRatingBar extends RatingBar {
    public YellowRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Drawable progressDrawable = getProgressDrawable();
        if(progressDrawable!=null) {
            progressDrawable.setColorFilter(getResources().getColor(R.color.red_color), PorterDuff.Mode.SRC_ATOP);
        }
    }

    public YellowRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public YellowRatingBar(Context context) {
        this(context, null);
    }
}
