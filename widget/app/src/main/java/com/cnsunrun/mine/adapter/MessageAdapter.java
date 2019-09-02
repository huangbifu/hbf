package com.cnsunrun.mine.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.mine.mode.MessageInfo;

/**
 *
 * 消息适配器
 */

public class MessageAdapter extends BaseQuickAdapter<MessageInfo,BaseViewHolder> {
    private Context context;

    public MessageAdapter(Context context, @LayoutRes int layoutResId) {
        super(layoutResId);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, final MessageInfo item) {

    }
}
