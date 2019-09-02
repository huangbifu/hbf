package com.cnsunrun.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.mine.mode.RechargeInfo;

/**
 * Created by wangchao on 2018-11-30.
 */
public class OffLineAdapter extends BaseQuickAdapter<RechargeInfo.PayListBean, BaseViewHolder> {
    public OffLineAdapter() {
        super(R.layout.item_off_line);
    }

    @Override
    protected void convert(BaseViewHolder helper, RechargeInfo.PayListBean item) {
        helper.setText(R.id.tvTitle, item.title).addOnClickListener(R.id.llContainer);
        if (item.type == 1) {
            helper.setBackgroundRes(R.id.ivIcon, R.drawable.chongzhi_icon_zhifubao_normal);
        } else {
            helper.setBackgroundRes(R.id.ivIcon, R.drawable.chongzhi_icon_yinlian_normal);
        }
    }
}
