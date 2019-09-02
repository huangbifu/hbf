package com.cnsunrun.common.quest;

import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.model.LoginInfo;
import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.http.BaseConfig;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.utils.AppUtils;


/**
 * App配置信息
 */

public class Config extends BaseConfig {

    public static final String START_NUM = "run_num";// 程序启动次数
    public static final String START_IMG = "guide";// 引导页图片路径
    public static final String ACCOUNT = "account";
    public static final String PASSWORD = "password";


    /**
     * 获取登录信息
     *
     * @return 不会为null(没有时返回空的LoginInfo对象)
     */
    public static LoginInfo getLoginInfo() {
        LoginInfo info = getDataCache(BaseConfig.LOGIN_INFO,
                new TypeToken<LoginInfo>() {});
        return info == null ? new LoginInfo() : info;
    }

    public static NAction UAction() {
        return new NAction()
                .put("version_name", AppUtils.getVersionName(CommonApp.getInstance()))
                .put("device_type", "Android")
                .put("community_id", getLoginInfo().getCommunity_id())
                .put("district_id",getLoginInfo().getDistrict_id());
    }

    public static String  KEY(String key){
        return Config.getLoginInfo().getId()+"_"+key;
    }


}
