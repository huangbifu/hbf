package com.cnsunrun.mine.activity;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.util.AlertDialogUtil;
import com.cnsunrun.common.util.LoginUtil;
import com.cnsunrun.common.util.PushHelper;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.http.cache.ACache;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;
import com.sunrun.sunrunframwork.utils.DataCleanManager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 我的-设置
 */
public class SettingActivity extends LBaseActivity {


    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.layout_account_safe)
    RelativeLayout layoutAccountSafe;
    @BindView(R.id.layout_message_notice)
    RelativeLayout layoutMessageNotice;
    @BindView(R.id.tv_version)
    TextView tvVersion;
    @BindView(R.id.layout_current_version)
    RelativeLayout layoutCurrentVersion;
    @BindView(R.id.tv_cache_size)
    TextView tvCacheSize;
    @BindView(R.id.iv_right_icon)
    ImageView ivRightIcon;
    @BindView(R.id.layout_clear_cache)
    RelativeLayout layoutClearCache;
    @BindView(R.id.layout_clear_all_chat_cache)
    RelativeLayout layoutClearAllChatCache;
    @BindView(R.id.layout_logout)
    RelativeLayout layoutLogout;

    private String totalCacheSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);

        totalCacheSize = DataCleanManager.getTotalCacheSize(that);
        tvCacheSize.setText(totalCacheSize);
        //获取版本信息比对
        initVersion();
    }

    private void initVersion() {
        try {
            PackageManager manager = that.getPackageManager();
            PackageInfo info = manager.getPackageInfo(that.getPackageName(), 0);
            tvVersion.setText("V" + info.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.layout_account_safe, R.id.layout_message_notice, R.id.layout_clear_cache, R.id.layout_clear_all_chat_cache, R.id.layout_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_account_safe:
                //账号安全
                CommonIntent.startAccountSafeActivity(that);
                break;
            case R.id.layout_message_notice:
                //新消息通知
                CommonIntent.startMessageNoticeActivity(that);
                break;
            case R.id.layout_clear_cache:
                //清理缓存
                AlertDialogUtil.showCommonConfirm(that, "提示", "是否清理缓存数据？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DataCleanManager.clearAllCache(that);
                        totalCacheSize = DataCleanManager.getTotalCacheSize(that);
                        tvCacheSize.setText(DataCleanManager.getTotalCacheSize(that));
                        ToastUtils.shortToast("缓存清理成功");
                    }
                }, null);
                break;
            case R.id.layout_clear_all_chat_cache:
                break;
            case R.id.layout_logout:
                //退出登录
                AlertDialogUtil.showCommonConfirm(that, "提示", "是否退出登录？", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        PushHelper.stopPush(that);
                        ACache.get(that).clear();
                        LoginUtil.exitLogin();
                        finish();
                        CommonIntent.startLoginActivity(that);
                    }
                }, null);
                break;
        }
    }
}
