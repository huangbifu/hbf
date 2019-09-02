package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.MessageTipDialog;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.ConstantValue;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.MyItemDecoration;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.adapter.AccountAdapter;
import com.cnsunrun.mine.dialogs.InputPasswordDialog;
import com.cnsunrun.mine.mode.BankCradBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_ALI_ACCOUNT_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_CHECK_PASSWORD_CODE;

/**
 * Created by wangchao on 2018-12-19.
 * 我的支付宝账户
 */
public class BindZhifubaoActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rlAddBankCard)
    RelativeLayout rlAddBankCard;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    private String dealPassword;
    private int type;
    PageLimitDelegate pageLimitDelegate = new PageLimitDelegate(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            BaseQuestStart.getZhifuBaoList(that, page);
        }
    });
    private AccountAdapter myBankAdapter;

    public static void startThis(Activity that, int type) {
        Intent intent = new Intent(that, BindZhifubaoActivity.class);
        intent.putExtra("type", type);
        that.startActivityForResult(intent, ConstantValue.GET_ALI_ACCOUNT_INFO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_zhifubao);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        pageLimitDelegate.refreshPage();
    }

    private void initViews() {
        myBankAdapter = new AccountAdapter(type);
        recyclerView.setAdapter(myBankAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that));
        recyclerView.addItemDecoration(new MyItemDecoration(that, R.color.grayF4, R.dimen.dp_10));
        pageLimitDelegate.attach(refreshLayout,recyclerView,myBankAdapter);
        myBankAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BankCradBean item = myBankAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.llContainer:
                        if (type == 1) {
                            Intent intent = new Intent();
                            intent.putExtra("aliCountId", item.id);
                            intent.putExtra("aliCount", item.account);
                            intent.putExtra("aliCardAddress", item.bank_adress);
                            setResult(RESULT_OK, intent);
                            finish();
                        }
                        break;
                    case R.id.btnUnBind:
                        showUnBindDiglog(item.id);
                        break;
                }
            }
        });
    }
    /**
     * 显示解绑提示框
     *
     * @param id
     */
    private void showUnBindDiglog(final String id) {
        MessageTipDialog.newInstance().setContentTxt("您确定要解绑您的支付宝账户吗？")
                .setOnSubmitAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                       UnBindZhifubaoActivity.startThis(that,id);
                    }
                })
                .show(getSupportFragmentManager(), "MessageTipDialog");
    }

    @OnClick(R.id.rlAddBankCard)
    public void onViewClicked() {
        InputPasswordDialog.newInstance().setOnSubmitAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showLoadDialog(that);
                dealPassword = ((TextView) v).getText().toString();
                BaseQuestStart.postCheckPassword(that, dealPassword);
            }
        }).show(getSupportFragmentManager(), InputPasswordDialog.class.getName());
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case GET_ALI_ACCOUNT_CODE:
                if (bean.status == 1) {
                    List<BankCradBean> bankCradBeanList = bean.Data();
                    pageLimitDelegate.setData(bankCradBeanList);
                }
                break;
//            case POST_UNBIND_BANK_CARD_CODE:
//                if (bean.status == 1) {
//                    BaseQuestStart.getBankCardList(that);
//                }
//                break;
            case POST_CHECK_PASSWORD_CODE:
                if (bean.status == 1) {
                    AddZhifubaoAccountActivity.startThis(that, dealPassword);
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }
}
