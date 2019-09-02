package com.cnsunrun.chat;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 咵天-搜索
 */
public class ChatSearchActivity extends LBaseActivity {


    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.layHistory)
    LinearLayout layHistory;
    @BindView(R.id.layAddressBook)
    LinearLayout layAddressBook;
    @BindView(R.id.layDynamics)
    LinearLayout layDynamics;
    @BindView(R.id.activity_search)
    LinearLayout activitySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_search);

    }


    @OnClick({R.id.layHistory, R.id.layAddressBook, R.id.layDynamics})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layHistory:
//                CommonIntent.startChatSearchHistoryActivity(that,"",0);
                break;
            case R.id.layAddressBook:
//                CommonIntent.startChatSearchAddressBookActivity(that);
                break;
            case R.id.layDynamics:
//                CommonIntent.startChatSearchDynamicsActivity(that);
                break;
        }
    }
}
