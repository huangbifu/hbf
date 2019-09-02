package com.cnsunrun.chat;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.uuzuche.lib_zxing.activity.CaptureFragment;
import com.uuzuche.lib_zxing.activity.CodeUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 二维码扫描
 * Created by WQ on 2017/5/26.
 */

public class ScanQRActivity extends LBaseActivity {
    @BindView(R.id.fl_my_container)
    FrameLayout flMyContainer;
    @BindView(R.id.titleBar)
    TitleBar titleBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activit_scan_qr);
        /**
         * 执行扫面Fragment的初始化操作
         */
        CaptureFragment captureFragment = new CaptureFragment();
        // 为二维码扫描界面设置定制化界面
        CodeUtils.setFragmentArgs(captureFragment, R.layout.fragment_scan_qr_camera);
/**
 * 二维码解析回调函数
 */
        CodeUtils.AnalyzeCallback analyzeCallback = new CodeUtils.AnalyzeCallback() {
            @Override
            public void onAnalyzeSuccess(Bitmap mBitmap, String result) {

                CommonIntent.startChatUserInfoActivity(that,result,false);
//                Intent resultIntent = new Intent();
//                Bundle bundle = new Bundle();
//                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_SUCCESS);
//                bundle.putString(CodeUtils.RESULT_STRING, result);
//                resultIntent.putExtras(bundle);
//                setResult(RESULT_OK, resultIntent);
//                finish();

            }

            @Override
            public void onAnalyzeFailed() {
                Intent resultIntent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putInt(CodeUtils.RESULT_TYPE, CodeUtils.RESULT_FAILED);
                bundle.putString(CodeUtils.RESULT_STRING, "");
                resultIntent.putExtras(bundle);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        };
        captureFragment.setAnalyzeCallback(analyzeCallback);
        try {
            /**
             * 替换我们的扫描控件
             */
            getSupportFragmentManager().beginTransaction().replace(R.id.fl_my_container, captureFragment).commit();
        } catch (Exception e) {
            UIUtils.shortM("摄像头打开失败");
            e.printStackTrace();
        }
    }
//
//    @Override
//    protected boolean isStatusContentDark() {
//        return false;
//    }
}
