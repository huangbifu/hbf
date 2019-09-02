package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.MessageTipDialog;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.util.AlertDialogUtil;
import com.cnsunrun.common.util.LoginUtil;
import com.cnsunrun.common.widget.FormLabLayout;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.DataCleanManager;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by wangchao on 2018-09-27.
 * 设置
 */
public class SettingsActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.flAccountSafe)
    FormLabLayout flAccountSafe;
    @BindView(R.id.flNewMsg)
    FormLabLayout flNewMsg;
    @BindView(R.id.flCurrentVersion)
    FormLabLayout flCurrentVersion;
    @BindView(R.id.flCleanCache)
    FormLabLayout flCleanCache;
    @BindView(R.id.flCleanChatRecord)
    FormLabLayout flCleanChatRecord;
    @BindView(R.id.rlExit)
    RelativeLayout rlExit;

    private String totalCacheSize;

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, SettingsActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_new);
        ButterKnife.bind(this);

        totalCacheSize = DataCleanManager.getTotalCacheSize(that);
        flCleanCache.setLabContentStr(totalCacheSize);
        //获取版本信息比对
        initVersion();
    }

    private void initVersion() {
        try {
            PackageManager manager = that.getPackageManager();
            PackageInfo info = manager.getPackageInfo(that.getPackageName(), 0);
            flCurrentVersion.setLabContentStr("V" + info.versionName);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.flAccountSafe, R.id.flNewMsg, R.id.flCurrentVersion, R.id.flCleanCache, R.id.flCleanChatRecord, R.id.rlExit})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.flAccountSafe://账户安全
                CommonIntent.startAccountSafeActivity(that);
                break;
            case R.id.flNewMsg://新消息通知
                CommonIntent.startMessageNoticeActivity(that);
                break;
            case R.id.flCurrentVersion://当前版本
                break;
            case R.id.flCleanCache://清理缓存
                //清理缓存
//                AlertDialogUtil.showCommonConfirm(that, "提示", "是否清理缓存数据？", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        DataCleanManager.clearAllCache(that);
//                        totalCacheSize = DataCleanManager.getTotalCacheSize(that);
//                        flCleanCache.setLabContentStr(DataCleanManager.getTotalCacheSize(that));
//                        ToastUtils.shortToast("缓存清理成功");
//                    }
//                }, null);


                MessageTipDialog.newInstance().setContentTxt("是否清理缓存数据？")
                        .setOnSubmitAction(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                DataCleanManager.clearAllCache(that);
                                totalCacheSize = DataCleanManager.getTotalCacheSize(that);
                                flCleanCache.setLabContentStr(DataCleanManager.getTotalCacheSize(that));
                                ToastUtils.shortToast("缓存清理成功");
                            }
                        })
                        .show(getSupportFragmentManager(), "MessageTipDialog");
                break;
            case R.id.flCleanChatRecord://清理聊天记录
                MessageTipDialog.newInstance().setContentTxt("是否清空全部聊天记录?")
                        .setOnSubmitAction(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                UIUtils.showLoadDialog(that,"清除中...");
                                RongIM.getInstance().getConversationList(new RongIMClient.ResultCallback<List<Conversation>>() {
                                    @Override
                                    public void onSuccess(List<Conversation> conversations) {
                                        if(conversations!=null)
                                        for (Conversation conversation : conversations) {

                                            RongIM.getInstance().deleteMessages(conversation.getConversationType(), conversation.getTargetId(), new RongIMClient.ResultCallback<Boolean>() {
                                                @Override
                                                public void onSuccess(Boolean aBoolean) {

                                                }

                                                @Override
                                                public void onError(RongIMClient.ErrorCode errorCode) {

                                                }
                                            });

                                        }
                                        UIUtils.cancelLoadDialog();
                                        UIUtils.shortM("清除成功");
                                    }

                                    @Override
                                    public void onError(RongIMClient.ErrorCode errorCode) {
                                        UIUtils.cancelLoadDialog();
                                        UIUtils.shortM("清除失败");
                                    }
                                });

                            }
                        }).show(getSupportFragmentManager(),"MessageTipDialog");
                break;
            case R.id.rlExit://退出登录
                MessageTipDialog.newInstance().setContentTxt("是否退出登录?")
                        .setOnSubmitAction(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                LoginUtil.exitLogin();
                                CommonIntent.startLoginActivity(that);
                            }
                        })
                        .show(getSupportFragmentManager(), "MessageTipDialog");
                break;
        }
    }
}
