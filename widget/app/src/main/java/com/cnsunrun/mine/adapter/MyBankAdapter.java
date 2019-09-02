package com.cnsunrun.mine.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.mine.mode.BankCradBean;
import com.mcxtzhang.swipemenulib.SwipeMenuLayout;

/**
 * Created by wangchao on 2018-10-08.
 */
public class MyBankAdapter extends BaseQuickAdapter<BankCradBean, BaseViewHolder> {
    private int type;

    public MyBankAdapter(int type) {
        super(R.layout.item_my_bank_card);
        this.type = type;
    }

    @Override
    protected void convert(BaseViewHolder helper, BankCradBean item) {
        helper.setText(R.id.tvCardName, item.bank_adress)
                .setText(R.id.tvCardNum, item.account.substring(item.account.length() - 4))
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
