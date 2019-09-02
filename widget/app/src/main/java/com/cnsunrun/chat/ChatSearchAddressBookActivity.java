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
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatSearchAddressBookAdapter;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.view.sidebar.CharacterParser;
import com.sunrun.sunrunframwork.view.sidebar.PinyinComparator;
import com.sunrun.sunrunframwork.view.sidebar.SortModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;

/**
 * 咵天,搜索通讯录
 * Created by WQ on 2017/10/30.
 */

public class ChatSearchAddressBookActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            if(page==1){
                pageLimitDelegate.setData(getData(linkManInfos,editSearch.getText().toString()));
            }else {
                pageLimitDelegate.setData(null);
            }
        }
    });
    @BindView(R.id.edit_search)
    EditText editSearch;
    List<LinkManInfo> linkManInfos;
    /**
     * 拼音排序的比较器
     */
    private static PinyinComparator pinyinComparator = new PinyinComparator();
    /**
     * 实例化汉字转拼音类
     */
    private static CharacterParser characterParser = CharacterParser.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search_history);
        linkManInfos= getSession().getBean("LinkManInfos",new TypeToken<List<LinkManInfo>>(){});
        final ChatSearchAddressBookAdapter repairsRecordAdapter = new ChatSearchAddressBookAdapter(null);
        recyclerView.setAdapter(repairsRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        pageLimitDelegate.attach(refreshLayout, recyclerView, repairsRecordAdapter);
        repairsRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                CommonIntent.startChatUserInfoActivity(that, repairsRecordAdapter.getItem(position).id, false);
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



    public static  <T extends SortModel> List<T > getData( List<T > sourceDateList,String filterStr){
        List<T > filterDateList = new ArrayList<T>();
        if (TextUtils.isEmpty(filterStr)) {
            filterDateList.addAll((Collection<? extends T>) sourceDateList);
        } else {
            filterDateList.clear();
            if(sourceDateList!=null)
                for (SortModel sortModel : sourceDateList) {
                    String name = String.valueOf(sortModel);
                    if (name.toLowerCase().contains(filterStr.toString().toLowerCase()) || characterParser.getSelling(name).startsWith(filterStr.toString())) {
                        filterDateList.add((T) sortModel);
                    }
                }
        }
        // 根据a-z进行排序
        Collections.sort(filterDateList, pinyinComparator);
        return filterDateList;
    }


}
