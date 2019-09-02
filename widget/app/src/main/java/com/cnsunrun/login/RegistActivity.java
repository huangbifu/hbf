package com.cnsunrun.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunrun.R;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.model.LoginInfo;
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

import static com.cnsunrun.login.event.LoginConfig.ACCOUNT;

/**
 * 注册界面
 */
public class RegistActivity extends LBaseActivity {

    @BindView(R.id.title_bar)
    TitleBar titleBar;
    @BindView(R.id.edit_account)
    EditText editAccount;
    @BindView(R.id.edit_verification_code)
    EditText editVerificationCode;
    @BindView(R.id.edit_password)
    EditText editPassword;
    @BindView(R.id.cb_password)
    CheckBox cbPassword;
    @BindView(R.id.edit_pic_code)
    EditText editPicCode;
    @BindView(R.id.tv_register)
    TextView tvRegister;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;
    @BindView(R.id.layout_account)
    LinearLayout layoutAccount;
    @BindView(R.id.layout_verification_code)
    RelativeLayout layoutVerificationCode;
    @BindView(R.id.layout_password)
    RelativeLayout layoutPassword;
    @BindView(R.id.tv_pic_code)
    TextView tvPicCode;
    @BindView(R.id.iv_pic_code)
    ImageView ivPicCode;
    @BindView(R.id.layout_pic_code)
    RelativeLayout layoutPicCode;
    @BindView(R.id.tv_verification_code)
    TextView tvVerificationCode;
    @BindView(R.id.tv_password)
    TextView tvPassword;
    @BindView(R.id.tv_account)
    TextView tvAccount;


    private int time = 60;
    private AHandler.Task task;
    private VerificationCodeInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);
        ButterKnife.bind(this);
        titleBar.setLeftAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AHandler.cancel(task);
                finish();
            }
        });
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                AHandler.cancel(task);
//                CommonIntent.startLoginActivity(that);
            }
        });
        cbPassword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //如果选中，显示密码
                    editPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    //否则隐藏密码
                    editPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());

                }
            }
        });
        initEdit();
        editAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() == 11) {
                    //获取图形验证码
                    BaseQuestStart.getRegisteredPicVerificationCode(that, editAccount.getText().toString(), "120", "30");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ivPicCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取图形验证码
                BaseQuestStart.getRegisteredPicVerificationCode(that, editAccount.getText().toString(), "120", "30");
            }
        });
        getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    private void initEdit() {
        editAccount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tvAccount.setTextColor(getResources().getColor(R.color.com_color));
                    layoutAccount.setBackgroundResource(R.drawable.shape_login_bg);
                } else {
                    tvAccount.setTextColor(getResources().getColor(R.color.text_color_333));
                    layoutAccount.setBackgroundResource(R.drawable.shape_login_bg2);
                }
            }
        });
        editPicCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tvPicCode.setTextColor(getResources().getColor(R.color.com_color));
                    layoutPicCode.setBackgroundResource(R.drawable.shape_login_bg);
                } else {
                    tvPicCode.setTextColor(getResources().getColor(R.color.text_color_333));
                    layoutPicCode.setBackgroundResource(R.drawable.shape_login_bg2);
                }
            }
        });
        editVerificationCode.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tvVerificationCode.setTextColor(getResources().getColor(R.color.com_color));
                    layoutVerificationCode.setBackgroundResource(R.drawable.shape_login_bg);
                } else {
                    tvVerificationCode.setTextColor(getResources().getColor(R.color.text_color_333));
                    layoutVerificationCode.setBackgroundResource(R.drawable.shape_login_bg2);
                }
            }
        });
        editPassword.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b) {
                    tvPassword.setTextColor(getResources().getColor(R.color.com_color));
                    layoutPassword.setBackgroundResource(R.drawable.shape_login_bg);
                } else {
                    tvPassword.setTextColor(getResources().getColor(R.color.text_color_333));
                    layoutPassword.setBackgroundResource(R.drawable.shape_login_bg2);
                }
            }
        });

    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case BaseQuestConfig.QUEST_GET_REGISTERED_VERIFICATION_CODE:
                if (bean.status > 0) {
                    //获取短信验证码
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
            case BaseQuestConfig.QUEST_REGISTER_CODE:
                if (bean.status > 0) {
                    //注册
                    ToastUtils.shortToast(bean.Data());
                    AHandler.cancel(task);
                    CommonIntent.startLoginActivity(that);
                    finish();
                } else {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
            case BaseQuestConfig.QUEST_GET_REGISTERED_PIC_VERIFICATION_CODE:
                if (bean.status > 0) {
                    //获取图形验证码
                    info = bean.Data();
                    Glide.with(that)
                            .load(info.getUrl())
                            .centerCrop()
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .error(R.drawable.rc_image_error)
                            .dontAnimate()
                            .into(ivPicCode);
                } else {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }


    @OnClick({R.id.tv_register, R.id.get_verification_code,R.id.tv_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                //获取验证码
                if (TextUtils.isEmpty(editAccount.getText().toString())) {
                    ToastUtils.shortToast("手机号不能为空");
                    return;
                }
//                if (!Utils.isMobileNO(editAccount.getText().toString())) {
//                    UIUtils.shortM("手机号码不正确");
//                    return;
//                }
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                if (task == null) {
                    getVerificationCode.setText("发送中..");
                    getVerificationCode.setEnabled(false);
                    UIUtils.showLoadDialog(that, "获取验证码..");
                    BaseQuestStart.getRegisteredVerificationCode(that, editAccount.getText().toString(), editPicCode.getText().toString());
                }
                break;
            case R.id.tv_register:
                //注册
                if (TextUtils.isEmpty(editAccount.getText().toString())) {
                    ToastUtils.shortToast("手机号不能为空");
                    return;
                }
//                if (!Utils.isMobileNO(editAccount.getText().toString())) {
//                    UIUtils.shortM("手机号码不正确");
//                    return;
//                }
                if (TextUtils.isEmpty(editVerificationCode.getText().toString())) {
                    ToastUtils.shortToast("验证码不能为空");
                    return;
                }
                if (TextUtils.isEmpty(editPassword.getText().toString())) {
                    ToastUtils.shortToast("密码不能为空");
                    return;
                }
                //注册
                UIUtils.showLoadDialog(that);
                BaseQuestStart.register(that, editAccount.getText().toString(), editVerificationCode.getText().toString(),
                        editPassword.getText().toString(), editPassword.getText().toString());
                break;
            case R.id.tv_login:
                //登录
                AHandler.cancel(task);
                CommonIntent.startLoginActivity(that);
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
