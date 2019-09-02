package com.cnsunrun.chat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.RedpacketDialog;
import com.cnsunrun.chat.logic.ChatBgLogic;
import com.cnsunrun.chat.logic.HeadClickLogic;
import com.cnsunrun.chat.mode.GroupInfoBean;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.ConversationHeightFix;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.config.Const;
import com.cnsunrun.common.config.IMUserInfoProvider;
import com.cnsunrun.common.config.RedPackMessage;
import com.cnsunrun.common.config.RedPackReceivedTipMessage;
import com.cnsunrun.common.config.RongIMHelper;
import com.cnsunrun.common.dialog.VersionTipDiglog;
import com.cnsunrun.common.event.DefaultEvent;
import com.cnsunrun.common.event.RefreshIMTokenEvent;
import com.cnsunrun.common.logic.NotificationLogic;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.cnsunrun.mine.mode.UserDataBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.log.Logger;
import com.tencent.bugly.crashreport.CrashReport;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIMessage;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;

import static com.cnsunrun.chat.ConversationActivity.isOpened;
import static com.cnsunrun.common.config.Const.EVENT_REFRESH_CONVERSATION;
import static com.cnsunrun.common.config.Const.REDPACK_GETED;
import static com.cnsunrun.common.quest.BaseQuestConfig.GET_USER_DATA_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_CHAT_INFO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_COLLECT_ATTACHMENT_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_COLLECT_CONTENT_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_INFO_CODE;


/**
 * 咵天-聊天页面
 */
public class ConversationOneActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.imgChatBg)
    ImageView imgChatBg;
    @BindView(R.id.layContent)
    ViewGroup layContent;
    @BindView(R.id.layRootView)
    ViewGroup layRootView;
    @BindView(R.id.layBottomDis)
    View layBottomDis;
    @BindView(R.id.rc_layout_network)
    View rc_layout_network;

    @BindView(R.id.rc_text_header_network)
    TextView rc_text_header_network;


    private UIMessage rcMessage;
    String targetId = "";
    int redpack_num = 7;
    ImageView rc_redpack;
    ImageView rc_plugin_toggle;
    int member_num = 0;
    boolean isCustom = false;//客服
    public boolean isRedpackGroup = false;
    NotificationLogic notificationLogic;
    boolean isLoaded = false;
    boolean isSendRedpack = false;
    public static boolean isOtherPage = false;
    public static ConversationOneActivity tmpConversation;
    private String id;
    public boolean isTextEnable;

    public ConversationOneActivity() {
        tmpConversation = this;
    }

    public static boolean isConversationRuning() {
        if (tmpConversation != null) {
            try {
                return !tmpConversation.isDestroyed() && !tmpConversation.isFinishing();
            } catch (Exception e) {
                return false;
            }
        }
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.E("初始化");
        isOpened = true;
        initEventBus();
        String title = "";
        Uri uri = getIntent().getData();
        if (uri != null) {
            title = uri.getQueryParameter("title");
            targetId = uri.getQueryParameter("targetId");
        }
        isCustom = getSession().getBoolean("customer_id_" + targetId);//标记客服
        RongIMHelper.initExtensionModules(!isCustom);
        setContentView(R.layout.activity_chat_conversation);
        notificationLogic = new NotificationLogic(this);
        if (uri != null) {
            title = uri.getQueryParameter("title");
            titleBar.setTitle(title);
            targetId = uri.getQueryParameter("targetId");

        } else {
            CrashReport.postCatchedException(new Exception("====聊天界面 uri为空,界面异常"));
            finish();
        }
        rc_redpack = (ImageView) findViewById(io.rong.imkit.R.id.rc_redpack);
        ImageView rc_yu_e = (ImageView) findViewById(io.rong.imkit.R.id.rc_yu_e);
        rc_plugin_toggle = (ImageView) findViewById(io.rong.imkit.R.id.rc_plugin_toggle);
        rc_plugin_toggle.setVisibility(View.GONE);
        IMUserInfoProvider.refreshAvatarCache();

        notificationLogic.clearNotify(String.valueOf(targetId).hashCode());
        new ConversationHeightFix(this, layContent);

        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isOtherPage = true;
                CommonIntent.startChatSetInfoActivity(that, targetId);
            }
        });
        doInitView();
        RongIM.setConversationBehaviorListener(new HeadClickLogic(that));
        if (!RongIMHelper.isConnect()) {
            EventBus.getDefault().post(RefreshIMTokenEvent.newInstance());//刷新Token
            return;
        }

        rc_layout_network.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rc_text_header_network.setText("重连中..");
                rc_text_header_network.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(RefreshIMTokenEvent.newInstance());//刷新Token
                    }
                }, 1000);
            }
        });
        RongIM.getInstance().setSendMessageListener(new RongIM.OnSendMessageListener() {
            @Override
            public Message onSend(Message message) {
                boolean isRedpack = (message.getContent() instanceof RedPackMessage) || (message.getContent() instanceof RedPackReceivedTipMessage);

                if (!isTextEnable && !isRedpack) {
                    return null;
                }
                return message;
            }

            @Override
            public boolean onSent(Message message, RongIM.SentMessageErrorCode sentMessageErrorCode) {
                return false;
            }
        });

        if (EmptyDeal.isEmpy(title)) {
            CrashReport.postCatchedException(new Exception("====聊天界面 标题为空,界面异常 isRedpackGroup:" + isRedpackGroup + "  isTextEnable:" + isTextEnable));
        }

    }

    private void doInitView() {
        isRedpackGroup = getSession().getBoolean("isRedpack_group" + targetId);
        isCustom = getSession().getBoolean("customer_id_" + targetId);//标记客服
        if (isCustom) {
            titleBar.setRightVisible(View.GONE);
            isTextEnable = true;
            EditText rc_edit_text = (EditText) findViewById(io.rong.imkit.R.id.rc_edit_text);
            TextView rc_edit_text2 = (TextView) findViewById(io.rong.imkit.R.id.rc_edit_text2);
            rc_edit_text.setHint("说点什么");
            rc_edit_text.setEnabled(true);
            rc_edit_text.setFocusable(true);
            rc_edit_text.setFocusableInTouchMode(true);
            rc_edit_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
            rc_edit_text.requestFocus();
//                        findViewById(io.rong.imkit.R.id.rc_voice_toggle).setEnabled(true);
            findViewById(io.rong.imkit.R.id.rc_emoticon_toggle).setEnabled(true);
            rc_plugin_toggle.setVisibility(View.VISIBLE);
            ImageView rc_yu_e = (ImageView) findViewById(io.rong.imkit.R.id.rc_yu_e);
            rc_redpack = (ImageView) findViewById(io.rong.imkit.R.id.rc_redpack);
            rc_yu_e.setVisibility(View.GONE);
            rc_redpack.setVisibility(View.GONE);
            rc_edit_text.setVisibility(View.VISIBLE);
            rc_edit_text2.setVisibility(View.GONE);
        } else {
            EditText rc_edit_text = (EditText) findViewById(io.rong.imkit.R.id.rc_edit_text);
            TextView rc_edit_text2 = (TextView) findViewById(io.rong.imkit.R.id.rc_edit_text2);
            rc_edit_text.setHint("说点什么");
            rc_edit_text.setEnabled(true);
            rc_edit_text.setFocusable(true);
            rc_edit_text.setFocusableInTouchMode(true);
            rc_edit_text.setMaxEms(200);
            rc_edit_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(200)});
            rc_edit_text.requestFocus();
            findViewById(io.rong.imkit.R.id.rc_emoticon_toggle).setEnabled(true);
            rc_plugin_toggle.setVisibility(View.VISIBLE);
            rc_edit_text.setVisibility(View.VISIBLE);
            rc_edit_text2.setVisibility(View.GONE);
        }
    }

    boolean needShowYueTip = true;

    private void getYuEInfo() {
        needShowYueTip = true;
        UserDataBean userBean = getSession().getObject("userBean", UserDataBean.class);
        if (userBean != null && userBean.balance != null) {
            showYue(userBean);
            needShowYueTip = false;
        }
        if (needShowYueTip) {
            UIUtils.showLoadDialog(that);
        }
        BaseQuestStart.getUserData(this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        isOtherPage = false;

        isOpened = true;
        loadPageData();
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case GET_USER_DATA_CODE:
                if (bean.status == 1) {
                    UserDataBean userDataBean = bean.Data();
                    getSession().put("userBean", userDataBean);
                    showYue(userDataBean);
                } else {
                    UIUtils.shortM(bean);
                }
                break;
            case NEIGHBORHOOD_IM_CHAT_INFO_CODE:
                if (bean.status == 1) {
                    LinkManInfo linkmana = bean.Data();
//                    isTextEnable = true;
                    if (isCustom) {
                        titleBar.setRightVisible(View.GONE);
                        isTextEnable = true;
                    } else {
                        titleBar.setTitle(linkmana.getNickname());
                        isTextEnable = true;
                    }
                    if ("1".equals(targetId))
                        findViewById(R.id.rc_extension).setVisibility(View.GONE);
                    getSession().put("chatinfo" + targetId, linkmana);

                } else {
                    titleBar.setRightVisible(View.GONE);
                }
                break;

            case NEIGHBORHOOD_IM_COLLECT_CONTENT_CODE:
            case NEIGHBORHOOD_IM_COLLECT_ATTACHMENT_CODE:
                UIUtils.shortM(bean);
                break;
            case BaseQuestStart.GRAB_REDPACK_CODE:
                JSONObject jobj = JsonDeal.createJsonObj(bean.Data().toString());
//
                if (bean.status == 1) {
//
                    rcMessage.setExtra(REDPACK_GETED + bean.status);
                    RongIM.getInstance().setMessageExtra(rcMessage.getMessageId(), REDPACK_GETED + bean.status, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            EventBus.getDefault().post(DefaultEvent.createEvent(EVENT_REFRESH_CONVERSATION, ""));
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                    ConversationOneActivity.isOtherPage = true;
//                    CommonIntent.startRedpackPageActivity(that, id);
                    return;
                }

                RedpacketDialog.newInstance()
                        .setId(id)
                        .setStates(bean)
                        .setRcMessage(rcMessage)
                        .show(getSupportFragmentManager(), "RedpacketDialog");

                break;
        }
        super.nofityUpdate(requestCode, bean);
    }

    private void showYue(UserDataBean userDataBean) {
        if (!needShowYueTip) return;
        VersionTipDiglog.newInstance()
                .setTvContent(String.format("账户余额: %s", userDataBean.balance))
                .setBtnStr("知道了")
                .setOnSubmitAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show(getSupportFragmentManager(), "VersionTipDiglog");
    }

    public void refreshChatBg() {
//        ChatBgLogic.loadChatBg(that, imgChatBg, targetId, isGroup);
    }


    @Subscribe
    public void onEvent(DefaultEvent defaultEvent) {
        if (defaultEvent.match(Const.EVENT_REFRESH_CONVERSATION)) {

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onConnectChange(ConnectionStatus connectionStatus) {
        rc_layout_network.setVisibility(View.VISIBLE);
        switch (connectionStatus) {
            case CONNECTED://连接成功
                rc_layout_network.setVisibility(View.GONE);
                break;
            case CONNECTING:
                rc_text_header_network.setText("正在连接服务器...");
                break;
            case DISCONNECTED:
                rc_text_header_network.setText("服务器连接断开");
                break;
            case NETWORK_UNAVAILABLE:
                rc_text_header_network.setText("网络连接不可用");
                break;
            default:
                rc_text_header_network.setText("服务器连接出错");
                break;

        }
    }

    @Subscribe
    public void onMessageClick(UIMessage message) {
        MessageContent content = message.getContent();
        rcMessage = message;
        if (content instanceof RedPackMessage) {
            RedPackMessage redPack = (RedPackMessage) content;

            String extra = String.valueOf(message.getExtra());
            if (extra.startsWith(Const.REDPACK_GETED) && !extra.contains("13")) {//不是余额不足
                isOtherPage = true;
//                CommonIntent.startRedpackPageActivity(that, redPack.getId());
                return;
            }
            id = redPack.getId();
            showRedpacketDialog(id);
        }
    }

    private void showRedpacketDialog(String id) {
//        UIUtils.showLoadDialog(that, "领取中..");
//        BaseQuestStart.grab_redpack(this, id);
    }

    private void loadPageData() {
        UIUtils.showLoadDialog(this);
        BaseQuestStart.NeighborhoodImChatInfo(this, targetId);
    }


    @Override
    protected void onPause() {
        super.onPause();
        isOpened = isOtherPage;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isOpened = isOtherPage;
    }

    @Override
    protected void onDestroy() {
        isOpened = false;
        if (isCustom) {
            RongIM.getInstance().removeConversation(Conversation.ConversationType.PRIVATE, targetId);
        }
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null && requestCode == 0x037) {
            RedPackMessage redPackMessage = data.getParcelableExtra("redpack");
            Message obtain = Message.obtain(targetId, Conversation.ConversationType.GROUP, redPackMessage);
            RongIM.getInstance().insertOutgoingMessage(Conversation.ConversationType.GROUP, targetId, Message.SentStatus.SENT, redPackMessage, System.currentTimeMillis(), new RongIMClient.ResultCallback<Message>() {
                @Override
                public void onSuccess(Message message) {
                    isSendRedpack = false;
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {
                    isSendRedpack = false;
                }
            });
//            RongIM.getInstance().sendMessage(obtain, "", "", new IRongCallback.ISendMessageCallback() {
//                @Override
//                public void onAttached(Message message) {
//
//                }
//
//                @Override
//                public void onSuccess(Message message) {
//                    EventBus.getDefault().post(DefaultEvent.createEvent(EVENT_REFRESH_CONVERSATION,""));
//                }
//
//                @Override
//                public void onError(Message message, RongIMClient.ErrorCode errorCode) {
//                    UIUtils.shortM("红包发送失败");
//                }
//            });
        }
    }
}
