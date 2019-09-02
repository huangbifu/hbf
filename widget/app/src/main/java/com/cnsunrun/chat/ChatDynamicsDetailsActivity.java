package com.cnsunrun.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatSearchDynamicsAdapter;
import com.cnsunrun.chat.dialog.CommentDialog;
import com.cnsunrun.chat.fragment.ChatHomePageFragment;
import com.cnsunrun.chat.mode.ChatDynamicsBean;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.base.StatusBarConfig;
import com.cnsunrun.common.event.DefaultEvent;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.util.RedDotUtil;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.common.config.Const.EVENT_CHAT_COMMENT;
import static com.cnsunrun.common.config.Const.EVENT_DELETE_DYNAMICS;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_DETAIL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_LIKE_CANCEL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_LIKE_CODE;

/**
 * 咵天,动态详情
 * Created by WQ on 2017/10/30.
 */

public class ChatDynamicsDetailsActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate<ChatDynamicsBean.ListBean> pageLimitDelegate = new PageLimitDelegate<>(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            if(page==1) {
//                BaseQuestStart.NeighborhoodImUpdatesDetail(that, getIntent().getStringExtra("id"));
            }else {
                pageLimitDelegate.setData(null);
            }
        }
    });
    @BindView(R.id.editContent)
    EditText editContent;
     ChatSearchDynamicsAdapter repairsRecordAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dynamics_details);
        initEventBus();
        StatusBarConfig.assistActivity(this);
        repairsRecordAdapter = new ChatSearchDynamicsAdapter(null,true);
        recyclerView.setAdapter(repairsRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        pageLimitDelegate.attach(refreshLayout, recyclerView, repairsRecordAdapter);
        repairsRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }
        });
        repairsRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()){
//                    case R.id.txtLike:
//                        ChatDynamicsBean.ListBean item = repairsRecordAdapter.getItem(position);
//                        UIUtils.showLoadDialog(that,"操作中..");
//                        if (!repairsRecordAdapter.containtLikes(item.like_list)) {
//                            BaseQuestStart.NeighborhoodImUpdatesLike(that, item.id);
//                        }else {
//                            BaseQuestStart.NeighborhoodImUpdatesLikeCancel(that,item.id);
//                        }
//                        break;
                }
            }
        });
        editContent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = getIntent().getStringExtra("id");
                CommentDialog.newInstance().setId(id)
                        .show(getSupportFragmentManager(),"CommentDialog");
            }
        });

        RedDotUtil.setNumber(0,RedDotUtil.CODES_DYNAMICS);//清空动态红点数
    }

    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_UPDATES_LIKE_CANCEL_CODE:
            case NEIGHBORHOOD_IM_UPDATES_LIKE_CODE:
                UIUtils.shortM(bean);
                pageLimitDelegate.refreshPage();
                recyclerView.smoothScrollToPosition(0);
                break;
            case NEIGHBORHOOD_IM_UPDATES_DETAIL_CODE:
                if(bean.status==1){
                    ChatDynamicsBean.ListBean detailsBean=bean.Data();
                    List<ChatDynamicsBean.ListBean> dataList = TestTool.asArrayList(detailsBean);
                    initExtend(dataList);
                    pageLimitDelegate.setData(dataList);
                }else {
                    UIUtils.shortM(bean);
                    finish();
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }

    private void initExtend(List<ChatDynamicsBean.ListBean> dataList) {
         if(dataList==null
                 )return;
        for (int i = 0; i < dataList.size(); i++) {
            repairsRecordAdapter.addExpand(i);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DefaultEvent event){
        switch (event.getAction()){
            case EVENT_CHAT_COMMENT:
                pageLimitDelegate.refreshPage();
                break;
            case EVENT_DELETE_DYNAMICS:
                finish();
        }
    }

}
