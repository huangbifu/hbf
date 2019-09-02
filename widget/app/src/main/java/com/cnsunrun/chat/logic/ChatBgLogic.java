package com.cnsunrun.chat.logic;

import android.widget.ImageView;

import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.uibase.BaseActivity;

/**
 * Created by WQ on 2017/12/7.
 */

public class ChatBgLogic {
    public static void loadChatBg(BaseActivity that, ImageView imgChatBg,String targetId,boolean isGroup){
        String bgImage;
        if(isGroup){
            bgImage=that.getSession().getString("groupBg"+targetId);
        }else {
            bgImage=that.getSession().getString("chatBg"+targetId);
        }
        GlideMediaLoader.load(that,imgChatBg,bgImage);
    }

    public static void saveChatBg(BaseActivity that, String path,String targetId,boolean isGroup){
        that.getSession().put((isGroup?"groupBg":"chatBg")+targetId,path);
    }
}
