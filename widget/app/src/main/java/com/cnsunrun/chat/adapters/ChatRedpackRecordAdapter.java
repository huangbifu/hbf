package com.cnsunrun.chat.adapters;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.mode.RedPackInfoBean;
import com.cnsunrun.chat.mode.RedPackRecord;

/**
 * 红包记录
 */

public class ChatRedpackRecordAdapter extends BaseQuickAdapter<RedPackRecord.ListBean, BaseViewHolder> {
    public ChatRedpackRecordAdapter( ) {
        super(R.layout.item_redpack_record);
    }

    @Override
    protected void convert(BaseViewHolder helper, RedPackRecord.ListBean item) {
        int layoutPosition = helper.getLayoutPosition();
//        GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon),item.);
        helper.setText(R.id.txtUserName,item.from_nickname);
        helper.setText(R.id.txtDate,item.update_time);
        helper.setText(R.id.txtMoney, String.format("%s元", item.money));
    }
}