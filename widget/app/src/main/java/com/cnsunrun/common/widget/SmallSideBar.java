package com.cnsunrun.common.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.sunrun.sunrunframwork.view.sidebar.SideBar;

/**
 * Created by weiquan on 2018/9/28.
 */
public class SmallSideBar extends SideBar {
    public String[] mNewb = new String[]{ "#","A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    public SmallSideBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
//        setB(mNewb);
    }

    public SmallSideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
//        setB(mNewb);
    }

    public SmallSideBar(Context context) {
        super(context);
        setB(mNewb);
    }

    public int sp2px(float spValue) {
        spValue=11;
        float fontScale = this.getResources().getDisplayMetrics().scaledDensity;
        return (int)(spValue * fontScale + 0.5F);
    }
}
