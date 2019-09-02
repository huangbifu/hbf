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
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.cnsunrun.R;
import com.cnsunrun.common.util.DisplayUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;


/**
 * 图片上传展示控件
 * Created by WQ on 16/3/16.
 */
public class UploadPicView extends MomentPicView implements MomentPicView.OnClickItemListener, MomentPicView.OnLongClickItemListener {
    protected RectF[] mDelRects;//删除按钮区域
    protected List<String> currentData;//当前实际图片数目
    protected String addImgResId = "-1";//添加按钮id
    protected int maxImg = 9;//最大张数
    protected boolean isAddImgInStart = false;//是否第一个是加号

    protected Bitmap deleteImg;//删除按钮

    public UploadPicView(Context context) {
        this(context, null);
    }

    public UploadPicView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public UploadPicView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.MomentPicView);
            addImgResId = "" + a.getResourceId(R.styleable.MomentPicView_mpvAddIcon, -1);
            isAddImgInStart = a.getBoolean(R.styleable.MomentPicView_mpvAddIconInStart, isAddImgInStart);
            maxImg = a.getInt(R.styleable.MomentPicView_mpvMaxImg, maxImg);
            setDeleteImg(a.getResourceId(R.styleable.MomentPicView_mpvDelIcon, -1));
        }
        this.onClickItemListener = this;
        this.onLongClickItemListener = this;
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        int clickItem = getClickItem(mDelRects, event);
        if (clickItem != -1) {//点到删除按钮了,移除指定元素
            if (isAddImgInStart) {
                currentData.remove(clickItem - 1);
            } else {
                currentData.remove(clickItem);
            }
            ArrayList<String> newImageUrls = new ArrayList<>(currentData);
            currentData.clear();
            setImageUrls(newImageUrls);
            return true;
        }
        return super.onSingleTapUp(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int length = mDelRects.length;
        if (length != 0) {
            for (int row = 0; row < mRows; row++) {
                for (int column = 0; column < mColumns; column++) {
                    int i = row * mColumns + column;
                    /**
                     * 判断加号,不绘制删除按钮
                     */
                    if (i >= length ||
                            (!isMax() && isAddImgInStart && i == 0) ||
                            (!isMax() && !isAddImgInStart && i == length - 1)) {
                        break;
                    }
                    if(mDelRects[i]==null){
                        mDelRects[i]=new RectF();
                    }
                    int width = deleteImg.getWidth();
                    int height = deleteImg.getHeight();
                    float left = getPaddingLeft() + column * mHorizontalSpace + (column + 1) * mImageWidth - width/2;
                    float top = getPaddingTop() + row * mVerticalSpace + row * mImageHeight-height/2;
                    mDelRects[i].set(left, top, left + width, top + height);
//                    mDelRects[i] = new RectF(left, top, left + width, top + height);
                    canvas.drawBitmap(deleteImg, left, top, mPaint);
                }
            }
        }
    }

    //设置图片url
    @Override
    public void setImageUrls(Collection<String> imageUrls) {
        if (currentData == null) currentData = new ArrayList<>();
        currentData.addAll(imageUrls);
        ArrayList<String> newImageUrls = new ArrayList<>(currentData);
        if (newImageUrls.size() < maxImg) {
            if (isAddImgInStart) {
                newImageUrls.add(0, addImgResId);
            } else {
                newImageUrls.add(addImgResId);
            }
        }
        mDelRects = new RectF[newImageUrls.size()];
        super.setImageUrls(newImageUrls);
    }
    //获取总图片数目
    public int getImageCount(){
        return currentData.size();
    }
    public boolean isMax() {
        return currentData != null && currentData.size() >= maxImg;
    }

    /**
     * 获取待上传的文件路径集合
     *
     * @return
     */
    public List<String> getImageFilePaths() {
        return new ArrayList<>(currentData);
    }

    /**
     * 获取待上传的文件集合
     *
     * @return
     */
    public List<File> getImageFiles() {
        ArrayList<File> newImageUrls = new ArrayList<>();
        for (String filePath : currentData) {
            newImageUrls.add(new File(filePath));
        }
        return newImageUrls;
    }

    /**
     * 设置删除按钮资源
     *
     * @param deleteImgRes
     */
    public void setDeleteImg(int deleteImgRes) {
        if (deleteImgRes == -1) return;
        deleteImg = BitmapFactory.decodeResource(getResources(), deleteImgRes);
    }

    protected OnClickItemListener clickItemListener;
    protected OnLongClickItemListener longClickItemListener;
    protected OnClickListener onAddBtnListener;

    @Override
    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.clickItemListener = onClickItemListener;
    }

    @Override
    public void setOnLongClickItemListener(OnLongClickItemListener onLongClickItemListener) {
        this.longClickItemListener = onLongClickItemListener;
    }

    public void setOnAddBtnListener(OnClickListener onAddBtnListener) {
        this.onAddBtnListener = onAddBtnListener;
    }

    @Override
    public void onClick(int i, ArrayList<String> url) {
        if ((isAddImgInStart && !isMax() && i == 0) ||
                (!isAddImgInStart && !isMax() && i == currentData.size() )) {
            if (onAddBtnListener != null)
                onAddBtnListener.onClick(this);
        } else if (clickItemListener != null) {
            clickItemListener.onClick(i, url);
        }
    }

    @Override
    public void onLongClick(int i, ArrayList<String> url) {
        if ((isAddImgInStart && !isMax() && i == 0) ||
                (!isAddImgInStart && !isMax() && i == currentData.size() - 1)) {
            if (onAddBtnListener != null)
                onAddBtnListener.onClick(this);
        } else if (longClickItemListener != null) {
            longClickItemListener.onLongClick(i, url);
        }
    }



}
