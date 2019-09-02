package com.cnsunrun.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.cnsunrun.chat.ChatAddRemarkActivity;
import com.cnsunrun.chat.ChatAddUserActivity;
import com.cnsunrun.chat.ChatGroupListActivity;
import com.cnsunrun.chat.ChatGroupNoticeDetailsActivity;
import com.cnsunrun.chat.ChatGroupPeoplesActivity;
import com.cnsunrun.chat.ChatGroupSetInfoActivity;
import com.cnsunrun.chat.ChatModifyGroupCardActivity;
import com.cnsunrun.chat.ChatModifyGroupNameActivity;
import com.cnsunrun.chat.ChatModifyGroupNoticeActivity;
import com.cnsunrun.chat.ChatNewFriendsActivity;
import com.cnsunrun.chat.ChatSearchFriendsActivity;
import com.cnsunrun.chat.ChatSearchHistoryActivity;
import com.cnsunrun.chat.ChatSelectLinkManActivity;
import com.cnsunrun.chat.ChatSetInfoActivity;
import com.cnsunrun.chat.ChatUserInfoActivity;
import com.cnsunrun.chat.GameGroupListActivity;
import com.cnsunrun.chat.GameTestActivity;
import com.cnsunrun.chat.JielongGameGroupListActivity;
import com.cnsunrun.chat.RedpackPageActivity;
import com.cnsunrun.chat.RedpackRecordActivity;
import com.cnsunrun.chat.ScanQRActivity;
import com.cnsunrun.chat.SendJielongRedpackActivity;
import com.cnsunrun.chat.SendNineRedpackActivity;
import com.cnsunrun.chat.SendRedpackActivity;
import com.cnsunrun.chat.WebDetailsActivity;
import com.cnsunrun.chat.mode.GroupInfoBean;
import com.cnsunrun.login.ForgetPasswordActivity;
import com.cnsunrun.login.LoginActivity;
import com.cnsunrun.login.RegistActivity;
import com.cnsunrun.login.SmsLoginActivity;
import com.cnsunrun.messages.NoticeMessageActivity;
import com.cnsunrun.messages.NoticeMessageDetailsActivity;
import com.cnsunrun.mine.activity.AccountSafeActivity;
import com.cnsunrun.mine.activity.AvoidCloseActivity;
import com.cnsunrun.mine.activity.ChangePhoneActivity;
import com.cnsunrun.mine.activity.MessageActivity;
import com.cnsunrun.mine.activity.MessageNoticeActivity;
import com.cnsunrun.mine.activity.ModifyPayPasswordActivity;
import com.cnsunrun.mine.activity.ModifyPwdActivity;
import com.cnsunrun.mine.activity.QIandaoRecordActivity;
import com.cnsunrun.mine.activity.QiandaoActivity;
import com.cnsunrun.mine.activity.RechargeOffLineActivity;
import com.cnsunrun.mine.activity.SettingActivity;
import com.cnsunrun.mine.activity.SettingPayPasswordActivity;
import com.cnsunrun.mine.activity.UpdateInfoActivity;
import com.cnsunrun.mine.activity.UserInfoActivity;
import com.sunrun.sunrunframwork.app.BaseApplication;
import com.sunrun.sunrunframwork.uibase.BaseActivity;
import com.sunrun.sunrunframwork.utils.BaseStartIntent;

import java.util.HashMap;
import java.util.Map;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;

/**
 * 页面跳转帮助类
 * Created by WQ on 2017/5/24.
 */

public class CommonIntent extends BaseStartIntent {

    public static void startLoginActivity(BaseActivity that) {
        that.startActivity(new Intent(that, LoginActivity.class));
    }

    public static void startLoginActivity() {
        BaseApplication instance = CommonApp.getInstance();
        Intent intent = new Intent(instance, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        instance.startActivity(intent);
    }

    /**
     * 注册界面
     * @param that
     */
    public static void startRegistActivity(BaseActivity that) {
        Intent intent = new Intent(that, RegistActivity.class);
        that.startActivity(intent);
    }

    /**
     * 短信登录界面
     * @param that
     */
    public static void startSmsLoginActivity(BaseActivity that) {
        that.startActivity(new Intent(that,SmsLoginActivity.class));
    }

    /**
     * 忘记密码界面
     * @param that
     */
    public static void startForgetPasswordActivity(BaseActivity that,int type) {
        Intent intent = new Intent(that, ForgetPasswordActivity.class);
        intent.putExtra("type",type);
        that.startActivity(intent);
    }
    public static void startModifyPasswordActivity(BaseActivity that) {
        that.startActivity(new Intent(that,ModifyPwdActivity.class));
    }
    /**
     * 咵天主界面
     *
     * @param that
     */
    public static void startChatNavigatorActivity(Activity that) {
        /**
         * 启动会话列表界面。
         * @param context               应用上下文。
         * @param supportedConversation 定义会话列表支持显示的会话类型，及对应的会话类型是否聚合显示。
         *                              例如：supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false) 非聚合式显示 private 类型的会话。
         */
        Map<String, Boolean> supportedConversation = new HashMap<>();
        supportedConversation.put(Conversation.ConversationType.PRIVATE.getName(), false);
//        supportedConversation.put(Conversation.ConversationType.GROUP.getName(), false);
//        supportedConversation.put(Conversation.ConversationType.CHATROOM.getName(), false);
//        supportedConversation.put(Conversation.ConversationType.SYSTEM.getName(), false);
        RongIM.getInstance().startConversationList(that, supportedConversation);
    }
//    /**
//     * 设置新密码
//     *
//     * @param that
//     */
//    public static void startSettingSetNewPayPwdActivity(BaseActivity that) {
//        Intent intent = new Intent(that, SettingSetNewPayPwdActivity.class);
//        that.startActivity(intent);
//    }

    /**
     * 设置支付密码
     *
     * @param that
     */
    public static void startSettingPayPasswordActivity(Activity that) {
        Intent intent = new Intent(that, SettingPayPasswordActivity.class);
        that.startActivity(intent);
    }
    /**
     * 重置支付密码
     * @param that
     */
    public static void startModifyPayPasswordActivity(Activity that) {
        Intent intent = new Intent(that,ModifyPayPasswordActivity.class);
        that.startActivity(intent);
    }

    /**
     * 个人资料
     * @param that
     */
    public static void startUserInfoActivity(Activity that) {
        Intent intent = new Intent(that, UserInfoActivity.class);
        that.startActivity(intent);
    }

    /**
     * 进入修改资料界面
     * @param activity
     */
    public static void startUpdateInfoActivity(Activity activity, String title, String content) {
        Intent intent = new Intent(activity, UpdateInfoActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("content", content);
        activity.startActivityForResult(intent,1);
    }

    /**
     * 消息界面
     * @param that
     */
    public static void startMessageActivity(Activity that) {
        that.startActivity(new Intent(that, MessageActivity.class));
    }

    /**
     * 设置界面
     * @param that
     */
    public static void startSettingActivity(Activity that) {
        that.startActivity(new Intent(that, SettingActivity.class));
    }

    /**
     * 新消息通知
     * @param that
     */
    public static void startMessageNoticeActivity(Activity that) {
        that.startActivity(new Intent(that, MessageNoticeActivity.class));
    }

    /**
     * 账号安全
     * @param that
     */
    public static void startAccountSafeActivity(Activity that) {
        that.startActivity(new Intent(that, AccountSafeActivity.class));
    }


    /**
     * 新朋友
     * @param that
     */
    public static void startChatNewFriendsActivity(Activity that) {
        Intent intent = new Intent(that, ChatNewFriendsActivity.class);
        that.startActivity(intent);
    }

    /**
     * 群组
     * @param that
     */
    public static void startChatGroupListActivity(Activity that) {
        Intent intent = new Intent(that, ChatGroupListActivity.class);
        that.startActivity(intent);
    }
    /**
     * 选择联系人
     *
     * @param that
     */
    public static void startChatSelectLinkManActivity(Activity that,String groupId) {
        Intent intent = new Intent(that, ChatSelectLinkManActivity.class);
        intent.putExtra("groupId", groupId);
        that.startActivity(intent);
    }
    /**
     * 选择联系人 单选 请求码 0x0130,返回对象LinkManInfo ,key linkman
     *
     * @param that
     */
    public static void startChatSelectLinkManActivity(Activity that,String title,boolean isSingleSelect) {
        Intent intent = new Intent(that, ChatSelectLinkManActivity.class);
        intent.putExtra("isSingleSelect", isSingleSelect);
        intent.putExtra("title", title);
        that.startActivityForResult(intent,0x0130);
    }
    /**
     * 咵天-添加好友-搜索结果
     *
     * @param that
     */
    public static void startChatSearchFriendsActivity(Activity that) {
        Intent intent = new Intent(that, ChatSearchFriendsActivity.class);
        that.startActivity(intent);
    }

    /**
     * 咵天-个人详细资料
     *
     * @param that
     * @param uid
     */
    public static void startChatUserInfoActivity(Activity that, String uid, boolean isFriend) {
        Intent intent = new Intent(that, ChatUserInfoActivity.class);
        intent.putExtra("isFriend", isFriend);
        intent.putExtra("uid", uid);
        that.startActivity(intent);
    }
    /**
     * 添加好友
     *
     * @param that
     * @param uid
     */
    public static void startChatAddUserActivity(BaseActivity that, String uid) {
        Intent intent = new Intent(that, ChatAddUserActivity.class);
        intent.putExtra("uid", uid);
        that.startActivity(intent);
    }
    /**
     * 群公告
     *
     * @param that
     * @param targetId
     * @param notice
     * @param notice_time
     */
    public static void startChatGroupNoticeDetailsActivity(Activity that, String targetId, String notice, String notice_time) {
        Intent intent = new Intent(that, ChatGroupNoticeDetailsActivity.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("notice", notice);
        intent.putExtra("notice_time", notice_time);
        that.startActivity(intent);
    }

    /**
     * 全部群成员
     *
     * @param that
     * @param targetId
     */
    public static void startChatGroupPeoplesActivity(Activity that, String targetId,boolean isAdmin,String activity_id) {
        Intent intent = new Intent(that, ChatGroupPeoplesActivity.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("isAdmin", isAdmin);
        intent.putExtra("activity_id", activity_id);
        that.startActivity(intent);
    }

    /**
     * 修改群名片
     *
     * @param that
     * @param targetId
     * @param remark
     */
    public static void startChatModifyGroupCardActivity(Activity that, String targetId, String remark) {
        Intent intent = new Intent(that, ChatModifyGroupCardActivity.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("remark", remark);
        that.startActivity(intent);
    }

    /**
     * 修改群名称
     *
     * @param that
     * @param targetId
     * @param title
     */
    public static void startChatModifyGroupNameActivity(Activity that, String targetId, String title) {
        Intent intent = new Intent(that, ChatModifyGroupNameActivity.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("title", title);
        that.startActivity(intent);
    }
    /**
     * 修改群公告
     * @param that
     * @param targetId
     * @param notice
     */
    public static void startChatModifyGroupNoticeActivity(BaseActivity that, String targetId, String notice) {
        Intent intent = new Intent(that, ChatModifyGroupNoticeActivity.class);
        intent.putExtra("id", targetId);
        intent.putExtra("content", notice);
        that.startActivity(intent);
    }
    /**
     * 咵天-聊天记录搜索
     *
     * @param that
     * @param targetId
     * @param type     0 全部 1 单聊 2群聊
     */
    public static void startChatSearchHistoryActivity(BaseActivity that, String targetId, int type) {
        Intent intent = new Intent(that, ChatSearchHistoryActivity.class);
        intent.putExtra("id", targetId);
        intent.putExtra("type", type);
        that.startActivity(intent);
    }
       /**
     * 设置备注信息
     *
     * @param that
     * @param remark
     * @param uid
     */
    public static void startChatAddRemarkActivity(BaseActivity that, String uid, String remark) {
        Intent intent = new Intent(that, ChatAddRemarkActivity.class);
        intent.putExtra("uid", uid);
        intent.putExtra("remark", remark);
        that.startActivity(intent);
    }
    /**
     * 公告详情
     *
     * @param that
     * @param id
     */
    public static void startNoticeMessageDetailsActivity(Context that, String id, String url) {
        Intent intent = new Intent(that, NoticeMessageDetailsActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("url", url);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        that.startActivity(intent);
    }
    /**
     * 公告
     *
     * @param that
     */
    public static void startNoticeMessageActivity(Activity that) {
        that.startActivity(new Intent(that, NoticeMessageActivity.class));
    }

    /**
     * 红包记录
     * @param that
     */
    public static void startRedpackRecordActivity(BaseActivity that,String type) {
        Intent intent = new Intent(that, RedpackRecordActivity.class);
        intent.putExtra("type",type);
        that.startActivity(intent);
    }

    /**
     * 群组信息设置
     *
     * @param that
     */
    public static void startChatGroupSetInfoActivity(BaseActivity that, String targetId) {
        Intent intent = new Intent(that, ChatGroupSetInfoActivity.class);
        intent.putExtra("targetId", targetId);
        that.startActivity(intent);
    }
    /**
     * 咵天-聊天信息页面
     *
     * @param that
     */
    public static void startChatSetInfoActivity(BaseActivity that, String targetId) {
        Intent intent = new Intent(that, ChatSetInfoActivity.class);
        intent.putExtra("targetId", targetId);
        that.startActivity(intent);
    }

    /**
     * 发红包
     * @param that
     * @param group_id
     */
    public static void startSendRedpackActivity(Activity that, String group_id,int member_number,int redpack_num,String titleNameStr,String type) {
        Intent intent;
        if("2".equals(type)){
            intent=   new Intent(that, SendJielongRedpackActivity.class);
            intent.putExtra("money_title",titleNameStr);
        }else {
            intent=  new Intent(that, SendRedpackActivity.class);
        }
        intent.putExtra("group_id", group_id);
        intent.putExtra("redpack_num", redpack_num);
        intent.putExtra("member_number", member_number);
        intent.putExtra("type",type);
        that.startActivityForResult(intent,0x037);

    }

    /**
     * 扫雷游戏组列表
     * @param that
     */
    public static void startGameGroupListActivity(Activity that) {
        Intent intent = new Intent(that, GameGroupListActivity.class);
        that.startActivity(intent);
    }
    /**
     *接龙红包-红包分组
     * @param that
     */
    public static void startJielongGameGroupListActivity(Activity that) {
        Intent intent = new Intent(that, JielongGameGroupListActivity.class);
        that.startActivity(intent);
    }

    /**
     * 红包详情
     * @param context
     * @param id
     */
    public static void startRedpackPageActivity(Context context, String id,String targetId,String type) {
        Intent intent = new Intent(context, RedpackPageActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("targetId",targetId);
        intent.putExtra("type",type);
        context.startActivity(intent);
    }

    /**
     * 更改手机号
     * @param that
     */
    public static void startChangePhoneActivity(Activity that) {
        Intent intent = new Intent(that,ChangePhoneActivity.class);
        that.startActivity(intent);
    }

    /**
     * 扫码
     * @param that
     */
    public static void startScanQRActivity(Activity that) {
        Intent intent = new Intent(that,ScanQRActivity.class);
        that.startActivity(intent);
    }

    /**
     * 网页详情
     * @param that
     * @param url
     */
    public static void startWebDetailsActivity(Activity that, String url,String title) {
        Intent intent = new Intent(that, WebDetailsActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        that.startActivity(intent);
    }

    /**
     * 线下充值
     * @param that
     * @param type
     */
    public static void startRechargeOffLine(BaseActivity that, int type) {
        Intent intent=new Intent(that, RechargeOffLineActivity.class);
        intent.putExtra("type",type);
        that.startActivity(intent);
    }

    /**
     * 签到明细
     * @param that
     */
    public static void startQIandaoRecordActivity(Activity that) {
        Intent intent=new Intent(that, QIandaoRecordActivity.class);
        that.startActivity(intent);
    }

    /**
     * 签到
     * @param that
     */
    public static void startQiandaoActivity(Activity that) {
        Intent intent=new Intent(that, QiandaoActivity.class);
        that.startActivity(intent);
    }

    public static void startGameTestActivity(Activity that) {
        Intent intent=new Intent(that, GameTestActivity.class);
        that.startActivity(intent);
    }

    /**
     * 打开免密支付设置界面
     * @param that
     */
    public static void startAvoidCloseActivity(Activity that) {
        that.startActivity(new Intent(that,AvoidCloseActivity.class));
    }

    /**
     * 发红包 九包
     * @param that
     */
    public static void startSendNineRedpackActivity(Activity that, String group_id, int member_number, int redpack_num, String titleNameStr, String type) {
       Intent intent=  new Intent(that, SendNineRedpackActivity.class);
        intent.putExtra("group_id", group_id);
        intent.putExtra("redpack_num", redpack_num);
        intent.putExtra("member_number", member_number);
        intent.putExtra("type",type);
        that.startActivityForResult(intent,0x037);

    }
}
