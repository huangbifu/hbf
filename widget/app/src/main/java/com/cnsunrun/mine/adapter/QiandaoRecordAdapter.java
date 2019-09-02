package com.cnsunrun.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.common.util.FontUtils;
import com.cnsunrun.mine.mode.QiandaoRecord;
import com.cnsunrun.mine.mode.WalletBean;

/**
 * 签到记录
 */

public class QiandaoRecordAdapter extends BaseQuickAdapter<QiandaoRecord, BaseViewHolder> {
    public QiandaoRecordAdapter() {
        super(R.layout.item_qiandao_records);
    }

    @Override
    protected void convert(BaseViewHolder helper, QiandaoRecord item) {
        helper.setText(R.id.tvTime, item.add_time)
                .setText(R.id.tvTitle, String.format("+%s元",item.total_money))
        ;

    }
}
