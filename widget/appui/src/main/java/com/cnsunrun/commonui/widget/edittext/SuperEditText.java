package com.cnsunrun.commonui.widget.edittext;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import com.cnsunrun.commonui.R;

/**
 * @时间: 2017/5/15
 * @功能描述:
 */


public class SuperEditText extends EditText {
    boolean hasRightAction;
    OnClickListener rightAction;
    private boolean leftToRight = true;
    private final int DEFAULT_ADDITIONAL_TOUCH_TARGET_SIZE = 40;
    public SuperEditText(@NonNull Context context) {
        this(context, null);
    }

    public SuperEditText(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, -1);
    }

    public SuperEditText(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(context, attrs);
    }
    Drawable leftDrawable,rightDrawable,showPwdIcon,hidePwdIcon;
    private void setAttrs(Context context, AttributeSet attrs) {
        TypedArray att = context.obtainStyledAttributes(attrs, R.styleable.SuperEditText);
        leftDrawable = att.getDrawable(R.styleable.SuperEditText_leftIcon);
        rightDrawable = att.getDrawable(R.styleable.SuperEditText_rightIcon);
        showPwdIcon = att.getDrawable(R.styleable.SuperEditText_showedIcon);
        hidePwdIcon = att.getDrawable(R.styleable.SuperEditText_hindeIcon);
        final String rightAction = att.getString(R.styleable.SuperEditText_rightIconAction);
        if(showPwdIcon==null){
            showPwdIcon=getVaildDrawable(getContext(),R.mipmap.icon_eye_open);
        }
        if(hidePwdIcon==null){
            hidePwdIcon=getVaildDrawable(getContext(),R.mipmap.icon_eye_close);
        }
        leftToRight=isLeftToRight();
        setMaxLines(1);
        setSingleLine(true);
        setRightActionInner(rightAction);
        att.recycle();
        int leftRightPadding=dp2px(10);
        setPadding(leftRightPadding,0,leftRightPadding,0);
        setGravity(Gravity.CENTER_VERTICAL);
        setCompoundDrawablePadding(dp2px(5));
        setFocusable(true);
        att = null;
    }

    private void setRightActionInner(String rightAction) {
        if (TextUtils.equals("0", rightAction)) {
            hasRightAction=true;
            handlePassword();
        } else if (TextUtils.equals("1", rightAction)) {
            hasRightAction=true;
            handleClearAll();
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {

        Drawable drawableEnd = getCompoundDrawables()[2];
        OnClickListener rightAction = this.rightAction;
        if (rightAction !=null&&event.getAction() == MotionEvent.ACTION_UP && drawableEnd != null) {
            Rect bounds = drawableEnd.getBounds();

            int x = (int) event.getX();

            //take into account the padding and additionalTouchTargetSize
            int drawableWidthWithPadding = bounds.width() + (leftToRight ? getPaddingRight() : getPaddingLeft()) + DEFAULT_ADDITIONAL_TOUCH_TARGET_SIZE;

            //check if the touch is within bounds of drawableEnd icon
            if ((leftToRight && (x >= (this.getRight() - (drawableWidthWithPadding)))) ||
                    (!leftToRight && (x <= (this.getLeft() + (drawableWidthWithPadding))))) {

                rightAction.onClick(this);
                //use this to prevent the keyboard from coming up
                event.setAction(MotionEvent.ACTION_CANCEL);
            }
        }

        return super.onTouchEvent(event);
    }
    private boolean isLeftToRight() {
        // If we are pre JB assume always LTR
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            return true;
        }

        // Other methods, seemingly broken when testing though.
        // return ViewCompat.getLayoutDirection(this) == ViewCompat.LAYOUT_DIRECTION_RTL
        // return !ViewUtils.isLayoutRtl(this);

        Configuration config = getResources().getConfiguration();
        return !(config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL);
    }
    public  int dp2px( int dp) {
        return (int) TypedValue.applyDimension(1, (float)dp, getResources().getDisplayMetrics());
    }
    private void handleClearAll() {
        rightAction=new ClearAllAction(this);
    }

     class ClearAllAction implements OnClickListener , TextWatcher{
        private EditText input;

        public ClearAllAction(EditText editView) {
            this.input = editView;
            toggleRightIcon(input.getText().toString());
        }

        @Override
        public void onClick(View v) {
            input.setText("");
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            final String content = s.toString().trim();
            toggleRightIcon(content);
        }

         private void toggleRightIcon(String content) {
             Drawable[] compoundDrawables = input.getCompoundDrawables();
             compoundDrawables[2]= TextUtils.isEmpty(content)?null:getVaildDrawable(rightDrawable);
             input.setCompoundDrawables(compoundDrawables[0],compoundDrawables[1],compoundDrawables[2],compoundDrawables[3]);
         }
     }


    private void handlePassword() {
        rightAction=new ShowOrHidePasswordAction(this);
    }

     class ShowOrHidePasswordAction implements OnClickListener {

        private EditText input;
        private boolean isShowingPassword = true;

        public ShowOrHidePasswordAction(EditText input) {
            this.input = input;
            toggleRightIcon(isShowingPassword);
        }

        @Override
        public void onClick(View v) {
            isShowingPassword = !isShowingPassword;
            toggleRightIcon(isShowingPassword);
            input.setSelection(input.getText().toString().length());
        }

         private void toggleRightIcon(boolean isShowingPassword) {
             int inputType=isShowingPassword?(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD):
                     InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD;
             input.setInputType(inputType);
             Drawable[] compoundDrawables = input.getCompoundDrawables();
             compoundDrawables[2]=getVaildDrawable(isShowingPassword?showPwdIcon:hidePwdIcon);
             input.setCompoundDrawables(compoundDrawables[0],compoundDrawables[1],compoundDrawables[2],compoundDrawables[3]);
         }
     }




    public static Drawable getVaildDrawable(Drawable drawable) {
        if(drawable == null) {
            return drawable;
        } else {
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            return drawable;
        }
    }

    public static Drawable getVaildDrawable(Context context, int drawableId) {
        return drawableId != -1 && drawableId != 0?getVaildDrawable(context.getResources().getDrawable(drawableId)):null;
    }
}
