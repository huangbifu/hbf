package com.cnsunrun.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.mine.mode.LevelBean;

/**
 * Created by wangchao on 2018-09-26.
 */
public class LevelAdapter extends BaseQuickAdapter<LevelBean.Info, BaseViewHolder> {
    public LevelAdapter() {
        super(R.layout.item_level_layout);
    }

    @Override
    protected void convert(BaseViewHolder helper, LevelBean.Info item) {
        helper.addOnClickListener(R.id.rlContainer);
        helper.setText(R.id.tvNikeName, item.nickname)
                .setText(R.id.tvTime, item.num);
        GlideMediaLoader.load(mContext, helper.getView(R.id.riv), item.avatar, R.drawable.mine_icon_touxiang_normal);

    }
}
