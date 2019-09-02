package com.cnsunrun.chat.adapters;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.logic.SideBarSortMode;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.base.LBaseAdapter;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.util.selecthelper.SelectableLBaseAdapter;

import java.util.List;

/**
 * @author WQ
 * 联系人-已选列表
 */
public class SelectedTagAdapter extends LBaseAdapter<LinkManInfo,BaseViewHolder> {
    public SelectedTagAdapter() {
        super(R.layout.item_chat_select_linkman_tag);
    }


    @Override
    protected void convert(BaseViewHolder helper, LinkManInfo item) {
        final int layoutPosition = helper.getLayoutPosition();
        GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon),item.avatar);
    }

}