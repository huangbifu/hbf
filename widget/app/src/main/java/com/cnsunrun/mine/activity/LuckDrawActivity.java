package com.cnsunrun.mine.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.SslErrorHandler;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.MessageTipDialog;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.mode.LotteryInfo;
import com.google.gson.Gson;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by wangchao on 2018-09-28.
 * 抽奖
 */
public class LuckDrawActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.webView)
    WebView mWebView;
    private WebViewClientBase webViewClient = new WebViewClientBase();
    private Gson gson;
    private String url;

    public static void startThis(Context that, String url) {
        Intent intent = new Intent(that, LuckDrawActivity.class);
        intent.putExtra("url", url);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        ButterKnife.bind(this);
        titleBar.setTitle("抽奖");
        url = getIntent().getStringExtra("url");
        UIUtils.showLoadDialog(that);
        setUpWebView();
        if (gson == null) {
            gson = new Gson();
        }
    }
    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView() {
        mWebView.setBackgroundColor(0);
        final WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDomStorageEnabled(true);
        webSettings.setGeolocationEnabled(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        mWebView.getSettings().setBlockNetworkImage(true);
        mWebView.setWebViewClient(webViewClient);
        mWebView.requestFocus(View.FOCUS_DOWN);
        mWebView.getSettings().setDefaultTextEncodingName("UTF-8");
        try {
            WebView.setWebContentsDebuggingEnabled(true);
        }catch (Exception e){}

        mWebView.setWebChromeClient(new WebChromeClient() {

            @Override
            public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {

//                AlertDialog.Builder dialog = new AlertDialog
//                        .Builder(that)
//                        .setTitle(R.string.app_name)
//                        .setMessage(message)
//                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                result.confirm();
//                            }
//                        });
//
//                dialog.setCancelable(false);
//                dialog.create();
//                dialog.show();
                MessageTipDialog.newInstance().setContentTxt(message)
                        .show(getSupportFragmentManager(), "MessageTipDialog");
                return true;
            }
        });
        mWebView.loadUrl(url);

        //把java里面的对象传递给js
        mWebView.addJavascriptInterface(new JsCallJava() {
            @JavascriptInterface
            @Override
            public void dialog(String data) {
                Log.d("LuckDrawActivity", data);
                final LotteryInfo lotteryInfo = gson.fromJson(data, LotteryInfo.class);
                refreshWebView();
                MessageTipDialog.newInstance().setContentTxt(lotteryInfo.title)
                        .show(getSupportFragmentManager(), "MessageTipDialog");
            }
        }, "app");
    }


    public interface JsCallJava{
        void dialog(String data);
    }


    private class WebViewClientBase extends WebViewClient {

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
//            super.onReceivedSslError(view, handler, error);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            UIUtils.cancelLoadDialog();
            view.getSettings().setBlockNetworkImage(false);
        }


        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            String errorHtml = "<html><body><h2>网页加载失败</h2></body></html>";
            view.loadDataWithBaseURL(null, errorHtml, "text/html", "UTF-8", null);
            UIUtils.cancelLoadDialog();
        }
    }
    /**
     * 刷新webView
     */
    public void refreshWebView() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mWebView.reload();
            }
        });
    }


}
