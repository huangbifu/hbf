package com.cnsunrun.chat.adapters;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.mode.FriendRequestBean;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import java.util.List;

/**
 * Created by cnsunrun on 2017/11/2.
 * 咵天-新朋友
 */

public class ChatNewFriendsAdapter extends BaseQuickAdapter<FriendRequestBean, BaseViewHolder> {
    public ChatNewFriendsAdapter(@Nullable List<FriendRequestBean> data) {
        super(R.layout.item_chat_newfriends, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, FriendRequestBean item) {
        int layoutPosition = helper.getLayoutPosition();
        GlideMediaLoader.load(mContext, helper.getView(R.id.imgUserIcon), item.avatar);
        helper.setText(R.id.txtUserName, item.fnickname);
        helper.setText(R.id.txtInfo, item.message);
        helper.setText(R.id.txtAgreed, item.status_title);
        helper.setVisible(R.id.txtInfo, !EmptyDeal.isEmpy(item.message));
        helper.setVisible(R.id.btnReject,item.status==0);
        helper.setVisible(R.id.btnAgree,item.status==0);
        helper.setVisible(R.id.txtAgreed,item.status!=0);
        helper.addOnClickListener(R.id.btnReject);
        helper.addOnClickListener(R.id.btnAgree);
    }
}