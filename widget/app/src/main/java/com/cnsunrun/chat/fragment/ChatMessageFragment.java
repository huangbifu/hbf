package com.cnsunrun.chat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.SearchChatResultAdapter;
import com.cnsunrun.chat.dialog.FilterPopWindow;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseFragment;
import com.cnsunrun.common.config.IMUserInfoProvider;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.recyclerview.DivideLineItemDecoration;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.log.Logger;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;

/**
 * 咵天-会话列表
 * Created by WQ on 2017/11/7.
 */

public class ChatMessageFragment extends LBaseFragment {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.searchResult)
    View searchResult;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    FilterPopWindow filterPopWindow;//右边功能菜单列表
    @BindView(R.id.edit_search)
    EditText editSearch;
    SearchChatResultAdapter searchChatResultAdapter;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加人
//                CommonIntent.startChatSearchFriendsActivity(that);
                filterPopWindow.showAsDropDown(v);
            }
        });
        filterPopWindow=new FilterPopWindow(that);
        filterPopWindow.setListData(TestTool.asArrayList("添加好友" ), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    CommonIntent.startChatSearchFriendsActivity(that);
                }else  if(position==1){
                    getSession().remove("selectedLinkMans");
                    CommonIntent.startChatSelectLinkManActivity(that,null);
                }
                filterPopWindow.dismiss();
            }
        });
        titleBar.setLeftAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonIntent.startNoticeMessageActivity(that);
            }
        });
        searchChatResultAdapter = new SearchChatResultAdapter(null);
        LinearLayoutManager layout = new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false);
        recyclerView.addItemDecoration(new DivideLineItemDecoration(that,LinearLayoutManager.VERTICAL, getResources().getColor(R.color.limit_line1), 1, TypedValue.COMPLEX_UNIT_PX));
        recyclerView.setLayoutManager(layout);
        recyclerView.setAdapter(searchChatResultAdapter);
//        pageLimitDelegate.attach(refreshLayout, recyclerView, searchChatResultAdapter);
        searchChatResultAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter baseQuickAdapter, View view, int position) {
                Conversation item = (Conversation) searchChatResultAdapter.getItem(position);
                String senderUserId = item.getSenderUserId();
                String title = null;
                UserInfo userInfo = IMUserInfoProvider.getInstance().getUserInfo(senderUserId);
                if (userInfo != null) {
                    title = userInfo.getName();
                }
                if (TextUtils.isEmpty(title)) {
                    title = item.getTargetId();
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
//                    pageLimitDelegate.page = 1;
                    searchChatResultAdapter.keywords = editSearch.getText().toString();
                    getPageData();
                }
                return true;
            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //搜索
//                pageLimitDelegate.page = 1;
                searchChatResultAdapter.keywords = editSearch.getText().toString();
                getPageData();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void getPageData() {
//        UIUtils.showLoadDialog(that);
        RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
            @Override
            public void onSuccess(List<Conversation> conversations) {
                List<Conversation> result = new ArrayList<>();
                if (!EmptyDeal.isEmpy(conversations)) {
                    for (Conversation conversation : conversations) {
                        Logger.D(conversation.getConversationTitle());
                        if (String.valueOf(conversation.getConversationTitle()).contains(editSearch.getText().toString())) {
                            result.add(conversation);
                        }
                    }
                }
                searchChatResultAdapter.setNewData(result);
                UIUtils.cancelLoadDialog();
                searchResultVisibility();
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                searchChatResultAdapter.setNewData(null);
                UIUtils.cancelLoadDialog();
                searchResultVisibility();
            }
        }, Conversation.ConversationType.PRIVATE);
    }

    private void searchResultVisibility() {
        if(EmptyDeal.isEmpy(searchChatResultAdapter.getData())||EmptyDeal.isEmpy(editSearch)){
            searchResult.setVisibility(View.GONE);
        }else {
            searchResult.setVisibility(View.VISIBLE);
        }
    }

    public static ChatMessageFragment newInstance() {
        
        Bundle args = new Bundle();
        
        ChatMessageFragment fragment = new ChatMessageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chat_message;
    }

}
