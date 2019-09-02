package com.cnsunrun.common.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.BitmapRequestBuilder;
import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.cnsunrun.R;
import com.cnsunrun.common.util.DisplayUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;


/**
 * 九宫格图片展示控件
 * Created by fan on 16/3/16.
 */
public class MomentPicView extends View {

    protected String[] mImageUrls;
    protected Bitmap[] mBitmaps;
    protected Bitmap mPlaceHolder;
    protected int mHorizontalSpace = 10;
    protected int mVerticalSpace = 10;
    protected int mRadius = 0;
    protected int mImageWidth = 0;
    protected int mImageHeight = 0;
    protected float mRatio = 1f;
    protected int mColumns;
    protected int mRows;
    protected Matrix matrix = new Matrix();
    protected final Paint mPaint = new Paint();
    protected final Paint mTextPaint = new Paint();
    protected final Paint mTextBgPaint = new Paint();
    protected OnClickItemListener onClickItemListener;
    protected OnLongClickItemListener onLongClickItemListener;
    protected MotionEvent mEventDown;
    protected int mDown;
    protected RectF[] mDrawRects;
    protected boolean haveCustomCol;
    protected Target<Bitmap>[] mTargets = new Target[0];
    protected int mpvPrevNum = 9;
    protected int mShowRule = RULE_123;
    protected boolean mIsTest = false;//是否测试模式,预览模式或测试模式下只绘制预览
    /**
     * 显示规则-一张时1列 2 4时2列,其他情况3列
     */
    public final static int RULE_123 = 1;
    /**
     * 显示规则-一张时1列,其他情况3列
     */
    public final static int RULE_13 = 2;
    /**
     * 显示规则-永远3列
     */
    public final static int RULE_3 = 3;
    /**
     * 显示规则-永远1列
     */
    public final static int RULE_1 = 4;
    protected GestureDetector gestureDetector;
    BitmatCreater bitmatCreater;

    public MomentPicView(Context context) {
        this(context, null);
    }

    public MomentPicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MomentPicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MomentPicView);
            try {
                for (int i = 0; i < a.getIndexCount(); i++) {
                    int attr = a.getIndex(i);
                    switch (attr) {
                        case R.styleable.MomentPicView_mpvHorizontalSpace:
                            mHorizontalSpace = a.getDimensionPixelSize(attr, mHorizontalSpace);
                            break;
                        case R.styleable.MomentPicView_mpvVerticalSpace:
                            mVerticalSpace = a.getDimensionPixelSize(attr, mVerticalSpace);
                            break;
                        case R.styleable.MomentPicView_mpvRadius:
                            mRadius = a.getDimensionPixelSize(attr, mRadius);
                            break;
                        case R.styleable.MomentPicView_mpvRatio:
                            mRatio = a.getFloat(attr, mRatio);
                            break;
                        case R.styleable.MomentPicView_mpvPrevNum:
                            mpvPrevNum = a.getInt(attr, 3);
                            break;
                        case R.styleable.MomentPicView_mpvShowRule:
                            mShowRule = a.getInt(attr, RULE_123);
                            break;
                        case R.styleable.MomentPicView_mpvTest:
                            mIsTest = a.getBoolean(attr, mIsTest);
                            break;

                    }
                }
            } finally {
                a.recycle();
            }
        }
        mPaint.setAntiAlias(true);
        mTextPaint.setAntiAlias(true);
        mPlaceHolder = BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_placeholder_image);
        initListener();
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        setImageUrls(new ArrayList<String>());
    }

    private void initListener() {
        gestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent event) {
                return MomentPicView.this.onSingleTapUp(event);
            }

            @Override
            public void onLongPress(MotionEvent event) {
                MomentPicView.this.onLongPress(event);
            }
        });
    }

    public void onLongPress(MotionEvent event) {
        int iUp = getClickItem(mDrawRects, event);
        if (iUp > -1) {
            if (onLongClickItemListener != null) {
                onLongClickItemListener.onLongClick(iUp, new ArrayList<>(Arrays.asList(mImageUrls)));
            }
        }
    }

    public boolean onSingleTapUp(MotionEvent event) {
        int iUp = getClickItem(mDrawRects, event);
        if (iUp > -1) {
            if (onClickItemListener != null) {
                onClickItemListener.onClick(iUp, new ArrayList<>(Arrays.asList(mImageUrls)));
                return true;
            }
        }
        return false;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        for (int i = 0; i < mImageUrls.length; i++) {
            loadBitmap(i, mImageUrls[i]);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mImageUrls.length != 0) {
            int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            mImageWidth = (width - (mColumns - 1) * mHorizontalSpace - getPaddingLeft() - getPaddingRight()) / mColumns;
            mImageHeight = (int) (mImageWidth * mRatio);
            mRows = (int) Math.ceil(mImageUrls.length * 1f / mColumns);
            int height = mImageHeight * mRows + (mRows - 1) * mVerticalSpace + getPaddingTop() + getPaddingBottom();
//            if(RULE_1==mShowRule){
//                height=getPaddingTop() + getPaddingBottom();
//                for (int i = 0; i < mImageUrls.length; i++) {
//                    if (mBitmaps == null||i>=mBitmaps.length||mBitmaps[i]==null) {
//                        height=height+mImageHeight+mVerticalSpace;
//                    }else {
//                        height=height+mBitmaps[i].getHeight();
//                    }
//                }
//            }
            setMeasuredDimension(width, height);
        } else {
            if (isInEditMode()) {
                int width = getDefaultSize(getSuggestedMinimumWidth(), widthMeasureSpec);
                calcueColums(mpvPrevNum);
                mImageWidth = (width - (mColumns - 1) * mHorizontalSpace - getPaddingLeft() - getPaddingRight()) / mColumns;
                mImageHeight = (int) (mImageWidth * mRatio);
                mRows = (int) Math.ceil(mpvPrevNum * 1f / mColumns);
                int height = mImageHeight * mRows + (mRows - 1) * mVerticalSpace + getPaddingTop() + getPaddingBottom();
                setMeasuredDimension(width, height);
                return;
            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode()) {
            drawPreview(canvas);
            return;
        }
        if (mBitmaps.length != 0) {
            for (int row = 0; row < mRows; row++) {
                for (int column = 0; column < mColumns; column++) {
                    int i = row * mColumns + column;
                    boolean isGif;
                    if (i >= mImageUrls.length) {
                        break;
                    } else {
                        isGif = String.valueOf(mImageUrls[i]).toLowerCase().contains(".gif");
                    }
                    Bitmap bitmap = null;
                    if (bitmatCreater != null) {
                        bitmap = bitmatCreater.createBitmap(mImageUrls[i], mImageWidth, mImageWidth);
                    }
                    if (bitmap == null) {
                        bitmap = mBitmaps[i];
                    }
                    if (bitmap == null) {
                        bitmap = mPlaceHolder;
                    }
                    float left = getPaddingLeft() + column * mHorizontalSpace + column * mImageWidth;
                    float top = getPaddingTop() + row * mVerticalSpace + row * mImageHeight;
                    float scale;
                    float dx = 0, dy = 0;

                    int dwidth = bitmap.getWidth();
                    int dheight = bitmap.getHeight();
                    int vwidth = mImageWidth;
                    int vheight = mImageHeight;
                    if (dwidth * vheight > vwidth * dheight) {
                        scale = (float) vheight / (float) dheight;
                        dx = (vwidth - dwidth * scale) * 0.5f;
                    } else {
                        scale = (float) vwidth / (float) dwidth;
                        dy = (vheight - dheight * scale) * 0.5f;
                    }

                    matrix.setScale(scale, scale);
                    matrix.postTranslate(left + Math.round(dx), top + Math.round(dy));

                    BitmapShader mBitmapShader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
                    mBitmapShader.setLocalMatrix(matrix);
                    mPaint.setShader(mBitmapShader);
                    RectF rectF = new RectF();
                    rectF.set(left, top, left + mImageWidth, top + mImageHeight);
                    mDrawRects[i] = rectF;
                    canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
                    if (isGif) {
                        String gifString = "GIF";
                        mTextPaint.setTextSize(DisplayUtil.dpToPx(12));
                        mTextPaint.setColor(Color.WHITE);
                        Rect bounds = new Rect();
                        mTextPaint.getTextBounds(gifString, 0, gifString.length(), bounds);
                        mTextBgPaint.setColor(Color.BLACK);
                        mTextBgPaint.setAlpha(60);
                        RectF rectB = new RectF();
                        rectB.set(left + DisplayUtil.dpToPx(6), top + DisplayUtil.dpToPx(6),
                                left + DisplayUtil.dpToPx(6) * 2 + bounds.width(), top + DisplayUtil.dpToPx(6) * 2 + bounds.height());
                        canvas.drawRoundRect(rectB, mRadius, mRadius, mTextBgPaint);
                        canvas.drawText(gifString, left + DisplayUtil.dpToPx(9), top + DisplayUtil.dpToPx(9) + bounds.height(), mTextPaint);
                    }
                }
            }
        }
    }

    /**
     * 绘制预览
     *
     * @param canvas
     */
    private void drawPreview(Canvas canvas) {
        mDrawRects = new RectF[mpvPrevNum];
        for (int row = 0; row < mRows; row++) {
            for (int column = 0; column < mColumns; column++) {
                int i = row * mColumns + column;
                if (i >= mDrawRects.length)
                    break;
                float left = getPaddingLeft() + column * mHorizontalSpace + column * mImageWidth;
                float top = getPaddingTop() + row * mVerticalSpace + row * mImageHeight;
                RectF rectF = new RectF();
                rectF.set(left, top, left + mImageWidth, top + mImageHeight);
                mDrawRects[i] = rectF;
                mPaint.setColor(Color.parseColor("#cccccc"));
                canvas.drawRoundRect(rectF, mRadius, mRadius, mPaint);
            }
        }
    }

    @Override
    public boolean isInEditMode() {
        return super.isInEditMode() || mIsTest;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (onClickItemListener != null || onLongClickItemListener != null) {
            gestureDetector.onTouchEvent(event);
            return true;
        }
        return
                false;
    }

    protected int getClickItem(RectF[] mDrawRects, MotionEvent event) {
        for (int i = 0; i < mDrawRects.length; i++) {
            if (mDrawRects[i] != null && mDrawRects[i].contains(event.getX(), event.getY())) {
                return i;
            }
        }
        return -1;
    }


    public interface OnClickItemListener {
        void onClick(int i, ArrayList<String> url);
    }

    public interface OnLongClickItemListener {
        void onLongClick(int i, ArrayList<String> url);
    }


    private void loadBitmap(final int i, final String url) {
        if (isInEditMode()) return;
        int resourceId = getResourceId(url);
        DrawableTypeRequest load;
        BitmapRequestBuilder bitmapRequestBuilder;
        if (resourceId == -1) {//判断类型是否为资源
            load = Glide.with(getContext()).load(url);
            bitmapRequestBuilder = load.asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.ALL);
        } else {
            load = Glide.with(getContext()).load(resourceId);
            bitmapRequestBuilder = load.asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE);

        }
        bitmapRequestBuilder.override(mImageWidth, mImageHeight);
        bitmapRequestBuilder
                .into(mTargets[i]);
    }

    private int getResourceId(String url) {
        try {
            return Integer.valueOf(url);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * @param imageUrls
     */
    public void setImageUrls(Collection<String> imageUrls) {
        // clean up outdated stuff
        for (Target target : mTargets) {
            Glide.clear(target); // clears mBitmaps[i] as well
        }
        if (imageUrls == null) imageUrls = Collections.emptyList();
        int newSize = imageUrls.size();
        mImageUrls = imageUrls.toArray(new String[newSize]);
        mBitmaps = new Bitmap[newSize];
        mDrawRects = new RectF[newSize];
        mTargets = new Target[newSize];
        calcueColums(newSize);
        for (int i = 0; i < imageUrls.size(); i++) {
            mTargets[i] = new PositionTarget(i);
        }
        requestLayout();
    }

    void calcueColums(int number) {
        if (haveCustomCol) return;
        switch (mShowRule) {
            case RULE_13:
                mColumns = number == 1 ? 1 : 3;
                break;
            case RULE_123:
                mColumns = number == 1 ? 1 :
                        ((number <= 4 && number % 2 == 0) ? 2 : 3);
                break;
            case RULE_3:
                mColumns = 3;
                break;
            case RULE_1:
                mColumns = 1;
                break;
            default:
                calcueColums(RULE_123);
                break;
        }
    }


    public void setColumns(int columns) {
        if (columns != 0) {
            mColumns = columns;
            haveCustomCol = true;
        } else {
            haveCustomCol = false;
        }
    }


    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    public void setOnLongClickItemListener(OnLongClickItemListener onLongClickItemListener) {
        this.onLongClickItemListener = onLongClickItemListener;
    }

    private void setImageRect(int pos) {
        int column = pos / mColumns;
        int row = pos % mColumns;

        int left = getPaddingLeft() + column * mHorizontalSpace + column * mImageWidth;
        int top = getPaddingTop() + row * mVerticalSpace + mImageHeight;
        invalidate(left, top, left + mImageWidth, top + mImageHeight);
    }

    public void setBitmatCreater(BitmatCreater bitmatCreater) {
        this.bitmatCreater = bitmatCreater;
    }

    private class PositionTarget extends SimpleTarget<Bitmap> {
        private final int i;

        PositionTarget(int i) {
            this.i = i;
        }

        @Override
        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
            mBitmaps[i] = resource;
            setImageRect(i);
        }

    }

    public interface BitmatCreater {
        Bitmap createBitmap(String mImageUrl, int mImageWidth, int imageWidth);
    }
}
