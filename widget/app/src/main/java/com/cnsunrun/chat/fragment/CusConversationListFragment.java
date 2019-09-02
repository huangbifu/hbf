package com.cnsunrun.chat.fragment;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.cnsunrun.chat.ConversationActivity;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.config.RedPackMessage;
import com.cnsunrun.common.config.RedPackReceivedTipMessage;
import com.cnsunrun.common.config.RongCloudEvent;
import com.cnsunrun.common.dialog.DataLoadDialogFragment;
import com.cnsunrun.common.event.RefreshIMTokenEvent;
import com.sunrun.sunrunframwork.http.cache.NetSession;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imkit.model.Event;
import io.rong.imkit.model.UIConversation;
import io.rong.imkit.widget.adapter.ConversationListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

/**
 * Created by weiquan on 2018/10/16.
 */
public class CusConversationListFragment extends ConversationListFragment {

    private ConversationListAdapter conversationListAdapter;

    @Override
    public void onEventMainThread(Event.OnReceiveMessageEvent event) {
        Message message = event.getMessage();
        String targetId = null;
        Uri uri = getUri();
        boolean isRedpack = (message.getContent() instanceof RedPackMessage) || (message.getContent() instanceof RedPackReceivedTipMessage);
        isRedpack = isRedpack && message.getMessageDirection() == Message.MessageDirection.RECEIVE;
        if (System.currentTimeMillis() - message.getSentTime() > 5 * 60 * 1000) {//大于十分钟 当前时间减发送时间大于十分钟,忽略提示,红包也忽略提示
            if ((targetId = uri.getQueryParameter("targetId")) != null) {
                RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.GROUP, targetId);
            }

            return;
        }
        if (isRedpack ) {
            RongIM.getInstance().clearMessagesUnreadStatus(Conversation.ConversationType.GROUP, message.getTargetId());
            return;
        }
        super.onEventMainThread(event);
    }

    @Override
    public void getConversationList(Conversation.ConversationType[] conversationTypes,final IHistoryDataResultCallback<List<Conversation>> callback) {
        RongIMClient.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            public void onSuccess(List<Conversation> conversations) {
                if (CusConversationListFragment.this.getActivity() != null && !CusConversationListFragment.this.getActivity().isFinishing()) {
                    if (callback != null) {
                        List<Conversation> resultConversations = new ArrayList();
                        if (conversations != null) {
                            Iterator var3 = conversations.iterator();
                            while(var3.hasNext()) {
                                Conversation conversation = (Conversation)var3.next();
                                if (!CusConversationListFragment.this.shouldFilterConversation(conversation.getConversationType(), conversation.getTargetId())) {
                                    MessageContent latestMessage = conversation.getLatestMessage();
                                    boolean isRedpack=( latestMessage instanceof RedPackMessage)||( latestMessage instanceof RedPackReceivedTipMessage);
                                    if(!isRedpack) {
                                        resultConversations.add(conversation);
                                    }
                                }
                            }
                        }
                        callback.onResult(resultConversations);
                    }
                }
            }

            public void onError(RongIMClient.ErrorCode e) {
                if (callback != null) {
                    callback.onError();
                }

            }
        }, conversationTypes);
        super.getConversationList(conversationTypes, callback);
    }

    public ConversationListAdapter onResolveAdapter(Context context) {
        conversationListAdapter = super.onResolveAdapter(context);
        return conversationListAdapter;
    }

    @Override
    public void onItemClick(final AdapterView<?> parent, final View view, final int tmposition, final long id) {
        final int position=    tmposition - ((ListView)parent).getHeaderViewsCount() ;
        if(position<0){
            EventBus.getDefault().post(RefreshIMTokenEvent.newInstance());
//            refreshIMToken(RefreshIMTokenEvent.newInstance());
        }
        if (conversationListAdapter != null&&position>=0) {
           final UIConversation item = conversationListAdapter.getItem(position);
            MessageContent messageContent = item.getMessageContent();
            boolean isRedpack = (messageContent instanceof RedPackMessage) || (messageContent instanceof RedPackReceivedTipMessage);
//            isRedpack=isRedpack&&message.getMessageDirection()== Message.MessageDirection.RECEIVE;
            NetSession instance = NetSession.instance(getContext());
            if (isRedpack||instance.getBoolean("is_redpack"+item.getConversationTargetId())) {
                instance.put("is_redpack"+item.getConversationTargetId(),true);
                RongIM.getInstance().deleteMessages(Conversation.ConversationType.GROUP, item.getConversationTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        RongIM.getInstance().joinGroup(item.getConversationTargetId(), item.getUIConversationTitle(), new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                CusConversationListFragment.super.onItemClick(parent, view, tmposition, id);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {

                            }
                        });

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {
                        CusConversationListFragment.super.onItemClick(parent, view, tmposition, id);
                    }
                });
            }else {
                super.onItemClick(parent, view, tmposition, id);
            }
        } else {
            super.onItemClick(parent, view, tmposition, id);
        }

    }
}
