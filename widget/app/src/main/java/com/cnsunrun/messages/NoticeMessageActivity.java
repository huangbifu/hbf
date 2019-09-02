package com.cnsunrun.messages;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.cnsunrun.messages.adapters.NoticeMessageAdapter;
import com.cnsunrun.messages.mode.NoticeListBean;
import com.sunrun.sunrunframwork.bean.BaseBean;

import java.util.List;

import butterknife.BindView;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_NOTICE_CODE;

/**
 * 我的消息
 */
public class NoticeMessageActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate<NoticeListBean> pageLimitDelegate=new PageLimitDelegate<>(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.get_notice(that,page);
        }
    });
    NoticeMessageAdapter newsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_messages);
        newsAdapter = new NoticeMessageAdapter(getSession(),null);
        recyclerView.setAdapter(newsAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        pageLimitDelegate.attach(refreshLayout,recyclerView,newsAdapter);
//        pageLimitDelegate.setPageEnable(false);
        GetEmptyViewUtils.bindEmptyView(that,newsAdapter, R.drawable.ic_empty_message,"暂无消息通知",true);
        newsAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                NoticeListBean item = newsAdapter.getItem(position);
                getSession().put(item.id,true);
                CommonIntent.startNoticeMessageDetailsActivity(that, item.id, item.h5);
                newsAdapter.notifyDataSetChanged();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(newsAdapter!=null){
//            newsAdapter.notifyDataSetChanged();
            pageLimitDelegate.refreshPage();
        }
    }

    public void nofityUpdate(int requestCode, BaseBean bean){
        switch(requestCode){
            case GET_NOTICE_CODE:
                NoticeListBean.NoticeListPage listPage=bean.Data();
                if(listPage!=null) {
                    pageLimitDelegate.setData(listPage.list);
                }else {
                    pageLimitDelegate.setData(null);
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }
}
