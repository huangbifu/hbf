package com.cnsunrun.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.mine.mode.BankCradBean;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

/**
 * Created by wangchao on 2018-12-19.
 */
public class AccountAdapter extends BaseQuickAdapter<BankCradBean, BaseViewHolder> {
    private final int type;

    public AccountAdapter(int type) {
        super(R.layout.item_my_bank_card_new);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, BankCradBean item) {
        helper.setText(R.id.tvCardName, item.bank_adress)
                .setText(R.id.tvCardNum, item.account)
                .setText(R.id.tvCardType, item.type)
                .addOnClickListener(R.id.btnUnBind)
                .addOnClickListener(R.id.llContainer);
        SwipeMenuLayout swipeMenuLayout = helper.getView(R.id.swipMenuLayout);
        if (type == 1) {
            swipeMenuLayout.setSwipeEnable(false);
            swipeMenuLayout.setIos(false);
            swipeMenuLayout.setLeftSwipe(false);
        } else {
            swipeMenuLayout.setSwipeEnable(true);
            swipeMenuLayout.setIos(true);
            swipeMenuLayout.setLeftSwipe(true);
        }
    }
}
