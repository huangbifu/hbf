package com.cnsunrun.common.config;


import android.net.Uri;
import android.text.TextUtils;

import com.cnsunrun.chat.mode.GroupInfoBean;
import com.cnsunrun.chat.mode.GroupItemBean;
import com.cnsunrun.chat.mode.MemberInfo;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.event.DefaultEvent;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.google.gson.reflect.TypeToken;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NetUtils;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.log.Logger;

import org.apache.http.Header;
import org.greenrobot.eventbus.EventBus;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.UserInfo;

import static com.cnsunrun.common.quest.TokenRequestPreProccess.addToken;

/**
 * 融云群组信息提供者
 * Created by WQ on 2017/5/17.
 */

public class IMGroupInfoProvider implements RongIM.GroupInfoProvider {

    private static Set<String> requestSet = Collections.synchronizedSet(new HashSet<String>());
    private static IMGroupInfoProvider _INSTANCE=new IMGroupInfoProvider();

    public static IMGroupInfoProvider getInstance() {
        return _INSTANCE;
    }

    @Override
    public Group getGroupInfo(final String groupId) {
        final NetSession session = NetSession.instance(CommonApp.getInstance());
        GroupItemBean user_info = session.getObject("group_info" + groupId, GroupItemBean.class);
        if (requestSet.contains(groupId)) {
            return user_info==null?null:( new Group(groupId, user_info.title, Uri.parse("" + user_info.image)));
        }
        String url = BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_LIST_;
        RequestParams params = new RequestParams();
        addToken();
        requestSet.add(groupId);
        httpGroupInfo(groupId, session, url, params);
        httpGroupInfo(groupId, session, BaseQuestConfig.GAME_GROUP_LIST, params);
        return refreshUserInfo(user_info, groupId);
    }

    private void httpGroupInfo(final String groupId, final NetSession session, String url, RequestParams params) {
        NetUtils.doGet(url, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
                //
                Logger.E("用户信息请求失败");
            }

            @Override
            public void onSuccess(int i, Header[] headers, String s) {
                BaseBean bean = JsonDeal.createBean(s, new TypeToken<List<GroupItemBean>>(){});
                List<GroupItemBean> groupInfos = bean.Data();
                if(groupInfos!=null){
                    boolean hasGroup=false;
                    for (GroupItemBean groupInfo : groupInfos) {
                        session.put("group_info" + groupInfo.id, groupInfo);
                        if(TextUtils.equals(groupInfo.id,groupId)){
                            refreshUserInfo(groupInfo, groupId);
                            hasGroup=true;
                        }
                    }
                    if(!hasGroup){//如果没有群组信息,移除掉
//                        RongIMHelper.removeGroup(groupId);

                    }
                }
            }

            @Override
            public void onFinish() {
                requestSet.remove(groupId);
                super.onFinish();
            }
        });
    }

    public static Group refreshUserInfo(GroupItemBean groupInfo, String userId) {
        if (groupInfo == null) return null;

        Group userInfo = new Group(userId, groupInfo.title, Uri.parse("" + groupInfo.image));
        RongIM.getInstance().refreshGroupInfoCache(userInfo);
        EventBus.getDefault().post(DefaultEvent.createEvent(Const.EVENT_UPDATE_GROUPINFO, groupInfo));
        return userInfo;
    }

}
