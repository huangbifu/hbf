package com.cnsunrun.chat.logic;

import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.mode.RedPackInfoBean;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.weight.SpanTextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by weiquan on 2018/9/28.
 */
public class RedPageHeadLogic {
    @BindView(R.id.imgBgBar)
    ImageView imgBgBar;
    @BindView(R.id.civUserPhoto)
    CircleImageView civUserPhoto;
    @BindView(R.id.tvFormUser)
    TextView tvFormUser;
    @BindView(R.id.tvDate)
    TextView tvDate;
    @BindView(R.id.tvMoney)
    TextView tvMoney;
    @BindView(R.id.layMoney)
    LinearLayout layMoney;
    @BindView(R.id.tvPackNum)
    TextView tvPackNum;
    @BindView(R.id.layNumber)
    LinearLayout layNumber;
    @BindView(R.id.layRedPackMoney)
    View layRedPackMoney;
    public RedPageHeadLogic(View headView) {
        ButterKnife.bind(this,headView);
    }

    public void setPageData(RedPackInfoBean redPackInfoBean){
        GlideMediaLoader.load(civUserPhoto.getContext(),civUserPhoto,redPackInfoBean.avatar);
        tvFormUser.setText(String.format("来自 %s 的红包",redPackInfoBean.nickname));
        tvDate.setText(redPackInfoBean.title);
        if(EmptyDeal.isEmpy(redPackInfoBean.grab_money)){
            layRedPackMoney.setVisibility(View.GONE);
        }
        tvMoney.setText(redPackInfoBean.grab_money);
        String redpackInfo = String.format("%s个红包共%s元", redPackInfoBean.num, redPackInfoBean.total_money);
        SpanTextView.SpanEditable spanEditable=new SpanTextView.SpanEditable(redpackInfo);
        spanEditable.setColorSpanAll(String.valueOf(redPackInfoBean.total_money), Color.parseColor("#cb3424"));
        tvPackNum.setText(redpackInfo);
    }
}
