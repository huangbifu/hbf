package com.cnsunrun.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatGroupsAdapter;
import com.cnsunrun.chat.adapters.ChatNewFriendsAdapter;
import com.cnsunrun.chat.logic.ChatBgLogic;
import com.cnsunrun.chat.mode.GroupItemBean;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uibase.BaseActivity;

import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_LIST_CODE;

/**
 * 咵天,群组
 * Created by WQ on 2017/10/30.
 */

public class ChatGroupListActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate<GroupItemBean> pageLimitDelegate = new PageLimitDelegate<>(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.NeighborhoodImGroupList(that,page);
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_groups);
        final ChatGroupsAdapter repairsRecordAdapter = new ChatGroupsAdapter(null);
        recyclerView.setAdapter(repairsRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        pageLimitDelegate.attach(refreshLayout, recyclerView, repairsRecordAdapter);
        pageLimitDelegate.setPageEnable(false);//设置不能分页
        GetEmptyViewUtils.bindEmptyView(that,repairsRecordAdapter,0,"暂无群组信息",true);
        repairsRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                CommonIntent.startChatUserInfoActivity(that,false);
                GroupItemBean item = repairsRecordAdapter.getItem(position);
                ChatBgLogic.saveChatBg((BaseActivity) that,item.chat_image,item.id,true);
                RongIM.getInstance().startGroupChat(that,item.id,item.title);
            }
        });
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //搜索
//                CommonIntent.startChatSearchActivity(that);
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        pageLimitDelegate.refreshPage();
    }

    public void nofityUpdate(int requestCode, BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_GROUP_LIST_CODE:
                if(bean.status==1){
                    List<GroupItemBean> data=bean.Data();
                    pageLimitDelegate.setData(data);
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }


}
