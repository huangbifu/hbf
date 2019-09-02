package com.cnsunrun.chat.adapters;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.logic.SideBarSortMode;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.chat.mode.RedPackInfoBean;
import com.cnsunrun.common.boxing.GlideMediaLoader;

/**
 * 红包
 */

public class ChatRedpackInfoAdapter extends BaseQuickAdapter<RedPackInfoBean.ListBean, BaseViewHolder> {
    public ChatRedpackInfoAdapter( ) {
        super(R.layout.item_redpack_list_info);
    }

    @Override
    protected void convert(BaseViewHolder helper, RedPackInfoBean.ListBean item) {
        int layoutPosition = helper.getLayoutPosition();
        GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon),item.avatar);
        helper.setText(R.id.txtUserName,item.nickname);
        helper.setText(R.id.txtDate,item.update_time);
        helper.setText(R.id.txtMoney, String.format("%s元", item.money));
        helper.setVisible(R.id.imgBoom,"1".equals(item.is_thunder));
    }
}