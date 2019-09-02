package com.cnsunrun.common.quest;

import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.model.LoginInfo;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.SyncHttpClient;
import com.sunrun.sunrunframwork.http.BaseRequestPreproccess;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.NetUtils;
import com.sunrun.sunrunframwork.utils.AppUtils;

import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;

import io.rong.imageloader.core.ImageLoader;
import io.rong.imageloader.core.ImageLoaderConfiguration;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_CHANGE_PHOTO_URL;

/**
 * 添加token请求头
 * Created by WQ on 2017/11/21.
 */

public class TokenRequestPreProccess extends BaseRequestPreproccess {

    public TokenRequestPreProccess() {
        AsyncHttpClient synClient = NetUtils.getAsynHttpClient();
        synClient.setTimeout(30000);
        synClient.setConnectTimeout(30000);
        SSLSocketFactoryEx.setSSLSocketFactory(NetUtils.getAsynHttpClient(), addTokenInner());
    }

    @Override
    public NAction proccess(NAction action) {
        addToken();
        if(action.url.equals(GET_CHANGE_PHOTO_URL)){
            NetUtils.getAsynHttpClient().removeAllHeaders();
            LoginInfo loginInfo = Config.getLoginInfo();
            NetUtils.getAsynHttpClient().addHeader("Authorization", loginInfo.token.token_type + " " + loginInfo.token.access_token);
        }
        action  .put("version_name", AppUtils.getVersionName(CommonApp.getInstance()));
        action    .put("device_type", "Android");
        return super.proccess(action);
    }

    public static void addToken() {
        LoginInfo loginInfo = Config.getLoginInfo();
        NetUtils.getAsynHttpClient().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 RedOne (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");

        if (loginInfo.isValid()) {
            NetUtils.getAsynHttpClient().addHeader("Authorization", loginInfo.token.token_type + " " + loginInfo.token.access_token);
            SyncHttpClient syncHttpClient = addTokenInner();
            if (syncHttpClient != null) {
                syncHttpClient.addHeader("Authorization", loginInfo.token.token_type + " " + loginInfo.token.access_token);
                syncHttpClient.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 RedOne (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36");

            }
        }
    }

    public static SyncHttpClient addTokenInner() {
        Field field = null;
        try {
            field = NetUtils.class.getDeclaredField("synClient");
            field.setAccessible(true);
            SyncHttpClient synClient = (SyncHttpClient) field.get(null);
            return synClient;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
