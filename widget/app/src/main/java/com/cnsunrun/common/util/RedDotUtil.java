package com.cnsunrun.common.util;

import android.view.View;
import android.widget.TextView;

import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.quest.Config;
import com.flyco.tablayout.CommonTabLayout;
import com.sunrun.sunrunframwork.http.cache.NetSession;

import io.rong.imkit.RongIM;

/**
 * Created by WQ on 2017/12/16.
 */

public class RedDotUtil {
    static NetSession session = NetSession.instance(CommonApp.getInstance());


    /**
     * 类型 - type
     * 物业
     * 10~19
     * 公告：10
     * 报修：11
     * 投诉建议：12
     * 门禁
     * 20~29
     * 停车
     * 30~39
     * 生活缴费
     * 40~49
     * 咵天
     * 50~59
     * 好友请求：50
     * 动态-点赞：55
     * 动态-评论：56
     * 动态-回复：57
     * 群组-删除：58
     * 约吧
     * 60~69
     * 赶集
     * 70~79
     * 我的钱包
     * 80~89
     */
    //物业
    final static public String CODE_10 = "code10";//红点_公告
    final static public String CODE_11 = "code11";//红点_报修
    final static public String CODE_12 = "code12";//红点_投诉建议

    //咵天
    final static public String CODE_50 = "code50";//红点_好友请求
    final static public String CODE_55 = "code55";//红点_点赞
    final static public String CODE_56 = "code56";//红点_评论
    final static public String CODE_57 = "code57";//红点_回复
    final static public String CODE_IM = "codeIM";//红点_聊天消息
    final static public String CODE_ = "code";//红点_key前缀

    //约吧
    final static public String CODE_60 = "code60";//红点_报名申请

    final static public int TYPE_60 = 60;//报名申请
    final static public int TYPE_10 = 10;//公告
    final static public String[] CODES_DYNAMICS = {CODE_55, CODE_56, CODE_57};//动态

    final static public String[] CODES_PROPERTY = {CODE_10, CODE_11, CODE_12};//物业

    public static String idKey(String id,int type){
        return "redDot_"+id+type+ Config.getLoginInfo().getId();
    }

    public static void addNumber(String... keys) {
        for (String key : keys) {
            session.put(key, session.getInt(key) + 1);
        }
    }

//    public static void addNumber(,String... keys) {
//        for (String key : keys) {
//            session.put(key, session.getInt(key) + 1);
//        }
//    }


    public static void setNumber(int num, String... keys) {
        for (String key : keys) {
            session.put(key, num);
        }
    }

    public static int getToalNumber(String... keys) {
        int total = 0;
        for (String key : keys) {
            int anInt = 0;
            if (CODE_IM.equals(key)) {
                anInt = RongIM.getInstance().getTotalUnreadCount();
                anInt=Math.max(anInt,0)+session.getInt(CODE_IM);//
            } else {
                anInt = session.getInt(key);
            }
            anInt=Math.max(anInt,0);//
            total = total + anInt;
        }
        return total;
    }

    public static void setRedDot(View txt, String... keys) {
        if (txt == null) return;
        int toalNumber = getToalNumber(keys);
        txt.setVisibility(toalNumber == 0 ? View.INVISIBLE : View.VISIBLE);
        if(txt instanceof TextView) {
            ((TextView) txt).setText(String.valueOf(toalNumber));
        }
    }


    public static void setRedDot(CommonTabLayout tabLayout, int position, String... keys) {
        if (tabLayout == null) return;
        int toalNumber = getToalNumber(keys);
        if (toalNumber != 0) {
            tabLayout.showDot(position);
        } else {
            tabLayout.hideMsg(position);
        }
    }
//    public static void setRedIMDot(final CommonTabLayout tabLayout, final int position){
//        if(tabLayout==null)return;
//        int totalUnreadCount = RongIM.getInstance().getTotalUnreadCount();
//        if(totalUnreadCount!=0) {
//            tabLayout.showDot(position);
//        }else {
//            tabLayout.hideMsg(position);
//        }
//    }

}
