package com.cnsunrun.mine.activity;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.login.bean.CaptchaStateMode;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uibase.BaseActivity;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.InputMethodUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.POST_ADD_BANK_CARD_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_COMMON_SMS_CODE;

/**
 * Created by wangchao on 2018-12-19.
 * 新增支付宝账户
 */
public class AddZhifubaoAccountActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etAccount)
    EditText etAccount;
    @BindView(R.id.btnBind)
    QMUIRoundButton btnBind;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;
    private String account;
    private String username;
    private String password;
    private String account_type = "2";
    private String code;
    private CaptchaStateMode captchaStateMode;

    public static void startThis(BaseActivity that, String dealPassword) {
        Intent intent = new Intent(that, AddZhifubaoAccountActivity.class);
        intent.putExtra("password", dealPassword);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_zhifubao_account);
        captchaStateMode = new CaptchaStateMode(this, getVerificationCode);
        ButterKnife.bind(this);
        password = getIntent().getStringExtra("password");
        getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }


    private boolean checkInput() {
        username = etName.getText().toString().trim();
        account = etAccount.getText().toString().trim();
        code = etCode.getText().toString().trim();
        if (EmptyDeal.isEmpy(username)) {
            UIUtils.shortM("请输入姓名");
            return false;
        } else if (EmptyDeal.isEmpy(account)) {
            UIUtils.shortM("请输入支付宝账号");
            return false;
        }else if (EmptyDeal.isEmpy(code)) {
            UIUtils.shortM("请输入请输入短信验证码");
            return false;
        }
        return true;
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case POST_ADD_BANK_CARD_CODE:
                if (bean.status == 1) {
                    InputMethodUtil.HideKeyboard(this);
                    finish();
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
            case POST_COMMON_SMS_CODE:
                if (bean.status==1){
                    captchaStateMode.beginCountDown();
                }else{
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }

    @OnClick({R.id.get_verification_code, R.id.btnBind})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                BaseQuestStart.getCommonSms(that);
                break;
            case R.id.btnBind:
                if (checkInput()) {
                    UIUtils.shortM(getString(R.string.loading));
                    BaseQuestStart.postAddBankCard(that, password, username, account, "", account_type,code,null,null, null);
                }
                break;
        }
    }
}
