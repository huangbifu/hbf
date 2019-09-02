package com.cnsunrun.chat.adapters;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.mode.GroupItemBean;
import com.cnsunrun.chat.mode.RedPackRecord;

/**
 * 扫雷红包游戏组列表
 */

public class ChatGameGroupAdapter extends BaseQuickAdapter<GroupItemBean, BaseViewHolder> {
    public ChatGameGroupAdapter( ) {
        super(R.layout.item_game_group);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupItemBean item) {
        int layoutPosition = helper.getLayoutPosition();
        helper.setText(R.id.txtGroupName,item.title);
    }
}