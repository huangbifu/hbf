package com.cnsunrun.chat;

import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.util.WebViewTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;

/**
 * html网页详情
 */
public class WebDetailsActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.webView)
    WebView webView;
    String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webdetails);
        UIUtils.showLoadDialog(that,"加载中...");
        url = getIntent().getStringExtra("url");
        setPageData();
        String title = getIntent().getStringExtra("title");
        if(!TextUtils.isEmpty(title))
        {
            titleBar.setTitle(title);
        }
    }

    private void setPageData() {
       WebViewTool.webviewDefaultConfig(webView);
        webView.setWebViewClient(new WebViewTool.WebViewClientBase(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                WebViewTool.imgReset(view);
            }
        });
        webView.loadUrl(url);

    }


}
