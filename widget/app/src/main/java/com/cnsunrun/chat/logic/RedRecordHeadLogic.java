package com.cnsunrun.chat.logic;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.mode.RedPackRecord;
import com.cnsunrun.common.boxing.GlideMediaLoader;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 红包记录,头部数据
 * Created by weiquan on 2018/9/28.
 */
public class RedRecordHeadLogic {


    @BindView(R.id.imgBgBar)
    ImageView imgBgBar;
    @BindView(R.id.imgUserIcon)
    CircleImageView imgUserIcon;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtMoney)
    TextView txtMoney;

    public RedRecordHeadLogic(View headView) {
        ButterKnife.bind(this, headView);
    }

    public void setPageData(RedPackRecord redPackRecord) {
        GlideMediaLoader.load(imgUserIcon.getContext(), imgUserIcon, redPackRecord.avatar);
        txtUserName.setText(String.format("%s共收到%s个红包,共计", redPackRecord.nickname, redPackRecord.total_num));
        txtMoney.setText(redPackRecord.total_money);
    }
}
