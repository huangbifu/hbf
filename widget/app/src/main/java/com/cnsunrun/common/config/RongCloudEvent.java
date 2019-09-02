package com.cnsunrun.common.config;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;

import com.cnsunrun.chat.ConversationActivity;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.event.RedDotEvent;
import com.cnsunrun.common.event.RefreshIMTokenEvent;
import com.cnsunrun.common.logic.NotificationLogic;
import com.cnsunrun.common.model.LoginInfo;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.LoginUtil;
import com.cnsunrun.common.util.RedDotUtil;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;
import com.sunrun.sunrunframwork.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.Utils;
import com.sunrun.sunrunframwork.utils.log.Logger;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.util.UUID;

import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Group;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.CommandMessage;
import io.rong.message.ImageMessage;
import io.rong.message.InformationNotificationMessage;
import io.rong.message.TextMessage;
import io.rong.message.VoiceMessage;

import static com.cnsunrun.common.util.RedDotUtil.CODE_;
import static com.cnsunrun.common.util.RedDotUtil.CODE_50;
import static com.cnsunrun.common.util.RedDotUtil.TYPE_60;
import static com.cnsunrun.common.util.RedDotUtil.idKey;
import static com.cnsunrun.login.event.LoginConfig.IS_RECEIVE_MESSAGE;
import static com.cnsunrun.login.event.LoginConfig.IS_SHOW_DETAIL_MESSAGE;
import static com.cnsunrun.login.event.LoginConfig.SHAKE_OPEN;
import static com.cnsunrun.login.event.LoginConfig.SOUND_OPEN;

public class RongCloudEvent implements RongIMClient.OnReceiveMessageListener, RongIMClient.ConnectionStatusListener {
    private static RongCloudEvent mRongCloudInstance;
    //半个小时
    protected long time = 1000 * 30 * 60;

    /**
     * 最近的 一分钟
     * @param sentTime
     * @return
     */
    public static boolean  isMoment(long sentTime){
//        if(isQuck(300))return false;
        return System.currentTimeMillis()-sentTime<1*60*1000;
//       return System.currentTimeMillis()-sentTime<5*1000;
    }
    @Override
    public boolean onReceived(Message message, int i) {
        String orderNum = "";
        MessageContent content = message.getContent();
        if (content instanceof TextMessage) {
            TextMessage textMessage = (TextMessage) content;
            dealTip(textMessage);
        } else if (content instanceof VoiceMessage) {
            VoiceMessage voiceMessage = (VoiceMessage) content;
            orderNum = voiceMessage.getExtra();
        } else if (content instanceof ImageMessage) {
            ImageMessage imageMessage = (ImageMessage) content;
            orderNum = imageMessage.getExtra();
        } else if (content instanceof InformationNotificationMessage) {
            InformationNotificationMessage informationNotificationMessage = (InformationNotificationMessage) content;
            String msgContent = informationNotificationMessage.getMessage();
        } else if (content instanceof CommandMessage) {
            CommandMessage commandMessage = (CommandMessage) content;
            String cmdName = commandMessage.getName();
            String targetId = commandMessage.getData();
            if ("delete_user".equals(cmdName)) {
                RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, targetId);
            }else if("add_user".equals(cmdName)){
                RedDotUtil.addNumber(CODE_50);
                EventBus.getDefault().post(RedDotEvent.newInstance());
                playNotifySounds();
                playVibrator();
                return false;
            }
        }
        return dealNotificationTip(message);
    }

    public static boolean dealNotificationTip(Message message) {
        boolean isRedpack=( message.getContent() instanceof RedPackMessage)||( message.getContent() instanceof RedPackReceivedTipMessage);
        isRedpack=isRedpack&&message.getMessageDirection()== Message.MessageDirection.RECEIVE;
        if (System.currentTimeMillis()-message.getSentTime()>10*60*1000) {//大于十分钟 当前时间减发送时间大于十分钟,忽略提示,红包也忽略提示
//            RongIMClient.getInstance().deleteMessages(new int[]{message.getMessageId()});

            RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.GROUP, message.getTargetId());
//            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()});
            return true;
        }
        if(isRedpack){
            RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.GROUP, message.getTargetId());
        }
        if(isRedpack&& !ConversationActivity.isOpened){
//            RongIM.getInstance().deleteMessages(new int[]{message.getMessageId()});
//            RongIMClient.getInstance().deleteMessages(new int[]{message.getMessageId()});

            return true;
        }
        if(( message.getContent() instanceof RedPackMessage)&&isMoment(message.getSentTime())){
//            if(!isQuck(1000))

//            playVibrator();
            return true;//红包不给提醒
        }
        if(isRedpack)return true;
        String targetId = message.getTargetId();
        String extra = message.getExtra();
        String pushContent = "message";
        MessageContent content = message.getContent();
        pushContent = getMessageDesc(content);
        return showImNotification(CommonApp.getInstance(), targetId, "", "", extra, pushContent,message.getConversationType());
    }

    public static void dealTip(TextMessage textMessage) {
        JSONObject jsonObject = JsonDeal.createJsonObj(textMessage.getExtra());
        int type = jsonObject.optInt("type", 0);
        String id = jsonObject.optString("id");
        switch (type) {
            case 10:
            case 11:
            case 12:
            case 50:
            case 55:
            case 56:
            case 57:
            case 58:
            case 60:
                RedDotUtil.addNumber(CODE_ + type);
                break;
            default:

                break;
        }
        RedDotUtil.addNumber(idKey(id, type));
        EventBus.getDefault().post(RedDotEvent.newInstance());
    }

    public static boolean showImNotification(Context context, String targetId, String pushData, String pushId, String extra, String pushContent, Conversation.ConversationType conversationType) {
        NotificationLogic notificationLogic = new NotificationLogic(context);
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent
                .FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.setAction(Intent.ACTION_VIEW);
        String targName = null;
        UserInfo userInfo = RongUserInfoManager.getInstance().getUserInfo(targetId);
        Group groupInfo = IMGroupInfoProvider.getInstance().getGroupInfo(targetId);
        if(groupInfo!=null){
            targName=groupInfo.getName();
        }
        if (userInfo != null) {
            targName = userInfo.getName();
        }
        Uri uri = Uri.parse("rong://" + context.getPackageName()).buildUpon().appendPath("conversation")
                .appendPath(conversationType.getName().toLowerCase())
                .appendQueryParameter("targetId", targetId)
                .appendQueryParameter("title", targName).build();
        intent.setData(uri);
        int nofityId = String.valueOf(targetId).hashCode();
        intent.putExtra("notifyId", nofityId);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, UUID.randomUUID().hashCode(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        int notifyMode = 0;

        boolean isRing = Config.getConfigInfo(context, SOUND_OPEN, "1").equals("1");
        boolean isVibrate = Config.getConfigInfo(context, SHAKE_OPEN, "1").equals("1");
        boolean isMessageTip = Config.getConfigInfo(context, IS_RECEIVE_MESSAGE, "1").equals("1");
        boolean isShowDetails = Config.getConfigInfo(context, IS_SHOW_DETAIL_MESSAGE, "1").equals("1");
//        LoginInfo loginInfo = Config.getLoginInfo();
//        Config.putConfigInfo(that, SOUND_OPEN, "1");
        if (!isRing && !isVibrate) {
//            return true;
            notifyMode = Notification.DEFAULT_LIGHTS;
        }
        if (isRing && isVibrate) {
            // 震动加响铃
            notifyMode = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;
        } else {
            if (isRing) {
                notifyMode = Notification.DEFAULT_SOUND;
            }
            if (isVibrate) {
                notifyMode = Notification.DEFAULT_VIBRATE;
            }
        }
        if(!isShowDetails){
            targName=null;
            pushContent="接到一条新消息";
        }
        if (isMessageTip) {
            notificationLogic.showNotify(pendingIntent, targName, pushContent, 0, notifyMode, nofityId);
        }
        return true;
    }

    public static String getMessageDesc(MessageContent content) {
        String pushContent = "";
        if (content instanceof TextMessage) {
            pushContent = ((TextMessage) content).getContent();
        } else if (content instanceof VoiceMessage) {
            pushContent = "[语音]";
        } else if (content instanceof ImageMessage) {
            pushContent = "[图片]";
        } else if (content instanceof RedPackMessage) {
            pushContent = "[红包]";
        } else if (content instanceof RedPackReceivedTipMessage) {
            pushContent = ((RedPackReceivedTipMessage) content).getTip_content();
        }
        return pushContent;
    }

    /**
     * 初始化 RongCloud.
     *
     * @param context 上下文。
     */
    public static void init(Context context) {
        if (mRongCloudInstance == null) {
            synchronized (RongCloudEvent.class) {
                if (mRongCloudInstance == null) {
                    mRongCloudInstance = new RongCloudEvent(context);
                }
            }
        }
    }

    /**
     * 构造方法。
     *
     * @param context 上下文。
     */
    private RongCloudEvent(Context context) {
        initDefaultListener();
        //添加消息长按收藏功能
//        CollectLongClickAction.addCollectAction();
    }

    /**
     * 获取RongCloud 实例。
     *
     * @return RongCloud。
     */
    public static RongCloudEvent getInstance() {
        return mRongCloudInstance;
    }

    /**
     * RongIM.init(this) 后直接可注册的Listener。
     */
    private void initDefaultListener() {
        RongIM.setOnReceiveMessageListener(this);//设置消息接收监听器
        RongIM.setConnectionStatusListener(this);
    }

    @Override
    public void onChanged(ConnectionStatus connectionStatus) {
        Logger.D("连接状态:" + connectionStatus);

        EventBus.getDefault().post(connectionStatus);
        switch (connectionStatus) {
            case CONNECTING:
            case CONNECTED:
                break;
            case KICKED_OFFLINE_BY_OTHER_CLIENT://其他设备登录
                RongIMHelper.cacheToken="";
                EventBus.getDefault().post(RefreshIMTokenEvent.newInstance());//刷新Token
//                AHandler.runTask(new AHandler.Task() {
//                    @Override
//                    public void update() {
//                        super.update();
//                        UIUtils.longM("您的账号已在其他设备登录");
//                    }
//                });
//                LoginUtil.exitLogin();
//                CommonIntent.startLoginActivity();
                break;
            default:
                RongIMHelper.cacheToken="";
                break;
        }
    }

    /**
     * 播放通知聲音
     */
    public static void playNotifySounds() {
        try {
             Ringtone ringtone;
            Uri ringUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            ringtone = RingtoneManager.getRingtone(CommonApp.getInstance(), ringUri);
            ringtone.play();
        } catch (Exception e) {
//            UIUtils.shortM("No Vibrator Permission ");
            e.printStackTrace();
        }

    }


    /**
     * 播放震动
     */
    private static void playVibrator() {
        try {
            ((Vibrator) CommonApp.getInstance().getSystemService(Context.VIBRATOR_SERVICE)).vibrate(500L);
        } catch (Exception e) {
//            UIUtils.shortM("No Vibrator Permission ");
            e.printStackTrace();
        }
    }


    private static long timeQuick=System.currentTimeMillis();
    public static boolean isQuck(long mm) {
        long temp = System.currentTimeMillis();
        if (temp - timeQuick <= mm) {
            timeQuick = temp;
            return true;
        } else {
            timeQuick = temp;
            return false;
        }
    }
}
