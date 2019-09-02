package com.cnsunrun.mine.activity;

import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.view.View;
import android.widget.LinearLayout;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.commonui.widget.edittext.EditView;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.cnsunrun.login.LoginActivity;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.CHANGE_PASSWORD_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.USER_SAFE_CHANGE_DEAL_PASSWORD_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.USER_SAFE_SET_DEAL_PASSWORD_CODE;


/**
 * 修改密码
 */
public class ModifyPwdActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.oldPassword)
    EditView oldPassword;
    @BindView(R.id.newPassword)
    EditView newPassword;
    @BindView(R.id.secondNewPassword)
    EditView secondNewPassword;
    @BindView(R.id.btnConfirm)
    QMUIRoundButton btnConfirm;
    @BindView(R.id.activity_login_four)
    LinearLayout activityLoginFour;
    int type=0;//0,修改登录密码 1 修改支付密码 2 重置设置支付密码
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        type=getIntent().getIntExtra("type",type);
        setContentView(R.layout.activity_modifypwd);
        if(type==2){
            titleBar.setTitle("重置设置支付密码");
            oldPassword.setVisibility(View.GONE);
            newPassword.EditText().setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            newPassword.EditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            secondNewPassword.EditText().setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            secondNewPassword.EditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            newPassword.EditText().setHint("输入6位支付密码");
            secondNewPassword.EditText().setHint("确认支付密码");
            btnConfirm.setText("确认");
        }else if(type==1) {
            titleBar.setTitle("修改支付密码"); //InputType.TYPE_NUMBER_VARIATION_PASSWORD
            oldPassword.EditText().setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            oldPassword.EditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            newPassword.EditText().setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            newPassword.EditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            secondNewPassword.EditText().setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_VARIATION_PASSWORD);
            secondNewPassword.EditText().setFilters(new InputFilter[]{new InputFilter.LengthFilter(6)});
            oldPassword.EditText().setHint("输入旧支付密码");
            newPassword.EditText().setHint("输入新支付密码");
            secondNewPassword.EditText().setHint("确认新支付密码");

        }
    }

    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case USER_SAFE_SET_DEAL_PASSWORD_CODE:
            case USER_SAFE_CHANGE_DEAL_PASSWORD_CODE:
            case CHANGE_PASSWORD_CODE:
                if(bean.status==1){
                    if(type==0&& Config.getLoginInfo().is_default_password()){//是在修改默认密码的话,就在设置完之后跳转到首页
                        Config.getLoginInfo().setIs_default_password("0");
                        CommonApp.getInstance().closeActivitys(LoginActivity.class);
//                        CommonIntent.startNavigatorAndEditInfoActivity(that);
                    }
                    UIUtils.shortM("密码修改成功!");
                    finish();
                }else {
                    UIUtils.shortM(bean);
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }


    @OnClick(R.id.btnConfirm)
    public void onClick() {
        String oldPwdText = oldPassword.getText();
        String newPasswordText = newPassword.getText();
        String secondNewPasswordText = secondNewPassword.getText();
        if(newPasswordText.length()<6){
            UIUtils.shortM("密码长度不能小于6位");
            return;
        }
        if(type==0&&!newPasswordText.matches("^(?=.*[0-9])(?=.*[a-zA-Z])(.{6,18})$")){
            UIUtils.shortM("密码必须包含字母和数字");
            return;
        }
//        if(newPasswordText.matches("/\^(?![\d]+$)(?![a-zA-Z]+$)(?![^\da-zA-Z]+$).{{6},{18}}$/")){
//
//        }
        if(!newPasswordText.equals(secondNewPasswordText)){
            UIUtils.shortM("两次输入的密码不一致");
            return;
        }
        UIUtils.showLoadDialog(that,"请求中..");
        if(type==0) {
            BaseQuestStart.change_password(that, oldPwdText, newPasswordText);
        }else if(type==1){
//            BaseQuestStart.UserSafeChangeDealPassword(that,oldPwdText,newPasswordText);
        }else if(type==2){
            BaseQuestStart.UserSafeSetDealPassword(that,newPasswordText);
        }
    }
}
