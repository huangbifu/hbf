package com.cnsunrun.common.util;

import java.io.File;
import java.io.IOException;
import java.net.URI;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.ResponseHandlerInterface;
import com.sunrun.sunrunframwork.utils.log.Logger;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;

public class VoiceManager implements OnCompletionListener, OnErrorListener,
        OnPreparedListener {
    static VoiceManager _SELF;
    MediaPlayer player;
    String play_url;
    boolean isPause;
    OnVoiceListener onVoiceListener;
    boolean isDestory;

    public MediaPlayer getPlayer() {
        return player;
    }

    // Set<OnVoiceListener>linset=new HashSet<>();
    public static class OnVoiceListener extends Thread {
        public static final int START = 0x001, STOP = 0x002, PAUSE = 0x003,
                PLAYING = 0x00, PREPARE = 0x004;
        Activity mContext;
        boolean isRun = true;
        int state = STOP;
        //MediaPlayer player;
        VoiceManager player;

        public OnVoiceListener(Activity mContext) {
            this.mContext = mContext;
            //setPriority(Thread.MIN_PRIORITY);
        }

        public void setPlayer(VoiceManager player) {
            this.player = player;
        }

        MediaPlayer getPlayer() {
            if (player != null) {
                return player.getPlayer();

            }
            return null;
        }

        public boolean isVaild() {
            return !mContext.isFinishing();
        }

        public void voiceUpdate(int state, int total, int seek) {

        }

       final public void voiceUpdateIm(final int state, final int total,
                                  final int seek) {
            this.state = state;

            mContext.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    int locTotal = total;
                    int locSeek = seek;
                    if (getPlayer() != null && !getPlayer().isPlaying()) {
                        locSeek = 0;
                    }
                    if (locSeek > locTotal) {
                        locSeek = locTotal;
                    }
                    voiceUpdate(state, total, seek);
                }
            });

        }

        @Override
        public void run() {
            while (isVaild() && isRun) {
                try {
                    if (state == PLAYING) {
                        voiceUpdateIm(state, getPlayer().getDuration(),
                                getPlayer().getCurrentPosition());
                    }
                    Thread.sleep(50);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }

        public void stopListener() {
            isRun = false;
            player = null;
        }

    }

    public VoiceManager() {
        createMediaPlayer();
//		player = new MediaPlayerEx();
//
//		player.setOnCompletionListener(this);
//		player.setOnErrorListener(this);
//		player.setOnPreparedListener(this);
//		player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {
//
//			@Override
//			public void onBufferingUpdate(MediaPlayer mp, int percent) {
//
//			}
//		});
    }

    void createMediaPlayer() {
        if (player != null) {
//			player.stop();
            stop();
            //player.reset();
            //player.release();
        }
        player = new MediaPlayerEx();

        player.setOnCompletionListener(this);
        player.setOnErrorListener(this);
        player.setOnPreparedListener(this);
        player.setOnBufferingUpdateListener(new OnBufferingUpdateListener() {

            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {

            }
        });
    }

    String getUrl() {
        return play_url;
    }

    public void voiceUpdate(int state) {
        if (isDestory() && state != OnVoiceListener.STOP) {
            stop();
        }
        if (onVoiceListener == null)
            return;
        if (onVoiceListener.isVaild()) {
            if (isPlaying()) {
                onVoiceListener.voiceUpdateIm(state, getPlayer().getDuration(),
                        getPlayer().getCurrentPosition());
            } else {
                onVoiceListener.voiceUpdateIm(state, 0, 0);
            }
        } else {
            onVoiceListener.stopListener();
        }
    }

    public void addOnVoiceListener(OnVoiceListener lin) {
        if (onVoiceListener != null) {
            voiceUpdate(OnVoiceListener.STOP);
        }
        lin.setPlayer(this);
        lin.start();
        onVoiceListener = lin;
    }

    public MediaPlayer pause() {
        if (isPlaying()) {
            voiceUpdate(OnVoiceListener.PAUSE);
            getPlayer().pause();
            isPause = true;
        }
        return getPlayer();
    }

    public MediaPlayer stop() {
        if (isPlaying()) {
            getPlayer().stop();
            getPlayer().release();
            play_url = null;
        }
        voiceUpdate(OnVoiceListener.STOP);
        play_url = null;
        return getPlayer();
    }

    boolean isPrepare = false;

    public MediaPlayer play(final Context context, String url) {
        try {

            if (isPrepare)
                return getPlayer();
            if (url.equals(play_url)) {// 如果是播放的这个地址已经暂停,继续播放
                if (isPause || !isPlaying()) {
                    isPause = false;
                    getPlayer().start();
                    voiceUpdate(OnVoiceListener.START);
                    voiceUpdate(OnVoiceListener.PLAYING);
                } else {
                    pause();
                }
                return getPlayer();
            }
            if (isPlaying()) {
                //player.stop();
                //voiceUpdate(OnVoiceListener.STOP);
                createMediaPlayer();
            }

            Logger.E("播放地址" + url);
            // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            try {
                // player.setAudioStreamType(AudioManager.STREAM_ALARM);
                // player.setDataSource(context.getAssets().openFd(play_url =
                // url).getFileDescriptor());
//				if(!url.startsWith("http"))
                {
                    getPlayer().reset();
                    AssetFileDescriptor fileDescriptor = context.getAssets()
                            .openFd(play_url = url);
                    getPlayer().setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
                }
            } catch (IOException e) {
                if (!url.startsWith("http")) {
                    e.printStackTrace();
                }
                getPlayer().reset();
                //getPlayer().setDataSource(play_url = url);
            }
            new Thread() {
                public void run() {
                    try {
                        voiceUpdate(OnVoiceListener.PREPARE);
                        isPrepare = true;
                        String path = NetMusicHelper.downFile(context, play_url);
                        if (path != null) {
                            getPlayer().setDataSource(path);
                        }

                        getPlayer().prepare();
                        if (isDestory()) return;
                        getPlayer().start();
                        isPrepare = false;
                        isPause = false;
                        voiceUpdate(OnVoiceListener.START);
                        voiceUpdate(OnVoiceListener.PLAYING);
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        isPrepare = false;
                        voiceUpdate(OnVoiceListener.STOP);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        isPrepare = false;
                        voiceUpdate(OnVoiceListener.STOP);
                    }
                }

                ;
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
            isPause = false;
        }
        return getPlayer();
    }

    public MediaPlayer play(final Context context, String url, final boolean isPlay) {
        try {

            if (isPrepare)
                return getPlayer();
            if (url.equals(play_url)) {// 如果是播放的这个地址已经暂停,继续播放
                if (isPause || !isPlaying()) {
                    isPause = false;
                    getPlayer().start();
                    voiceUpdate(OnVoiceListener.START);
                    voiceUpdate(OnVoiceListener.PLAYING);
                } else {
                    pause();
                }
                return getPlayer();
            }
            if (isPlaying()) {
                //player.stop();
                //voiceUpdate(OnVoiceListener.STOP);
                createMediaPlayer();
            }

            Logger.E("播放地址" + url);
            // Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            try {
                // player.setAudioStreamType(AudioManager.STREAM_ALARM);
                // player.setDataSource(context.getAssets().openFd(play_url =
                // url).getFileDescriptor());
//				if(!url.startsWith("http"))
                {
                    getPlayer().reset();
                    AssetFileDescriptor fileDescriptor = context.getAssets()
                            .openFd(play_url = url);
                    getPlayer().setDataSource(fileDescriptor.getFileDescriptor(),
                            fileDescriptor.getStartOffset(),
                            fileDescriptor.getLength());
                }
            } catch (IOException e) {
                if (!url.startsWith("http")) {
                    e.printStackTrace();
                }
                getPlayer().reset();
                //getPlayer().setDataSource(play_url = url);
            }
            new Thread() {
                public void run() {
                    try {
                        //voiceUpdate(OnVoiceListener.PREPARE);
                        isPrepare = true;
                        String path = NetMusicHelper.downFile(context, play_url);
                        if (path != null) {
                            getPlayer().setDataSource(path);
                        }

                        getPlayer().prepare();
                        if (isDestory() || !isPlay) {
                            isPrepare = false;
                            //getPlayer().start();
                            //getPlayer().pause();
                            isPause = true;
                            if (onVoiceListener != null) {
                                onVoiceListener.voiceUpdateIm(OnVoiceListener.PLAYING, getPlayer().getDuration(),
                                        getPlayer().getCurrentPosition());
                            }

                            return;
                        }
                        getPlayer().start();
                        isPrepare = false;
                        voiceUpdate(OnVoiceListener.START);
                        voiceUpdate(OnVoiceListener.PLAYING);
                    } catch (IllegalStateException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        isPrepare = false;
                        voiceUpdate(OnVoiceListener.STOP);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                        isPrepare = false;
                        voiceUpdate(OnVoiceListener.STOP);
                    }
                }

                ;
            }.start();

        } catch (Exception e) {
            e.printStackTrace();
            isPause = false;
        }
        return getPlayer();
    }

    void down2Cache() {

    }

    public boolean isPlaying() {
        try {
            if (getPlayer() == null) return false;
            if (getPlayer().isPlaying()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean isPlaying(String url) {
        if (isPlaying() && url.equals(play_url)) {
            return true;

        }
        return false;
    }

    public static VoiceManager getInstance() {
        if (_SELF == null) {
            _SELF = new VoiceManager();
        }
        return _SELF;
    }

    public static VoiceManager getNew() {
        return new VoiceManager();
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        Logger.E("onError");
        play_url = null;
        // voiceUpdate(OnVoiceListener.STOP);
        return true;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        Logger.E("onCompletion");
        play_url = null;
        voiceUpdate(OnVoiceListener.STOP);
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }

    public synchronized boolean isDestory() {
        return isDestory;
    }

    public void destory() {
        try {
            isDestory = true;
            stop();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
