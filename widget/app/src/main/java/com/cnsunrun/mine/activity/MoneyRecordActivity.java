package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.base.ViewPagerFragmentAdapter;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.fragment.MoneyFragment;
import com.cnsunrun.mine.mode.WalletBean;
import com.flyco.tablayout.SlidingTabLayout;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchao on 2018-09-26.
 * 资金记录
 */
public class MoneyRecordActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.llContainer)
    LinearLayout llContainer;
    @BindView(R.id.tabLayout)
    SlidingTabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    private String[] mTitles = {"充值记录", "转账记录", "提现记录", "支付记录"};
    private List<Fragment> baseFragments;
    private ViewPagerFragmentAdapter mVPAdapter;

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, MoneyRecordActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initEventBus();
        setContentView(R.layout.activity_money_record);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        baseFragments = new ArrayList<>();
        baseFragments.add(MoneyFragment.newInstance("1"));
        baseFragments.add(MoneyFragment.newInstance("2"));
        baseFragments.add(MoneyFragment.newInstance("3"));
        baseFragments.add(MoneyFragment.newInstance("4"));
        mVPAdapter = new ViewPagerFragmentAdapter(this.getSupportFragmentManager());
        mVPAdapter.setFragments(baseFragments);
        viewPager.setAdapter(mVPAdapter);
        viewPager.setOffscreenPageLimit(baseFragments.size());
        setTabData(tabLayout, mTitles);
        viewPager.setCurrentItem(0, false);
    }

    /**
     * 设置标签视图内容,
     *
     * @param tabLayout
     * @param titles    文字
     */
    public void setTabData(SlidingTabLayout tabLayout, String titles[]) {
        tabLayout.setViewPager(viewPager, titles);
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {

            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Subscribe
    public void eventBusMethod(WalletBean walletBean) {
        if (!EmptyDeal.isEmpy(walletBean)) {
            tvMoney.setText(walletBean.balance);
        }
    }
}
