package com.cnsunrun.chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.InputContentDialog;
import com.cnsunrun.chat.mode.GroupInfoBean;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.config.RedPackMessage;
import com.cnsunrun.common.dialog.DataLoadDialogFragment;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.widget.tagFlow.FlowLayout;
import com.cnsunrun.common.widget.tagFlow.TagAdapter;
import com.cnsunrun.common.widget.tagFlow.TagFlowLayout;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.cnsunrun.commonui.widget.SwitchButton.SwitchButton;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.mine.mode.UserDataBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.Utils;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_USER_DATA_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.SEND_REDPACK_CODE;
import static com.cnsunrun.login.event.LoginConfig.IS_SECRET_FREE;

public class SendNineRedpackActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.etRedPackNumber)
    EditText etRedPackNumber;
    @BindView(R.id.etMoney)
    EditText etMoney;
    @BindView(R.id.tvReleaseRange)
    TextView tvReleaseRange;
    @BindView(R.id.tagFlowLayout)
    TagFlowLayout tagFlowLayout;
    @BindView(R.id.tvMultiple)
    TextView tvMultiple;
    @BindView(R.id.btnSubmit)
    QMUIRoundButton btnSubmit;
    private GroupInfoBean groupInfoBean;
    private TreeSet<Integer> selectedList;
    UserDataBean userDataBean;
    boolean isSetDealPwd;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_redpack_nine);
        EventBus.getDefault().register(this);
        ButterKnife.bind(this);
        initViews();
        userDataBean=getSession().getObject("userBean", UserDataBean.class);
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
    private void initViews() {
        TagAdapter tagAdapter = new TagAdapter<String>(Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9")) {
            @Override
            public View getView(FlowLayout parent, int position, String item) {
                TextView tvTag = (TextView) LayoutInflater.from(that).inflate(R.layout.item_tag, tagFlowLayout, false);
                tvTag.setText(item);
                return tvTag;
            }
        };
        tagFlowLayout.setAdapter(tagAdapter);
        tagFlowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                selectedList = tagFlowLayout.getSelectedList();
                if (!EmptyDeal.isEmpy(groupInfoBean)){
                    List<GroupInfoBean.MoreThunderRatioBean> more_thunder_ratio = groupInfoBean.more_thunder_ratio;
                    if (!EmptyDeal.isEmpy(more_thunder_ratio)){
                        for (int i = 0; i < more_thunder_ratio.size(); i++) {
                            if (selectedList.size()==more_thunder_ratio.get(i).num){
                                tvMultiple.setText(groupInfoBean.more_thunder_title+more_thunder_ratio.get(i).ratio);
                            }
                        }
                        if (selectedList.size()==0){
                            tvMultiple.setText(groupInfoBean.more_thunder_title);
                        }
                    }
                }

                return false;
            }
        });
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
                            password =inputContentDialog.getInputContent();
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
        TreeSet<Integer> selectedList = tagFlowLayout.getSelectedList();
        if (selectedList.size()==0){
            UIUtils.shortM("请选择雷数");
            return;
        }
        List<String> strings=new ArrayList<>();
        Iterator<Integer> iterator = selectedList.iterator();
        while (iterator.hasNext()) {
            strings.add(String.valueOf(iterator.next()));
        }
        String group_id = getIntent().getStringExtra("group_id");
        String type = getIntent().getStringExtra("type");
        UIUtils.showLoadDialog(that,"提交中..");
        BaseQuestStart.send_redpack(that,etMoney,etRedPackNumber,Utils.listToString(strings,new Utils.DefaultToString<String>("")),group_id,password,type,1);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getEvents(GroupInfoBean groupInfoBean) {
        if (!EmptyDeal.isEmpy(groupInfoBean)) {
            this.groupInfoBean = groupInfoBean;
            tvReleaseRange.setText(groupInfoBean.money_range);
            tagFlowLayout.setMaxSelectCount(groupInfoBean.max_thunder_num);
            tvMultiple.setText(groupInfoBean.more_thunder_title);
        }
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
}
