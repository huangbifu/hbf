package com.cnsunrun.chat.adapters;

import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseAdapter;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.config.IMUserInfoProvider;
import com.cnsunrun.common.util.IMChatRecordUtils;
import com.sunrun.sunrunframwork.http.utils.DateUtil;
import com.sunrun.sunrunframwork.uiutils.DisplayUtil;
import com.sunrun.sunrunframwork.uiutils.LayoutUtil;
import com.sunrun.sunrunframwork.uiutils.ScreenUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import java.util.List;

import io.rong.imkit.utils.RongDateUtils;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.cnsunrun.common.config.RongCloudEvent.getMessageDesc;
import static com.cnsunrun.common.quest.BaseQuestConfig.TEST_IMAGE;

/**
 * 聊天搜索结果
 * Created by WQ on 2018/1/24.
 */

public class SearchChatResultAdapter extends LBaseAdapter<Conversation, BaseViewHolder> {

    public String keywords = "";

    public SearchChatResultAdapter(@Nullable List data) {
        super(R.layout.item_search_chat_result, data);

    }

    @Override
    protected void convert(BaseViewHolder helper, Conversation item) {
//        GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon),TEST_IMAGE);
//        String senderUserId = item.getSenderUserId();
        String headUrl = item.getPortraitUrl();
        String userName = item.getConversationTitle();
        if (EmptyDeal.isEmpy(headUrl)) {//从缓存里边读取
            UserInfo userInfo = IMUserInfoProvider.getInstance().getUserInfo(item.getTargetId());
            headUrl = String.valueOf(userInfo.getPortraitUri());
            userName = userInfo.getName();
        }
//        if(userInfo!=null)
        {
            helper.setText(R.id.txtUserName, userName);
            GlideMediaLoader.load(mContext, helper.getView(R.id.imgUserIcon), headUrl);
            helper.setText(R.id.txtContent, getMessageDesc(item.getLatestMessage()));
        }
        long sentTime = item.getSentTime();

        helper.setText(R.id.txtAddtime, RongDateUtils.getConversationListFormatDate(sentTime, mContext));
    }
}
