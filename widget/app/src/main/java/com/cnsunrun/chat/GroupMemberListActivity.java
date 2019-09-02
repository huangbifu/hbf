package com.cnsunrun.chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.GroupMemberAdapter;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_MEMBER_LIST_CODE;

/**
 * Created by wangchao on 2018-11-12.
 * 游戏群组-群成员
 */
public class GroupMemberListActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private String id;
    PageLimitDelegate pageLimitDelegate=new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.NeighborhoodImGroupMemberList(that,id,page);
        }
    });
    private GroupMemberAdapter groupMemberAdapter;

    public static void startThis(Context that, String targetId) {
        Intent intent = new Intent(that, GroupMemberListActivity.class);
        intent.putExtra("id", targetId);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        id = getIntent().getStringExtra("id");
        groupMemberAdapter = new GroupMemberAdapter();
        recyclerView.setAdapter(groupMemberAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(that,5));
        pageLimitDelegate.attach(refreshLayout,recyclerView, groupMemberAdapter);
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case NEIGHBORHOOD_IM_GROUP_MEMBER_LIST_CODE:
                List<LinkManInfo> linkManInfos=bean.Data();
                pageLimitDelegate.setData(linkManInfos);
//                if (bean.status==1){
//
//                    if (!EmptyDeal.isEmpy(linkManInfos)){
//
//                    }else{
//
//                    }
//                }else{
//                    pageLimitDelegate.loadComplete();
//                    groupMemberAdapter.setEnableLoadMore(false);
//                }
                break;
        }
    }
}
