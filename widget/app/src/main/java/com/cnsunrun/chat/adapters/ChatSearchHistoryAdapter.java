package com.cnsunrun.chat.adapters;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.config.IMUserInfoProvider;
import com.cnsunrun.common.util.IMChatRecordUtils;
import com.cnsunrun.common.util.TestTool;
import com.sunrun.sunrunframwork.http.utils.DateUtil;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.SearchConversationResult;
import io.rong.imlib.model.UserInfo;
import io.rong.message.RichContentMessage;
import io.rong.message.TextMessage;

/**
 * Created by cnsunrun on 2017/11/2.
 * 咵天-聊天记录搜索
 */

public class ChatSearchHistoryAdapter extends BaseQuickAdapter<Object, BaseViewHolder> {
  public   String keywords="";
    public ChatSearchHistoryAdapter(@Nullable List<Object> data) {
        super(R.layout.item_search_history, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, Object item) {
        int layoutPosition = helper.getLayoutPosition();
//        Conversation conversation = item.getConversation();
        if(item instanceof Message) {
            dealMessage(helper, (Message) item);
        }else if(item instanceof SearchConversationResult){
            dealConversationResult((SearchConversationResult) item);
        }


    }

    private void dealConversationResult(SearchConversationResult item) {

    }

    private void dealMessage(BaseViewHolder helper, Message item) {
        MessageContent content = item.getContent();
        String senderUserId = item.getSenderUserId();
        UserInfo userInfo = IMUserInfoProvider.getInstance().getUserInfo(senderUserId);
        if(userInfo!=null){
            helper.setText(R.id.txtUserName, userInfo.getName());
            GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon),String.valueOf(userInfo.getPortraitUri()));
        }
        long sentTime = item.getSentTime();
        helper.setText(R.id.txtTime, DateUtil.getStringByFormat(sentTime,"yyyy/MM/dd"));
        SpannableStringBuilder coloredChattingRecord = IMChatRecordUtils.getColoredChattingRecord(keywords, content);
        helper.setText(R.id.txtContent,coloredChattingRecord);
    }
}