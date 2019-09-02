package com.cnsunrun.common.receiver;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.cnsunrun.chat.logic.HeadClickLogic;
import com.cnsunrun.common.config.RongCloudEvent;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.RedDotUtil;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.DateUtil;
import com.sunrun.sunrunframwork.utils.log.Logger;

import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

import static com.cnsunrun.common.util.RedDotUtil.CODE_;
import static com.cnsunrun.common.util.RedDotUtil.CODE_IM;

/**
 * 处理推送动作
 *
 * @author WQ 下午1:17:37
 */
public class DealPushAction {
    public DealPushAction(Context context, Intent intent) {
        String action = intent.getAction();
        if (action == null)
            return;
        printMsg(intent);
        switch (action) {
            case "cn.jpush.android.intent.NOTIFICATION_OPENED":
                // //通知消息打开
                clickHandler(context, getCustomMsg(intent));
                Logger.E("消息打开");
                break;
            case "cn.jpush.android.intent.REGISTRATION":// 注册成功
                String device_token = intent.getExtras().getString(
                        JPushInterface.EXTRA_REGISTRATION_ID);
                Config.putConfigInfo(context, "device_token", device_token);// 保存设备号
                break;
            case "cn.jpush.android.intent.MESSAGE_RECEIVED":// 接收到了自定义消息
                try {
//                    receiverMsg(context, intent, getNofityID(intent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "cn.jpush.android.intent.NOTIFICATION_RECEIVED":// 接收到了通知消息
                try {
                    receiverMsg(context, intent, getNofityID(intent));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 接收消息时的事务处理
     *
     * @param context
     */
    private void receiverMsg(Context context, Intent  intent, int notifiId) {
        boolean rst = false;
        Bundle bundle = intent.getExtras();
        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
        String message = bundle.getString(JPushInterface.EXTRA_ALERT);
        String extra = getCustomMsg(intent);
        insertMessageToIM(title,message,extra);
    }

    //插入消息到融云里面
    private void insertMessageToIM(String title, String message, String extra) {
        Logger.E(extra+" "+title+"  "+message);
        Message.ReceivedStatus receivedStatus = new Message.ReceivedStatus(0);
        JSONObject jsonObject = com.sunrun.sunrunframwork.utils.JsonDeal.createJsonObj(extra);
        int type = jsonObject.optInt("type", 0);
        if(type==55 ||type==56 || type==57||type==50){
            receivedStatus.setRetrieved();//咵天模块的消息设置为已读
        }
//        receivedStatus.setRetrieved();
        TextMessage txtMessage = TextMessage.obtain(title+"\n"+message);
        txtMessage.setExtra(extra);
        RongIM.getInstance().insertIncomingMessage(Conversation.ConversationType.PRIVATE,"1","1", receivedStatus,txtMessage,new RongIMClient.ResultCallback<Message>(){
            @Override
            public void onSuccess(Message message) {
                Logger.E(""+message);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Logger.E(""+errorCode);
            }
        });
//        RedDotUtil.addNumber(CODE_IM);
        RongCloudEvent.dealTip(txtMessage);
    }

    /**
     * 通知栏点击事件事务处理
     *
     * @param context
     * @param msgJson
     */
    private void clickHandler(Context context, String msgJson) {
        if (!TextUtils.isEmpty(msgJson)) {
            JSONObject jsonObject= com.sunrun.sunrunframwork.utils.JsonDeal.createJsonObj(msgJson);
            int type = jsonObject.optInt("type", 0);
            String id = jsonObject.optString("id");
            HeadClickLogic.dealMessageClick(context, type, id);
        }
    }

    public String getCustomMsg(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null)
            return null;
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        return extras;
    }

    public int getNofityID(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null)
            return 0;
        int notificationId = bundle
                .getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
        return notificationId;
    }

    private void printMsg(Intent intent) {
        Bundle bundle = intent.getExtras();
        if (bundle == null)
            return;
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        String type = bundle.getString(JPushInterface.EXTRA_CONTENT_TYPE);
        Logger.E("消息内容:" + extras + "  " + message + "  " + "  " + type + "  "
                + intent.getAction());

    }
}
