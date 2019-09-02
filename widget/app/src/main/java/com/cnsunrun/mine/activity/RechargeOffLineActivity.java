package com.cnsunrun.mine.activity;

import android.content.ClipboardManager;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.CashierInputFilter;
import com.cnsunrun.common.util.EditTextHelper;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.mine.mode.OfflineInfoBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_OFFLINE_INFO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_OFFLINE_INFO_CODE;

/**
 * Created by wangchao on 2018-11-30.
 * 线下收款
 */
public class RechargeOffLineActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tvPayee)
    TextView tvPayee;
    @BindView(R.id.tvOpeningBank)
    TextView tvOpeningBank;
    @BindView(R.id.tvBankNum)
    TextView tvBankNum;
    @BindView(R.id.etUserName)
    EditText etUserName;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.etRemark)
    EditText etRemark;
    @BindView(R.id.btnSubmit)
    QMUIRoundButton btnSubmit;
    @BindView(R.id.rlCopyName)
    RelativeLayout rlCopyName;
    @BindView(R.id.rlCopyBankNum)
    RelativeLayout rlCopyBankNum;
    private ClipboardManager myClipboard;
    private String user_name;
    private String total_money;
    private String remark;
    private int bank_id;
    private int pay_type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_offline);
        ButterKnife.bind(this);
        initViews();
        initData();
    }

    private void initViews() {
        pay_type = getIntent().getIntExtra("type", 0);
        EditTextHelper.setTextAndSelectEnd(etMoney, etMoney.getText().toString());
        InputFilter[] filters = {new CashierInputFilter()};
        etMoney.setFilters(filters);
        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
    }

    private void initData() {
        UIUtils.showLoadDialog(that, getString(R.string.loading));
        BaseQuestStart.getOffLineInfo(that);
    }

    @OnClick({R.id.rlCopyName, R.id.rlCopyBankNum, R.id.btnSubmit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlCopyName:
                String name = tvPayee.getText().toString();
                if (!EmptyDeal.isEmpy(name)) {
                    myClipboard.setText(name);
                    UIUtils.shortM("复制成功");
                }
                break;
            case R.id.rlCopyBankNum:
                String bankNum = tvBankNum.getText().toString();
                if (!EmptyDeal.isEmpy(bankNum)) {
                    myClipboard.setText(bankNum);
                    UIUtils.shortM("复制成功");
                }
                break;
            case R.id.btnSubmit:
                if (checkInput()) {
                    UIUtils.showLoadDialog(that, "提交审核中...");
                    BaseQuestStart.postSubmitRechargeInfo(that, user_name, total_money, remark, bank_id, pay_type);
                }
                break;
        }
    }

    private boolean checkInput() {
        user_name = etUserName.getText().toString();
        total_money = etMoney.getText().toString().trim();
        remark = etRemark.getText().toString().trim();
        if (EmptyDeal.isEmpy(user_name)) {
            UIUtils.shortM("请输入付款人姓名");
            return false;
        } else if (EmptyDeal.isEmpy(total_money)) {
            UIUtils.shortM("请输入付款金额");
            return false;
        }
        return true;
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case GET_OFFLINE_INFO_CODE:
                if (bean.status == 1) {
                    OfflineInfoBean offlineInfoBean = bean.Data();
                    uodateUI(offlineInfoBean);
                }
                break;
            case POST_OFFLINE_INFO_CODE:
                if (bean.status == 1) {
                    CommonApp.getInstance().closeActivitys(RechargeActivity.class, RechargeOffLineActivity.class);
                    UIUtils.shortM(bean.Data().toString());
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }

    private void uodateUI(OfflineInfoBean offlineInfoBean) {
        bank_id = offlineInfoBean.id;
        tvPayee.setText(offlineInfoBean.payee);
        tvOpeningBank.setText(offlineInfoBean.opening_bank);
        tvBankNum.setText(offlineInfoBean.bank_number);
    }

}
