package com.cnsunrun.chat;

import android.os.Bundle;
import android.text.Html;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 咵天,群公告
 * Created by WQ on 2017/10/30.
 */

public class ChatGroupNoticeDetailsActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tv_message_title)
    TextView tvMessageTitle;
    @BindView(R.id.tvMessageTime)
    TextView tvMessageTime;
    @BindView(R.id.tv_message_content)
    TextView tvMessageContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_group_notice_details);
        String targetId = getIntent().getStringExtra("targetId");
        tvMessageTime.setText(getIntent().getStringExtra("notice_time"));
        tvMessageContent.setText(Html.fromHtml(String.valueOf(getIntent().getStringExtra("notice"))));
    }


}
