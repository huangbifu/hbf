package com.cnsunrun.mine.adapter;

import android.graphics.Color;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.mine.mode.QiandaoInfo;
import com.cnsunrun.mine.mode.QiandaoInfo.SginListBean;
import com.cnsunrun.mine.mode.QiandaoRecord;

/**
 * 连续签到情况
 */
public class QiandaoDateAdapter extends BaseQuickAdapter<SginListBean, BaseViewHolder> {
    public QiandaoDateAdapter() {
        super(R.layout.item_qiandao_lianxu);
    }

    @Override
    protected void convert(BaseViewHolder helper, SginListBean item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.tvDate, item.date)
                .setText(R.id.tvVal,"+"+ item.reward_money)
        .setVisible(R.id.tvVal,item.is_sign!=1)
        .setVisible(R.id.imgComplete,item.is_sign==1)
        .setVisible(R.id.layLine,layoutPosition!=getItemCount()-1)
        ;
        helper.setBackgroundRes(R.id.layDateBg,item.is_sign==1?R.drawable.shap_bg_cir_white:R.drawable.shap_bg_cir_black);
        helper.setBackgroundColor(R.id.layLine,item.is_sign==1? Color.parseColor("#ffb2a1"):Color.parseColor("#c23110"));
    }
}
