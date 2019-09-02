package com.cnsunrun.chat.adapters;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.boxing.GlideMediaLoader;

/**
 * Created by wangchao on 2018-11-12.
 */
public class GroupMemberAdapter extends BaseQuickAdapter<LinkManInfo, BaseViewHolder> {
    public GroupMemberAdapter() {
        super(R.layout.item_chat_userinfo_tag_new);
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkManInfo item) {
        GlideMediaLoader.load(mContext, helper.getView(R.id.imgUserIcon), item.avatar);
        helper.setText(R.id.txtUserName, item.nickname);
    }
}
