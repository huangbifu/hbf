package com.cnsunrun.common;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.bilibili.boxing.BoxingCrop;
import com.bilibili.boxing.BoxingMediaLoader;
import com.bilibili.boxing.loader.IBoxingMediaLoader;
import com.cnsunrun.common.boxing.BoxingGlideLoader;
import com.cnsunrun.common.boxing.BoxingUcrop;
import com.cnsunrun.common.boxing.FixDefaultMediaLoader;
import com.cnsunrun.common.config.RongCloudEvent;
import com.cnsunrun.common.config.RongIMHelper;
import com.cnsunrun.common.quest.TokenRequestPreProccess;
import com.cnsunrun.common.util.OtherDataConvert;
import com.cnsunrun.common.util.PushHelper;
import com.sunrun.sunrunframwork.app.BaseApplication;
import com.sunrun.sunrunframwork.common.DefaultMediaLoader;
import com.sunrun.sunrunframwork.http.NetServer;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import io.rong.imlib.AnnotationNotFoundException;
import io.rong.imlib.RongIMClient;
import io.rong.message.InformationNotificationMessage;


/**
 * Created by WQ on 2017/5/24.
 */

public class CommonApp extends BaseApplication//ConvenientSDKEmphasis
{

    @Override
    public void onCreate() {
        super.onCreate();
        String processName = getCurProcessName(this);
        IBoxingMediaLoader loader = new BoxingGlideLoader();
        BoxingMediaLoader.getInstance().init(loader);
        BoxingCrop.getInstance().init(new BoxingUcrop());
        ((DefaultMediaLoader) DefaultMediaLoader.getInstance()).init(new FixDefaultMediaLoader());
        NetServer.Settings.getSetting().setDataConvert(new OtherDataConvert());
        NetServer.Settings.getSetting().setRequestPreproccess(new TokenRequestPreProccess());
        Bugly.init(getApplicationContext(), "6280ebe27c", false);
        Beta.upgradeCheckPeriod=5*1000;
        MultiDex.install(this);

        //初始化融云
        RongIMHelper.init(this);
        RongCloudEvent.init(this);
        try {
            RongIMClient.registerMessageType(InformationNotificationMessage.class);
        } catch (AnnotationNotFoundException e) {
            e.printStackTrace();
        }
        PushHelper.initPush(this);
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }
    public static BaseApplication getInstance() {
        return BaseApplication.getInstance();
    }

}
