package com.cnsunrun.common.config;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.ConversationActivity;
import com.cnsunrun.common.CommonIntent;
import com.sunrun.sunrunframwork.weight.SpanTextView;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.message.InformationNotificationMessage;

/**
 * Created by weiquan on 2018/9/27.
 */
@ProviderTag(messageContent = RedPackReceivedTipMessage.class,
showPortrait = false,
        centerInHorizontal = true,
        showSummaryWithName = false
)
public class RedPackReceivedTipMessageItemProvider extends  IContainerItemProvider.MessageProvider<RedPackReceivedTipMessage> {
    class ViewHolder {
        TextView tvTitle;
    }

    @Override
    public void bindView(View view, int i, final RedPackReceivedTipMessage redPackReceivedTipMessage, final UIMessage uiMessage) {
        ViewHolder holder = (ViewHolder) view.getTag();
        SpanTextView.SpanEditable spanEditable=new SpanTextView.SpanEditable(String.valueOf(redPackReceivedTipMessage.getTip_content()));
        spanEditable.setSpan("红包", new SpanTextView.NoLineClickSpan(Color.parseColor("#fe481e")) {
            @Override
            public void click(View view) {
//                if(redPackReceivedTipMessage.getType()==1) {
                    ConversationActivity.isOtherPage=true;
                  CommonIntent.startRedpackPageActivity(view.getContext(),redPackReceivedTipMessage.getRepack_id(),uiMessage.getTargetId(),redPackReceivedTipMessage.getType()+"");
//                }
            }
        });
        holder.tvTitle.setText(spanEditable.commit());
    }

    @Override
    public Spannable getContentSummary(RedPackReceivedTipMessage redPackReceivedTipMessage) {
        return new SpannableString(redPackReceivedTipMessage.getTip_content());
    }

    @Override
    public void onItemClick(View view, int i, RedPackReceivedTipMessage redPackReceivedTipMessage, UIMessage uiMessage) {

    }

    @Override
    public View newView(Context context, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_rc_redpack_received_tip_message, null);
        ViewHolder holder = new ViewHolder();
        holder.tvTitle = (TextView) view.findViewById(R.id.tvContent);
        view.setTag(holder);
        return view;
    }
}
