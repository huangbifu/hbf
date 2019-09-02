package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.adapter.RecommendAdapter;
import com.cnsunrun.mine.mode.CommissionBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_COMMOISSION_CODE;

/**
 * Created by wangchao on 2018-09-21.
 * 我的推荐
 */
public class MyRecommendActvity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tvMyIncome)
    TextView tvMyIncome;
    @BindView(R.id.tvTodayIncome)
    TextView tvTodayIncome;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
                BaseQuestStart.getCommissionData(that, page);
        }
    });

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, MyRecommendActvity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_recommend);
        ButterKnife.bind(this);
        initViews();
        initListener();
    }

    private void initListener() {
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecommendLevelActrivity.startThis(that,1,"");
            }
        });
    }

    private void initViews() {
        recyclerView.setLayoutManager(new LinearLayoutManager(that));
        RecommendAdapter recommendAdapter = new RecommendAdapter();
        View headView = View.inflate(that, R.layout.item_recommend_header_layout, null);
        recommendAdapter.addHeaderView(headView);
        recyclerView.setAdapter(recommendAdapter);
        pageLimitDelegate.attach(refreshLayout, recyclerView, recommendAdapter);
        GetEmptyViewUtils.bindEmptyView(that, recommendAdapter, R.drawable.ic_empty_message, "暂无数据", true);
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case GET_COMMOISSION_CODE:
                if (bean.status == 1) {
                    CommissionBean commissionBean = bean.Data();
                    updateUi(commissionBean);
                }
                break;
        }

    }

    private void updateUi(CommissionBean commissionBean) {
        tvMyIncome.setText(commissionBean.total_money);
        tvTodayIncome.setText(EmptyDeal.isEmpy(commissionBean.today_total_money) ? "+0.00" : commissionBean.today_total_money);
        pageLimitDelegate.setData(commissionBean.list);
    }
}