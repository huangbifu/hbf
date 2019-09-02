package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alipay.sdk.app.H5PayCallback;
import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.util.H5PayResultModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.CashierInputFilter;
import com.cnsunrun.common.util.EditTextHelper;
import com.cnsunrun.common.util.WebViewTool;
import com.cnsunrun.common.widget.MyItemDecoration;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.mine.adapter.OffLineAdapter;
import com.cnsunrun.mine.mode.PayUrlBean;
import com.cnsunrun.mine.mode.RechargeInfo;
import com.sunrun.sunrunframwork.adapter.SelectableAdapter;
import com.sunrun.sunrunframwork.adapter.ViewHodler;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.InputMethodUtil;
import com.sunrun.sunrunframwork.weight.GridViewForScroll;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_RECHAR_INFO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.USER_RECHARGE_CODE;

/**
 * Created by wangchao on 2018-09-21.
 * 充值
 */
public class RechargeActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.gridMoneyType)
    GridViewForScroll gridMoneyType;
    @BindView(R.id.tvRealPayMoney)
    TextView tvRealPayMoney;
    @BindView(R.id.btnPay)
    QMUIRoundButton btnPay;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvOffLine)
    TextView tvOffLine;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.tvUpLine)
    TextView tvUpLine;
    @BindView(R.id.gridViewAili)
    GridViewForScroll gridViewAili;
    @BindView(R.id.llRechargeEdit)
    LinearLayout llRechargeEdit;
    @BindView(R.id.tvVideo)
    TextView tvVideo;
    private SelectableAdapter<RechargeInfo.MoneyListBean> selectableAdapter;
    private WebView webView;
    private PayUrlBean payUrlBean;
    private OffLineAdapter offLineAdapter;
    private SelectableAdapter<RechargeInfo.PayChannelBean> selectAiliAdapter;
    private List<RechargeInfo.PayChannelBean> pay_channel;
    private RechargeInfo rechargeInfo;

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, RechargeActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);
        ButterKnife.bind(this);
        initViews();
        initData();
        initListener();
    }

    private void initData() {
        UIUtils.showLoadDialog(that, getString(R.string.loading));
        BaseQuestStart.getRecharInfo(that);
    }

    private void initListener() {
        etMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!EmptyDeal.isEmpy(etMoney) && !TextUtils.equals(etMoney.getText().toString(), "")) {
                    if (selectableAdapter.getSelectItem() == null || selectableAdapter.getSelectItem().money.equals(s.toString())) {

                    } else {
                        selectableAdapter.select(-1);
                    }

                    tvRealPayMoney.setText(s.toString());
                } else {
                    selectableAdapter.select(0);
                    tvRealPayMoney.setText(selectableAdapter.getSelectItem().money);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        offLineAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                RechargeInfo.PayListBean item = offLineAdapter.getItem(position);
                CommonIntent.startRechargeOffLine(that, item.type);
            }
        });
    }


    private void initViews() {

        EditTextHelper.setTextAndSelectEnd(etMoney, etMoney.getText().toString());
        InputFilter[] filters = {new CashierInputFilter()};
        etMoney.setFilters(filters);
        offLineAdapter = new OffLineAdapter();
        recyclerView.setAdapter(offLineAdapter);
        recyclerView.addItemDecoration(new MyItemDecoration(that, R.color.color_f8f8f8, R.dimen.dp_10));
        recyclerView.setLayoutManager(new LinearLayoutManager(that));


    }

    /**
     * 初始化WebView
     */
    private void initWebView(String url) {
        webView = new WebView(this);
        WebViewTool.webviewDefaultConfig(webView);
        webView.setWebViewClient(new WebViewTool.WebViewClientBase() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);

            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                WebViewTool.imgReset(view);
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (!(url.startsWith("http") || url.startsWith("https"))) {
                    return true;
                }
                if (payUrlBean.pay_type == 1) {
                    H5Pay(view, url);
                }

                return true;
            }
        });
        webView.loadUrl(url);
    }


    /**
     * pay_type==1
     * H5支付
     *
     * @param view
     * @param url
     */
    private void H5Pay(WebView view, String url) {
        /**
         * 推荐采用的新的二合一接口(payInterceptorWithUrl),只需调用一次
         */
        final PayTask task = new PayTask(RechargeActivity.this);
        /**
         * 支付宝H5支付URL拦截器，完成拦截及支付方式转化
         *
         * @param h5PayUrl          待过滤拦截的 URL
         * @param isShowPayLoading  是否出现loading
         * @param callback          异步回调接口
         *
         * @return true：表示URL为支付宝支付URL，URL已经被拦截并支付转化；false：表示URL非支付宝支付URL；
         *
         */
        boolean isIntercepted = task.payInterceptorWithUrl(url, true, new H5PayCallback() {
            @Override
            public void onPayResult(final H5PayResultModel result) {
                // 支付结果返回
                final String url = result.getReturnUrl();
                if (!TextUtils.isEmpty(url)) {
                    RechargeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            webView.loadUrl(url);
                        }
                    });
                }
            }
        });
        /**
         * 判断是否成功拦截
         * 若成功拦截，则无需继续加载该URL；否则继续加载
         */
        if (!isIntercepted) {
            view.loadUrl(url);
        }

    }
    @OnClick({R.id.tvVideo, R.id.btnPay})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvVideo:
                CommonIntent.startActivity(that,VideoPlayActivity.class);
                break;
            case R.id.btnPay:
                String money = tvRealPayMoney.getText().toString();
                if (EmptyDeal.isEmpy(selectAiliAdapter.getSelectItem())) {
                    UIUtils.shortM("请选择充值渠道");
                    return;
                }
                if (Double.parseDouble(money) > 0.00) {
                    UIUtils.showLoadDialog(that, "正在充值中,请稍候...");
                    BaseQuestStart.recharge(that, money, selectAiliAdapter.getSelectItem().id);
                } else {
                    UIUtils.shortM("实付金额输入不正确，请重新输入");
                }
                break;
        }
    }



    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case GET_RECHAR_INFO_CODE:
                if (bean.status == 1) {
                    rechargeInfo = bean.Data();
                    updateUi(rechargeInfo);
                }
                break;
            case USER_RECHARGE_CODE:
                if (bean.status == 1) {
                    payUrlBean = bean.Data();
                    if (payUrlBean.pay_type == 1) {
                        initWebView(payUrlBean.url);
                    } else {
                        toWeixinPay(payUrlBean.url);
                    }
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }

    private void toWeixinPay(String url) {
        webView = new WebView(this);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(url);

    }


    private void updateUi(RechargeInfo rechargeInfo) {
//        List<RechargeInfo.MoneyListBean> money_list = rechargeInfo.money_list;
        if (!EmptyDeal.isEmpy(rechargeInfo)) {
            btnPay.setVisibility(View.VISIBLE);
        } else {
            btnPay.setVisibility(View.GONE);
        }
        List<RechargeInfo.PayListBean> pay_list = rechargeInfo.pay_list;
        pay_channel = rechargeInfo.pay_channel;
        if (EmptyDeal.isEmpy(pay_list)) {
            tvOffLine.setVisibility(View.GONE);
            tvUpLine.setVisibility(View.GONE);
        } else {
            offLineAdapter.setNewData(pay_list);
            tvOffLine.setVisibility(View.VISIBLE);
            tvUpLine.setVisibility(View.VISIBLE);
        }
        selectAiliAdapter = new SelectableAdapter<RechargeInfo.PayChannelBean>(that, pay_channel, R.layout.item_aili_type) {

            @Override
            public void fillView(ViewHodler viewHodler, RechargeInfo.PayChannelBean payChannelBean, int position) {
                CheckBox checkBox = viewHodler.getView(R.id.checkbox);
                checkBox.setChecked(isSelected(position));
                checkBox.setText(payChannelBean.title);
            }
        };
        selectAiliAdapter.select(0);
        gridViewAili.setAdapter(selectAiliAdapter);

        gridViewAili.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectAiliAdapter.select(position);
                setRechargePrice(pay_channel, position);
            }
        });
        if (EmptyDeal.isEmpy(pay_channel)) return;
        setRechargePrice(pay_channel, 0);

    }

    private void setRechargePrice(List<RechargeInfo.PayChannelBean> pay_channel, int position) {
        List<RechargeInfo.MoneyListBean> money_list = pay_channel.get(position).money_list;
        if (TextUtils.equals(pay_channel.get(position).is_show, "1")) {
            llRechargeEdit.setVisibility(View.VISIBLE);
        } else {
            llRechargeEdit.setVisibility(View.GONE);
        }
        selectableAdapter = new SelectableAdapter<RechargeInfo.MoneyListBean>(that, money_list, R.layout.item_trunk_space_type) {
            @Override
            public void fillView(ViewHodler viewHodler, RechargeInfo.MoneyListBean item, int position) {
                View layBg = viewHodler.getView(R.id.layBg);
                CheckBox txtPrice = viewHodler.getView(R.id.txtPrice);
                txtPrice.setChecked(isSelected(position));
                layBg.setBackgroundResource(isSelected(position) ? R.drawable.type_check_bg2 : R.drawable.type_check_bg);
                txtPrice.setText(String.format("%s元", item.money));
            }
        };
        selectableAdapter.select(0);
        tvRealPayMoney.setText(money_list.get(0).money);
        gridMoneyType.setAdapter(selectableAdapter);
        gridMoneyType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectableAdapter.select(position);
                tvRealPayMoney.setText(selectableAdapter.getItem(position).money);
                EditTextHelper.setTextAndSelectEnd(etMoney, selectableAdapter.getItem(position).money);
            }
        });
        if (!selectableAdapter.isEmpty()) {
            gridMoneyType.performItemClick(null, 0, 0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodUtil.HideKeyboard(this);
    }



}
