package com.cnsunrun.chat;

import android.os.Bundle;
import android.widget.EditText;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_FRIEND_REQUEST_CODE;


/**
 * 咵天-加好友-附加信息
 */
public class ChatAddUserActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.btnSendAddMsg)
    QMUIRoundButton btnSendAddMsg;
    @BindView(R.id.editContent)
    EditText editContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_adduser);

    }

    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_FRIEND_REQUEST_CODE:
                UIUtils.shortM(bean);
                if(bean.status==1){
                    //发送命令消息添加好友
//                    String targetId  = getIntent().getStringExtra("uid");
//                    Message delete_user = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, CommandMessage.obtain("add_user", Config.getLoginInfo().getId()));
//                    RongIM.getInstance().sendMessage(delete_user, null, null, new IRongCallback.ISendMessageCallback() {
//                        @Override
//                        public void onAttached(Message message) {
//
//                        }
//
//                        @Override
//                        public void onSuccess(Message message) {
//                            finish();
//                        }
//
//                        @Override
//                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//                            finish();
//                        }
//                    });
                    finish();
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }

    @OnClick(R.id.btnSendAddMsg)
    public void onClick() {
        UIUtils.showLoadDialog(that,"请求中...");
        BaseQuestStart.NeighborhoodImFriendRequest(that,getIntent().getStringExtra("uid"),editContent);

    }
}
