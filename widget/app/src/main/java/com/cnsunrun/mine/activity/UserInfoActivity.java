package com.cnsunrun.mine.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing.utils.ImageCompressor;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.model.AreaEntity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.AlertDialogUtil;
import com.cnsunrun.common.util.ChooserHelper;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.common.widget.wheel.WheelAreaPicker;
import com.cnsunrun.mine.dialogs.SelectPhotoDialog;
import com.cnsunrun.mine.dialogs.SexChooseDialog;
import com.cnsunrun.mine.mode.UserDataBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.unistrong.yang.zb_permission.ZbPermission;
import com.unistrong.yang.zb_permission.ZbPermissionFail;
import com.unistrong.yang.zb_permission.ZbPermissionSuccess;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.cnsunrun.common.config.Const.PERMISSION;
import static com.cnsunrun.common.quest.BaseQuestConfig.GET_USER_DATA_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.QUEST_GET_CHANGE_PHOTO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.QUEST_GET_SAVE_USER_INFO_CODE;

/**
 * Created by yepeng on 2018-09-26.
 * 我的-个人信息
 */

public class UserInfoActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.iv_user_photo)
    CircleImageView ivUserPhoto;
    @BindView(R.id.layout_change_user_photo)
    RelativeLayout layoutChangeUserPhoto;
    @BindView(R.id.tv_nickname)
    TextView tvNickname;
    @BindView(R.id.layout_change_nickname)
    RelativeLayout layoutChangeNickname;
    @BindView(R.id.tv_realname)
    TextView tvRealname;
    @BindView(R.id.layout_change_realname)
    RelativeLayout layoutChangeRealname;
    @BindView(R.id.tv_sex)
    TextView tvSex;
    @BindView(R.id.layout_change_sex)
    RelativeLayout layoutChangeSex;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.layout_change_phone)
    RelativeLayout layoutChangePhone;
    @BindView(R.id.tv_place)
    TextView tvPlace;
    @BindView(R.id.layout_change_place)
    RelativeLayout layoutChangePlace;



    private UserDataBean memberInfo;
    private String headImageFilePath;
    private String gender;
    private SexChooseDialog sexChooseDialog;
    public Uri imageUri;
    public static File tempFile;
    private ChooserHelper contactAreaChooser = new ChooserHelper();
    private String provinceId;
    private String cityId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        ButterKnife.bind(this);
        UIUtils.showLoadDialog(that);
        BaseQuestStart.getUserData(this);
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaseQuestStart.getSaveUserInfo(that, tvNickname.getText().toString(),tvRealname.getText().toString(),gender,provinceId,cityId);
            }
        });
    }

    private void initText() {
        //设置用户头像
        Glide.with(this)
                .load(memberInfo.avatar)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.drawable.mine_icon_touxiang_normal)
                .dontAnimate()
                .into(ivUserPhoto);
        tvNickname.setText(memberInfo.nickname);
        tvRealname.setText(memberInfo.realname);
        gender = memberInfo.gender;
        if (memberInfo.gender.equals("0")) {
            tvSex.setText("");
        } else if (memberInfo.gender.equals("1")) {
            tvSex.setText("男");
        } else if (memberInfo.gender.equals("2")) {
            tvSex.setText("女");
        }
        tvPhone.setText(memberInfo.mobile);
        tvPlace.setText(memberInfo.address);
        provinceId=memberInfo.province;
        cityId=memberInfo.city;
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case GET_USER_DATA_CODE:
                if (bean.status == 1) {
                    memberInfo = bean.Data();
                    initText();
                }
                break;
            case QUEST_GET_CHANGE_PHOTO_CODE:
                if (bean.status == 1) {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
            case QUEST_GET_SAVE_USER_INFO_CODE:
                if (bean.status == 1) {
                    ToastUtils.shortToast(bean.msg);
                    finish();
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 1) {
                if (data != null) {
                    String title = data.getStringExtra("title");
                    String content = data.getStringExtra("content");
                    switch (title) {
                        case "昵称":
                            tvNickname.setText(content);
                            break;
                        case "真实姓名":
                            tvRealname.setText(content);
                            break;
                    }
                }
            }
            if (requestCode == 2) {
                //相册获取
                List<BaseMedia> medias = Boxing.getResult(data);
                //这里一定要做非空的判断
                if (medias != null && medias.size() > 0) {
                    BaseMedia baseMedia = medias.get(0);
                    String path;
                    if (baseMedia instanceof ImageMedia) {
                        ((ImageMedia) baseMedia).compress(new ImageCompressor(that));
                        path = ((ImageMedia) baseMedia).getThumbnailPath();
                    } else {
                        path = baseMedia.getPath();
                    }
                    File file = new File(path);
                    headImageFilePath = file.getAbsolutePath();
                    //设置用户头像
                    Glide.with(this)
                            .load(headImageFilePath)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.mine_icon_touxiang_normal)
                            .dontAnimate()
                            .into(ivUserPhoto);
                    BaseQuestStart.getChangePhoto(that, TextUtils.isEmpty(headImageFilePath) ? new File("") : new File(headImageFilePath));
                }
            }
            if (requestCode == 3) {
                //拍照获取头像
                if (resultCode == RESULT_OK) {
                    UCrop.Options crop = new UCrop.Options();
                    // do not copy exif information to crop pictures
                    // because png do not have exif and png is not Distinguishable
                    crop.setCompressionFormat(Bitmap.CompressFormat.PNG);
                    crop.setToolbarColor(Color.parseColor("#000000"));
                    crop.setStatusBarColor(Color.parseColor("#000000"));
                    crop.setHideBottomControls(true);
                    crop.withMaxResultSize(200, 200);
                    crop.withAspectRatio(1, 1);
                    crop.setFreeStyleCropEnabled(true);
                    crop.setShowCropGrid(false);
                    File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
                    File file = new File(path, System.currentTimeMillis() + ".jpg");
                    UCrop.of(imageUri, Uri.parse(file.toString()))
                            .withOptions(crop)
                            .start(that, 4);
                }
            }
            if (requestCode == 4) {
                if (resultCode == RESULT_OK || resultCode == 96) {
                    Uri output = UCrop.getOutput(data);
                    headImageFilePath = output.getPath();
                    tempFile = new File(headImageFilePath);
                    //设置用户头像
                    Glide.with(this)
                            .load(headImageFilePath)
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.mine_icon_touxiang_normal)
                            .dontAnimate()
                            .into(ivUserPhoto);
                    BaseQuestStart.getChangePhoto(that, tempFile);
                }
            }
        }
    }

    private void getPhoto() {
        AlertDialogUtil.selectPhotoDialog(that, "拍照", "从相册获取", new SelectPhotoDialog.OnSelectItemClickListener() {
            @Override
            public void selectItemOne(View view) {
                //拍照
                ZbPermission.with(that)
                        .addRequestCode(PERMISSION)
                        .permissions(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE)
                        .request();
            }
            @Override
            public void selectItemTwo(View view) {
                //从相册获取
                String cachePath = BoxingFileHelper.getCacheDir(that);
                if (TextUtils.isEmpty(cachePath)) {
                    Toast.makeText(that, R.string.storage_deny, Toast.LENGTH_SHORT).show();
                    return;
                }
                Uri destUri = new Uri.Builder()
                        .scheme("file")
                        .appendPath(cachePath)
                        .appendPath(String.format(Locale.US, "%s.jpg", System.currentTimeMillis()))
                        .build();
                //这里设置的config的mode是single_img  就是单选图片的模式  支持相机
                BoxingConfig singleCropImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG)
                        .needCamera()
                        .needGif()
                        .withCropOption(new BoxingCropOption(destUri).withMaxResultSize(200, 200).aspectRatio(1, 1));
                Boxing.of(singleCropImgConfig).withIntent(that, BoxingActivity.class).start(that, 2);
            }
        });
    }

    private void startCarmera() {
        //獲取系統版本
        int currentapiVersion = Build.VERSION.SDK_INT;
        // 激活相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        if (hasSdcard()) {
            File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
            tempFile = new File(path, System.currentTimeMillis() + ".jpg");
            if (!path.exists()) {
                path.mkdirs();
            }
            if (currentapiVersion < 24) {
                // 从文件中创建uri
                imageUri = Uri.fromFile(tempFile);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            } else {
                //兼容android7.0 使用共享文件的形式
                ContentValues contentValues = new ContentValues(1);
                contentValues.put(MediaStore.Images.Media.DATA, tempFile.getAbsolutePath());
                imageUri = that.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            }
            // 开启一个带有返回值的Activity，请求码为PHOTO_REQUEST_CAREMA
            that.startActivityForResult(intent, 3);
        }
    }

    /**
     * 判断sdcard是否被挂载
     */
    public static boolean hasSdcard() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }


    @OnClick({R.id.layout_change_user_photo, R.id.layout_change_nickname, R.id.layout_change_realname, R.id.layout_change_sex, R.id.layout_change_phone, R.id.layout_change_place})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.layout_change_user_photo:
                //头像
                getPhoto();
                break;
            case R.id.layout_change_nickname:
                //昵称
                CommonIntent.startUpdateInfoActivity(that, "昵称", tvNickname.getText().toString());
                break;
            case R.id.layout_change_realname:
                //昵称
                CommonIntent.startUpdateInfoActivity(that, "真实姓名", tvRealname.getText().toString());
                break;
            case R.id.layout_change_sex:
                //性别
                sexChooseDialog = new SexChooseDialog(that, new SexChooseDialog.OnViewClickListener() {
                    @Override
                    public void onItemClick(String sex) {
                        tvSex.setText(sex);
                        if(sex.endsWith("男")){
                            gender = "1";
                        }else if(sex.endsWith("女")){
                            gender = "2";
                        }
                    }
                });
                break;
            case R.id.layout_change_phone:
                break;
            case R.id.layout_change_place:
                //选择省市区
                contactAreaChooser.showAreaChooser(that, tvPlace);
                contactAreaChooser.setOnselectValListener(new ChooserHelper.OnselectValListener() {
                    @Override
                    public void onSelectValListener(String text, int index) {
//                        tvPlace.setText(text);
                        WheelAreaPicker areaPicker = contactAreaChooser.areaPicker;
                        provinceId = ((AreaEntity) areaPicker.getProvince()).getId();
                        cityId = ((AreaEntity.ChildBeanX) areaPicker.getCity()).getId();
                        tvPlace.setText(((AreaEntity) areaPicker.getProvince()).getTitle()+((AreaEntity.ChildBeanX) areaPicker.getCity()).getTitle());
//                        if (((AreaEntity.ChildBeanX.ChildBean) areaPicker.getArea()) != null) {
//                            areaId = ((AreaEntity.ChildBeanX.ChildBean) areaPicker.getArea()).getId();
//                        } else {
//                            areaId = "";
//                        }
                    }
                });
                break;
        }
    }
    @ZbPermissionSuccess(requestCode = PERMISSION)
    public void permissionSuccess() {
        startCarmera();
    }

    @ZbPermissionFail(requestCode = PERMISSION)
    public void permissionFail() {
//        startLogin(null);
        UIUtils.shortM("请在设置里面打开应用使用相机的权限");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ZbPermission.onRequestPermissionsResult(UserInfoActivity.this, requestCode, permissions, grantResults);
    }
}
