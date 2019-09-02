package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
import com.cnsunrun.common.widget.MyItemDecoration;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.adapter.MyBankAdapter;
import com.cnsunrun.mine.dialogs.InputPasswordDialog;
import com.cnsunrun.mine.mode.BankCradBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_BANK_CARD_LIST_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_CHECK_PASSWORD_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_UNBIND_BANK_CARD_CODE;

/**
 * Created by wangchao on 2018-10-08.
 * 我的银行卡
 */
public class MyBankCardActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.rlAddBankCard)
    RelativeLayout rlAddBankCard;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    private MyBankAdapter myBankAdapter;
    private String dealPassword;
    private int type;

    public static void startThis(Activity that, int type) {
        Intent intent = new Intent(that, MyBankCardActivity.class);
        intent.putExtra("type", type);
        that.startActivityForResult(intent, ConstantValue.GET_BANK_CARD_INFO);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_card);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        UIUtils.showLoadDialog(that, getString(R.string.loading));
        initData();
    }

    private void initData() {
        BaseQuestStart.getBankCardList(that);
    }

    private void initViews() {
        myBankAdapter = new MyBankAdapter(type);
        recyclerView.setAdapter(myBankAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that));
        recyclerView.addItemDecoration(new MyItemDecoration(that, R.color.grayF4, R.dimen.dp_10));
        myBankAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                BankCradBean item = myBankAdapter.getItem(position);
                switch (view.getId()) {
                    case R.id.llContainer:
                        if (type == 1) {
                            Intent intent = new Intent();
                            Bundle bundle=new Bundle();
                            bundle.putString("BankCradId",item.id);
                            bundle.putString("BankCardNum",item.account.substring(item.account.length() - 4));
                            bundle.putString("BankCard",item.bank_adress);
                            intent.putExtra("BankInfo",bundle);
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
        MessageTipDialog.newInstance().setContentTxt("您确定要解绑您的银行卡吗？")
//                .setLeftBtnTxt("我再想想")
//                .setRightBtnTxt("立即执行")
                .setOnSubmitAction(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UIUtils.showLoadDialog(that, getString(R.string.unbinding));
                        BaseQuestStart.postUnBindBankCard(that, id);
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
            case GET_BANK_CARD_LIST_CODE:
                if (bean.status == 1) {
                    List<BankCradBean> bankCradBeanList = bean.Data();
                    myBankAdapter.setNewData(bankCradBeanList);
                }
                break;
            case POST_UNBIND_BANK_CARD_CODE:
                if (bean.status == 1) {
                    BaseQuestStart.getBankCardList(that);
                }
                break;
            case POST_CHECK_PASSWORD_CODE:
                if (bean.status == 1) {
                    AddBankCardActivity.startThis(that, dealPassword);
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }
}
