package com.cnsunrun.login.event;

/**
 * Created by cnsunrun on 2017/11/9.
 */

public interface LoginConfig {
    // 用户名
    String ACCOUNT = "account";
    // 密码
    String PWD = "pwd";
    //是否免密
    String IS_SECRET_FREE = "is_secret_free";
    //是否接受新消息通知
    String IS_RECEIVE_MESSAGE ="is_receive_message";
    //是否显示消息详情
    String IS_SHOW_DETAIL_MESSAGE = "is_show_detail_message";
    //声音
    String SOUND_OPEN = "sound_open";
    //震动
   String SHAKE_OPEN = "shake_open";
}
