package com.cnsunrun.mine.activity;

import android.os.Bundle;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.commonui.widget.edittext.EditView;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.config.Const.OLD_DEAL_PWD_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.USER_SAFE_SET_DEAL_PASSWORD_CODE;

/**
 * 设置-设置支付密码
 * Created by WQ on 2017/10/30.
 */

public class SettingPayPasswordActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.edit_pay_password)
    EditView editPayPassword;
    @BindView(R.id.btnNext)
    QMUIRoundButton btnNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_modifyphone);
        ButterKnife.bind(this);
        editPayPassword.setMaxLength(6);
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case USER_SAFE_SET_DEAL_PASSWORD_CODE:
                if (bean.status == 1) {
                    UIUtils.shortM(bean.msg);
                    finish();
                } else {
                    UIUtils.shortM(bean);
                }
                break;

        }
        super.nofityUpdate(requestCode, bean);
    }

    @OnClick(R.id.btnNext)
    public void onClick() {

        if (EmptyDeal.isEmpy(editPayPassword)) {
            UIUtils.shortM("验证码不能为空");
            return;
        }
        UIUtils.showLoadDialog(that, "验证中...");
        BaseQuestStart.UserSafeSetDealPassword(that, editPayPassword.getText().toString());
        getSession().put(OLD_DEAL_PWD_CODE, editPayPassword.getText().toString());
    }
}
