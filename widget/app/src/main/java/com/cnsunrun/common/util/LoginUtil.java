package com.cnsunrun.common.util;

import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.config.RongIMHelper;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.login.LoginActivity;
import com.sunrun.sunrunframwork.uiutils.UIUtils;


/**
 * 登录工具类
 * Created by WQ on 2017/1/10.
 */

public class LoginUtil {
    public static void exitLogin() {
        exitLogin( false);
    }

    public static void exitLogin(boolean isOrtherLogin) {
        Config.putLoginInfo(null);
        try {
            CommonApp.getInstance().closeAllActivity(LoginActivity.class);
            RongIMHelper.stopIM();
            PushHelper.stopPush(CommonApp.getInstance());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void closeOther(){
        try {
            CommonApp.getInstance().closeAllActivity(LoginActivity.class);
            RongIMHelper.stopIM();
            PushHelper.stopPush(CommonApp.getInstance());
            UIUtils.shortM("已退出登录");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
