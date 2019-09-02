package com.cnsunrun.mine.activity;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.adapter.MessageAdapter;
import com.cnsunrun.mine.mode.MessageInfo;
import com.sunrun.sunrunframwork.bean.BaseBean;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 我的-消息列表
 */
public class MessageActivity extends LBaseActivity implements PageLimitDelegate.DataProvider {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    private MessageAdapter mAdapter;
    private PageLimitDelegate<MessageInfo> pageLimitDelegate = new PageLimitDelegate<>(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_message);
        ButterKnife.bind(this);
        initRecyclerView();
    }


    private void initRecyclerView() {
        swipeRefreshLayout.setColorSchemeResources(R.color.com_color);
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(mAdapter = new MessageAdapter(this, R.layout.item_message));
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {


            }
        });
        pageLimitDelegate.attach(swipeRefreshLayout, recyclerView, mAdapter);

        pageLimitDelegate.setData(Arrays.asList(new MessageInfo(),new MessageInfo()));

    }


    @Override
    public void loadData(int page) {
//        BaseQuestStart.getMessage(that, Config.getLoginInfo().getMember_id(), tabLayout.getCurrentTab() + 1, page);
    }
    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
//        if (requestCode == BaseQuestConfig.QUEST_GET_MESSAGE_CODE) {
//            if (bean.status > 0) {
//                //消息列表
//                List<MessageInfo> listBeen = bean.Data();
//                pageLimitDelegate.setData(listBeen);
//            }
//        }
//        if (requestCode == BaseQuestConfig.QUEST_GET_IS_READ_MESSAGE_CODE) {
//            if (bean.status > 0) {
//                //消息是否已读
//                pageLimitDelegate.refreshPage();
//            }
//        }

        swipeRefreshLayout.setRefreshing(false);
        GetEmptyViewUtils.bindEmptyView(that, mAdapter, R.drawable.com_no_data_pic, "暂无数据信息", true);
        super.nofityUpdate(requestCode, bean);
    }



}
