package com.cnsunrun.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatNewFriendsAdapter;
import com.cnsunrun.chat.dialog.FilterPopWindow;
import com.cnsunrun.chat.mode.FriendRequestBean;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.config.RongCloudEvent;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.util.RedDotUtil;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.log.Logger;

import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.IRongCallback;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.message.TextMessage;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_FRIEND_REQUEST_ACCEPT_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_FRIEND_REQUEST_LIST_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_FRIEND_REQUEST_REFUSE_CODE;

/**
 * 咵天,新朋友
 * Created by WQ on 2017/10/30.
 */

public class ChatNewFriendsActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate<FriendRequestBean> pageLimitDelegate = new PageLimitDelegate<>(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.NeighborhoodImFriendRequestList(that, page);
        }
    });
String fuid;
    FilterPopWindow filterPopWindow;//右边功能菜单列表
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_newfriends);
        final ChatNewFriendsAdapter repairsRecordAdapter = new ChatNewFriendsAdapter(null);
        recyclerView.setAdapter(repairsRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        pageLimitDelegate.attach(refreshLayout, recyclerView, repairsRecordAdapter);
        GetEmptyViewUtils.bindEmptyView(that,repairsRecordAdapter,0,"暂无好友请求",true);
        repairsRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                CommonIntent.startChatUserInfoActivity(that, repairsRecordAdapter.getItem(position).fuid, false);
            }
        });
        repairsRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                FriendRequestBean item = repairsRecordAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.btnAgree:
                        UIUtils.showLoadDialog(that,"操作中...");
                        BaseQuestStart.NeighborhoodImFriendRequestAccept(that,fuid=item.fuid);
                        break;
                    case R.id.btnReject:
                        UIUtils.showLoadDialog(that,"操作中...");
                        BaseQuestStart.NeighborhoodImFriendRequestRefuse(that,item.fuid);
                        break;
                }
            }
        });
        filterPopWindow=new FilterPopWindow(that);
        filterPopWindow.setListData(TestTool.asArrayList("添加好友"), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    CommonIntent.startChatSearchFriendsActivity(that);
                }else  if(position==1){
                    getSession().remove("selectedLinkMans");
                    CommonIntent.startChatSelectLinkManActivity(that,null);
                }
                filterPopWindow.dismiss();
            }
        });
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加人
                filterPopWindow.showAsDropDown(v);
            }
        });
        RedDotUtil.setNumber(0,RedDotUtil.CODE_50);//清空好友请求红点数
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case NEIGHBORHOOD_IM_FRIEND_REQUEST_REFUSE_CODE:
            case NEIGHBORHOOD_IM_FRIEND_REQUEST_ACCEPT_CODE:
                UIUtils.shortM(bean);
                if(bean.status==1){
                    if(requestCode==NEIGHBORHOOD_IM_FRIEND_REQUEST_ACCEPT_CODE){
                        //插入消息到融云里面
                        TextMessage txtMessage = TextMessage.obtain("我通过了你的朋友验证请求，现在我们可以开始聊天了");
                        Message message = Message.obtain(fuid, Conversation.ConversationType.PRIVATE, txtMessage);
                        RongIM.getInstance().sendMessage(message, "", "", new IRongCallback.ISendMessageCallback() {
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
                    }
                    pageLimitDelegate.refreshPage();
                }
                break;
            case NEIGHBORHOOD_IM_FRIEND_REQUEST_LIST_CODE:
                List<FriendRequestBean> data = bean.Data();
                pageLimitDelegate.setData(data);
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }


}
