package com.cnsunrun.messages;

import android.os.Bundle;
import android.text.Html;
import android.webkit.WebView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.RedDotUtil;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.cnsunrun.messages.mode.NoticeListBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_NOTICE_DETAIL_CODE;
import static com.cnsunrun.common.util.RedDotUtil.TYPE_10;
import static com.cnsunrun.common.util.RedDotUtil.idKey;

/**
 * 我的消息详情
 */
public class NoticeMessageDetailsActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.imgRedDot)
    RoundedImageView imgRedDot;
    @BindView(R.id.tv_message_title)
    TextView tvMessageTitle;
    @BindView(R.id.tv_message_time)
    TextView tvMessageTime;
    @BindView(R.id.tv_message_content)
    TextView tvMessageContent;
    @BindView(R.id.webView)
    WebView webView;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_messages_details);
        UIUtils.showLoadDialog(that,"加载中...");
        String id = getIntent().getStringExtra("id");
        url = getIntent().getStringExtra("url");
        BaseQuestStart.get_notice_Details(that, id);
        RedDotUtil.setNumber(0,idKey(id,TYPE_10));
    }

    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case GET_NOTICE_DETAIL_CODE:
                if(bean.status==1){
                    NoticeListBean notice=bean.Data();
                    setPageData(notice);
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }

    private void setPageData(NoticeListBean notice) {
        tvMessageTitle.setText(notice.title);
        tvMessageTime.setText(notice.add_time);
        tvMessageContent.setText(Html.fromHtml(String.valueOf(notice.content)));
//        WebViewTool.webviewDefaultConfig(webView);
//        webView.setWebViewClient(new WebViewTool.WebViewClientBase(){
//            @Override
//            public void onPageFinished(WebView view, String url) {
//                super.onPageFinished(view, url);
//                WebViewTool.imgReset(view);
//            }
//        });
//        webView.loadUrl(notice.h5);
//        webView.loadDataWithBaseURL(BuildConfig.WEB_SITE, notice.content, "text/html;charset=UTF-8", null, null);

    }


}
