package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.mine.mode.PopLogBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_POP_LOG_CODE;

/**
 * Created by wangchao on 2018-09-21.
 * 推广海报
 */
public class PromotionPosterActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.ivErWeiMa)
    ImageView ivErWeiMa;
    @BindView(R.id.llCode)
    LinearLayout llCode;
    @BindView(R.id.tvAppName)
    TextView tvAppName;
    @BindView(R.id.ivLogo)
    ImageView ivLogo;
    @BindView(R.id.frameLayout)
    FrameLayout frameLayout;

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, PromotionPosterActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promotion_poster);
        ButterKnife.bind(this);
        initData();
    }

    private void initData() {
        UIUtils.showLoadDialog(that, getString(R.string.loading));
        BaseQuestStart.getPopLog(that);
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        if (requestCode == GET_POP_LOG_CODE) {
            if (bean.status == 1) {
                PopLogBean popLogBean = bean.Data();
                upadateUi(popLogBean);
            }
        }
    }

    private void upadateUi(PopLogBean popLogBean) {
        GlideMediaLoader.load(that, ivLogo, popLogBean.app_logo);
        GlideMediaLoader.load(that, ivErWeiMa, popLogBean.app_qrcode);
        tvAppName.setText(popLogBean.app_name);
    }
}
