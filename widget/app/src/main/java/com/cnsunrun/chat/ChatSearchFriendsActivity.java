package com.cnsunrun.chat;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.view.ItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_MEMBER_SEARCH_CODE;

/**
 * 咵天,搜索好友
 * Created by WQ on 2017/10/30.
 */

public class ChatSearchFriendsActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.itemScan)
    ItemView itemScan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search_history);
        editSearch.setHint("手机号");
        editSearch.setInputType(InputType.TYPE_CLASS_NUMBER);
        editSearch.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (editSearch.length() != 11) {
                        UIUtils.shortM("请输入正确的手机号");
                        return true;
                    }
                    //搜索
                    UIUtils.showLoadDialog(that, "搜索中");
                    BaseQuestStart.NeighborhoodImMemberSearch(that, editSearch);
                }
                return true;
            }
        });
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case NEIGHBORHOOD_IM_MEMBER_SEARCH_CODE:
                String id = JsonDeal.createJsonObj(bean.toString()).optString("id");
                if (EmptyDeal.isEmpy(id)) {
                    UIUtils.shortM(bean);
                } else {
                    CommonIntent.startChatUserInfoActivity(that, id, false);
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }


    @OnClick(R.id.itemScan)
    public void onViewClicked() {
        CommonIntent.startScanQRActivity(this);
    }
}
