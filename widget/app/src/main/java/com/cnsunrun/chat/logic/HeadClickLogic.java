package com.cnsunrun.chat.logic;

import android.content.Context;
import android.view.View;

import com.cnsunrun.chat.ConversationActivity;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.config.RedPackMessage;
import com.cnsunrun.common.config.RedPackReceivedTipMessage;
import com.sunrun.sunrunframwork.uibase.BaseActivity;
import com.sunrun.sunrunframwork.utils.JsonDeal;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.math.BigDecimal;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;
import io.rong.message.TextMessage;

/**
 * 会话页面,头像点击处理逻辑
 * Created by WQ on 2017/12/8.
 */

public class HeadClickLogic implements RongIM.ConversationBehaviorListener {
    BaseActivity that;
    String sysUserId="1";
    public HeadClickLogic(BaseActivity that) {
        this.that = that;
    }

    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        String id = userInfo.getUserId();
        if(sysUserId.equals(id))return false;
//        CommonIntent.startChatUserInfoActivity(that, id, true);
        return false;
    }

    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        if(message .getContent() instanceof RedPackMessage){

            return false;
        }
        if(message .getContent()  instanceof RedPackReceivedTipMessage){
            RedPackReceivedTipMessage content = (RedPackReceivedTipMessage) message.getContent();
//            if(content.getType()==1) {
                ConversationActivity.isOtherPage=true;
                CommonIntent.startRedpackPageActivity(view.getContext(),content.getRepack_id(),message.getTargetId(),content.getType()+"");
//            }
//            CommonIntent.startRedpackPageActivity(that,content.getRepack_id());
            return false;
        }
        if(!sysUserId.equals(message.getSenderUserId()))return false;
//        TextMessage textMessage = (TextMessage) message.getContent();
//        JSONObject jsonObject= JsonDeal.createJsonObj(textMessage.getExtra());
//        int type = jsonObject.optInt("type", 0);
//        String id = jsonObject.optString("id");
//        dealMessageClick(that, type, id);
        return false;
    }

    public static void dealMessageClick(Context that, int type, String id) {
        switch (type){
//            case 50://好友请求
//                CommonIntent.startChatNewFriendsActivity(that);
//                break;
//            case 55:
//            case 56:
//            case 57://动态消息
//                CommonIntent.startChatDynamicsDetailsActivity(that,id);
//                break;
//            case 60://活动报名
//                CommonIntent.startMeetingApplyListActivity(that,id);
//                break;
//            case 11://报修
//                CommonIntent.startRepairsDetailsActivity(that,id);
//                break;
//            case 12://投诉
//                CommonIntent.startComplaintsDetailsActivity(that,id);
//                break;
//            case 10://公告
//                CommonIntent.startNoticeMessageDetailsActivity(that,id,"");
//                break;

        }
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        if(sysUserId.equals(message.getSenderUserId()))return true;
        return false;
    }
}
