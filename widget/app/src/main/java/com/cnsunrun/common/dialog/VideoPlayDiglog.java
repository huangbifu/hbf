package com.cnsunrun.common.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wangchao on 2019-01-19.
 */
public class VideoPlayDiglog extends LBaseDialogFragment {
    @BindView(R.id.video_item_player)
    StandardGSYVideoPlayer gsyVideoPlayer;
    Unbinder unbinder;

    public static VideoPlayDiglog newInstance() {
        Bundle args = new Bundle();
        VideoPlayDiglog fragment = new VideoPlayDiglog();
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    protected int getLayoutRes() {
        return R.layout.diglog_video_play;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        gsyVideoPlayer.getFullscreenButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //全屏幕按键处理
                gsyVideoPlayer.startWindowFullscreen(that, true, true);
            }
        });
        gsyVideoPlayer.setUp("https://xihongshi01.oss-cn-shanghai.aliyuncs.com/video/withdraw.mp4", false, "视频教程");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();

    }
}
