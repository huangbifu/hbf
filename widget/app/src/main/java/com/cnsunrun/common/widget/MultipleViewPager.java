package com.cnsunrun.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 多页ViewPager
 * Created by cnsunrun on 2017/4/24.
 */

public class MultipleViewPager extends ViewPager {
    boolean isNeedScale=false;
    public float maxScale=0;
    private boolean left = false;
    private boolean right = false;
    private boolean isScrolling = false;
    private int lastValue = -1;
  public   boolean canTouch=false;
    public void setNeedScale(boolean isNeedScale){
        this.isNeedScale=isNeedScale;
        if(isNeedScale) {
            setPageTransformer(false, transformer);
        }else {
            setPageTransformer(false, null);
        }
    }

    public void setMaxScale(float maxScale){
        this.maxScale=maxScale;
        MAX_SCALE=maxScale;
    }
    public MultipleViewPager(Context context) {
        super(context);
        init();
    }


    public MultipleViewPager(Context context, AttributeSet attrs) {

        super(context, attrs);
        MAX_SCALE = (624f/820);//200f/313;
        init();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
       if(getParent() instanceof ViewGroup){
           ((ViewGroup) getParent()).setOnTouchListener(new OnTouchListener() {
               @Override
               public boolean onTouch(View v, MotionEvent event) {
                   return dispatchTouchEvent(event);
               }
           });
       }
       if(isNeedScale) {
           setPageTransformer(false, transformer);
       }
       postDelayed(new Runnable() {
           @Override
           public void run() {
               canTouch=true;
           }
       },300);
    }
    public static  float MAX_SCALE = 1f;//(624f/820);//200f/313;
    private static final float MIN_ALPHA = 90;
    private PageTransformer transformer = new PageTransformer() {
        @Override
        public void transformPage(View page, float position) {
            int pageWidth = getMeasuredWidth() - getPaddingLeft() - getPaddingRight();
            int paddingLeft = getPaddingLeft();
            float transformPos = (float) (page.getLeft() - (getScrollX() + paddingLeft)) / pageWidth;
            final float normalizedposition = Math.abs(Math.abs(transformPos) - 1);



            if (transformPos < -1) {
                page.setPivotX(page.getWidth());
                page.setPivotY(page.getHeight()/2);
                page.setScaleX(MAX_SCALE);
                page.setScaleY(MAX_SCALE);

//                page.setAlpha(MAX_SCALE);
            } else if (transformPos <= 1) {

                if(transformPos<0){
                    page.setPivotX(page.getWidth());
                    page.setPivotY(page.getHeight()/2);
                }else if(position>getCurrentItem()){
                    page.setPivotX(0);
                    page.setPivotY(page.getHeight()/2);
                }
                page.setScaleX(MAX_SCALE
                        + (1 - MAX_SCALE) * (1 - Math.abs(transformPos)));
                page.setScaleY(MAX_SCALE
                        + (1 - MAX_SCALE) * (1 - Math.abs(transformPos)));

//                page.setAlpha((MAX_SCALE
//                        + (1 - MAX_SCALE) * (1 - Math.abs(transformPos))));
            } else {
                page.setScaleX(MAX_SCALE);
                page.setScaleY(MAX_SCALE);
//                page.setAlpha(MAX_SCALE);
            }
        }
    };
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_UP) {
            View view = viewOfClickOnScreen(ev);
            if (view != null) {
                int index = indexOfChild(view);
                if (getCurrentItem() != index) {
//                    setCurrentItem(indexOfChild(view));
                }
            }
        }
        return canTouch&&super.dispatchTouchEvent(ev);
    }


    float downX,downY;

    /**
     * @param ev
     * @return
     */
    private View viewOfClickOnScreen(MotionEvent ev) {
        int childCount = getChildCount();
        int[] location = new int[2];
        for (int i = 0; i < childCount; i++) {
            View v = getChildAt(i);
            v.getLocationOnScreen(location);

            int minX = location[0];
            int minY = getTop();

            int maxX = location[0] + v.getWidth();
            int maxY = getBottom();

            float x = ev.getX();
            float y = ev.getY();

            if ((x > minX && x < maxX) && (y > minY && y < maxY)) {
                return v;
            }
        }
        return null;
    }


    /**
     * init method .
     */
    private void init() {
        setOnPageChangeListener(listener);
    }


    /**
     * listener ,to get move direction .
     */
    public  OnPageChangeListener listener = new OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int arg0) {
            if (arg0 == 1) {
                isScrolling = true;
            } else {
                isScrolling = false;
            }

            if (arg0 == 2) {
                right = left = false;
            }

        }


        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
            if (isScrolling) {
                if (lastValue > arg2) {
                    // 递减，向右侧滑动
                    right = true;
                    left = false;
                } else if (lastValue < arg2) {
                    // 递减，向右侧滑动
                    right = false;
                    left = true;
                } else if (lastValue == arg2) {
                    right = left = false;
                }
            }
            lastValue = arg2;
        }


        @Override
        public void onPageSelected(int arg0) {
        }
    };

    /**
     * 得到是否向右侧滑动
     * @return true 为右滑动
     */
    public boolean getMoveRight(){
        return right;
    }

    /**
     * 得到是否向左侧滑动
     * @return true 为左做滑动
     */
    public boolean getMoveLeft(){
        return left;
    }


}
