package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputFilter;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.SpannedString;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.MessageTipDialog;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.CashierInputFilter;
import com.cnsunrun.common.util.ConstantValue;
import com.cnsunrun.common.util.EditTextHelper;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.common.widget.SwitchMultiButton;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.login.bean.CaptchaStateMode;
import com.cnsunrun.mine.dialogs.AliPayLogic;
import com.cnsunrun.mine.dialogs.InputPasswordDialog;
import com.cnsunrun.mine.mode.WithDrawalBean;
import com.cnsunrun.pay.alipay.AuthResult;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.InputMethodUtil;
import com.sunrun.sunrunframwork.utils.Utils;
import com.sunrun.sunrunframwork.utils.log.Logger;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_WITH_DRAWAL_INFO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_COMMON_SMS_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.USER_WALLET_LOGIN_ALIPAY_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.USER_WALLET_WITHDRAW_CODE;

/**
 * Created by wangchao on 2018-09-21.
 * 提现
 */
public class WithdrawalActivity extends LBaseActivity implements Handler.Callback {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvEnableBalance)
    TextView tvEnableBalance;
    @BindView(R.id.tvInfo)
    TextView tvInfo;
    @BindView(R.id.btnWithdrawal)
    QMUIRoundButton btnWithdrawal;
    @BindView(R.id.llAiliPay)
    LinearLayout llAiliPay;
    @BindView(R.id.tvCardName)
    TextView tvCardName;
    @BindView(R.id.llBankCard)
    LinearLayout llBankCard;
    @BindView(R.id.tvZhifubaoAccount)
    TextView tvZhifubaoAccount;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;
    @BindView(R.id.switchmultibutton)
    SwitchMultiButton switchmultibutton;
    private WithDrawalBean withDrawalBean;
    private String dealPassword;
    private AliPayLogic aliPayLogic;
    private String open_id;
    private int pay_type = 10;
    private String bankCardId;
    private int status;
    private String aliCountID;
    private CaptchaStateMode captchaStateMode;
    private int withDrawalType=1;

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, WithdrawalActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_withdrawal);
        ButterKnife.bind(this);
        captchaStateMode = new CaptchaStateMode(this, getVerificationCode);
        aliPayLogic = new AliPayLogic(this, new Handler(this));
        initViews();
        initData();
    }

    private void initViews() {
        switchmultibutton.setText("我的余额","我的收益");
        getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        tvEnableBalance.setVisibility(View.INVISIBLE);
        EditTextHelper.setTextAndSelectEnd(etMoney, etMoney.getText().toString());
        InputFilter[] filters = {new CashierInputFilter()};
        etMoney.setFilters(filters);

        switchmultibutton.setOnSwitchListener(new SwitchMultiButton.OnSwitchListener() {
            @Override
            public void onSwitch(int position, String tabText) {
                switch (position){
                    case 0://我的余额
                        withDrawalType=1;
                        tvBalance.setText(String.format(getString(R.string.balance_text), withDrawalBean.balance));
                        break;
                    case 1://我的收益
                        withDrawalType=2;
                        tvBalance.setText(String.format(getString(R.string.earnings_text), withDrawalBean.commission_balance));
                        break;
                }
            }
        });
    }

    private void initData() {
        UIUtils.showLoadDialog(that, getString(R.string.loading));
        BaseQuestStart.getWithDrawalInfo(that);
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case GET_WITH_DRAWAL_INFO_CODE:
                if (bean.status == 1) {
                    withDrawalBean = bean.Data();
                    updateUi(withDrawalBean);
                }
                break;
            case USER_WALLET_LOGIN_ALIPAY_CODE:
                if (bean.status == 1) {
                    aliPayAuth(bean.toString());
                } else if (bean.status == 40) {
                    MessageTipDialog.newInstance().setLeftBtnTxt("忘记密码").setRightBtnTxt("重试").setContentTxt(bean.msg + "请重试或忘记密码?").setOnSubmitAction(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
//                            pay_type  10 支付宝  20 银行卡
                            if (pay_type == 10) {
                                withDrawalToAili();
                            } else {
                                withDrawalBankCard();
                            }
                        }
                    }).setOnCancelAction(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CommonIntent.startModifyPayPasswordActivity(that);
                        }
                    }).show(getSupportFragmentManager(), "MessageTipDialog");
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
            case USER_WALLET_WITHDRAW_CODE:
                UIUtils.shortM(bean);
                if (bean.status == 1) {
//                    CommonIntent.startMineWithdrawSuccessActivity(that, editMoney.getText(), paymethod);
                    finish();
                }
                break;
            case POST_COMMON_SMS_CODE:
                if (bean.status == 1) {
                    captchaStateMode.beginCountDown();
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }

    private void updateUi(WithDrawalBean withDrawalBean) {
        //status 0 都可以 1 提现维护 2 支付宝维护 3 银行卡维护
        status = withDrawalBean.status;
        if (withDrawalBean.status == 1) {
            showTipDiglog(withDrawalBean.withdraw_explain);
            etMoney.setFocusable(false);
            etMoney.setFocusableInTouchMode(false);
            etMoney.setEnabled(false);
            btnWithdrawal.setEnabled(false);
            SpannableString ss = new SpannableString(withDrawalBean.withdraw_explain);//定义hint的值
            AbsoluteSizeSpan ass = new AbsoluteSizeSpan(12, true);//设置字体大小 true表示单位是sp
            ss.setSpan(ass, 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            etMoney.setHint(new SpannedString(ss));
        } else if (withDrawalBean.status == 2) {
            llAiliPay.setBackgroundResource(R.drawable.shape_white_recharge_bg);
            llBankCard.setBackgroundResource(R.drawable.shape_white_recharge_select_bg);
            pay_type = 20;
        } else if (withDrawalBean.status == 3) {
            pay_type = 10;
//            aliCountID = withDrawalBean.ali_account.id;
            llBankCard.setBackgroundResource(R.drawable.shape_white_recharge_bg);
            llAiliPay.setBackgroundResource(R.drawable.shape_white_recharge_select_bg);
        }
        tvBalance.setText(String.format(getString(R.string.balance_text), withDrawalBean.balance));
        tvEnableBalance.setText(String.format(getString(R.string.balance_enable_text), withDrawalBean.enbale_balance));
        tvInfo.setText(String.format(getString(R.string.withdrawal_info), withDrawalBean.info));
        if (!EmptyDeal.isEmpy(withDrawalBean.bank_card) && !EmptyDeal.isEmpy(withDrawalBean.bank_card.type)) {
            bankCardId = withDrawalBean.bank_card.id;
            tvCardName.setText(withDrawalBean.bank_card.bank_adress + "("
                    + withDrawalBean.bank_card.account.substring(withDrawalBean.bank_card.account.length() - 4) + ")");
        }
        if (!EmptyDeal.isEmpy(withDrawalBean.ali_account)) {
            aliCountID = withDrawalBean.ali_account.id;
            tvZhifubaoAccount.setText(withDrawalBean.ali_account.bank_adress + "(" + withDrawalBean.ali_account.account + ")");
        }
    }

    /**
     * 根据状态是否弹出提示框  status==1 弹出
     *
     * @param withdraw_explain
     */

    private void showTipDiglog(String withdraw_explain) {
        MessageTipDialog.newInstance().setContentTxt(withdraw_explain)
                .show(getSupportFragmentManager(), "MessageTipDialog");
    }

    @OnClick({R.id.btnWithdrawal, R.id.llAiliPay, R.id.tvZhifubaoAccount, R.id.llBankCard, R.id.tvCardName, R.id.get_verification_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnWithdrawal:
//                pay_type  10 支付宝  20 银行卡
                if (pay_type == 10) {
                    //status 0 都可以 1 提现维护 2 支付宝维护 3 银行卡维护
                    withDrawalToAili();
                } else {
                    withDrawalBankCard();
                }
                break;
            case R.id.llAiliPay:
                if (status == 2) {
                    UIUtils.longM("支付宝提现正在维护中...");
                } else {
                    pay_type = 10;
                    llBankCard.setBackgroundResource(R.drawable.shape_white_recharge_bg);
                    llAiliPay.setBackgroundResource(R.drawable.shape_white_recharge_select_bg);

                }
                break;
            case R.id.tvZhifubaoAccount:
                if (status == 2) {
                    UIUtils.longM("支付宝提现正在维护中...");
                } else {
                    pay_type = 10;
                    llBankCard.setBackgroundResource(R.drawable.shape_white_recharge_bg);
                    llAiliPay.setBackgroundResource(R.drawable.shape_white_recharge_select_bg);
                    BindZhifubaoActivity.startThis(that, 1);
                }
                break;
            case R.id.llBankCard:

                if (status == 3) {
                    UIUtils.longM("银行卡提现正在维护中...");
                } else {
                    pay_type = 20;
                    llAiliPay.setBackgroundResource(R.drawable.shape_white_recharge_bg);
                    llBankCard.setBackgroundResource(R.drawable.shape_white_recharge_select_bg);
                }
                break;
            case R.id.tvCardName:
                if (status == 3) {
                    UIUtils.longM("银行卡提现正在维护中...");
                } else {
                    MyBankCardActivity.startThis(that, 1);
                    pay_type = 20;
                    llAiliPay.setBackgroundResource(R.drawable.shape_white_recharge_bg);
                    llBankCard.setBackgroundResource(R.drawable.shape_white_recharge_select_bg);
                }
                break;
            case R.id.get_verification_code:
                BaseQuestStart.getCommonSms(that);
                break;

        }
    }


    /**
     * 提现到支付宝
     */
    private void withDrawalToAili() {
        if (withDrawalBean == null) return;

//        if (!AlipayUtils.checkAliPayInstalled(that)) {
//            UIUtils.shortM("您的手机尚未安装支付宝客户端");
//            return;
//        }
        if (Utils.valueOf(etMoney.getText().toString(), 0) < Utils.valueOf(withDrawalBean.min_money, 0)) {
            UIUtils.shortM("提现金额不能少于" + withDrawalBean.min_money);
            return;
        }
        if (TestTool.isNotEnough(etMoney.getText().toString(), withDrawalBean.max_money)) {
            UIUtils.shortM("提现金额不能大于" + withDrawalBean.max_money);
            return;
        }

        if (withDrawalType==1){
            if (TestTool.isNotEnough(etMoney.getText().toString(), withDrawalBean.balance)) {
                UIUtils.shortM("余额不足");
                return;
            }
        }else{
            if (TestTool.isNotEnough(etMoney.getText().toString(), withDrawalBean.commission_balance)) {
                UIUtils.shortM("余额不足");
                return;
            }
        }
        if (EmptyDeal.isEmpy(aliCountID)) {
            UIUtils.shortM("请先绑定支付宝账户");
            return;
        }
//        code = etCode.getText().toString().trim();
//        if (EmptyDeal.isEmpy(code)){
//            UIUtils.shortM("请输入短信验证码");
//            return;
//        }

        InputPasswordDialog.newInstance().setOnSubmitAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showLoadDialog(that);
                dealPassword = ((TextView) v).getText().toString();
//                BaseQuestStart.UserWalletLoginAlipay(that,etMoney.getText().toString(), dealPassword);
                BaseQuestStart.UserWalletWithdraw(that, aliCountID, etMoney.getText().toString(), dealPassword, pay_type,withDrawalType);
            }
        }).show(getSupportFragmentManager(), InputPasswordDialog.class.getName());

    }

    /**
     * 提现到银行卡
     */
    private void withDrawalBankCard() {
        if (Utils.valueOf(etMoney.getText().toString(), 0) < Utils.valueOf(withDrawalBean.min_money, 0)) {
            UIUtils.shortM("提现金额不能少于" + withDrawalBean.min_money);
            return;
        }
        if (TestTool.isNotEnough(etMoney.getText().toString(), withDrawalBean.max_money)) {
            UIUtils.shortM("提现金额不能大于" + withDrawalBean.max_money);
            return;
        }
        if (withDrawalType==1){
            if (TestTool.isNotEnough(etMoney.getText().toString(), withDrawalBean.balance)) {
                UIUtils.shortM("余额不足");
                return;
            }
        }else{
            if (TestTool.isNotEnough(etMoney.getText().toString(), withDrawalBean.commission_balance)) {
                UIUtils.shortM("余额不足");
                return;
            }
        }

        if (EmptyDeal.isEmpy(bankCardId)) {
            UIUtils.shortM("请先绑定银行卡");
            return;
        }
//        code = etCode.getText().toString().trim();
//        if (EmptyDeal.isEmpy(code)){
//            UIUtils.shortM("请输入短信验证码");
//            return;
//        }
        InputPasswordDialog.newInstance().setOnSubmitAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showLoadDialog(that);
                dealPassword = ((TextView) v).getText().toString();
                BaseQuestStart.UserWalletWithdraw(that, bankCardId, etMoney.getText().toString(), dealPassword, pay_type, withDrawalType);
            }
        }).show(getSupportFragmentManager(), InputPasswordDialog.class.getName());

    }


    private void aliPayAuth(String authInfo) {
        UIUtils.showLoadDialog(that, "启动支付宝客户端...");
        aliPayLogic.startAliAuth(authInfo);
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            case 2:
                UIUtils.cancelLoadDialog();
                AuthResult authResult = (AuthResult) msg.obj;
                // 为“200”则代表授权成功，具体状态码代表含义可参考授权接口文档
                if (TextUtils.equals(authResult.getResultCode(), "200")) {
                    // 获取alipay_open_id，调支付时作为参数extern_token 的value
                    // 传入，则支付账户为该授权账户
                    open_id = authResult.getUser_id();
                    UIUtils.showLoadDialog(that);
                    BaseQuestStart.UserWalletWithdraw(that, open_id, etMoney.getText().toString(), dealPassword, pay_type, withDrawalType);
                    Logger.D("open_id =" + open_id);
                } else {
                    // 其他状态值则为授权失败
                    Toast.makeText(WithdrawalActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

                }
                break;
        }
        return false;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (etMoney.isFocusable()) {
            InputMethodUtil.HideKeyboard(this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == ConstantValue.GET_BANK_CARD_INFO && resultCode == RESULT_OK) {
//            if (!EmptyDeal.isEmpy(data)) {
//                bankCardId = data.getStringExtra("BankCradId");
//                String bankCardNum = data.getStringExtra("BankCardNum");
//                String bankCard = data.getStringExtra("BankCard");
//                tvCardName.setText(bankCard + "(" + bankCardNum + ")");
//
//
//                aliCountID = data.getStringExtra("aliCountId");
//                String aliCount = data.getStringExtra("aliCount");
//                String aliCardAddress = data.getStringExtra("aliCardAddress");
//                tvCardName.setText(aliCardAddress + "(" + aliCount + ")");
//
//            }
//
//        }
        switch (requestCode) {
            case ConstantValue.GET_BANK_CARD_INFO:
                if (!EmptyDeal.isEmpy(data)) {
                    Bundle bankInfo = data.getBundleExtra("BankInfo");
                    bankCardId = bankInfo.getString("BankCradId");
                    String bankCardNum = bankInfo.getString("BankCardNum");
                    String bankCard = bankInfo.getString("BankCard");
//                    bankCardId = data.getStringExtra("BankCradId");
//                    String bankCardNum = data.getStringExtra("BankCardNum");
//                    String bankCard = data.getStringExtra("BankCard");
                    tvCardName.setText(bankCard + "(" + bankCardNum + ")");
                }
                break;
            case ConstantValue.GET_ALI_ACCOUNT_INFO:
                if (!EmptyDeal.isEmpy(data)) {
                    aliCountID = data.getStringExtra("aliCountId");
                    String aliCount = data.getStringExtra("aliCount");
                    String aliCardAddress = data.getStringExtra("aliCardAddress");
                    tvZhifubaoAccount.setText(aliCardAddress + "(" + aliCount + ")");
                }
                break;
        }
    }

}
