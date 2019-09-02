package com.cnsunrun.common.config;

import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.cnsunrun.chat.ConversationActivity;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.LoginUtil;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.log.Logger;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;

/**
 * Created by WQ on 2017/5/12.
 */

public class RongIMHelper {
    /**
     * <p>连接服务器，在整个应用程序全局，只需要调用一次，需在 {@link } 之后调用。</p>
     * <p>如果调用此接口遇到连接失败，SDK 会自动启动重连机制进行最多10次重连，分别是1, 2, 4, 8, 16, 32, 64, 128, 256, 512秒后。
     * 在这之后如果仍没有连接成功，还会在当检测到设备网络状态变化时再次进行重连。</p>
     *
     * @param token    从服务端获取的用户身份令牌（Token）。
     * @return RongIM  客户端核心类的实例。
     */
    static String cacheToken = "";

    public static void connect(final String token) {
        if (cacheToken.equals(token)) {
            Logger.E("该token:" + token + "已连接,忽略该连接动作");
            return;
        }
        RongIM.connect(token, new RongIMClient.ConnectCallback() {
            /**
             * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
             *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
             */
            @Override
            public void onTokenIncorrect() {
                cacheToken = "";
                Log.e("LoginActivityInfo", "onTokenIncorrect: ");
            }

            /**
             * 连接融云成功
             * @param userid 当前 token 对应的用户 id
             */
            @Override
            public void onSuccess(String userid) {
                cacheToken = token;
                Log.d("LoginActivityInfo", "--onSuccess" + userid);
                try {
                    NetSession session = NetSession.instance(CommonApp.getInstance());
                    String redpack_group_id = session.getString(Config.KEY("redpack_group_id"));
                    if(!TextUtils.isEmpty(redpack_group_id)&& !ConversationActivity.isOpened)
                    {
                        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, redpack_group_id, new RongIMClient.ResultCallback<Boolean>() {
                            @Override
                            public void onSuccess(Boolean aBoolean) {

                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                        RongIMClient.getInstance().cleanRemoteHistoryMessages(Conversation.ConversationType.GROUP, redpack_group_id, 0, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {}

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {}
                        });
                        RongIMClient.getInstance().setOfflineMessageDuration(1, new RongIMClient.ResultCallback<Long>() {
                            @Override
                            public void onSuccess(Long aLong) { }
                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {}
                        });
                        RongIMClient.getInstance().quitGroup(redpack_group_id, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            /**
             * 连接融云失败
             * @param errorCode 错误码，可到官网 查看错误码对应的注释
             */
            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                cacheToken = "";
                Log.e("LoginActivityInfo", "errorCode:" + errorCode);
            }
        });
        initExtensionModules(false);
    }

    public static void connectFocus(){
        String cacheToken = RongIMHelper.cacheToken;
        RongIMHelper.cacheToken="";
        connect(cacheToken);
    }
    public static void connectFocus(String token){
        RongIMHelper.stopIM();
        connect(token);
    }
    public static void init(Context context) {
        RongIM.init(context);

        /**
         * 设置消息体内是否携带用户信息。
         *
         * @param state 是否携带用户信息，true 携带，false 不携带。
         */
//        RongIM.getInstance().setMessageAttachedUserInfo(true);
        RongIM.setUserInfoProvider(new IMUserInfoProvider(), true);
        RongIM.setGroupInfoProvider(new IMGroupInfoProvider(), true);
        RongIM.registerMessageType(RedPackMessage.class);
        RongIM.registerMessageType(RedPackReceivedTipMessage.class);
        RongIM.registerMessageTemplate(new RedpackMessageItemProvider());
        RongIM.registerMessageTemplate(new RedPackReceivedTipMessageItemProvider());
    }

    public static void initExtensionModules(boolean hasTransfer) {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        //查找默认已注册模块
        if (moduleList != null)
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
        if (defaultModule != null) {
            //移除已注册的默认模块，替换成自定义模块
            RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
            RongExtensionManager.getInstance().registerExtensionModule(new SealExtensionModule(hasTransfer));
        }
    }

    public static void stopIM() {
        cacheToken="";
        RongIM.getInstance().disconnect();
    }

    public static boolean  isConnect(){
        return !EmptyDeal.empty(cacheToken);
    }
//    public static  void saveUserInfo(Context context) {
//        /**
//         * 设置当前用户信息，
//         *
//         * @param userInfo 当前用户信息
//         */
//        RongIM.getInstance().setCurrentUserInfo(new UserInfo(Config.getLoginInfo().getId(),
//                ACache.get(context).getAsString(CacheConfig.USERNAME).nickname,Uri.parse(personalCenterInfo.avatar)));
//    }

    public static void removeGroup(String targetId){
        RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetId, new RongIMClient.ResultCallback<Boolean>() {
            @Override
            public void onSuccess(Boolean aBoolean) {
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {

            }
        });
    }

    public static Uri generateUri(Context context, Map<String, Boolean> supportedConversation) {
        Uri.Builder builder = Uri.parse("rong://" + context.getApplicationInfo().packageName).buildUpon().appendPath("conversationlist");
        if (supportedConversation != null && supportedConversation.size() > 0) {
            Set keys = supportedConversation.keySet();
            Iterator i$ = keys.iterator();

            while (i$.hasNext()) {
                String key = (String) i$.next();
                builder.appendQueryParameter(key, ((Boolean) supportedConversation.get(key)).booleanValue() ? "true" : "false");
            }
        }
        return builder.build();
    }
}
