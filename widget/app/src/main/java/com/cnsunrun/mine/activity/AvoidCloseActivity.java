package com.cnsunrun.mine.activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.login.bean.CaptchaStateMode;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.POST_COMMON_SMS_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.QUEST_NO_PAY_PWD_BET_CODE;
import static com.cnsunrun.login.event.LoginConfig.IS_SECRET_FREE;

/**
 * 2019-01-03.
 * 免密支付
 */
public class AvoidCloseActivity extends LBaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.tv_account)
    TextView tvAccount;
    @BindView(R.id.layout_account)
    LinearLayout layoutAccount;
    @BindView(R.id.edit_verification_code)
    EditText editVerificationCode;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;
    @BindView(R.id.layout_verification_code)
    RelativeLayout layoutVerificationCode;
    @BindView(R.id.tv_modify)
    TextView tvModify;
    private CaptchaStateMode captchaStateMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_avoid_close);
        captchaStateMode = new CaptchaStateMode(this, getVerificationCode);
        ButterKnife.bind(this);
        tvAccount.setText(Config.getLoginInfo().mobile);
        getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    @OnClick({R.id.get_verification_code, R.id.tv_modify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                BaseQuestStart.getCommonSms(that);
                break;
            case R.id.tv_modify:
                String code = editVerificationCode.getText().toString().trim();
                if (!EmptyDeal.isEmpy(code)){
                    BaseQuestStart.setNoPaypwdBet(that,"1",code);
                }else{
                    UIUtils.shortM("请输入短信验证码");
                }
                break;
        }
    }
    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case POST_COMMON_SMS_CODE:
                if (bean.status==1){
                    captchaStateMode.beginCountDown();
                }else{
                    UIUtils.shortM(bean.msg);
                }
                break;
            case QUEST_NO_PAY_PWD_BET_CODE:
                if (bean.status==1){
                    UIUtils.shortM(bean.msg);
                    //保存是否免密支付
                    Config.putConfigInfo(that, IS_SECRET_FREE, "1");
                    finish();
                }else {
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }
}
