package com.cnsunrun.mine.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.QrCodeDialog;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseFragment;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.config.RongIMHelper;
import com.cnsunrun.common.quest.BaseQuestConfig;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.cnsunrun.mine.activity.BindZhifubaoActivity;
import com.cnsunrun.mine.activity.FillCodeActivity;
import com.cnsunrun.mine.activity.LuckDrawActivity;
import com.cnsunrun.mine.activity.MoneyRecordActivity;
import com.cnsunrun.mine.activity.MyBankCardActivity;
import com.cnsunrun.mine.activity.MyRecommendActvity;
import com.cnsunrun.mine.activity.PromotionPosterActivity;
import com.cnsunrun.mine.activity.RechargeActivity;
import com.cnsunrun.mine.activity.SettingsActivity;
import com.cnsunrun.mine.activity.WithdrawalActivity;
import com.cnsunrun.mine.mode.UserDataBean;
import com.makeramen.roundedimageview.RoundedImageView;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.view.ItemView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

import static com.cnsunrun.common.quest.BaseQuestConfig.GET_USER_DATA_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_COMMISSION_TRANSFER_CODE;
import static com.cnsunrun.login.event.LoginConfig.IS_SECRET_FREE;

/**
 * Created by wangchao on 2018-09-20.
 */
public class NewUserMineFragment extends LBaseFragment {
    @BindView(R.id.tvNotice)
    ImageView tvNotice;
    @BindView(R.id.ivSetting)
    ImageView ivSetting;
    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.ivUserIcon)
    RoundedImageView ivUserIcon;
    @BindView(R.id.tvPersonalData)
    TextView tvPersonalData;
    @BindView(R.id.tvBalance)
    TextView tvBalance;
    @BindView(R.id.tvMoneyRecord)
    TextView tvMoneyRecord;
    @BindView(R.id.tvTransfer)
    TextView tvTransfer;
    @BindView(R.id.tvWithdrawal)
    TextView tvWithdrawal;
    @BindView(R.id.tvVoucher)
    TextView tvVoucher;
    @BindView(R.id.itemRecommend)
    ItemView itemRecommend;
    @BindView(R.id.itemShare)
    ItemView itemShare;
    @BindView(R.id.itemAward)
    ItemView itemAward;
    @BindView(R.id.itemService)
    ItemView itemService;
    @BindView(R.id.tvNikeName)
    TextView tvNikeName;
    @BindView(R.id.tvIdNum)
    TextView tvIdNum;
    @BindView(R.id.llNikeName)
    LinearLayout llNikeName;
    @BindView(R.id.itemMyBankCard)
    ItemView itemMyBankCard;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.itemQiandao)
    ItemView itemQiandao;
    @BindView(R.id.itemZhifubao)
    ItemView itemZhifubao;
    @BindView(R.id.tvEarnings)
    TextView tvEarnings;
    @BindView(R.id.tvEarningsRecord)
    TextView tvEarningsRecord;
    @BindView(R.id.tvIntoBalance)
    TextView tvIntoBalance;
    Unbinder unbinder;
    private UserDataBean userDataBean;

    public static NewUserMineFragment newInstance() {
        NewUserMineFragment userMineFragment = new NewUserMineFragment();
        Bundle bundle = new Bundle();
        userMineFragment.setArguments(bundle);
        return userMineFragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_new_user_mine;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        BaseQuestStart.getUserData(this);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                initData();

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        BaseQuestStart.getUserData(this);
    }

    @OnClick({R.id.tvNotice, R.id.ivSetting, R.id.tvPersonalData, R.id.tvMoneyRecord, R.id.tvTransfer, R.id.tvWithdrawal, R.id.tvVoucher, R.id.itemRecommend, R.id.itemShare, R.id.itemAward, R.id.itemService,
            R.id.llNikeName, R.id.ivUserIcon, R.id.itemMyBankCard,
            R.id.itemQiandao, R.id.itemZhifubao,R.id.tvEarningsRecord, R.id.tvIntoBalance
    })
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.itemQiandao:
                CommonIntent.startQiandaoActivity(that);
                break;
            case R.id.tvNotice://消息
                //消息
                CommonIntent.startNoticeMessageActivity(that);
//                CommonIntent.startMessageActivity(that);
                break;
            case R.id.ivSetting://设置
                SettingsActivity.startThis(that);
                break;
            case R.id.tvPersonalData://个人资料
                CommonIntent.startUserInfoActivity(that);
                break;
            case R.id.tvMoneyRecord://资金记录
                MoneyRecordActivity.startThis(that);
                break;
            case R.id.tvTransfer://转账
                CommonIntent.startChatSelectLinkManActivity(that, "转账", true);
                break;
            case R.id.tvWithdrawal://提现
                WithdrawalActivity.startThis(that);
                break;
            case R.id.tvVoucher://充值
                RechargeActivity.startThis(that);
                break;
            case R.id.tvEarningsRecord://我的收益记录
            case R.id.itemRecommend://我的推荐
                if (EmptyDeal.isEmpy(userDataBean)) return;
                if (userDataBean.is_enroller.equals("0")) {
                    FillCodeActivity.startThis(that);
                } else {
                    MyRecommendActvity.startThis(that);
                }
                break;
            case R.id.itemShare://推广海报
                PromotionPosterActivity.startThis(that);
                break;
            case R.id.itemAward://抽奖
                if (EmptyDeal.isEmpy(userDataBean)) return;
                LuckDrawActivity.startThis(that, BaseQuestConfig.LUCKDRAW_URL + userDataBean.id);
                break;
            case R.id.itemService://客服中心
                if (EmptyDeal.isEmpy(userDataBean)) return;
                getSession().put("customer_id_" + userDataBean.customer_id, true);//标记客服
                RongIMHelper.initExtensionModules(false);
                RongIM.getInstance().startConversation(that, Conversation.ConversationType.PRIVATE, userDataBean.customer_id, userDataBean.customer_name);
                break;
            case R.id.llNikeName:
            case R.id.ivUserIcon:
                QrCodeDialog.newInstance().show(getChildFragmentManager(), "QrCodeDialog");
                break;
            case R.id.itemMyBankCard://我的银行卡
                MyBankCardActivity.startThis(that, 0);
                break;
            case R.id.itemZhifubao://我的支付宝
                BindZhifubaoActivity.startThis(that, 0);
                break;
            case R.id.tvIntoBalance://转入钱包
                if (TextUtils.equals(userDataBean.total_commission,"0.00")){
                    return;
                }
                UIUtils.showLoadDialog(that);
                BaseQuestStart.toBalance(this);
                break;
        }
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode) {
            case GET_USER_DATA_CODE:
                if (bean.status == 1) {
                    userDataBean = bean.Data();
                    getSession().put("userBean", userDataBean);
                    updateUi(userDataBean);
                }
                refreshLayout.setRefreshing(false);
                break;
            case POST_COMMISSION_TRANSFER_CODE:
                if (bean.status==1){
                    UIUtils.showLoadDialog(that);
                    initData();
                }else{
                    UIUtils.shortM(bean.msg);
                }
                break;
        }
    }

    private void updateUi(UserDataBean userDataBean) {
        itemRecommend.setRightText(userDataBean.invite_code);
        GlideMediaLoader.load(that, ivUserIcon, userDataBean.avatar, R.drawable.mine_icon_touxiang_normal);
        tvNikeName.setText(userDataBean.nickname);
        tvIdNum.setText(String.format("ID:%s", userDataBean.uid));
        tvBalance.setText(userDataBean.balance);
        tvEarnings.setText(userDataBean.total_commission);
        //保存是否免密支付
        Config.putConfigInfo(that, IS_SECRET_FREE, userDataBean.is_secret_free);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

}
