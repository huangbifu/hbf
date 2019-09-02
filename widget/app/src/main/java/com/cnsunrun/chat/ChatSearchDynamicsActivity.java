package com.cnsunrun.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatSearchDynamicsAdapter;
import com.cnsunrun.chat.mode.ChatDynamicsBean;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.view.sidebar.SortModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_LIST_CODE;

/**
 * 咵天,搜索动态
 * Created by WQ on 2017/10/30.
 */

public class ChatSearchDynamicsActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            if(EmptyDeal.isEmpy(editSearch)){
                pageLimitDelegate.setData(null);
            }else {
//                BaseQuestStart.NeighborhoodImUpdatesList(that,page,editSearch.getText().toString());
            }
        }
    });
    @BindView(R.id.edit_search)
    EditText editSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search_history);
        final ChatSearchDynamicsAdapter repairsRecordAdapter = new ChatSearchDynamicsAdapter(null){
            @Override
            protected void convert(BaseViewHolder helper, ChatDynamicsBean.ListBean item) {
                super.convert(helper, item);
                helper.setVisible(R.id.layLike, false);
                helper.setVisible(R.id.layComments, false);
                helper.setVisible(R.id.viewHandleLine, false);
                helper.setVisible(R.id.txtDelete, false);
                helper.setVisible(R.id.txtLike, false);
                helper.setVisible(R.id.txtComment, false);
            }
        };
        recyclerView.setAdapter(repairsRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        pageLimitDelegate.attach(refreshLayout, recyclerView, repairsRecordAdapter);
        repairsRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                ChatDynamicsBean.ListBean item = repairsRecordAdapter.getItem(position);
//                CommonIntent.startChatDynamicsDetailsActivity(that, item.id);
            }
        });
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    //搜索
                    pageLimitDelegate.refreshPage();
                }
                return true;
            }
        });


    }

    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_UPDATES_LIST_CODE:
                if(bean.status==1){
                    ChatDynamicsBean dynamicsBean=bean.Data();
                    List<ChatDynamicsBean.ListBean> data = getData(dynamicsBean.list,editSearch.getText().toString());
                    if(EmptyDeal.isEmpy(data)){
                        UIUtils.shortM("未搜索到相关信息");
                    }else {
                        pageLimitDelegate.setData(data);
                    }
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }

    public static   List<ChatDynamicsBean.ListBean> getData(List<ChatDynamicsBean.ListBean> sourceDateList, String filterStr){
        List<ChatDynamicsBean.ListBean> filterDateList = new ArrayList<ChatDynamicsBean.ListBean>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList.addAll( sourceDateList);
        } else {
            filterDateList.clear();
            if(sourceDateList!=null)
                for (ChatDynamicsBean.ListBean sortModel : sourceDateList) {
                    String content = String.valueOf(sortModel.content);
                    if (content.toLowerCase().contains(filterStr.toString().toLowerCase()) ) {
                        filterDateList.add(sortModel);
                    }
                }
        }
        return filterDateList;
    }
}
