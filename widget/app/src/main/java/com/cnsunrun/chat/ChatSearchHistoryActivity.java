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
import com.cnsunrun.chat.adapters.ChatSearchHistoryAdapter;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.config.Const;
import com.cnsunrun.common.config.IMUserInfoProvider;
import com.cnsunrun.common.event.DefaultEvent;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.view.ItemView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.SearchConversationResult;
import io.rong.imlib.model.UserInfo;

/**
 * 咵天,聊天记录搜索
 * Created by WQ on 2017/10/30.
 */

public class ChatSearchHistoryActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.itemScan)
    ItemView itemScan;

    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            if(page==1) {
                getPageData();
            }else {
                pageLimitDelegate.setData(null);
            }
        }
    });
    @BindView(R.id.edit_search)
    EditText editSearch;
    int type = 0;//0 全部 1 单聊 2 群组
    ChatSearchHistoryAdapter repairsRecordAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search_history);
        initEventBus();
        itemScan.setVisibility(View.GONE);
        type=getIntent().getIntExtra("type",0);
        repairsRecordAdapter = new ChatSearchHistoryAdapter(null);
        recyclerView.setAdapter(repairsRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        pageLimitDelegate.attach(refreshLayout, recyclerView, repairsRecordAdapter);
        repairsRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Message item = (Message) repairsRecordAdapter.getItem(position);
                String senderUserId = item.getSenderUserId();
                String title=null;
                UserInfo userInfo = IMUserInfoProvider.getInstance().getUserInfo(senderUserId);
                if(userInfo!=null){
                    title=userInfo.getName();
                }
                RongIM.getInstance().startConversation(that, item.getConversationType(), item.getTargetId(), title,
                        item.getSentTime());
            }
        });
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    //搜索
                    pageLimitDelegate.page=1;
                    repairsRecordAdapter.keywords=editSearch.getText().toString();
                    getPageData();
                }
                return true;
            }
        });
    }

    private void getPageData() {
        UIUtils.showLoadDialog(that);
        if (type == 0) {
            RongIMClient.getInstance().searchConversations(editSearch.getText().toString(),
                new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE, Conversation.ConversationType.GROUP},
                new String[]{"RC:TxtMsg", "RC:ImgTextMsg", "RC:FileMsg"}, new RongIMClient.ResultCallback<List<SearchConversationResult>>() {
                    @Override
                    public void onSuccess(List<SearchConversationResult> searchConversationResults) {
                        if(EmptyDeal.isEmpy(searchConversationResults)){
                            UIUtils.shortM("未搜索到相关聊天记录");
                            UIUtils.cancelLoadDialog();
                        }
                        for (SearchConversationResult searchConversationResult : searchConversationResults) {
                            Conversation conversation = searchConversationResult.getConversation();
                            String targetId = conversation.getTargetId();
                            Conversation.ConversationType conversationType = conversation.getConversationType();
                            final List<Message> messagesData=new ArrayList<Message>();
                            RongIMClient.getInstance().searchMessages(conversationType,
                                    targetId, editSearch.getText().toString(), 200, 0, new RongIMClient.ResultCallback<List<Message>>() {
                                        @Override
                                        public void onSuccess(List<Message> messages) {
                                            pageLimitDelegate.setData(messages);
                                            pageLimitDelegate.page++;
                                            UIUtils.cancelLoadDialog();
                                        }

                                        @Override
                                        public void onError(RongIMClient.ErrorCode e) {
                                            pageLimitDelegate.setData(null);
                                            UIUtils.cancelLoadDialog();
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode e) {
                        pageLimitDelegate.setData(null);
                        UIUtils.cancelLoadDialog();
                    }
                });
        } else {
            String targetId = getIntent().getStringExtra("id");
            Conversation.ConversationType conversationType =type==1? Conversation.ConversationType.PRIVATE: Conversation.ConversationType.GROUP;
            RongIMClient.getInstance().searchMessages(conversationType,
                    targetId, editSearch.getText().toString(), 200, 0, new RongIMClient.ResultCallback<List<Message>>() {
                        @Override
                        public void onSuccess(List<Message> messages) {
                            pageLimitDelegate.setData(messages);
                            UIUtils.cancelLoadDialog();
                        }

                        @Override
                        public void onError(RongIMClient.ErrorCode e) {
                            pageLimitDelegate.setData(null);
                            UIUtils.cancelLoadDialog();
                        }
                    });
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DefaultEvent defaultEvent){
        switch (defaultEvent.getAction()){
            case Const.EVENT_UPDATE_USERINFO:
                repairsRecordAdapter.notifyDataSetChanged();
                break;
        }
    }

}
