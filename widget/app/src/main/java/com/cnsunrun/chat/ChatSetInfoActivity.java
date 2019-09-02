package com.cnsunrun.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.MessageTipDialog;
import com.cnsunrun.chat.logic.ChatBgLogic;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.logic.ImgDealLogic;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.common.widget.FormLabLayout;
import com.cnsunrun.common.widget.SwitchView;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_CHAT_IMAGE_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_IS_TOP_CANCEL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_IS_TOP_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_NOT_DISTURB_CANCEL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_NOT_DISTURB_CODE;
import static io.rong.imlib.model.Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
import static io.rong.imlib.model.Conversation.ConversationNotificationStatus.NOTIFY;


/**
 * 咵天-聊天信息页面
 */
public class ChatSetInfoActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.userInfos)
    TagFlowLayout userInfos;
    @BindView(R.id.toggleAllowNotify)
    SwitchView toggleAllowNotify;
    @BindView(R.id.itemAllowNotify)
    FormLabLayout itemAllowNotify;
    @BindView(R.id.toggleIsTop)
    SwitchView toggleIsTop;
    @BindView(R.id.itemItemTop)
    FormLabLayout itemItemTop;
    @BindView(R.id.itemFindHistory)
    FormLabLayout itemFindHistory;
    @BindView(R.id.itemClearHistory)
    FormLabLayout itemClearHistory;
    String targetId = "";
    LinkManInfo linkManInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_setinfo);
        targetId = getIntent().getStringExtra("targetId");
        linkManInfo = getSession().getObject("chatinfo" + targetId, LinkManInfo.class);
        userInfos.setAdapter(new TagAdapter<LinkManInfo>(TestTool.asArrayList(linkManInfo, new LinkManInfo())) {
            @Override
            public View getView(FlowLayout parent, int position,final LinkManInfo o) {
                View inflate = getLayoutInflater().inflate(R.layout.item_chat_userinfo_tag, parent, false);
                ImageView imgUserIcon = (ImageView) inflate.findViewById(R.id.imgUserIcon);
                TextView txtUserName = (TextView) inflate.findViewById(R.id.txtUserName);
                txtUserName.setText(o.getNickname());

                if (o.id == null) {
                    imgUserIcon.setImageResource(R.drawable.ic_chat_add_user);
                    imgUserIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getSession().put("selectedLinkMans",TestTool.asArrayList(linkManInfo));
//                            CommonIntent.startChatSelectLinkManActivity(that,null);
                        }
                    });
                }else {
                    imgUserIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            CommonIntent.startChatUserInfoActivity(that,o.id,false);
                        }
                    });
                    GlideMediaLoader.load(that, imgUserIcon, o.avatar);
                }
                return inflate;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }
        });
        toggleAllowNotify.setOpened("1".equals(linkManInfo.not_disturb));
        toggleIsTop.setOpened("1".equals(linkManInfo.is_top));
    }


    @OnClick({R.id.toggleAllowNotify, R.id.toggleIsTop, R.id.itemFindHistory, R.id.itemClearHistory})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toggleAllowNotify:
                /**
                 * 设置会话消息提醒状态。
                 * @param conversationType   会话类型。
                 * @param targetId           目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
                 * @param notificationStatus 是否屏蔽。
                 * @param callback           设置状态的回调。
                 */
                Conversation.ConversationNotificationStatus status=   toggleAllowNotify.isOpened()? DO_NOT_DISTURB: NOTIFY;
                RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.PRIVATE, targetId, status, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        if (toggleAllowNotify.isOpened()) {
                            BaseQuestStart.NeighborhoodImNotDisturb(that, targetId);
                        } else {
                            BaseQuestStart.NeighborhoodImNotDisturbCancel(that, targetId);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });


                break;
            case R.id.toggleIsTop:
                RongIM.getInstance().setConversationToTop(Conversation.ConversationType.PRIVATE, targetId, toggleIsTop.isOpened(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (aBoolean) {
                            BaseQuestStart.NeighborhoodImIsTop(that, targetId);
                        } else {
                            BaseQuestStart.NeighborhoodImIsTopCancel(that, targetId);
                        }

                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
                break;
            case R.id.itemFindHistory:
                CommonIntent.startChatSearchHistoryActivity(that,targetId,1);
                break;
            case R.id.itemClearHistory:
                MessageTipDialog.newInstance().setContentTxt("是否清空聊天记录?")
                        .setOnSubmitAction(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UIUtils.showLoadDialog(that,"清除中...");
                                RongIM.getInstance().deleteMessages(Conversation.ConversationType.PRIVATE, targetId, new RongIMClient.ResultCallback<Boolean>() {
                                    @Override
                                    public void onSuccess(Boolean aBoolean) {
                                        UIUtils.cancelLoadDialog();
                                        UIUtils.shortM("清除成功");
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        UIUtils.cancelLoadDialog();
                                        UIUtils.shortM("清除失败");
                                    }
                                });
                            }
                        }).show(getSupportFragmentManager(),"MessageTipDialog");

                break;

        }
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case NEIGHBORHOOD_IM_IS_TOP_CODE:
            case NEIGHBORHOOD_IM_IS_TOP_CANCEL_CODE:
            case NEIGHBORHOOD_IM_NOT_DISTURB_CODE:
            case NEIGHBORHOOD_IM_NOT_DISTURB_CANCEL_CODE:
                UIUtils.shortM(bean);
                if (bean.status == 1) {

                }
                break;
            case NEIGHBORHOOD_IM_CHAT_IMAGE_CODE:
                UIUtils.shortM(bean);
                if(bean.status==1){
                    ChatBgLogic.saveChatBg(that,pic,targetId,false);
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }
    String pic="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pic = ImgDealLogic.getPic(that, data);
        if (!EmptyDeal.isEmpy(pic)) {
            UIUtils.showLoadDialog(that, "正在设置聊天背景..");
//            BaseQuestStart.NeighborhoodImChatImage(that, targetId, new File(pic));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
