package com.cnsunrun.mine.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.model.AreaEntity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.ChooserHelper;
import com.cnsunrun.common.util.SelectHelprUtils;
import com.cnsunrun.common.widget.dialog.WheelDialogFragment;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.common.widget.wheel.WheelAreaPicker;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.login.bean.CaptchaStateMode;
import com.cnsunrun.mine.mode.BankListBean;
import com.nanchen.bankcardutil.ContentWithSpaceEditText;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.InputMethodUtil;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_BANK_LIST_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_ADD_BANK_CARD_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_COMMON_SMS_CODE;

/**
 * Created by wangchao on 2018-10-08.
 * 添加银行卡
 */
public class AddBankCardActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etCardNum)
    ContentWithSpaceEditText etCardNum;
    @BindView(R.id.etOpeningBank)
    EditText etOpeningBank;
    @BindView(R.id.btnBind)
    QMUIRoundButton btnBind;
    @BindView(R.id.etCode)
    EditText etCode;
    @BindView(R.id.get_verification_code)
    TextView getVerificationCode;
    @BindView(R.id.tvBankName)
    TextView tvBankName;
    @BindView(R.id.tvCityName)
    TextView tvCityName;
    private String username;
    private String account;
    private String bank_adress;
    private String password;
    private String code;
    private CaptchaStateMode captchaStateMode;
    private ChooserHelper contactAreaChooser = new ChooserHelper();
    private String province;
    private String city;
    private List<BankListBean> bankListBeans;
    private String branch_bank_adress;

    public static void startThis(Context that, String dealPassword) {
        Intent intent = new Intent(that, AddBankCardActivity.class);
        intent.putExtra("password", dealPassword);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card);
        ButterKnife.bind(this);
        captchaStateMode = new CaptchaStateMode(this, getVerificationCode);
        password = getIntent().getStringExtra("password");
        getVerificationCode.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        initData();
    }

    private void initData() {
        BaseQuestStart.getBankList(that);
    }


    private boolean checkInput() {
        username = etName.getText().toString().trim();
        account = etCardNum.getTextWithoutSpace();
        branch_bank_adress = etOpeningBank.getText().toString().trim();
        code = etCode.getText().toString().trim();
        if (EmptyDeal.isEmpy(username)) {
            UIUtils.shortM("请输入持卡人姓名");
            return false;
        } else if (EmptyDeal.isEmpy(account)) {
            UIUtils.shortM("请输入卡号");
            return false;
        } else if (account.length() < 15) {
            UIUtils.shortM("请正确输入银行卡号");
            return false;
        } else if (EmptyDeal.isEmpy(bank_adress)) {
            UIUtils.shortM("请选择开户行");
            return false;
        } else if (EmptyDeal.isEmpy(branch_bank_adress)) {
            UIUtils.shortM("请输入开户行所在支行");
            return false;
        }else if (EmptyDeal.isEmpy(province)) {
            UIUtils.shortM("请选择开户行所在城市");
            return false;
        } else if (EmptyDeal.isEmpy(code)) {
            UIUtils.shortM("请输入短信验证码");
            return false;
        }
        return true;
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);

        switch (requestCode) {
            case POST_ADD_BANK_CARD_CODE:
                if (bean.status == 1) {
                    InputMethodUtil.HideKeyboard(this);
                    finish();
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
            case POST_COMMON_SMS_CODE:
                if (bean.status == 1) {
                    captchaStateMode.beginCountDown();
                } else {
                    UIUtils.shortM(bean.msg);
                }
                break;
            case GET_BANK_LIST_CODE:
                if (bean.status==1){
                    bankListBeans = bean.Data();
                }
                break;
        }
    }

    @OnClick({R.id.get_verification_code, R.id.btnBind,R.id.tvBankName, R.id.tvCityName})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.get_verification_code:
                BaseQuestStart.getCommonSms(that);
                break;
            case R.id.btnBind:
                if (checkInput()) {
                    UIUtils.shortM(getString(R.string.loading));
                    BaseQuestStart.postAddBankCard(that, password, username, account, bank_adress, "1", code,branch_bank_adress,province,city);
                }
                break;
            case R.id.tvBankName://选择开户行
                SelectHelprUtils.dealResult(that, bankListBeans, new WheelDialogFragment.OnWheelDialogListener<BankListBean>() {
                    @Override
                    public void onClickLeft(DialogFragment dialog, String value) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onClickRight(DialogFragment dialog, BankListBean value) {
                        dialog.dismiss();
                        tvBankName.setText(value.title);
                        bank_adress = value.id;
                    }

                    @Override
                    public void onValueChanged(DialogFragment dialog, String value) {

                    }
                });
                break;
            case R.id.tvCityName://开户所在城市
                contactAreaChooser.showAreaChooser(that, tvCityName);
                contactAreaChooser.setOnselectValListener(new ChooserHelper.OnselectValListener() {
                    @Override
                    public void onSelectValListener(String text, int index) {
                        WheelAreaPicker areaPicker = contactAreaChooser.areaPicker;
                        province = ((AreaEntity) areaPicker.getProvince()).getTitle();
                        city = ((AreaEntity.ChildBeanX) areaPicker.getCity()).getTitle();
                        tvCityName.setText(((AreaEntity) areaPicker.getProvince()).getTitle()+((AreaEntity.ChildBeanX) areaPicker.getCity()).getTitle());
                    }
                });
                break;
        }
    }



}
