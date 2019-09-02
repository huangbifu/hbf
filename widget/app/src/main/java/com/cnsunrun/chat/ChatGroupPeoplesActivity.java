package com.cnsunrun.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.MessageTipDialog;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

import butterknife.BindView;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_MEMBER_DEL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_MEMBER_LIST_CODE;


/**
 * 咵天-群聊 全部成员
 */
public class ChatGroupPeoplesActivity extends LBaseActivity {


    public static final int IMG_REQUEST_CODE = 0x010;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.userInfos)
    TagFlowLayout userInfos;
    String targetId;
    boolean isAdmin;
    String activity_id="0";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_peoples);
        activity_id=getIntent().getStringExtra("activity_id");
    }

    private void loadPageData() {
        UIUtils.showLoadDialog(that,"获取成员列表..");
        targetId = getIntent().getStringExtra("targetId");
        isAdmin=getIntent().getBooleanExtra("isAdmin",false);
        BaseQuestStart.NeighborhoodImGroupMemberList(that,targetId,0);
    }

    @Override
    protected void onResume() {
        loadPageData();
        super.onResume();
    }

    public void nofityUpdate(int requestCode, BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_GROUP_MEMBER_LIST_CODE:
                if(bean.status==1){
                   final List<LinkManInfo> temps=bean.Data();
                    List<LinkManInfo> linkInfos=TestTool.safeList(temps);
                    excludeAdminSelf(linkInfos);
                    linkInfos.add(new LinkManInfo());
                    userInfos.setAdapter(new TagAdapter<LinkManInfo>(linkInfos) {
                        @Override
                        public View getView(FlowLayout parent, int position,final LinkManInfo o) {
                            View inflate = getLayoutInflater().inflate(R.layout.item_chat_userinfo_tag, parent, false);
                            ImageView imgUserIcon = (ImageView) inflate.findViewById(R.id.imgUserIcon);
                            TextView txtUserName = (TextView) inflate.findViewById(R.id.txtUserName);
                            txtUserName.setText(o.getNickname());
                            View imgDelete = inflate.findViewById(R.id.imgDelete);
                            if (o.id == null) {//加号 不显示删除按钮
                                imgDelete.setVisibility(View.GONE);
                                imgUserIcon.setImageResource(R.drawable.ic_chat_add_user);
                                imgUserIcon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(EmptyDeal.isEmpy(activity_id)||"0".equals(activity_id)) {
                                            getSession().put("selectedLinkMans",temps);
                                            CommonIntent.startChatSelectLinkManActivity(that, targetId);
                                        }else {
//                                            CommonIntent.startMeetingAppliedListActivity(that,activity_id);
                                        }
                                    }
                                });
                            }else {
                                if(isAdmin&&o.adminId==null) {
                                    imgDelete.setVisibility(View.VISIBLE);
                                    imgDelete.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            doDeletePeoples(o.id);
                                        }
                                    });
                                }else {//当前登录用户不是群主,或者这头像是群主,不显示删除按钮
                                    imgDelete.setVisibility(View.GONE);
                                }
                                imgUserIcon.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        CommonIntent.startChatUserInfoActivity(that,o.id,false);
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
                }
                break;
            case NEIGHBORHOOD_IM_GROUP_MEMBER_DEL_CODE:

                if(bean.status==1){
                    loadPageData();
                    UIUtils.shortM(bean);
//                    UIUtils.shortM("删除成功");
                }else {
                    UIUtils.shortM(bean);
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }

    /**
     * 如果是管理员,排除掉自己
     * @param linkInfos
     */
    private void excludeAdminSelf(List<LinkManInfo> linkInfos) {
        if(!isAdmin)return;
        String id = Config.getLoginInfo().getId();
        for (LinkManInfo linkInfo : linkInfos) {
            if (TextUtils.equals(linkInfo.id,id)) {
                linkInfo.adminId=linkInfo.id;
               // linkInfos.remove(linkInfo);
                return;
            }
        }
    }

    private void doDeletePeoples(final String uid) {
        MessageTipDialog.newInstance().setContentTxt("是否移除该成员?")
                .setOnSubmitAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.showLoadDialog(that,"操作中..");
                        BaseQuestStart.NeighborhoodImGroupMemberDel(that,targetId,uid);
                    }
                }).show(getSupportFragmentManager(),"MessageTipDialog");
    }


}
