package com.cnsunrun.common.config;

import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;

import org.greenrobot.eventbus.EventBus;

import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Message;

import static com.cnsunrun.chat.RedpackPageActivity.DEBUGGGG;
import static com.cnsunrun.common.config.Const.REDPACK_GETED;


@ProviderTag(messageContent = RedPackMessage.class)
public class RedpackMessageItemProvider extends IContainerItemProvider.MessageProvider<RedPackMessage> {

    class ViewHolder {
        ImageView imgIcon;
        View rcLayout,layBg;
        TextView tvTitle;
    }

    @Override
    public View newView(Context context, ViewGroup group) {
        View view = group.findViewById(R.id.rc_layout);
        if(view==null) {
             view = LayoutInflater.from(context).inflate(R.layout.item_rong_redpack_message, null);
            ViewHolder holder = new ViewHolder();
            holder.imgIcon = (ImageView) view.findViewById(R.id.imgIcon);
            holder.tvTitle = (TextView) view.findViewById(R.id.tvTitle);
            holder.rcLayout = view.findViewById(R.id.rc_layout);
            holder.layBg = view.findViewById(R.id.layBg);

            view.setTag(holder);
        }
        return view;
    }

    @Override
    public void bindView(View v, int position, RedPackMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();
        holder.layBg.setAlpha(1f);
        holder.tvTitle.setText(content.getTitle());
//        holder.imgIcon.setImageResource(R.drawable.ic_chats_redp_icon_full_3x);
        String extra = message.getMessage().getExtra();
        boolean isRead=String.valueOf(extra).startsWith(Const.REDPACK_GETED);
        if(isRead){//已领取开头 的
//            holder.layBg.setAlpha(0.5f);
//            holder.imgIcon.setImageResource(R.drawable.ic_chats_redp_icon_empty_3x);
        }
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {//消息方向，自己发送的
            if(isRead) {
                holder.layBg.setBackgroundResource(R.drawable.ic_red_pack_right_read);
            }else {
                holder.layBg.setBackgroundResource(R.drawable.ic_red_pack_right);
            }
        } else {
            if(isRead) {
                holder.layBg.setBackgroundResource(R.drawable.ic_red_pack_left_read);
            }else {
                holder.layBg.setBackgroundResource(R.drawable.ic_red_pack_left);
            }
        }

//
    }

    @Override
    public Spannable getContentSummary(RedPackMessage data) {
        return new SpannableString("[红包]");
    }

    @Override
    public void onItemClick(View view, int position, RedPackMessage content, UIMessage message) {

//        RedPackMessage content = (RedPackMessage) message.getContent();
        EventBus.getDefault().post(message);
    }

    @Override
    public void onItemLongClick(View view, int position, RedPackMessage content, UIMessage message) {

    }

}