package com.cnsunrun.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatRedpackInfoAdapter;
import com.cnsunrun.chat.adapters.ChatRedpackRecordAdapter;
import com.cnsunrun.chat.logic.RedRecordHeadLogic;
import com.cnsunrun.chat.mode.RedPackInfoBean;
import com.cnsunrun.chat.mode.RedPackRecord;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_MY_REDPACK_RECORD_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.GET_REDPACK_CODE;

/**
 * 红包记录
 */
public class RedpackRecordActivity extends LBaseActivity implements PageLimitDelegate.DataProvider{


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate<RedPackRecord.ListBean> pageLimitDelegate = new PageLimitDelegate<>(this);
    private ChatRedpackRecordAdapter redpackInfoAdapter;
    RedRecordHeadLogic redRecordHeadLogic;
    public static void startThis(Activity that) {
        Intent intent = new Intent(that, RedpackRecordActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpack_record);
        View headView=getLayoutInflater().inflate(R.layout.include_redpack_record_head,null);
        redRecordHeadLogic=new RedRecordHeadLogic(headView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        redpackInfoAdapter = new ChatRedpackRecordAdapter();
        redpackInfoAdapter.setHeaderView(headView);
        recyclerView.setAdapter(redpackInfoAdapter);
        pageLimitDelegate.attach(refreshLayout,recyclerView,redpackInfoAdapter);

    }
    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case GET_MY_REDPACK_RECORD_CODE:
                RedPackRecord redPackRecord=bean.Data();
                if(redPackRecord!=null){
                    redRecordHeadLogic.setPageData(redPackRecord);
                    pageLimitDelegate.setData(redPackRecord.list);
                }else {
                    UIUtils.shortM(bean);
                }
                break;
        }
    }


    @Override
    public void loadData(int page) {
        BaseQuestStart.get_my_redpack_record(this,page,getIntent().getStringExtra("type"));
    }
}
