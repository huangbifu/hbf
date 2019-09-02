package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.POST_INVITE_CODE;

/**
 * Created by wangchao on 2018-09-25.
 * 填写邀请码
 */
public class FillCodeActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.btnSubmit)
    QMUIRoundButton btnSubmit;

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, FillCodeActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_code);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.btnSubmit)
    public void onViewClicked() {
        String code = etCode.getText().toString().trim();
        if (EmptyDeal.isEmpy(code)) {
            UIUtils.shortM("请输入邀请码");
        } else {
            UIUtils.showLoadDialog(that, getString(R.string.loading));
            BaseQuestStart.postInvite(that, code);
        }
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case POST_INVITE_CODE:
                if (bean.status == 1) {
                    MyRecommendActvity.startThis(that);
                    finish();
                    UIUtils.shortM(bean.Data().toString());
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }
}
