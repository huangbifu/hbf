package com.cnsunrun.chat.adapters;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;

import java.util.List;

/**
 */

public class ChatPageIndicatorAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    int selectIndex=-1;
    public ChatPageIndicatorAdapter(@Nullable List data) {
        super(R.layout.item_page_indicator, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        int colorRes=helper.getLayoutPosition()==selectIndex?R.color.color_fe481e:R.color.text5;
        helper.setImageDrawable(R.id.imgIndicator,new ColorDrawable(mContext.getResources().getColor(colorRes)));
    }

    public void setSelectIndex(int selectIndex) {
        this.selectIndex = selectIndex;
        notifyDataSetChanged();
    }
}