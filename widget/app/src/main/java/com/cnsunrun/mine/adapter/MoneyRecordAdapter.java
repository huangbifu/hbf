package com.cnsunrun.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.common.util.FontUtils;
import com.cnsunrun.mine.mode.WalletBean;

/**
 * Created by wangchao on 2018-09-26.
 */
public class MoneyRecordAdapter extends BaseQuickAdapter<WalletBean.WalletInfoBean, BaseViewHolder> {
    public MoneyRecordAdapter() {
        super(R.layout.item_money_record_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, WalletBean.WalletInfoBean item) {
        helper.setText(R.id.tvTime, FontUtils.setChangeLine(item.add_time))
                .setText(R.id.tvMoney, item.money)
                .setText(R.id.tvStates, item.pay_title);
        helper.setTextColor(R.id.tvTime, mContext.getResources().getColor(R.color.text_color_333))
                .setTextColor(R.id.tvMoney, mContext.getResources().getColor(R.color.text_color_333))
                .setTextColor(R.id.tvStates, mContext.getResources().getColor(R.color.text_color_333));
        if (helper.getLayoutPosition()%2==0){
            helper.setBackgroundColor(R.id.llContainer,mContext.getResources().getColor(R.color.fafafa));
        }else{
            helper.setBackgroundColor(R.id.llContainer,mContext.getResources().getColor(R.color.white));
        }

    }
}
