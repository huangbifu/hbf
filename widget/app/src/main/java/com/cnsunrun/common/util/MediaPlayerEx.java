package com.cnsunrun.common.util;

import java.io.FileDescriptor;

import android.content.Context;
import android.media.MediaExtractor;
import android.media.MediaFormat;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;

public class MediaPlayerEx extends MediaPlayer {
	int mDurationEx,mCurrentPositionEx;
	protected OnErrorListener mOnErrorListener;
	MediaExtractor extractor=new MediaExtractor();
		
	public class MediaProgressThread extends Thread{
		
	}
	public MediaPlayerEx() {
		setOnTimedTextListener(new OnTimedTextListener() {
			
			@Override
			public void onTimedText(MediaPlayer mp, TimedText text) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	@Override
	public void setOnErrorListener(OnErrorListener listener) {
		this.mOnErrorListener=listener;
		super.setOnErrorListener(listener);
	}
	@Override
	public void reset() {
		try {
			super.reset();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public int getDuration() {
		try {
			return mDurationEx=super.getDuration();
		} catch (Exception e) {
			e.printStackTrace();
			///reset();
		}
		return mDurationEx;
	}
	@Override
	public int getCurrentPosition() {
		try {
			return mCurrentPositionEx=super.getCurrentPosition();
		} catch (Exception e) {
			e.printStackTrace();
			//reset();
		}
		return mCurrentPositionEx;
	}
	@Override
	public void setDataSource(FileDescriptor fd, long offset, long length)
			 {
		try {
			super.setDataSource(fd, offset, length);
		
		} catch (Exception e) {
			e.printStackTrace();
			reset();
			if(mOnErrorListener!=null){
				mOnErrorListener.onError(this, -1, -1);
			}
		} 
	}
	@Override
	public void setDataSource(FileDescriptor fd)  {
		try {
			super.setDataSource(fd);
		} catch (Exception e) {
			e.printStackTrace();
			reset();
			if(mOnErrorListener!=null){
				mOnErrorListener.onError(this, -1, -1);
			}
		} 
	}
	@Override
	public void setDataSource(Context context, Uri uri)  {
		try {
			super.setDataSource(context, uri);
		} catch (Exception e) {
			e.printStackTrace();
			reset();
			if(mOnErrorListener!=null){
				mOnErrorListener.onError(this, -1, -1);
			}
		} 
	}
	@Override
	public void setDataSource(String path) {
		try {
			super.setDataSource(path);
//			try{
//			extractor.setDataSource(path);
//			MediaFormat format = extractor.getTrackFormat(0);
//			format.getLong(MediaFormat.KEY_DURATION);
//			}catch(Exception e){
//				e.printStackTrace();
//				Logger.E("解析出错...");
//			}
			//mDurationEx=getDuration();
		} catch (Exception e) {
			e.printStackTrace();
			reset();
			if(mOnErrorListener!=null){
				mOnErrorListener.onError(this, -1, -1);
			}
		}
	}
	@Override
	public boolean isPlaying() {
		try {
			return super.isPlaying();
		} catch (Exception e) {
			e.printStackTrace();
			reset();
		}
		return false;
	}
	
	
	
	@Override
	public void pause()  {
		try {
			super.pause();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			reset();
		}
	}
	@Override
	public void release() {
		try {
			super.release();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Override
	public void start()   {
		try {
			super.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			reset();
		}
	}
	
	@Override
	public void stop()   {
		try {
			super.stop();
		} catch (IllegalStateException e) {
			e.printStackTrace();
			reset();
		}
	}
	@Override
	public void seekTo(int msec)   {
		try {
			super.seekTo(msec);
		} catch (IllegalStateException e) {
			e.printStackTrace();
			reset();
		}
	}
	
}
