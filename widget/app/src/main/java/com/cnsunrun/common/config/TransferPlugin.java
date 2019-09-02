//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cnsunrun.common.config;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;

import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.mine.activity.TransferActivity;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import io.rong.imkit.RongExtension;
import io.rong.imkit.R.drawable;
import io.rong.imkit.R.string;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imkit.plugin.image.PictureSelectorActivity;
import io.rong.imkit.utilities.PermissionCheckUtil;
import io.rong.imlib.model.Conversation.ConversationType;

public class TransferPlugin implements IPluginModule {
    ConversationType conversationType;
    String targetId;

    public TransferPlugin() {
    }

    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, drawable.rc_ext_plugin_transfer);
    }

    public String obtainTitle(Context context) {
        return "转账";
    }

    public void onClick(Fragment currentFragment, RongExtension extension) {
//        extension.startActivityForPluginResult(intent, 23, this);
//        CommonIntent.startChatSelectLinkManActivity(currentFragment.getActivity(), "转账", true);
        String targetId = extension.getTargetId();

        LinkManInfo linkManInfo = NetSession.instance(currentFragment.getContext()).getObject("chatinfo" + targetId, LinkManInfo.class);
        if(linkManInfo==null){
            UIUtils.shortM("数据获取失败,请稍后重试");
            return;
        }
        linkManInfo.id_num=linkManInfo.id;
        TransferActivity.startThis(currentFragment.getActivity(),linkManInfo);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
    }
}
