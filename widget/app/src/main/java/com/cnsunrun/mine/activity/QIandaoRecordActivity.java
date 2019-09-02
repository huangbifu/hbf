package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.adapter.QiandaoRecordAdapter;
import com.cnsunrun.mine.mode.QiandaoRecord;
import com.sunrun.sunrunframwork.bean.BaseBean;

import butterknife.BindView;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_RECHAR_INFO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.SIGN_RECORD_CODE;

/**
 * 签到更多记录
 */
public class QIandaoRecordActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private QiandaoRecordAdapter qiandaoRecordAdapter;
    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.sign_record(QIandaoRecordActivity.this, page);
        }
    });

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, QIandaoRecordActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qiandao_record);
        qiandaoRecordAdapter =new QiandaoRecordAdapter();
        recyclerView.setAdapter(qiandaoRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that));
        pageLimitDelegate.attach(refreshLayout,recyclerView, qiandaoRecordAdapter);
        GetEmptyViewUtils.bindEmptyView(that, qiandaoRecordAdapter, R.drawable.ic_empty_message, "暂无数据", true);
    }



    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case SIGN_RECORD_CODE:
                QiandaoRecord data = bean.Data();
                pageLimitDelegate.setData(QiandaoRecord.getList(data));
                break;
        }
    }




}
