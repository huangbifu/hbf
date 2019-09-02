package com.cnsunrun.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;


import com.cnsunrun.R;

import java.lang.reflect.Field;

/**
 * 盒子控件,在LinearLayout基础上进行扩展.
 * 支持设置边线
 * Created by WQ on 2017/9/4.
 * 已知bug,在Recycleview 中作为item存在时,该控件高度刷新不及时,
 */

public class BoxLayout extends LinearLayout {
    public static final int TOP = 0x30, BOTTOM = 0x50, LEFT = 0x03, RIGHT = 0x05, BOTH = 0x77, NONE = 0x00;
    int BORDERS[] = {TOP, BOTTOM, LEFT, RIGHT};
    protected LinearLayout childGroup;
    int border = NONE;
    int borderWidth;
    int borderColor;
    Drawable borderDrawable;

    public BoxLayout(Context context) {
        super(context);
    }

    public BoxLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.BoxLayout));
    }

    public BoxLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.BoxLayout));
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        border = a.getInt(R.styleable.BoxLayout_box_Border, NONE);
        borderWidth = a.getDimensionPixelSize(R.styleable.BoxLayout_box_BorderWidth, 2);
        borderColor = a.getColor(R.styleable.BoxLayout_box_BorderColor, Color.parseColor("#dddddd"));
        String padding = a.getString(R.styleable.BoxLayout_box_Padding);
        if (padding != null) {
            String[] split = padding.trim().split(" ");
            if (split.length > 1) {
                setPadding(strDp2Px(split[0]), strDp2Px(split[1]), strDp2Px(split[2]), strDp2Px(split[3]));
            } else {
                int left = strDp2Px(split[0]);
                setPadding(left, left, left, left);
            }
        }
//        borderDrawable = a.getDrawable(R.styleable.BoxLayout_box_BorderDrawable);
        borderDrawable = new BorderDrawable();
    }

    private int strDp2Px(String dp) {
        final Resources resources = getResources();
        final DisplayMetrics metrics = resources.getDisplayMetrics();
        final int unit = TypedValue.COMPLEX_UNIT_DIP;
        return (int) TypedValue.applyDimension(unit, Integer.parseInt(dp), metrics);
    }

    public int getGravity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return super.getGravity();
        } else {
            try {
                final Field staticField = LinearLayout.class.getDeclaredField("mGravity");
                staticField.setAccessible(true);
                return staticField.getInt(this);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    @Override
    public void setBackgroundDrawable(Drawable background) {
        super.setBackgroundDrawable(background);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        borderDrawable.setBounds(0, 0, 1, 1);
        if (childGroup != null) {
            borderDrawable.setBounds(0, 0, childGroup.getMeasuredWidth(), childGroup.getMeasuredHeight());
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        childGroup = new LinearLayout(getContext());
        childGroup.setOrientation(getOrientation());
        childGroup.setGravity(getGravity());
        childGroup.setDividerDrawable(getDividerDrawable());
        childGroup.setShowDividers(getShowDividers());
        childGroup.setDividerPadding(getDividerPadding());
        childGroup.setClipChildren(getClipChildren());
        childGroup.setClipToPadding(getClipToPadding());
        setOrientation(VERTICAL);
        for (; getChildCount() > 0; ) {
            View childAt = getChildAt(0);
            removeView(childAt);
            childGroup.addView(childAt);
        }

        childGroup.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        setPadding(0, 0, 0, 0);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        addView(childGroup, layoutParams);
        borderDrawable.setBounds(0, 0, childGroup.getWidth(), childGroup.getHeight());
        childGroup.setBackgroundDrawable(borderDrawable);
    }

    public boolean getClipToPadding() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            return super.getClipToPadding();
        }
        Integer mGroupFlags = get(this, "mGroupFlags");
        return mGroupFlags != null && (mGroupFlags & 0x2) == 0x2;
    }

    public static <T> T get(Object obj, String fieldName) {
        try {

            Class<?> clazz = obj.getClass().getClass();
            for (; clazz != Object.class; clazz = clazz.getSuperclass()) {
                Field[] fields = clazz.getFields();
                for (Field field : fields) {
                    if (field.getName().equals(fieldName)) {
                        field.setAccessible(true);
                        return (T) field.get(obj);
                    }
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    class BorderDrawable extends BitmapDrawable {
        Paint borderLine;

        public BorderDrawable() {
            borderLine = new Paint();
            borderLine.setColor(borderColor);
            borderLine.setStyle(Paint.Style.STROKE);
            borderLine.setStrokeWidth(borderWidth);
        }


        @Override
        public void setAlpha(int alpha) {
            borderLine.setAlpha(alpha);
        }

        @Override
        public void setColorFilter(ColorFilter cf) {
            borderLine.setColorFilter(cf);
        }

        @Override
        public int getOpacity() {
            return PixelFormat.TRANSLUCENT;
        }

        @Override
        public void draw(Canvas canvas) {
            super.draw(canvas);
            for (int i = 0; i < 4; i++) {
                int border = BoxLayout.this.border & BORDERS[i];
                switch (border) {
                    case TOP:
                        canvas.drawLine(0, 0, getIntrinsicWidth(), 0, borderLine);
                        break;
                    case BOTTOM:
                        canvas.drawLine(0, getIntrinsicHeight(), getIntrinsicWidth(), getIntrinsicHeight(), borderLine);
                        break;
                    case LEFT:
                        canvas.drawLine(0, 0, 0, getIntrinsicHeight(), borderLine);
                        break;
                    case RIGHT:
                        canvas.drawLine(getIntrinsicWidth(), 0, getIntrinsicWidth(), getIntrinsicHeight(), borderLine);
                        break;
                }
            }
        }
    }
}
