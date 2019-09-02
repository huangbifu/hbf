package com.cnsunrun.mine.activity;

import android.os.Bundle;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.SwitchButton.SwitchButton;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.login.event.LoginConfig.IS_RECEIVE_MESSAGE;
import static com.cnsunrun.login.event.LoginConfig.IS_SHOW_DETAIL_MESSAGE;
import static com.cnsunrun.login.event.LoginConfig.SHAKE_OPEN;
import static com.cnsunrun.login.event.LoginConfig.SOUND_OPEN;


/**
 * 我的-设置-消息通知
 */
public class MessageNoticeActivity extends LBaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.sb_message_get)
    SwitchButton sbMessageGet;
    @BindView(R.id.sb_message_show)
    SwitchButton sbMessageShow;
    @BindView(R.id.sb_sound)
    SwitchButton sbSound;
    @BindView(R.id.sb_shake)
    SwitchButton sbShake;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_notice);
        ButterKnife.bind(this);
        initSwitchButton();
    }

    private void initSwitchButton() {
        //接收消息
        sbMessageGet.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    Config.putConfigInfo(that,IS_RECEIVE_MESSAGE,"1");
                }else {
                    Config.putConfigInfo(that,IS_RECEIVE_MESSAGE,"0");
                }
            }
        });
        if(Config.getConfigInfo(that,IS_RECEIVE_MESSAGE,"1").equals("1")){
            sbMessageGet.setChecked(true);
        }else {
            sbMessageGet.setChecked(false);
        }
        //显示详情
        sbMessageShow.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    Config.putConfigInfo(that,IS_SHOW_DETAIL_MESSAGE,"1");
                }else {
                    Config.putConfigInfo(that,IS_SHOW_DETAIL_MESSAGE,"0");
                }
            }
        });
        if(Config.getConfigInfo(that,IS_SHOW_DETAIL_MESSAGE,"1").equals("1")){
            sbMessageShow.setChecked(true);
        }else {
            sbMessageShow.setChecked(false);
        }
        //声音
        sbSound.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    Config.putConfigInfo(that,SOUND_OPEN,"1");
                }else {
                    Config.putConfigInfo(that,SOUND_OPEN,"0");
                }
            }
        });
        if(Config.getConfigInfo(that,SOUND_OPEN,"1").equals("1")){
            sbSound.setChecked(true);
        }else {
            sbSound.setChecked(false);
        }
        //震动
        sbShake.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    Config.putConfigInfo(that,SHAKE_OPEN,"1");
                }else {
                    Config.putConfigInfo(that,SHAKE_OPEN,"0");
                }
            }
        });
        if(Config.getConfigInfo(that,SHAKE_OPEN,"1").equals("1")){
            sbShake.setChecked(true);
        }else {
            sbShake.setChecked(false);
        }

    }

}
