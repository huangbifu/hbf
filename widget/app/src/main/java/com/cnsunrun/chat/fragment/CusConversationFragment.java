package com.cnsunrun.chat.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.cnsunrun.chat.ConversationActivity;
import com.cnsunrun.chat.adapters.CusMessageListAdapter;
import com.cnsunrun.common.config.Const;
import com.cnsunrun.common.config.RongCloudEvent;
import com.cnsunrun.common.config.RongIMHelper;
import com.cnsunrun.common.event.DefaultEvent;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.List;

import io.rong.common.RLog;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationFragment;
import io.rong.imkit.fragment.IHistoryDataResultCallback;
import io.rong.imkit.model.Event;
import io.rong.imkit.widget.adapter.MessageListAdapter;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

/**
 * Created by weiquan on 2018/9/28.
 */
public class CusConversationFragment extends ConversationFragment {

    private CusMessageListAdapter cusMessageListAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
          boolean  isCustom = NetSession.instance(getActivity()).getBoolean("customer_id_" + getTargetId());//标记客服
            RongIMHelper.initExtensionModules(!isCustom);
            EventBus.getDefault().register(this);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe
    public void onEvent(DefaultEvent defaultEvent) {
        if (defaultEvent.match(Const.EVENT_REFRESH_CONVERSATION)) {
            MessageListAdapter messageAdapter = getMessageAdapter();
            if (messageAdapter != null) {
                messageAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public MessageListAdapter onResolveAdapter(Context context) {
        cusMessageListAdapter = new CusMessageListAdapter(context);
        return cusMessageListAdapter;
    }


    @Override
    public MessageListAdapter getMessageAdapter() {
        return super.getMessageAdapter();
    }

    boolean needNext = true;//是否还需要下一个
    int messageCount = 0;

    @Override
    public void getHistoryMessage(Conversation.ConversationType conversationType, String targetId, int lastMessageId, final int reqCount, LoadMessageDirection direction, final IHistoryDataResultCallback<List<Message>> callback) {
        if (getActivity() instanceof ConversationActivity) {
            ConversationActivity activity = (ConversationActivity) getActivity();
            boolean aBoolean = activity.getSession().getBoolean("isRedpack_group" + targetId);
            if (aBoolean) {//红包群
                if (direction == ConversationFragment.LoadMessageDirection.UP && needNext) {
                    RongIMClient.getInstance().getHistoryMessages(conversationType, targetId, lastMessageId, reqCount, new RongIMClient.ResultCallback<List<Message>>() {
                        public void onSuccess(final List<Message> messages) {
                          final   int delay = messageCount == 0 ? 300 : 0;

                            getView().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (callback != null) {
                                        messageCount += reqCount;
                                        if (!EmptyDeal.isEmpy(messages)) {
                                            long sentTime = messages.get(messages.size() - 1).getSentTime();
                                            needNext = RongCloudEvent.isMoment(sentTime);//System.currentTimeMillis()-sentTime<10*60*1000;
                                        }
                                        callback.onResult(messages);
                                        if(delay!=0){
                                            cusMessageListAdapter.currentTime=System.currentTimeMillis();
                                            cusMessageListAdapter.isFirstLoad=false;
                                        }
                                    }
                                }
                            }, delay);

                        }

                        public void onError(RongIMClient.ErrorCode e) {
                            RLog.e("ConversationFragment", "getHistoryMessages " + e);
                            if (callback != null) {
                                callback.onResult((List<Message>) null);
                            }

                        }
                    });
                } else {
                    if (callback != null) {
                        callback.onResult((List<Message>) null);
                    }
                }
                return;
            }
        }
//        RongIM.getInstance().g
        super.getHistoryMessage(conversationType, targetId, lastMessageId, reqCount, direction, callback);
    }


    @Override
    public void getRemoteHistoryMessages(Conversation.ConversationType conversationType, String targetId, long dateTime, int reqCount, IHistoryDataResultCallback<List<Message>> callback) {
        if (callback != null) {
            callback.onResult((List<Message>) null);
        }
//        super.getRemoteHistoryMessages(conversationType, targetId, dateTime, reqCount, callback);
    }
}
