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
import com.cnsunrun.chat.mode.GroupInfoBean;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.config.IMGroupInfoProvider;
import com.cnsunrun.common.logic.ImgDealLogic;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.common.widget.FormLabLayout;
import com.cnsunrun.common.widget.SwitchView;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_IMAGE_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_INFO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_IS_TOP_CANCEL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_IS_TOP_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CANCEL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_QUIT_CODE;
import static io.rong.imlib.model.Conversation.ConversationNotificationStatus.DO_NOT_DISTURB;
import static io.rong.imlib.model.Conversation.ConversationNotificationStatus.NOTIFY;


/**
 * 咵天-群聊信息页面
 */
public class ChatGroupSetInfoActivity extends LBaseActivity {


    public static final int IMG_REQUEST_CODE = 0x010;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.userInfos)
    TagFlowLayout userInfos;
    @BindView(R.id.txtShowAllPeople)
    TextView txtShowAllPeople;
    @BindView(R.id.itemGroupName)
    FormLabLayout itemGroupName;
    @BindView(R.id.txtAnnouncement)
    TextView txtAnnouncement;
    @BindView(R.id.itemMyGroupName)
    FormLabLayout itemMyGroupName;
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
    @BindView(R.id.itemSetBackground)
    FormLabLayout itemSetBackground;
    @BindView(R.id.btnExitGroup)
    QMUIRoundButton btnExitGroup;
    String targetId="";
    GroupInfoBean groupInfoBean;
    boolean isAdmin;
    String activity_id="0";
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_setinfo);
        targetId=getIntent().getStringExtra("targetId");
        groupInfoBean=getSession().getObject("groupinfo"+targetId,GroupInfoBean.class);
        setPageData(groupInfoBean);
    }

    public static boolean isNotify(String targetId){
        String key = "notify_" + targetId;
            NetSession netSession = NetSession.instance(CommonApp.getInstance());
            if(!netSession.hasValue(key))return true;
        return netSession.getBoolean(key);
    }

    private void setPageData(GroupInfoBean groupInfoBean) {
        toggleAllowNotify.setOpened(isNotify(targetId));
        type = Integer.parseInt(groupInfoBean.type);
        activity_id=groupInfoBean.activity_id;
        List<LinkManInfo> member_list = TestTool.safeList(groupInfoBean.member_list);
//        member_list.add(new LinkManInfo());
        userInfos.setAdapter(new TagAdapter<LinkManInfo>(member_list) {
            @Override
            public View getView(FlowLayout parent, int position, final LinkManInfo o) {
                View inflate = getLayoutInflater().inflate(R.layout.item_chat_userinfo_tag, parent, false);
                ImageView imgUserIcon = (ImageView) inflate.findViewById(R.id.imgUserIcon);
                TextView txtUserName = (TextView) inflate.findViewById(R.id.txtUserName);
                txtUserName.setText(o.getNickname());
                GlideMediaLoader.load(that,imgUserIcon,o.avatar);
                if(o.id==null){
                    imgUserIcon.setImageResource(R.drawable.ic_chat_add_user);
                    imgUserIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommonIntent.startChatSelectLinkManActivity(that,targetId);
                        }
                    });
                }else {
                    imgUserIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (type==0){
                                CommonIntent.startChatUserInfoActivity(that,o.id,false);
                            }
                        }
                    });
                }
                return inflate;
            }

            @Override
            public int getCount() {
                return super.getCount();
            }
        });
        isAdmin= Config.getLoginInfo().getId().equals(groupInfoBean._uid);//是否群组
//        toggleAllowNotify.setOpened("1".equals(groupInfoBean.not_disturb));//是否免打扰
        toggleIsTop.setOpened("1".equals(groupInfoBean.is_top));//是否置顶
        itemGroupName.setLabContentStr(groupInfoBean.title);
        txtAnnouncement.setText(EmptyDeal.isEmpy(groupInfoBean.notice)?"暂无公告":groupInfoBean.notice);
        itemMyGroupName.setLabContentStr(groupInfoBean.remark);
        btnExitGroup.setText(isAdmin?"解散群组":"退出群聊");
        if (groupInfoBean.isRedpackGroup()) {
            btnExitGroup.setVisibility(View.GONE);
        }
//        GroupItemBean groupItemBean=new GroupItemBean();
//        groupItemBean.id=groupInfoBean.id;
//        groupItemBean.title=groupInfoBean.title;
//        groupItemBean.image=groupInfoBean.
        IMGroupInfoProvider.getInstance().getGroupInfo(targetId);//刷新群组信息
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadPageData();
    }

    private void loadPageData() {
        BaseQuestStart.NeighborhoodImGroupInfo(that,targetId);//刷新页面数据
    }

    public static final int NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CODE=0x0102;
    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_GROUP_IMAGE_CODE:
                UIUtils.shortM(bean);
                if(bean.status==1){
                    ChatBgLogic.saveChatBg(that,pic,targetId,true);
                }
                break;
            case NEIGHBORHOOD_IM_GROUP_IS_TOP_CANCEL_CODE:
            case NEIGHBORHOOD_IM_GROUP_IS_TOP_CODE:
            case NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CANCEL_CODE:
            case NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CODE:
                UIUtils.shortM(bean);
                if (bean.status == 1) {

                }
                break;
            case NEIGHBORHOOD_IM_GROUP_QUIT_CODE:
                UIUtils.shortM(bean);
                if (bean.status == 1) {
                    CommonApp.getInstance().closeActivitys(ConversationActivity.class);
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP,targetId);
                    finish();

                }
                break;
            case NEIGHBORHOOD_IM_GROUP_INFO_CODE:
                if(bean.status==1){
                    GroupInfoBean groupInfoBean=bean.Data();
                    setPageData(groupInfoBean);
                    getSession().put("groupinfo"+targetId,groupInfoBean);

                }else {
                    UIUtils.shortM(bean);
                    RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, targetId, new RongIMClient.ResultCallback<Boolean>() {
                        @Override
                        public void onSuccess(Boolean aBoolean) {
                            finish();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode errorCode) {

                        }
                    });
                }
        }
        super.nofityUpdate(requestCode,bean);
    }
    String pic="";
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        pic = ImgDealLogic.getPic(that, data);
        if (!EmptyDeal.isEmpy(pic)) {
            UIUtils.showLoadDialog(that, "正在设置聊天背景..");
//            BaseQuestStart.NeighborhoodImGroupImage(that, targetId, new File(pic));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @OnClick({R.id.txtShowAllPeople, R.id.itemGroupName,R.id.layAnnouncement, R.id.txtAnnouncement, R.id.itemMyGroupName, R.id.toggleAllowNotify, R.id.itemAllowNotify, R.id.toggleIsTop, R.id.itemItemTop, R.id.itemFindHistory, R.id.itemClearHistory, R.id.itemSetBackground, R.id.btnExitGroup})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtShowAllPeople:
                //0普通群,1红包群
                if (type==1){
                    GroupMemberListActivity.startThis(that,targetId);
                }else{
                    CommonIntent.startChatGroupPeoplesActivity(this,targetId,isAdmin,activity_id);
                }
                break;
            case R.id.itemGroupName:
                if(isAdmin) {
                    CommonIntent.startChatModifyGroupNameActivity(this, targetId, groupInfoBean.title);
                }
                break;
            case R.id.layAnnouncement:
            case R.id.txtAnnouncement:
                if(!isAdmin&&EmptyDeal.isEmpy(groupInfoBean.notice)){
                    UIUtils.shortM("暂无公告");
                    return;
                }else {
                    if(isAdmin){
                        CommonIntent.startChatModifyGroupNoticeActivity(this,targetId,groupInfoBean.notice);
                    }else {
                        CommonIntent.startChatGroupNoticeDetailsActivity(this, targetId, groupInfoBean.notice, groupInfoBean.notice_time);
                    }

                }
                break;
            case R.id.itemMyGroupName:
                CommonIntent.startChatModifyGroupCardActivity(this,targetId,groupInfoBean.remark);
                break;
            case R.id.toggleAllowNotify:

                getSession().put("notify_"+targetId,toggleAllowNotify.isOpened());
                /**
                 * 设置会话消息提醒状态。
                 * @param conversationType   会话类型。
                 * @param targetId           目标 Id。根据不同的 conversationType，可能是用户 Id、讨论组 Id、群组 Id。
                 * @param notificationStatus 是否屏蔽。
                 * @param callback           设置状态的回调。
                 */
                Conversation.ConversationNotificationStatus status=   toggleAllowNotify.isOpened()? DO_NOT_DISTURB: NOTIFY;
                RongIM.getInstance().setConversationNotificationStatus(Conversation.ConversationType.GROUP, targetId, status, new RongIMClient.ResultCallback<Conversation.ConversationNotificationStatus>() {
                    @Override
                    public void onSuccess(Conversation.ConversationNotificationStatus conversationNotificationStatus) {
                        SwitchView toggleAllowNotify = ChatGroupSetInfoActivity.this.toggleAllowNotify;
                        if(toggleAllowNotify ==null)return;
                        if (toggleAllowNotify.isOpened()) {
                            BaseQuestStart.NeighborhoodImGroupNotDisturb(that,targetId);
                        }else {
                            BaseQuestStart.NeighborhoodImGroupNotDisturbCancel(that,targetId);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });

                break;
            case R.id.toggleIsTop:
                RongIM.getInstance().setConversationToTop(Conversation.ConversationType.GROUP,targetId, toggleIsTop.isOpened(), new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        if (toggleIsTop.isOpened()) {
                            BaseQuestStart.NeighborhoodImGroupIsTop(that,targetId);
                        }else {
                            BaseQuestStart.NeighborhoodImGroupIsTopCancel(that,targetId);
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
                break;
            case R.id.itemFindHistory:
                CommonIntent.startChatSearchHistoryActivity(that,targetId,2);
                break;
            case R.id.itemClearHistory:
                MessageTipDialog.newInstance().setContentTxt("是否清空聊天记录?")
                        .setOnSubmitAction(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UIUtils.showLoadDialog(that,"清除中...");
                                RongIM.getInstance().clearMessages(Conversation.ConversationType.GROUP, targetId,new RongIMClient.ResultCallback<Boolean>() {
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
//                                RongIMClient.getInstance().clea(Conversation.ConversationType.GROUP, mG
//                                RongIM.getInstance().deleteMessages(Conversation.ConversationType.GROUP, targetId, new RongIMClient.ResultCallback<Boolean>() {
//                                    @Override
//                                    public void onSuccess(Boolean aBoolean) {
//                                        UIUtils.cancelLoadDialog();
//                                        UIUtils.shortM("清除成功");
//                                    }
//
//                                    @Override
//                                    public void onError(RongIMClient.ErrorCode errorCode) {
//                                        UIUtils.cancelLoadDialog();
//                                        UIUtils.shortM("清除失败");
//                                    }
//                                });
                            }
                        }).show(getSupportFragmentManager(),"MessageTipDialog");

                break;
            case R.id.itemSetBackground:
                BoxingConfig mulitImgConfig = new BoxingConfig(BoxingConfig.Mode.MULTI_IMG)
                        .needCamera()
                        .needGif()
                        .withMaxCount(1);
                Boxing.of(mulitImgConfig).
                        withIntent(that, BoxingActivity.class).
                        start(that, IMG_REQUEST_CODE);
                break;
            case R.id.btnExitGroup:
                String content=isAdmin?"解散":"退出";
                MessageTipDialog.newInstance().setContentTxt("是否"+content+"该群")
                        .setOnSubmitAction(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UIUtils.showLoadDialog(that,"操作中..");
                                BaseQuestStart.NeighborhoodImGroupQuit(that,targetId);
                            }
                        }).show(getSupportFragmentManager(),"MessageTipDialog");
                break;
        }
    }
}
