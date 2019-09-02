package com.cnsunrun.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.ChineseNumUtils;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.adapter.LevelAdapter;
import com.cnsunrun.mine.mode.LevelBean;
import com.sunrun.sunrunframwork.bean.BaseBean;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_NEXT_LEVEL_CODE;

/**
 * Created by wangchao on 2018-09-26.
 * 级别推荐
 */
public class RecommendLevelActrivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private int distance;
    private String member_id;
    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.getNextLevel(that, distance, member_id, page);
        }
    });

    public static void startThis(Context that, int distance, String member_id) {
        Intent intent = new Intent(that, RecommendLevelActrivity.class);
        intent.putExtra("distance", distance);
        intent.putExtra("member_id", member_id);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommend_level);
        ButterKnife.bind(this);
        distance = getIntent().getIntExtra("distance", 1);
        member_id = getIntent().getStringExtra("member_id");
        initViews();
    }

    private void initViews() {
        titleBar.setTitle(ChineseNumUtils.foematInteger(distance) + "级推荐");
        final LevelAdapter levelAdapter = new LevelAdapter();
        recyclerView.setAdapter(levelAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that));
        pageLimitDelegate.attach(refreshLayout, recyclerView, levelAdapter);
        GetEmptyViewUtils.bindEmptyView(that, levelAdapter, R.drawable.ic_empty_message, "暂无数据", true);

        levelAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                LevelBean.Info item = levelAdapter.getItem(position);
                RecommendLevelActrivity.startThis(that, item.distance + 1, item.member_id);
            }
        });
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case GET_NEXT_LEVEL_CODE:
                if (bean.status == 1) {
                    LevelBean levelBean = bean.Data();
                    updateUi(levelBean);
                }
                break;
        }
    }

    private void updateUi(LevelBean levelBean) {
        pageLimitDelegate.setData(levelBean.list);
    }
}
