package com.cnsunrun.chat.adapters;


import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.logic.SideBarSortMode;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.boxing.GlideMediaLoader;

/**
 * Created by cnsunrun on 2017/11/2.
 * 咵天-通讯录
 */

public class ChatAddressBookAdapter extends BaseQuickAdapter<LinkManInfo, BaseViewHolder> {
    private SideBarSortMode sideBarSortMode;
    public ChatAddressBookAdapter(SideBarSortMode sideBarSortMode) {
        super(R.layout.item_chat_address_book);
        this.sideBarSortMode=sideBarSortMode;
    }

    @Override
    protected void convert(BaseViewHolder helper, LinkManInfo item) {
        int layoutPosition = helper.getLayoutPosition();
        String sortLetterTitle = getSortLetterTitle(layoutPosition);
        helper.setText(R.id.txtGroupTitle,sortLetterTitle);
        helper.setText(R.id.txtUserName,item.toString());
        helper.setVisible(R.id.txtGroupTitle,sortLetterTitle!=null);
        GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon),item.avatar);
        helper.setText(R.id.txtUserName,item.getNickname());
    }
    private String getSortLetterTitle(int layoutPosition) {
        if(sideBarSortMode==null)return null;
        return sideBarSortMode.getSortLetterTitle(layoutPosition);
    }
}