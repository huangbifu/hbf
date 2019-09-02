package com.cnsunrun.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatUserInfoPicAdapter;
import com.cnsunrun.chat.dialog.MessageTipDialog;
import com.cnsunrun.chat.mode.MemberInfo;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.FormLabLayout;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.PictureShow;
import com.sunrun.sunrunframwork.uiutils.TextColorUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.CommandMessage;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_FRIEND_DEL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_MEMBER_INFO_CODE;


/**
 * 咵天-聊天用户详细资料
 */
public class ChatUserInfoActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.imgUserIcon)
    ImageView imgUserIcon;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtInfo)
    TextView txtInfo;
    @BindView(R.id.txtSignature)
    TextView txtSignature;
    @BindView(R.id.itemSetRemark)
    FormLabLayout itemSetRemark;
    @BindView(R.id.btnSendMsg)
    QMUIRoundButton btnSendMsg;
    @BindView(R.id.btnDelete)
    QMUIRoundButton btnDelete;
    @BindView(R.id.layRemark)
    View layRemark;

    int[] genderIcon = {0, R.drawable.ic_chat_sex_man, R.drawable.ic_chat_sex_woman};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_userinfo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPageData();
    }

    private void loadPageData() {
        UIUtils.showLoadDialog(that);
        BaseQuestStart.NeighborhoodImMemberInfo(this, getIntent().getStringExtra("uid"));
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case NEIGHBORHOOD_IM_MEMBER_INFO_CODE:
                if (bean.status == 1) {
                    MemberInfo memberInfo = bean.Data();
                    setPageData(memberInfo);
                } else {
                    UIUtils.shortM(bean);
                    finish();
                }
                break;
            case NEIGHBORHOOD_IM_FRIEND_DEL_CODE:
                UIUtils.shortM(bean);
                if (bean.status == 1) {
                    CommonApp.getInstance().closeActivitys(ConversationActivity.class);
                    String targetId = getIntent().getStringExtra("uid");
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, targetId);

                    //发送命令消息删除好友
                    Message delete_user = Message.obtain(targetId, Conversation.ConversationType.PRIVATE, CommandMessage.obtain("delete_user", Config.getLoginInfo().getId()));
                    RongIM.getInstance().sendMessage(delete_user, null, null, new IRongCallback.ISendMessageCallback() {
                        @Override
                        public void onAttached(Message message) {

                        }

                        @Override
                        public void onSuccess(Message message) {

                        }

                        @Override
                        public void onError(Message message, RongIMClient.ErrorCode errorCode) {

                        }
                    });
                    finish();
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }

    boolean isFriend;
    String remark, conversationTitle,avatar;

    private void setPageData(MemberInfo memberInfo) {
        remark = memberInfo.remark;
        GlideMediaLoader.load(this, imgUserIcon, avatar=memberInfo.avatar);
        txtUserName.setText(conversationTitle = memberInfo.getNickname());
        txtInfo.setText("电话: " + memberInfo.nickname);
        txtInfo.setVisibility(View.INVISIBLE);
        txtSignature.setText("ID:" + memberInfo.id_num);
        itemSetRemark.setLabContentStr(memberInfo.remark);
//        txtSignature.setVisibility(EmptyDeal.isEmpy(memberInfo.description) ? View.GONE : View.VISIBLE);
        TextColorUtils.setCompoundDrawables(txtUserName, 0, 0, genderIcon[memberInfo.gender], 0);
        isFriend = memberInfo.getIsFriend();
        if (TextUtils.equals(getIntent().getStringExtra("uid"), Config.getLoginInfo().getId())) {
            layRemark.setVisibility(View.GONE);
        } else if (!isFriend) {
            btnSendMsg.setText("添加好友");
            layRemark.setVisibility(View.GONE);
            btnSendMsg.setVisibility(View.VISIBLE);
        } else {
            layRemark.setVisibility(View.VISIBLE);
            btnSendMsg.setText("发消息");
            btnSendMsg.setVisibility(View.VISIBLE);
            btnDelete.setVisibility(View.VISIBLE);
        }
    }


    @OnClick({R.id.itemSetRemark, R.id.imgUserIcon, R.id.btnSendMsg, R.id.btnDelete})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgUserIcon:
                PictureShow pictureShow=new PictureShow(that);
                pictureShow.setArgment(new String[]{avatar});
                pictureShow.show();
                break;
            case R.id.itemSetRemark:
                CommonIntent.startChatAddRemarkActivity(that, getIntent().getStringExtra("uid"), remark);
                break;
            case R.id.btnSendMsg:
                if (!isFriend) {
                    //添加好友
                    CommonIntent.startChatAddUserActivity(this, getIntent().getStringExtra("uid"));
                } else {
                    RongIM.getInstance().startConversation(that, Conversation.ConversationType.PRIVATE, getIntent().getStringExtra("uid"), conversationTitle);
                }
                break;
            case R.id.btnDelete:
                MessageTipDialog.newInstance().setContentTxt("是否删除该好友?")
                        .setOnSubmitAction(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UIUtils.showLoadDialog(that, "操作中...");
                                BaseQuestStart.NeighborhoodImFriendDel(that, getIntent().getStringExtra("uid"));
                            }
                        }).show(getSupportFragmentManager(), "MessageTipDialog");


                break;
        }
    }
}
