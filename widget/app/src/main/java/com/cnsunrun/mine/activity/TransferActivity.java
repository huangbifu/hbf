package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.ChatSelectLinkManActivity;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.CashierInputFilter;
import com.cnsunrun.common.util.EditTextHelper;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.login.bean.CaptchaStateMode;
import com.cnsunrun.mine.dialogs.InputPasswordDialog;
import com.cnsunrun.mine.mode.UserDataBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.InputMethodUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.POST_COMMON_SMS_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_TRANSFER_CODE;

/**
 * Created by wangchao on 2018-09-21.
 * 转账
 */
public class TransferActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.ivUserIcon)
    RoundedImageView ivUserIcon;
    @BindView(R.id.tvNikeName)
    TextView tvNikeName;
    @BindView(R.id.tvIdNum)
    TextView tvIdNum;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.btnTransfer)
    QMUIRoundButton btnTransfer;
    @BindView(R.id.rlContainer)
    RelativeLayout rlContainer;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;
    private String uid;
    private LinkManInfo linkManInfo;
    private UserDataBean userBean;
    private String balance;
    private String money;
    private CaptchaStateMode captchaStateMode;
    private String code;

    public static void startThis(Activity that, LinkManInfo linkManInfo) {
        Intent intent = new Intent(that, TransferActivity.class);
        intent.putExtra("LinkManInfo", linkManInfo);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
        captchaStateMode = new CaptchaStateMode(this, getVerificationCode);
        linkManInfo = (LinkManInfo) getIntent().getSerializableExtra("LinkManInfo");
        userBean = getSession().getObject("userBean", UserDataBean.class);
        initViews();
    }

    private void initViews() {
        getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        EditTextHelper.setTextAndSelectEnd(etMoney, etMoney.getText().toString());
        InputFilter[] filters = {new CashierInputFilter()};
        etMoney.setFilters(filters);
        if (!EmptyDeal.isEmpy(userBean)) {
            balance = userBean.balance;
            tvBalance.setText(String.format(getString(R.string.balance_text), balance));
        }
        if (!EmptyDeal.isEmpy(linkManInfo)) {
            GlideMediaLoader.load(that, ivUserIcon, linkManInfo.avatar, R.drawable.mine_icon_touxiang_normal);
            tvNikeName.setText(linkManInfo.nickname);
            tvIdNum.setText(String.format("ID:%s", linkManInfo.id_num));
            uid = linkManInfo.id;
        }
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case POST_TRANSFER_CODE:
                if (bean.status == 1) {
                    CommonApp.getInstance().closeActivitys(TransferActivity.class, ChatSelectLinkManActivity.class);
                    UIUtils.shortM(bean.Data().toString());
                } else {
                    UIUtils.shortM(bean.msg);
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


    @OnClick({R.id.rlContainer, R.id.btnTransfer, R.id.get_verification_code})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlContainer:
                finish();
                break;
            case R.id.btnTransfer:
                transfer();
                break;
            case R.id.get_verification_code:
                BaseQuestStart.getCommonSms(that);
                break;
        }
    }

    private void transfer() {
        money = etMoney.getText().toString();
        if (EmptyDeal.isEmpy(money)) {
            UIUtils.shortM("请输入转账金额");
            return;
        }
        if (TestTool.isNotEnough(money, balance)) {
            UIUtils.shortM("余额不足");
            return;
        }
        code = etCode.getText().toString().trim();
        if (EmptyDeal.isEmpy(code)){
            UIUtils.shortM("请输入短信验证码");
            return;
        }
        InputPasswordDialog.newInstance().setOnSubmitAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.showLoadDialog(that, "正在转账中。。。");
                BaseQuestStart.postTransfer(that, uid, money, v,code);
            }
        }).show(getSupportFragmentManager(), "InputPasswordDialog");

    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodUtil.HideKeyboard(this);
    }

}
