package com.cnsunrun.mine.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.login.bean.CaptchaStateMode;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uibase.BaseActivity;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wangchao on 2018-12-19.
 * 解绑支付宝
 */
public class UnBindZhifubaoActivity extends LBaseActivity {
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
    private int time = 60;
    private AHandler.Task task;
    private String id;
    private CaptchaStateMode captchaStateMode;

    public static void startThis(BaseActivity that, String id) {
        Intent intent = new Intent(that, UnBindZhifubaoActivity.class);
        intent.putExtra("id", id);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unbind_zhifubao);
        ButterKnife.bind(this);
        id = getIntent().getStringExtra("id");
        captchaStateMode = new CaptchaStateMode(this, getVerificationCode);
        tvAccount.setText(Config.getLoginInfo().mobile);

    }


    @OnClick({R.id.get_verification_code, R.id.tv_modify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                UIUtils.showLoadDialog(that, "获取验证码..");
                BaseQuestStart.getVerificationCode(that);
                break;
            case R.id.tv_modify:
                //解绑
                if (TextUtils.isEmpty(editVerificationCode.getText().toString())) {
                    ToastUtils.shortToast("验证码不能为空");
                    return;
                }
                UIUtils.showLoadDialog(that);
                BaseQuestStart.unBindZhifubaoAccount(that, id, editVerificationCode.getText().toString());
                break;
        }
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case BaseQuestConfig.QUEST_GET_VERIFICATION_CODE:
                if (bean.status == 1) {
                    UIUtils.longM(bean.Data().toString());
                    captchaStateMode.beginCountDown();
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
            case BaseQuestConfig.UNBIND_ZHIFUBAO_ACCOUNT_CODE:
                if (bean.status == 1) {
                    //修改
                    ToastUtils.shortToast(bean.msg);
                    AHandler.cancel(task);
                    finish();
                } else {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }


}
