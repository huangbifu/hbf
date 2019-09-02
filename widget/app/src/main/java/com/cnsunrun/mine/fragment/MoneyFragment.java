package com.cnsunrun.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseFragment;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.mine.adapter.MoneyRecordAdapter;
import com.cnsunrun.mine.mode.WalletBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_WALLET_INDEX_CODE;

/**
 * Created by wangchao on 2018-09-26.
 */
public class MoneyFragment extends LBaseFragment {

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private String type;
    private MoneyRecordAdapter moneyRecordAdapter;
    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.getWalletIndex(MoneyFragment.this, page, type);
        }
    });

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_money;
    }

    public static MoneyFragment newInstance(String type) {
        MoneyFragment moneyFragment = new MoneyFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        moneyFragment.setArguments(bundle);
        return moneyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            type = getArguments().getString("type");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
    }

    private void initViews() {
        UIUtils.showLoadDialog(that, getString(R.string.loading));
        moneyRecordAdapter = new MoneyRecordAdapter();
        View headView = View.inflate(that, R.layout.item_money_layout, null);
        moneyRecordAdapter.addHeaderView(headView);
        recyclerView.setAdapter(moneyRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that));
        pageLimitDelegate.attach(refreshLayout,recyclerView,moneyRecordAdapter);
        GetEmptyViewUtils.bindEmptyView(that, moneyRecordAdapter, R.drawable.ic_empty_message, "暂无数据", true);

    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case GET_WALLET_INDEX_CODE:
                if (bean.status==1){
                    WalletBean walletBean=bean.Data();
                    updateUi(walletBean);
                }
                break;
        }
    }

    private void updateUi(WalletBean walletBean) {
        EventBus.getDefault().post(walletBean);
        pageLimitDelegate.setData(walletBean.list);
    }
}
