package com.cnsunrun.mine.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.SwitchButton.SwitchButton;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.login.event.LoginConfig.IS_SECRET_FREE;


/**
 * 我的-设置-账号安全
 */
public class AccountSafeActivity extends LBaseActivity {


    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.layout_mobile)
    RelativeLayout layoutMobile;
    @BindView(R.id.layout_login_password)
    RelativeLayout layoutLoginPassword;
    @BindView(R.id.layout_pay_password)
    RelativeLayout layoutPayPassword;
    @BindView(R.id.sb_no_password_bet)
    SwitchButton sbNoPasswordBet;

    boolean isSetDealPwd=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_safe);
        ButterKnife.bind(this);
        that.requestAsynGet(BaseQuestStart.UserSafeIndex());
        sbNoPasswordBet.setOnCheckedChangeListener(new SwitchButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                if(isChecked){
                    CommonIntent.startAvoidCloseActivity(that);
                }else {
                    BaseQuestStart.setNoPaypwdBet(that,"0", "");
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(Config.getConfigInfo(that,IS_SECRET_FREE,null).equals("1")){
            sbNoPasswordBet.setChecked(true);
        }else {
            sbNoPasswordBet.setChecked(false);
        }
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case BaseQuestConfig.USER_SAFE_INDEX_CODE:
                if (bean.status ==1) {
                    JSONObject jsonObj = JsonDeal.createJsonObj(bean.toString());
                    isSetDealPwd = "1".equals(jsonObj.optString("is_set_deal_password"));
//                    sbNoPasswordBet.setChecked(isSetDealPwd);
                } else {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
            case BaseQuestConfig.QUEST_NO_PAY_PWD_BET_CODE:
                if (bean.status ==1) {
                    ToastUtils.shortToast(bean.msg);
                    Config.putConfigInfo(that,IS_SECRET_FREE,"0");
//                    if(Config.getConfigInfo(that,IS_SECRET_FREE,null).equals("1")){
//                        Config.putConfigInfo(that,IS_SECRET_FREE,"0");
//                        break;
//                    }
//                    if(Config.getConfigInfo(that,IS_SECRET_FREE,null).equals("0")){
//                        Config.putConfigInfo(that,IS_SECRET_FREE,"1");
//                    }

                } else {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }

    @OnClick({R.id.layout_mobile, R.id.layout_login_password, R.id.layout_pay_password})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_mobile:
                //更改手机
                CommonIntent.startChangePhoneActivity(that);
                break;
            case R.id.layout_login_password:
                //忘记密码
                CommonIntent.startForgetPasswordActivity(that,1);
                break;
            case R.id.layout_pay_password:
                if (isSetDealPwd) {
                    //重置支付密码
                    CommonIntent.startModifyPayPasswordActivity(that);
                } else {
                    //设置支付密码
                    CommonIntent.startSettingPayPasswordActivity(that);
                }
                break;
        }
    }

    @Override
    protected void onRestart() {
        that.requestAsynGet(BaseQuestStart.UserSafeIndex());
        super.onRestart();
    }
}
