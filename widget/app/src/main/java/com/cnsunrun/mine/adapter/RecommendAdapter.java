package com.cnsunrun.mine.adapter;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseAdapter;
import com.cnsunrun.common.util.FontUtils;
import com.cnsunrun.mine.mode.CommissionBean;

/**
 * Created by wangchao on 2018-09-25.
 */
public class RecommendAdapter extends LBaseAdapter<CommissionBean.InfoBean, BaseViewHolder> {
    public RecommendAdapter() {
        super(R.layout.item_recommend_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, CommissionBean.InfoBean item) {
        helper.setText(R.id.tvNikeName, item.from_nickname)
                .setText(R.id.tvMoney, item.total_money)
                .setText(R.id.tvlevel, item.level)
                .setText(R.id.tvTime, FontUtils.setChangeLine(item.add_time));
        if (helper.getLayoutPosition() % 2 == 0) {
            helper.setBackgroundColor(R.id.llContainer, mContext.getResources().getColor(R.color.fafafa));
        } else {
            helper.setBackgroundColor(R.id.llContainer, mContext.getResources().getColor(R.color.white));
        }
    }
}
