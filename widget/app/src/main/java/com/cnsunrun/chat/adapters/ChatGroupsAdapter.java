package com.cnsunrun.chat.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.mode.GroupItemBean;
import com.cnsunrun.common.boxing.GlideMediaLoader;

import java.util.List;

/**
 * Created by cnsunrun on 2017/11/2.
 * 咵天-群聊
 */

public class ChatGroupsAdapter extends BaseQuickAdapter<GroupItemBean, BaseViewHolder> {
    public ChatGroupsAdapter(@Nullable List<GroupItemBean> data) {
        super(R.layout.item_chat_groups, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, GroupItemBean item) {
        int layoutPosition = helper.getLayoutPosition();
        GlideMediaLoader.load(mContext,helper.getView(R.id.imgGroupIcon),item.image);
        helper.setText(R.id.txtGroupName,item.title);
    }
}