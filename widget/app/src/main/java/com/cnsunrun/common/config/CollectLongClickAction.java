package com.cnsunrun.common.config;

import android.content.Context;

import com.cnsunrun.common.event.DefaultEvent;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.RongMessageItemLongClickActionManager;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.MessageItemLongClickAction;

/**
 * Created by WQ on 2017/12/8.
 */

public class CollectLongClickAction {
    public static  void addCollectAction(){
        MessageItemLongClickAction actionListener = new MessageItemLongClickAction.Builder().title("收藏消息").actionListener(new MessageItemLongClickAction.MessageItemLongClickListener() {
            @Override
            public boolean onMessageItemLongClick(Context context, UIMessage uiMessage) {
                EventBus.getDefault().post(DefaultEvent.createEvent(Const.EVENT_COLLECT,uiMessage));
//                UIUtils.shortM("收藏消息");
                return true;
            }
        }).build();
        RongMessageItemLongClickActionManager instance = RongMessageItemLongClickActionManager.getInstance();
        instance.init();
        instance.addMessageItemLongClickAction(actionListener,1);
    }
}
