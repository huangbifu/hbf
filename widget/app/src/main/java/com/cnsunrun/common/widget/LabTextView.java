package com.cnsunrun.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.sunrun.sunrunframwork.uiutils.LayoutUtil;

import static android.util.TypedValue.COMPLEX_UNIT_PX;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

/**
 * 带标签的TextView
 * Created by WQ on 2017/9/12.
 */

public class LabTextView extends LinearLayout {
    TextView labTxt;
    TextView contentTxt;
    int labGrap = 0;

    public LabTextView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        setGravity(Gravity.CENTER_VERTICAL);
        labTxt = new TextView(getContext());
        contentTxt = new TextView(getContext());
        addView(labTxt);
        addView(contentTxt);
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        labGrap = a.getDimensionPixelSize(R.styleable.LabTextView_labGap, labGrap);
        labTxt.setText(a.getString(R.styleable.LabTextView_labText));
        labTxt.setTextSize(COMPLEX_UNIT_PX,a.getDimensionPixelSize(R.styleable.LabTextView_labSize, 28));
        labTxt.setTextColor(a.getColor(R.styleable.LabTextView_labColor, Color.BLACK));
        LayoutUtil.setMargin(labTxt, 0, 0, labGrap, 0);

        contentTxt.setText(a.getString(R.styleable.LabTextView_contentText));
        contentTxt.setTextSize(COMPLEX_UNIT_PX,a.getDimensionPixelSize(R.styleable.LabTextView_contentSize, 28));
        contentTxt.setTextColor(a.getColor(R.styleable.LabTextView_contentColor, Color.BLACK));
    }

    public LabTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.LabTextView));
    }

    public LabTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.LabTextView));
    }

    public TextView getContentTxt() {
        return contentTxt;
    }


    public TextView getLabTxt() {
        return labTxt;
    }


    @Override
    protected void onFinishInflate() {

        super.onFinishInflate();
    }
}
