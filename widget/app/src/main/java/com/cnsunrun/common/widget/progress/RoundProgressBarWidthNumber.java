package com.cnsunrun.common.widget.progress;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.cnsunrun.R;

public class RoundProgressBarWidthNumber extends
		HorizontalProgressBarWithNumber
{
	/**
	 * mRadius of view
	 */
	private int mRadius = dp2px(30);
	private int mMaxPaintWidth;
	private String progress_text_unit;
	public RoundProgressBarWidthNumber(Context context)
	{
		this(context, null);
	}

	public RoundProgressBarWidthNumber(Context context, AttributeSet attrs)
	{
		super(context, attrs);

		mReachedProgressBarHeight = (int) (mUnReachedProgressBarHeight * 2.5f);
		TypedArray ta = context.obtainStyledAttributes(attrs,
				R.styleable.RoundProgressBarWidthNumber);
		mRadius = (int) ta.getDimension(
				R.styleable.RoundProgressBarWidthNumber_radius, mRadius);

		progress_text_unit=ta.getString(R.styleable.RoundProgressBarWidthNumber_progress_text_unit);
		if(progress_text_unit==null){
			progress_text_unit="s";
		}
		mPaint.setStyle(Style.STROKE);
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStrokeCap(Cap.ROUND);
		ta.recycle();
	}

	/**
	 * 这里默认在布局中padding值要么不设置，要么全部设置
	 */
	@Override
	protected synchronized void onMeasure(int widthMeasureSpec,
			int heightMeasureSpec)
	{

		mMaxPaintWidth = Math.max(mReachedProgressBarHeight,
				mUnReachedProgressBarHeight);
		int expect = mRadius * 2 + mMaxPaintWidth + getPaddingLeft()
				+ getPaddingRight();
		int width = resolveSize(expect, widthMeasureSpec);
		int height = resolveSize(expect, heightMeasureSpec);
		int realWidth = Math.min(width, height);

		mRadius = (realWidth - getPaddingLeft() - getPaddingRight() - mMaxPaintWidth) / 2;

		setMeasuredDimension(realWidth, realWidth);

	}
	int unit=100;

	public void setUnit(int unit) {
		this.unit = unit;
	}

	boolean isPecent=true;//是否百分比

	public void setPecent(boolean pecent) {
		isPecent = pecent;
	}

	@Override
	protected synchronized void onDraw(Canvas canvas)
	{
		float radio = getProgress() * 1.0f / getMax();
		String text =isPecent? (int)(radio*unit) + progress_text_unit:(getProgress()+progress_text_unit);
		float textWidth = mPaint.measureText(text);
		float textHeight = (mPaint.descent() + mPaint.ascent()) / 2;

		canvas.save();
		canvas.translate(getPaddingLeft() + mMaxPaintWidth / 2, getPaddingTop()
				+ mMaxPaintWidth / 2);
		mPaint.setStyle(Style.STROKE);
		// draw unreaded bar
		mPaint.setColor(mUnReachedBarColor);
		mPaint.setStrokeWidth(mReachedProgressBarHeight);
		canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0,
				359.99f, false, mPaint);
		// draw reached bar
		mPaint.setColor(mReachedBarColor);
		mPaint.setStrokeWidth(mReachedProgressBarHeight);
		float sweepAngle = getProgress() * 1.0f / getMax() * 360;
		canvas.drawArc(new RectF(0, 0, mRadius * 2, mRadius * 2), 0,
				sweepAngle, false, mPaint);
		// draw text
		mPaint.setStyle(Style.FILL);
		canvas.drawText(text, mRadius - textWidth / 2, mRadius - textHeight,
				mPaint);

		canvas.restore();

	}

}