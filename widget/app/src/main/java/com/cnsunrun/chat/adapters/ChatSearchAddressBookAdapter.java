package com.cnsunrun.chat.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.boxing.GlideMediaLoader;

import java.util.List;

/**
 * Created by cnsunrun on 2017/11/2.
 * 咵天-联系人搜索
 */

public class ChatSearchAddressBookAdapter extends BaseQuickAdapter<LinkManInfo, BaseViewHolder> {
    public ChatSearchAddressBookAdapter(@Nullable List<LinkManInfo> data) {
        super(R.layout.item_chat_search_address_book, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkManInfo item) {
        int layoutPosition = helper.getLayoutPosition();
        GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon),item.avatar);
        helper.setText(R.id.txtUserName,item.getNickname());
    }
}