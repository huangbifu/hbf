package com.cnsunrun.mine.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.login.bean.VerificationCodeInfo;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 变更手机
 */
public class ChangePhoneActivity extends LBaseActivity {


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
    @BindView(R.id.edit_new_account)
    EditText editNewAccount;
    @BindView(R.id.tv_modify)
    TextView tvModify;

    private int time = 60;
    private AHandler.Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        ButterKnife.bind(this);
        titleBar.setLeftAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AHandler.cancel(task);
                finish();
            }
        });
        tvAccount.setText(Config.getLoginInfo().mobile);
        getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }


    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case BaseQuestConfig.QUEST_GET_CHANGE_PHONE_VERIFICATION_CODE:
                if (bean.status == 1) {
                    //获取短信验证码
//                    ToastUtils.shortToast(bean.Data());
                    ToastUtils.shortToast("发送成功");
                    runCountDown();
                } else {
                    ToastUtils.shortToast(bean.msg);
                    getVerificationCode.setText("获取验证码");
                    getVerificationCode.setEnabled(true);
                    getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                    getVerificationCode.getPaint().setAntiAlias(true);//抗锯齿
                    AHandler.cancel(task);
                }
                break;
            case BaseQuestConfig.QUEST_MODIFY_PHONE_CODE:
                if (bean.status ==1) {
                    //修改
                    ToastUtils.shortToast(bean.msg);
                    AHandler.cancel(task);
                    CommonIntent.startLoginActivity(that);
                } else {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }



    @OnClick({R.id.get_verification_code, R.id.tv_modify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                if (task == null) {
                    getVerificationCode.setText("发送中..");
                    getVerificationCode.setEnabled(false);
                    UIUtils.showLoadDialog(that, "获取验证码..");
                    BaseQuestStart.getChangePhoneVerificationCode(that);
                }
                break;
            case R.id.tv_modify:
                //修改
                if (TextUtils.isEmpty(editVerificationCode.getText().toString())) {
                    ToastUtils.shortToast("验证码不能为空");
                    return;
                }
                UIUtils.showLoadDialog(that);
                BaseQuestStart.modifyPhone(that,editNewAccount.getText().toString(), editVerificationCode.getText().toString());
                break;
        }
    }

    /**
     * 开启倒计时
     */
    private void runCountDown() {
        AHandler.runTask(task = new AHandler.Task() {
            @SuppressLint("DefaultLocale")
            @Override
            public void update() {
                if (time <= 0) {
                    task.cancel();
                } else {
                    getVerificationCode.setEnabled(false);
                    getVerificationCode.setText(String.format("(%ds)", time--));
                }
            }

            @Override
            public boolean cancel() {
                boolean flag = super.cancel();
                getVerificationCode.setEnabled(true);
                getVerificationCode.setText("获取验证码");
                getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
                getVerificationCode.getPaint().setAntiAlias(true);//抗锯齿
                time = 60;
                task = null;
                return flag;
            }

        }, 0, 1000);
    }

    /**
     * 加载失败回调方法
     *
     * @param requestCode
     * @param bean
     */
    @Override
    public void loadFaild(int requestCode, BaseBean bean) {
        super.loadFaild(requestCode, bean);
        if (requestCode == BaseQuestConfig.QUEST_GET_REGISTERED_VERIFICATION_CODE) {
            getVerificationCode.setEnabled(true);
            getVerificationCode.setText("获取验证码");
            getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
            getVerificationCode.getPaint().setAntiAlias(true);//抗锯齿
            AHandler.cancel(task);
        }
    }

    @Override
    public void onBackPressed() {
        AHandler.cancel(task);
        finish();
    }

}
