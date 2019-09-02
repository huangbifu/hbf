package com.cnsunrun.chat.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.ConversationActivity;
import com.cnsunrun.chat.ConversationOneActivity;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.config.RedPackMessage;
import com.cnsunrun.common.config.RedPackReceivedTipMessage;
import com.cnsunrun.common.config.RongCloudEvent;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.utils.Utils;

import java.util.Collection;

import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

import static com.cnsunrun.chat.ChatGroupSetInfoActivity.isNotify;
import static com.cnsunrun.common.config.RongCloudEvent.isMoment;
import static com.cnsunrun.common.config.RongCloudEvent.isQuck;

/**
 * Created by weiquan on 2018/9/28.
 */
public class CusMessageListAdapter extends MessageListAdapter {
    private Context mContext;
   public boolean isFirstLoad=true;
    int loadSize=0;

   public long currentTime=System.currentTimeMillis();
    public CusMessageListAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    protected void bindView(View v, int position, UIMessage data) {
        super.bindView(v, position, data);
        try {
            ViewHolder holder = (ViewHolder) v.getTag();
            TextView message = holder.contentView.findViewById(android.R.id.text1);
            TextView rc_left = holder.contentView.findViewById(R.id.rc_left);
            TextView rc_right = holder.contentView.findViewById(R.id.rc_right);
            setLeftRightTextColor(message,data);
            setLeftRightTextColor(rc_left,data);
            setLeftRightTextColor(rc_right,data);
        }catch (Exception e){

        }

    }


    private boolean checkIsRedpack(UIMessage uiMessage){

        Message message = uiMessage.getMessage();
        String targetId=null;
//        Uri uri = getUri();
        boolean isRedpack=( message.getContent() instanceof RedPackMessage)||( message.getContent() instanceof RedPackReceivedTipMessage);
        //不是最近的,并且是接收的消息
        if ((!isMoment(message.getSentTime()))&&isRedpack&&message.getMessageDirection()== Message.MessageDirection.RECEIVE) {//大于十分钟 当前时间减发送时间大于十分钟,忽略提示,红包也忽略提示
            return true;
        }
        if((!isFirstLoad)&&isRedpack&&message.getMessageDirection()== Message.MessageDirection.RECEIVE&&isQuck(50))return true;
        if(isRedpack&& !ConversationActivity.isOpened){
            return true;
        }
        return false;
    }

    @Override
    public void add(UIMessage uiMessage, int position) {
        if(checkIsRedpack(uiMessage))return;
        boolean texEnable=false;
        if(mContext instanceof ConversationOneActivity){
            texEnable=true;//单聊才能显示消息
        }
        if(!texEnable&&uiMessage.getContent() instanceof TextMessage)return;
        Message message = uiMessage.getMessage();
        boolean isRedpack=( message.getContent() instanceof RedPackMessage)||( message.getContent() instanceof RedPackReceivedTipMessage);
        if(!isRedpack&&mContext instanceof ConversationActivity)return;//如果红包群不是红包的消息,忽略掉
        if(!isFirstLoad&&( uiMessage.getContent() instanceof RedPackMessage)) {
            if(isNotify( uiMessage.getTargetId()))
            RongCloudEvent.playNotifySounds();
        }
        if(System.currentTimeMillis()-currentTime>2*1000){
            isFirstLoad=false;
        }
        super.add(uiMessage, position);
    }

    @Override
    public void addCollection(UIMessage... collection) {
        if(!ConversationActivity.isOpened)return;
        super.addCollection(collection);
    }

    @Override
    public void addCollection(Collection<UIMessage> collection) {
        if(!ConversationActivity.isOpened)return;
        super.addCollection(collection);
    }

    @Override
    public void add(UIMessage uiMessage) {
        if(checkIsRedpack(uiMessage))return;
        if(!isFirstLoad&&( uiMessage.getContent() instanceof RedPackMessage)) {
            RongCloudEvent.playNotifySounds();
        }
        if(System.currentTimeMillis()-currentTime>2*1000){
            isFirstLoad=false;
        }
        super.add(uiMessage);
    }

    void setLeftRightTextColor(TextView message, UIMessage data){
        if (message != null) {
            if (data.getMessageDirection() == Message.MessageDirection.SEND) {
                message.setTextColor(ContextCompat.getColor(mContext, R.color.white));
            } else {
                message.setTextColor(ContextCompat.getColor(mContext, R.color.text2));
            }
        }
    }
}
