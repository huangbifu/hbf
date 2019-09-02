package com.cnsunrun.common.config;


import android.net.Uri;
import android.util.SparseArray;
import android.util.SparseBooleanArray;

import com.cnsunrun.chat.mode.MemberInfo;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.event.DefaultEvent;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.cnsunrun.common.util.TestTool;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NetUtils;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.AppUtils;
import com.sunrun.sunrunframwork.utils.log.Logger;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

import static com.cnsunrun.common.quest.TokenRequestPreProccess.addToken;

/**
 * 融云用户信息提供者
 * Created by WQ on 2017/5/17.
 */

public class IMUserInfoProvider implements RongIM.UserInfoProvider {

    public static HashMap<String, String> userListUri = new HashMap<>();
    public static HashMap<String, String> userListNic = new HashMap<>();
    private static Set<String> requestSet = Collections.synchronizedSet(new HashSet<String>());
    private static IMUserInfoProvider _INSTANCE=new IMUserInfoProvider();

    public static IMUserInfoProvider getInstance() {
        return _INSTANCE;
    }

    @Override
    public UserInfo getUserInfo(final String userId) {
        final NetSession session = NetSession.instance(CommonApp.getInstance());
        MemberInfo user_info = session.getObject("user_info" + userId, MemberInfo.class);
        if (requestSet.contains(userId)||user_info!=null) {
            return user_info==null?null:( new UserInfo(userId, user_info.getNickname(), Uri.parse("" + AvatarCache(user_info.avatar))));
        }
        String url = BaseQuestConfig.NEIGHBORHOOD_IM_IM_MEMBER_INFO_;
        RequestParams params = new RequestParams();
        params.put("uid", userId);
        params  .put("version_name", AppUtils.getVersionName(CommonApp.getInstance()));
        params    .put("device_type", "Android");
        requestSet.add(userId);
        addToken();
        NetUtils.doGet(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                //
                Logger.E("用户信息请求失败");
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                BaseBean bean = JsonDeal.createBean(s, MemberInfo.class);
                MemberInfo memberInfo = bean.Data();
                session.put("user_info" + userId, memberInfo);
                refreshUserInfo(memberInfo, userId);
            }

            @Override
            public void onFinish() {
                requestSet.remove(userId);
                super.onFinish();
            }
        });
        return refreshUserInfo(user_info, userId);
    }
    public static String AvatarCache(String path){
        if(!String.valueOf(path).contains("time=")) {
            path = path + "?time=" + (System.currentTimeMillis() / (1000 * 10))+""+CACHE_TIME;//10秒缓存
        }
        return path;
    }
    public static long CACHE_TIME=System.currentTimeMillis();
    public static void refreshAvatarCache(){
        CACHE_TIME=System.currentTimeMillis();
    }
    public static UserInfo refreshUserInfo(MemberInfo memberInfo, String userId) {
        if (memberInfo == null) return null;
        UserInfo userInfo = new UserInfo(userId, memberInfo.getNickname(), Uri.parse("" + AvatarCache(memberInfo.avatar)));
        RongIM.getInstance().refreshUserInfoCache(userInfo);
        EventBus.getDefault().post(DefaultEvent.createEvent(Const.EVENT_UPDATE_USERINFO, memberInfo));
        return userInfo;
    }

    public static void focusRefeshUserInfo(String userId){
        final NetSession session = NetSession.instance(CommonApp.getInstance());
        session.remove("user_info" + userId);
        TestTool.invokeMethod(RongUserInfoManager.getInstance(),"clearUserInfoCache");
    }
}
