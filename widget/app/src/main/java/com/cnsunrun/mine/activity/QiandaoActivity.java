package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.adapter.QiandaoDateAdapter;
import com.cnsunrun.mine.adapter.QiandaoRecordAdapter;
import com.cnsunrun.mine.mode.QiandaoInfo;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.SIGN_INFO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.SIGN_IN_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.SIGN_RULE_URL;


/**
 * 签到
 */
public class QiandaoActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.llMoney)
    LinearLayout llMoney;
    @BindView(R.id.btnQiandao)
    Button btnQiandao;
    @BindView(R.id.tvTotalDay)
    TextView tvTotalDay;
    @BindView(R.id.tvShowAll)
    TextView tvShowAll;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.rlvDate)
    RecyclerView rlvDate;
    QiandaoDateAdapter qiandaoDateAdapter;
    private QiandaoRecordAdapter qiandaoRecordAdapter;
    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.sign_info(QiandaoActivity.this);
        }
    });

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, QiandaoActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiandao_page);
        ButterKnife.bind(this);
        qiandaoRecordAdapter =new QiandaoRecordAdapter();
        recyclerView.setAdapter(qiandaoRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that));
        pageLimitDelegate.attach(refreshLayout,recyclerView, qiandaoRecordAdapter);
        GetEmptyViewUtils.bindEmptyView(that, qiandaoRecordAdapter, R.drawable.ic_empty_message, "暂无数据", true);
        pageLimitDelegate.setPageEnable(false);

        qiandaoDateAdapter=new QiandaoDateAdapter();
        rlvDate.setLayoutManager(new LinearLayoutManager(that,LinearLayoutManager.HORIZONTAL,false));
        rlvDate.setAdapter(qiandaoDateAdapter);
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonIntent.startWebDetailsActivity(that,SIGN_RULE_URL,"签到规则");
            }
        });


    }


    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode){
            case SIGN_IN_CODE:
                UIUtils.shortM(bean);
                pageLimitDelegate.refreshPage();
                break;
            case SIGN_INFO_CODE:
                if(bean.status==1) {
                    setPageData((QiandaoInfo)bean.Data());
                }else {
                    UIUtils.shortM(bean);
                }
                break;
        }

    }

    private void setPageData(QiandaoInfo data) {
        tvMoney.setText(""+data.sign_reward_money);
        tvTotalDay.setText( data.sign_num+"天");
        if (data.today_sign==1) {
            llMoney.setVisibility(View.VISIBLE);
            btnQiandao.setVisibility(View.GONE);
        }else {
            btnQiandao.setVisibility(View.VISIBLE);
            llMoney.setVisibility(View.GONE);
        }
        pageLimitDelegate.setData(data.sign_reward_record);
        qiandaoDateAdapter.setNewData(data.sgin_list);
    }

    @OnClick({R.id.btnQiandao, R.id.tvShowAll})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnQiandao:
                UIUtils.showLoadDialog(that,"签到中..");
                BaseQuestStart.sign_in(this);
                break;
            case R.id.tvShowAll:
                CommonIntent.startQIandaoRecordActivity(this);
                break;
        }
    }
}
