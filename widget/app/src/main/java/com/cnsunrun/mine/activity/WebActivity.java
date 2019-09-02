package com.cnsunrun.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.webkit.WebView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.cnsunrun.common.util.WebViewTool;
import com.cnsunrun.common.widget.titlebar.TitleBar;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchao on 2018-09-21.
 * 充值-webView唤醒支付宝支付
 */
public class WebActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.webView)
    WebView webView;

    public static void startThis(Context that, String url) {
        Intent intent = new Intent(that, WebActivity.class);
        intent.putExtra("url", url);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        String url = getIntent().getStringExtra("url");
        if (url.startsWith(BaseQuestConfig.LUCKDRAW_URL)) {
            titleBar.setTitle("抽奖");
        } else {
            titleBar.setTitle("充值");
        }
        WebViewTool.webviewDefaultConfig(webView);

        webView.setWebViewClient(new WebViewTool.WebViewClientBase() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                WebViewTool.imgReset(view);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("platformapi/startapp")) {
                    startAlipayActivity(url);
                    // android  6.0 两种方式获取intent都可以跳转支付宝成功,7.1测试不成功
                } else if ((Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
                        && (url.contains("platformapi") && url.contains("startapp"))) {
                    startAlipayActivity(url);
                } else {
                    webView.loadUrl(url);
                }
                return true;
            }
        });
        webView.loadUrl(url);
    }

    private void startAlipayActivity(String url) {
        Intent intent;
        try {
            intent = Intent.parseUri(url,
                    Intent.URI_INTENT_SCHEME);
            intent.addCategory(Intent.CATEGORY_BROWSABLE);
            intent.setComponent(null);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
