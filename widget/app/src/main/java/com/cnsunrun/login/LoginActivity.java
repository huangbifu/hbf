package com.cnsunrun.login;

import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.model.LoginInfo;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.Utils;
import com.tencent.bugly.crashreport.CrashReport;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.QUEST_LOGIN_CODE;
import static com.cnsunrun.login.event.LoginConfig.ACCOUNT;
import static com.sunrun.sunrunframwork.utils.EmptyDeal.empty;

/**
 * 登录
 */
public class LoginActivity extends LBaseActivity {

    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.cb_password)
    CheckBox cbPassword;
    @BindView(R.id.btn_login)
    TextView btnLogin;
    @BindView(R.id.tv_forgot_password)
    TextView tvForgotPassword;
    @BindView(R.id.layout_account)
    LinearLayout layoutAccount;
    @BindView(R.id.layout_password)
    RelativeLayout layoutPassword;
    @BindView(R.id.tv_sms_login)
    TextView tvSmsLogin;
    @BindView(R.id.tv_account)
    TextView tvAccount;

    private String account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        //加载保存的用户名
        editAccount.setText(account = Config.getConfigInfo(this, ACCOUNT, null));
        cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        editAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tvAccount.setTextColor(getResources().getColor(R.color.com_color));
                    layoutAccount.setBackgroundResource(R.drawable.shape_login_bg);
                } else {
                    tvAccount.setTextColor(getResources().getColor(R.color.text_color_333));
                    layoutAccount.setBackgroundResource(R.drawable.shape_login_bg2);
                }
            }
        });
        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tvPassword.setTextColor(getResources().getColor(R.color.com_color));
                    layoutPassword.setBackgroundResource(R.drawable.shape_login_bg);
                } else {
                    tvPassword.setTextColor(getResources().getColor(R.color.text_color_333));
                    layoutPassword.setBackgroundResource(R.drawable.shape_login_bg2);
                }
            }
        });
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonIntent.startRegistActivity(that);
            }
        });
    }


    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case QUEST_LOGIN_CODE:
                if (bean.status == 1) {
                    checkLoginStatus(bean);
                } else {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (!Utils.isQuck(2000)) {
            UIUtils.shortM("再按一次退出程序");
        } else {
            finish();
        }
    }

    /**
     * 检查登录状态
     *
     * @param bean
     */
    private void checkLoginStatus(BaseBean bean) {
        LoginInfo info = bean.Data();
        if (info != null) {
            info.setPwd(editPassword.getText().toString());
            Config.putLoginInfo(info);
            CommonIntent.startChatNavigatorActivity(that);
            finish();
        }
    }


    /**
     * 内容输入验证
     *
     * @return
     */
    private boolean check() {
        if (empty(editAccount)) {
            ToastUtils.shortToast("手机号不能为空");
            return false;
        }
        if (empty(editPassword)) {
            ToastUtils.shortToast("密码不能为空");
            return false;
        }
        account = editAccount.getText().toString();
        return true;
    }

    @OnClick({R.id.btn_login, R.id.tv_forgot_password, R.id.tv_sms_login,R.id.btn_register
    })
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //登录
                if (check()) {
                    UIUtils.showLoadDialog(this, "登录中..");
                    BaseQuestStart.login(this, editAccount.getText().toString(), editPassword.getText().toString());
                    // 保存账户信息
                    Config.putConfigInfo(this, ACCOUNT, account);
                }
                break;
            case R.id.tv_sms_login:
                //短信登录
                CommonIntent.startSmsLoginActivity(that);
                break;
            case R.id.tv_forgot_password:
                //忘记密码
                CommonIntent.startForgetPasswordActivity(that,0);
                break;
            case R.id.btn_register:
                //注册
                CommonIntent.startRegistActivity(that);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}