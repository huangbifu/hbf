package com.cnsunrun.commonui.widget.ratbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.widget.RatingBar;

import com.cnsunrun.commonui.R;


public class StepRatingBar extends RatingBar {
    /**
     * 空白的默认星星图片
     */
    private Bitmap starEmptyBitmap;
    /**
     * 选中后的星星填充图片
     */
    private Bitmap starFillBitmap;
    /**
     * 每个星星的大小
     */
    private float starImageWidth,starImageHeight;
    /**
     * 每个星星的间距
     */
    private float starPadding;

    Paint progressPaint;
    PorterDuffXfermode porterDuffXfermode = new PorterDuffXfermode(PorterDuff.Mode.SRC_OUT);
    RectF progressRect=new RectF();
    public StepRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.StepRatingBar);
            BitmapDrawable starEmpty =(BitmapDrawable) mTypedArray.getDrawable(R.styleable.StepRatingBar_ratingStarEmpty);
            starEmptyBitmap = starEmpty.getBitmap();
            BitmapDrawable starFill =(BitmapDrawable) mTypedArray.getDrawable(R.styleable.StepRatingBar_ratingStarFill);
            starFillBitmap =starFill.getBitmap();
            starImageWidth = starEmpty.getIntrinsicWidth();
            starImageHeight=starEmpty.getIntrinsicHeight();
            starPadding = mTypedArray.getDimensionPixelSize(R.styleable.StepRatingBar_ratingStarPadding, 10);
        }
        progressPaint=new Paint();
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setColor(Color.WHITE);
    }


    public StepRatingBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StepRatingBar(Context context) {
        this(context, null);
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        float width = getNumStars() * starImageWidth + (getNumStars() - 1) * starPadding;
        float height = this.starImageHeight;
        setMeasuredDimension((int)width,(int) height);
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected synchronized void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
        if(getWidth()<=0||getHeight()<=0)return;

        drawStar(canvas,starFillBitmap,progressPaint);

        Bitmap srcBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);//创建单独的画布
        Canvas srcCanvas = new Canvas(srcBitmap);
        drawStar(srcCanvas,starEmptyBitmap,progressPaint);
        progressPaint.setXfermode(porterDuffXfermode);// 设置混合模式
        progressRect.set(0,0,getWidthForProgress(),getHeight());
        srcCanvas.drawRect(progressRect, progressPaint);
        // 绘制目标图
        canvas.drawBitmap(srcBitmap, 0, 0, null);
        // 清除混合模式
        progressPaint.setXfermode(null);

    }

    private float getWidthForProgress() {
        float rating = getProgress();
        int max = getMax();
        float percent = 1f * rating / max;
        float progressWidth = percent * getWidth();//进度区域的宽度
        return progressWidth;
    }

    void   drawStar(Canvas canvas,Bitmap bitmap,Paint paint){
       float x = 0;
       for (int i = 0; i < getNumStars(); i++) {
           canvas.drawBitmap(bitmap,x,0,paint);
           x = x + bitmap.getWidth() + starPadding;
       }
   }
}
