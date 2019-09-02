package com.cnsunrun.chat.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.mode.UserQrCode;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.TextColorUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import de.hdodenhof.circleimageview.CircleImageView;

import static android.os.Environment.DIRECTORY_DCIM;

/**
 * 二维码提示
 * Created by WQ on 2017/10/30.
 */

public class QrCodeDialog extends LBaseDialogFragment {

    @BindView(R.id.imgUserIcon)
    CircleImageView imgUserIcon;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtInfo)
    TextView txtInfo;
    @BindView(R.id.txtSignature)
    TextView txtSignature;
    @BindView(R.id.tvScanQrcode)
    TextView tvScanQrcode;
    @BindView(R.id.center_line)
    View centerLine;
    @BindView(R.id.tvSave)
    TextView tvSave;
    @BindView(R.id.imgQrCode)
    ImageView imgQrCode;
    int[] genderIcon = {0, R.drawable.ic_chat_sex_man, R.drawable.ic_chat_sex_woman};

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseQuestStart.user_qrcode(this);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case BaseQuestStart.USER_QRCODE_CODE:
                if(bean.status==1){
                    UserQrCode userQrCode=bean.Data();
                    GlideMediaLoader.load(that,imgUserIcon,userQrCode.avatar);
                    GlideMediaLoader.load(that,imgQrCode,userQrCode.qrcode);
                    TextColorUtils.setCompoundDrawables(txtUserName, 0, 0, genderIcon[userQrCode.gender], 0);
                    txtUserName.setText(userQrCode.nickname);
                    txtInfo.setText(String.format("电话: %s",userQrCode.mobile));
                    txtSignature.setText(String.format("ID: %s",userQrCode.uid));

                }
                break;
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_user_qrcode;
    }


    public static QrCodeDialog newInstance() {
        Bundle args = new Bundle();
        QrCodeDialog fragment = new QrCodeDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }



    @OnClick({R.id.tvScanQrcode, R.id.tvSave})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvScanQrcode:
                CommonIntent.startScanQRActivity(that);
                dismissAllowingStateLoss();
                break;
            case R.id.tvSave:
                AHandler.runTask(new AHandler.Task() {
                    @Override
                    public void update() {
                        getScreenShot(getView());
                    }
                },10);
                break;
        }
    }

    public void getScreenShot(View view){
        Context context = view.getContext();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();  //启用DrawingCache并创建位图
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache()); //创建一个DrawingCache的拷贝，因为DrawingCache得到的位图在禁用后会被回收
        view.setDrawingCacheEnabled(false);  //禁用DrawingCahce否则会影响性能
        File dcimDirectory = Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM);
        File screenshot = new File(dcimDirectory, System.currentTimeMillis() + ".jpg");
        if (UIUtils.saveBitmapToFile(bitmap, screenshot.getAbsolutePath())) {
            UIUtils.shortM("截图成功,保存在"+screenshot.getAbsolutePath());
            // 其次把文件插入到系统图库
            try {
                MediaStore.Images.Media.insertImage(context.getContentResolver(),
                        screenshot.getAbsolutePath(), screenshot.getName(), null);
                // 最后通知图库更新

                context.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + screenshot.getAbsolutePath())));
            } catch (Exception e) {
                e.printStackTrace();
                UIUtils.shortM("截图失败,请检查存储权限");
            }

        }else {
            UIUtils.shortM("截图失败,请检查存储权限");
        }
    }
}
