package com.cnsunrun.chat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.InputContentDialog;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.config.RedPackMessage;
import com.cnsunrun.common.dialog.DataLoadDialogFragment;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.SwitchButton.SwitchButton;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.mine.mode.UserDataBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.Utils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_USER_DATA_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.SEND_REDPACK_CODE;
import static com.cnsunrun.login.event.LoginConfig.IS_SECRET_FREE;

/**
 * 发接龙红包
 */
public class SendJielongRedpackActivity extends LBaseActivity {
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.etMoney)
    TextView etMoney;
    @BindView(R.id.etRedPackNumber)
    TextView etRedPackNumber;
    @BindView(R.id.tvMemberNumber)
    TextView tvMemberNumber;
    @BindView(R.id.etBombNumber)
    TextView etBombNumber;
    @BindView(R.id.btnSubmit)
    QMUIRoundButton btnSubmit;
    String password;
    UserDataBean userDataBean=new UserDataBean();
    boolean isSetDealPwd;
    public static void startThis(Activity that) {
        Intent intent = new Intent(that, SendJielongRedpackActivity.class);
        that.startActivity(intent);
    }

    @SuppressLint("DefaultLocale")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        setContentView(R.layout.activity_send_jielong_redpack);
        int redpack_num = getIntent().getIntExtra("redpack_num", 7);
        if(redpack_num<=0){
            redpack_num=7;
//            finish();
//            return;
        }
        etMoney.setText(String.format("%s",getIntent().getStringExtra("money_title")));
        etRedPackNumber.setText(String.format("%d", redpack_num));
        tvMemberNumber.setText(String.format("群组共%d人", getIntent().getIntExtra("member_number", 0)));
        userDataBean=getSession().getObject("userBean", UserDataBean.class);

//        if(userDataBean==null)
        {
            UIUtils.showLoadDialog(that);
            BaseQuestStart.getUserData(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        DataLoadDialogFragment.getInstance(getSupportFragmentManager(), BaseQuestStart.UserSafeIndex(), new DataLoadDialogFragment.onDataLoadeListener() {
            @Override
            public void onDataLoaded(BaseBean bean) {
                JSONObject jsonObj =  JsonDeal.createJsonObj(bean.toString());
                isSetDealPwd = "1".equals(jsonObj.optString("is_set_deal_password"));
//                CommonIntent.startModifyPwdActivity(that,"1".equals(isSetDealPwd)?1:2);
            }
        });
        BaseQuestStart.getUserData(this);
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case SEND_REDPACK_CODE:
                if(bean.status==1) {
                    JSONObject jobj = JsonDeal.createJsonObj(bean.Data().toString());
                    String group_id = getIntent().getStringExtra("group_id");
                    RedPackMessage redPackMessage = RedPackMessage.obtain(jobj.optString("title"), jobj.optString("id"),getIntent().getStringExtra("type"));
                    Intent result=new Intent();
                    result.putExtra("redpack",redPackMessage);
                   setResult(RESULT_OK,result);
                   finish();
                }else {
                    UIUtils.shortM(bean);
                }
//
                break;
            case GET_USER_DATA_CODE:
                if (bean.status == 1) {
                    userDataBean = bean.Data();
                    getSession().put("userBean",userDataBean);
                }
                break;
            case BaseQuestConfig.QUEST_NO_PAY_PWD_BET_CODE:
                if (bean.status ==1) {
                    ToastUtils.shortToast(bean.msg);
                    BaseQuestStart.getUserData(this);
                    if(Config.getConfigInfo(that,IS_SECRET_FREE,null).equals("1")){
                        Config.putConfigInfo(that,IS_SECRET_FREE,"0");
                        break;
                    }
                    if(Config.getConfigInfo(that,IS_SECRET_FREE,null).equals("0")){
                        Config.putConfigInfo(that,IS_SECRET_FREE,"1");
                    }

                } else {
                    ToastUtils.shortToast(bean.msg);
                }
                break;
        }
    }

    @OnClick(R.id.btnSubmit)
    public void onViewClicked() {
        String is_secret_free = userDataBean.is_secret_free;
        if("1".equals(is_secret_free)){
            submitRedPackInfo();
        }else {
            if(!isSetDealPwd){
                UIUtils.shortM("请先设置支付密码");
                CommonIntent.startSettingPayPasswordActivity(that);
                return;
            }
            final InputContentDialog inputContentDialog = InputContentDialog.newInstance().setRightBtnTxt("确认支付")
                    .setInfoContentTxt(String.format("%.2f元", Utils.valueOf(etMoney.getText().toString(),0))).setInputPwd(true)
                    .setContentHintTxt("输入支付密码").setTitleTxt("支付");
            inputContentDialog
                    .setOnSubmitAction(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            password=inputContentDialog.getInputContent();
                            submitRedPackInfo();
                        }
                    }).setOnSwitchBtnAction(new SwitchButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(SwitchButton view, boolean isChecked) {
                    if(!isChecked) {
                        BaseQuestStart.setNoPaypwdBet(that, isChecked ? "1" : "0", "");
                    }else {
                        CommonIntent.startAvoidCloseActivity(that);
                        inputContentDialog.dismissAllowingStateLoss();
                    }
                }

            }).show(getSupportFragmentManager(),"InputContentDialog");
        }

    }

    private void submitRedPackInfo() {
        String group_id = getIntent().getStringExtra("group_id");
        String type = getIntent().getStringExtra("type");
        UIUtils.showLoadDialog(that,"提交中..");
        BaseQuestStart.send_redpack(SendJielongRedpackActivity.this,etMoney,etRedPackNumber,etBombNumber,group_id,password,type,0);
    }
}
