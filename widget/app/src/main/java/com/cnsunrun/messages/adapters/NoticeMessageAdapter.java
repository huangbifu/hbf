package com.cnsunrun.messages.adapters;

import android.support.annotation.Nullable;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.common.util.RedDotUtil;
import com.cnsunrun.messages.mode.NoticeListBean;
import com.sunrun.sunrunframwork.http.cache.NetSession;

import java.util.List;

import static com.cnsunrun.common.util.RedDotUtil.TYPE_10;
import static com.cnsunrun.common.util.RedDotUtil.idKey;

/**
 * Created by : Z_B on 2017/10/20.
 * Effect : 我的消息的adapter
 */

public class NoticeMessageAdapter extends BaseQuickAdapter<NoticeListBean, BaseViewHolder> {
    NetSession session;
    public NoticeMessageAdapter(NetSession netSession, @Nullable List<NoticeListBean> data) {
        super(R.layout.item_notice_message, data);
        this.session=netSession;
    }

    @Override
    protected void convert(BaseViewHolder helper, NoticeListBean item) {
//        int toalNumber = RedDotUtil.getToalNumber(idKey(item.id, TYPE_10));
//        boolean isNoRead = toalNumber!=0;//||helper.getLayoutPosition()<10&&(!session.hasValue(item.id));
        helper.setText(R.id.tv_message_title, item.title);
        helper.setText(R.id.tv_message_time, item.add_time);
        helper.getView(R.id.imgRedDot).setVisibility("1".equals(item.is_read)? View.INVISIBLE: View.VISIBLE);
    }
}
