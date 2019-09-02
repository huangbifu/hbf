package com.cnsunrun.chat.dialog;

import android.content.DialogInterface;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.ConversationActivity;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.cnsunrun.common.event.DefaultEvent;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.utils.JsonDeal;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imkit.model.UIMessage;
import io.rong.imlib.RongIMClient;

import static com.cnsunrun.common.config.Const.EVENT_REFRESH_CONVERSATION;
import static com.cnsunrun.common.config.Const.REDPACK_GETED;

/**
 * 红包
 * Created by WQ on 2017/10/30.
 */

public class RedpacketDialog extends LBaseDialogFragment {


    @BindView(R.id.tvOver)
    TextView tvOver;
    @BindView(R.id.tvShowDetails)
    TextView tvShowDetails;

    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.tvShowAll)
    TextView tvShowAll;
    @BindView(R.id.laySuccess)
    LinearLayout laySuccess;
    @BindView(R.id.layTip)
    View layTip;

    String id,type;
    private UIMessage rcMessage;
    private BaseBean bean;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        UIUtils.showLoadDialog(that,"领取中..");
//        BaseQuestStart.grab_redpack(this, id);
        if (bean.status == 10 || bean.status == 11 || bean.status == 13 || bean.status == 0) {
            layTip.setVisibility(View.VISIBLE);
            tvOver.setText(bean.msg);
            if(bean.status==11){
                tvShowDetails.setVisibility(View.VISIBLE);
            }
        } else if (bean.status == 12) {
            tvShowAll.performClick();
        } else {
            if (bean.status == 1) {
//                        "money": "1.32",   红包金额
//                        "redpack_id": "25"   红包id
                laySuccess.setVisibility(View.VISIBLE);
                JSONObject jobj = JsonDeal.createJsonObj(bean.Data().toString());
                tvMoney.setText(jobj.optString("money"));

                rcMessage.setExtra(REDPACK_GETED + bean.status);
                RongIM.getInstance().setMessageExtra(rcMessage.getMessageId(), REDPACK_GETED + bean.status, new RongIMClient.ResultCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean aBoolean) {
                        EventBus.getDefault().post(DefaultEvent.createEvent(EVENT_REFRESH_CONVERSATION, ""));
                    }

                    @Override
                    public void onError(RongIMClient.ErrorCode errorCode) {

                    }
                });
            }
        }
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissAllowingStateLoss();
            }
        });
    }

    public RedpacketDialog setId(String id) {
        this.id = id;
        return this;
    }

    public RedpacketDialog setType(String type) {
        this.type = type;
        return this;
    }

    public RedpacketDialog setRcMessage(UIMessage rcMessage) {
        this.rcMessage = rcMessage;
        return this;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_redpacket;
    }


    public static RedpacketDialog newInstance() {
        Bundle args = new Bundle();
        RedpacketDialog fragment = new RedpacketDialog();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
    }


    @OnClick(R.id.tvShowAll)
    public void onViewClicked() {
        ConversationActivity.isOtherPage = true;
        CommonIntent.startRedpackPageActivity(that, id,rcMessage.getTargetId(),type);
        dismissAllowingStateLoss();
    }

    @OnClick(R.id.tvShowDetails)
    public void tvShowDetails() {
        ConversationActivity.isOtherPage = true;
        CommonIntent.startRedpackPageActivity(that, id,rcMessage.getTargetId(),type);
        dismissAllowingStateLoss();
    }


    public RedpacketDialog setStates(BaseBean baseBean) {
        this.bean = baseBean;
        return this;

    }
}
