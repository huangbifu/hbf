package com.cnsunrun.common.quest;

import com.cnsunrun.chat.mode.BannerBean;
import com.cnsunrun.chat.mode.FriendRequestBean;
import com.cnsunrun.chat.mode.GroupInfoBean;
import com.cnsunrun.chat.mode.GroupItemBean;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.chat.mode.MemberInfo;
import com.cnsunrun.chat.mode.RedPackInfoBean;
import com.cnsunrun.chat.mode.RedPackRecord;
import com.cnsunrun.chat.mode.UserQrCode;
import com.cnsunrun.common.model.ApkVersion;
import com.cnsunrun.common.model.LoginInfo;
import com.cnsunrun.login.bean.VerificationCodeInfo;
import com.cnsunrun.messages.mode.NoticeListBean;
import com.cnsunrun.mine.mode.BankCradBean;
import com.cnsunrun.mine.mode.BankListBean;
import com.cnsunrun.mine.mode.CommissionBean;
import com.cnsunrun.mine.mode.LevelBean;
import com.cnsunrun.mine.mode.OfflineInfoBean;
import com.cnsunrun.mine.mode.PayUrlBean;
import com.cnsunrun.mine.mode.PopLogBean;
import com.cnsunrun.mine.mode.QiandaoInfo;
import com.cnsunrun.mine.mode.QiandaoRecord;
import com.cnsunrun.mine.mode.RechargeInfo;
import com.cnsunrun.mine.mode.UserDataBean;
import com.cnsunrun.mine.mode.WalletBean;
import com.cnsunrun.mine.mode.WithDrawalBean;
import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.NetRequestHandler;
import com.sunrun.sunrunframwork.uibase.BaseActivity;

import java.io.File;
import java.util.List;

import static com.cnsunrun.common.quest.Config.getLoginInfo;

/**
 * 接口请求调用帮助类
 */
public class BaseQuestStart extends BaseQuestConfig {


    /**
     * 签到信息
     *
     * @param netRequestHandler
     */
    public static void sign_info(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.SIGN_INFO)
                .setRequestCode(BaseQuestConfig.SIGN_INFO_CODE)
                .setTypeToken(QiandaoInfo.class)
        );

    }

    /**
     * 签到
     *
     * @param netRequestHandler
     */
    public static void sign_in(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.SIGN_IN)
                .setRequestCode(BaseQuestConfig.SIGN_IN_CODE)
        );

    }

    /**
     * 签到明细
     *
     * @param netRequestHandler
     * @param page
     */
    public static void sign_record(NetRequestHandler netRequestHandler, int page) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.SIGN_RECORD)
                .put("p", page)
                .setRequestCode(BaseQuestConfig.SIGN_RECORD_CODE)
                .setTypeToken(QiandaoRecord.class));

    }

    /**
     * 获取图形验证码(注册，短信登录)
     *
     * @param netRequestHandler
     * @param mobile            手机号
     * @param width             宽
     * @param height            高
     */
    public static void getRegisteredPicVerificationCode(NetRequestHandler netRequestHandler, String mobile,
                                                        String width, String height) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.GET_REGISTERED_PIC_VERIFICATION_CODE_URL)
                .put("mobile", mobile)
                .put("width", width)
                .put("height", height)
                .setRequestCode(BaseQuestConfig.QUEST_GET_REGISTERED_PIC_VERIFICATION_CODE)
                .setTypeToken(VerificationCodeInfo.class));
    }

    /**
     * 获取验证码 （注册）
     *
     * @param netRequestHandler
     * @param mobile            手机号
     * @param verifi_code       图形验证码
     */
    public static void getRegisteredVerificationCode(NetRequestHandler netRequestHandler, String mobile, String verifi_code) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.GET_REGISTERED_VERIFICATION_CODE_URL)
                .put("mobile", mobile)
                .put("verifi_code", verifi_code)
                .setRequestCode(BaseQuestConfig.QUEST_GET_REGISTERED_VERIFICATION_CODE));
    }

    /**
     * 注册
     *
     * @param netRequestHandler
     * @param mobile            手机号
     * @param code              验证码
     * @param password          密码
     * @param repassword        确认密码
     */
    public static void register(NetRequestHandler netRequestHandler, String mobile, String code, String password, String repassword) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.REGISTER_URL)
                .put("mobile", mobile)
                .put("code", code)
                .put("password", password)
                .put("repassword", repassword)
                .setRequestCode(BaseQuestConfig.QUEST_REGISTER_CODE));
    }

    /**
     * 登录
     *
     * @param netRequestHandler
     * @param username          手机号
     * @param password          密码
     */
    public static void login(NetRequestHandler netRequestHandler, String username, String password) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.LOGIN_URL)
                .put("username", username)
                .put("password", password)
                .setRequestCode(BaseQuestConfig.QUEST_LOGIN_CODE)
                .setTypeToken(LoginInfo.class));
    }


    /**
     * 获取验证码 （忘记密码）
     *
     * @param netRequestHandler
     * @param mobile            手机号
     */
    public static void getForgetPasswordVerificationCode(NetRequestHandler netRequestHandler, String mobile) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.GET_FORGET_PASSWORD_VERIFICATION_CODE_URL)
                .put("mobile", mobile)
                .setRequestCode(BaseQuestConfig.QUEST_GET_FORGET_PASSWORD_VERIFICATION_CODE));
    }

    /**
     * 修改密码（保存）
     *
     * @param netRequestHandler
     * @param mobile            手机号
     * @param code              验证码
     * @param password          密码
     */
    public static void modifyPassword(NetRequestHandler netRequestHandler, String mobile, String code, String password) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.MODIFY_PASSWORD_URL)
                .put("mobile", mobile)
                .put("code", code)
                .put("password", password)
                .setRequestCode(BaseQuestConfig.MODIFY_PASSWORD_CODE));
    }

    /**
     * 获取验证码 （短信登录）
     *
     * @param netRequestHandler
     * @param mobile            手机号
     * @param verifi_code       图形验证码
     */
    public static void getSmsLoginVerificationCode(NetRequestHandler netRequestHandler, String mobile, String verifi_code) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.GET_SMS_LOGIN_VERIFICATION_CODE_URL)
                .put("mobile", mobile)
                .put("verifi_code", verifi_code)
                .setRequestCode(BaseQuestConfig.QUEST_GET_SMS_LOGIN_VERIFICATION_CODE));
    }

    /**
     * 短信登录
     *
     * @param netRequestHandler
     * @param username          手机号
     * @param code              验证码
     */
    public static void smsLogin(NetRequestHandler netRequestHandler, String username, String code) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.SMS_LOGIN_URL)
                .put("username", username)
                .put("code", code)
                .setRequestCode(BaseQuestConfig.QUEST_SMS_LOGIN_CODE)
                .setTypeToken(LoginInfo.class));
    }


//    *
//     * 我的 - 设置,帮助及版本信息

//    public static void UserIndexIndexSet(NetRequestHandler netRequestHandler) {
//        netRequestHandler.requestAsynGet(Config.UAction().setUrl(USER_INDEX_INDEX_SET_)
//                .setRequestCode(USER_INDEX_INDEX_SET_CODE).setTypeToken(AboutInfo.class)
//        );
//    }

//


//    *
//     * 我的 - 钱包 - 支付宝提现 - 获取授权登录用信息

    public static void UserWalletLoginAlipay(NetRequestHandler netRequestHandler, String money, Object password) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(USER_WALLET_LOGIN_ALIPAY_)
                .setRequestCode(USER_WALLET_LOGIN_ALIPAY_CODE).put("password", password).put("money", money)
        );
    }

//    *
//     * 我的 - 找回密码 - 验证短信验证码
//     *
//     * @param mobile
//     * @param code

    public static void UserLoginVerifyMobileSms(NetRequestHandler netRequestHandler, Object mobile, Object code) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(USER_LOGIN_VERIFY_MOBILE_SMS_)
                .setRequestCode(USER_LOGIN_VERIFY_MOBILE_SMS_CODE)
                .put("mobile", mobile).put("code", code));
    }


//    *
//     * 邻里 - 咵天 - 群组 - 群名称
//     *
//     * @param id
//     * @param title

    public static void NeighborhoodImGroupTitle(NetRequestHandler netRequestHandler, Object id, Object title) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_TITLE_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_TITLE_CODE)
                .put("id", id).put("title", title));
    }

//    *
//     * 邻里 - 咵天 - 群组 - 设置名片
//     *
//     * @param id
//     * @param remark

    public static void NeighborhoodImGroupRemark(NetRequestHandler netRequestHandler, Object id, Object remark) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_REMARK_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_REMARK_CODE)
                .put("id", id).put("remark", remark));
    }

    /**
     * 邻里 - 咵天 - 添加好友 - 搜索
     *
     * @param mobile
     */
    public static void NeighborhoodImMemberSearch(NetRequestHandler netRequestHandler, Object mobile) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(NEIGHBORHOOD_IM_MEMBER_SEARCH_)
                .setRequestCode(NEIGHBORHOOD_IM_MEMBER_SEARCH_CODE)
                .put("mobile", mobile));
    }

    /**
     * 邻里 - 咵天 - 获取 token
     */
    public static void NeighborhoodImGetToken(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(NEIGHBORHOOD_IM_GET_TOKEN_)
                .setRequestCode(NEIGHBORHOOD_IM_GET_TOKEN_CODE)
        );
    }


    /**
     * 登录
     *
     * @param netRequestHandler
     * @param username
     * @param password
     */
    public static void user_login(NetRequestHandler netRequestHandler, Object username, Object password) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(USER_LOGIN)
                .setRequestCode(USER_LOGIN_CODE).setTypeToken(new TypeToken<LoginInfo>() {
                }).put("username", username)
                .put("password", password)
        );
    }

    /**
     * 修改密码
     *
     * @param netRequestHandler
     * @param password
     * @param newpassword
     */
    public static void change_password(NetRequestHandler netRequestHandler, Object password, Object newpassword) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(CHANGE_PASSWORD)
                .setRequestCode(CHANGE_PASSWORD_CODE).put("new_password", newpassword)
                .put("password", password)
        );
    }


    /**
     * 我的 - 刷新 token
     */
    public static void UserOauthRefresh(NetRequestHandler netRequestHandler) {
        String refresh_token = getLoginInfo().token.refresh_token;
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(USER_OAUTH_REFRESH_)
                .setRequestCode(USER_OAUTH_REFRESH_CODE)
                .setTypeToken(LoginInfo.TokenBean.class)
                .put("refresh_token", refresh_token)
                .put("uid", getLoginInfo().getId())
        );
    }

    /**
     * 我的 - 获取 token
     */
    public static void UserOauthToken(NetRequestHandler netRequestHandler) {
//        if (Utils.isQuck(500)) {
//            return;
//        }
        String password = getLoginInfo().pwd;
        String _password = getLoginInfo().password;
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(USER_OAUTH_TOKEN_)
                .setRequestCode(USER_OAUTH_TOKEN_CODE)
                .setTypeToken(LoginInfo.TokenBean.class)
                .put("uid", getLoginInfo().getId())
                .put("password", password).put("_password", _password));
    }

    /**
     * 充值
     *
     * @param netRequestHandler
     * @param money
     */

    public static void recharge(NetRequestHandler netRequestHandler, String money, String pay_channel) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.USER_RECHARGE)
                .put("money", money)
                .put("pay_channel", pay_channel)
                .setRequestCode(BaseQuestConfig.USER_RECHARGE_CODE)
                .setTypeToken(PayUrlBean.class)
        );
    }

    /**
     * 获取个人数据
     *
     * @param netRequestHandler
     */
    public static void getUserData(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.GET_USER_DATA)
                .setRequestCode(BaseQuestConfig.GET_USER_DATA_CODE)
                .setTypeToken(UserDataBean.class));

    }

    /**
     * 获取充值金额列表
     *
     * @param netRequestHandler
     */
    public static void getRecharInfo(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.GET_RECHAR_INFO)
                .setRequestCode(BaseQuestConfig.GET_RECHAR_INFO_CODE)
                .setTypeToken(RechargeInfo.class));

    }

    /**
     * 个人中心 - 设置邀请人
     *
     * @param netRequestHandler
     * @param code
     */
    public static void postInvite(NetRequestHandler netRequestHandler, String code) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.POST_INVITE)
                .put("invite_code", code)
                .setRequestCode(BaseQuestConfig.POST_INVITE_CODE));

    }

    /**
     * 个人中心 - 返佣记录
     *
     * @param netRequestHandler
     * @param page
     */
    public static void getCommissionData(NetRequestHandler netRequestHandler, int page) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.GET_COMMOISSION)
                .put("p", page)
                .setRequestCode(BaseQuestConfig.GET_COMMOISSION_CODE)
                .setTypeToken(CommissionBean.class));
    }

    /**
     * 提现 - 提现信息
     *
     * @param netRequestHandler
     */
    public static void getWithDrawalInfo(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.GET_WITH_DRAWAL_INFO)
                .setRequestCode(BaseQuestConfig.GET_WITH_DRAWAL_INFO_CODE)
                .setTypeToken(WithDrawalBean.class));
    }

    /**
     * 我的 - 设置 - 账户与安全 - h获取
     */

    public static NAction UserSafeIndex() {
        return new NAction().setUrl(USER_SAFE_INDEX_)
                .setRequestCode(USER_SAFE_INDEX_CODE).setRequestType(NetRequestHandler.GET);
    }

    /**
     * 个人中心-支付密码 - 设置
     *
     * @param netRequestHandler
     * @param password
     */

    public static void UserSafeSetDealPassword(NetRequestHandler netRequestHandler, Object password) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(USER_SAFE_SET_DEAL_PASSWORD_)
                .setRequestCode(USER_SAFE_SET_DEAL_PASSWORD_CODE)
                .put("password", password));
    }

    /**
     * 我的 - 钱包 - 支付宝提现
     *
     * @param netRequestHandler
     * @param user_id
     * @param money
     * @param password
     * @param withDrawalType
     */
    public static void UserWalletWithdraw(NetRequestHandler netRequestHandler, String user_id, String money, Object password, int pay_type, int withDrawalType) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(USER_WALLET_WITHDRAW_)
                .setRequestCode(USER_WALLET_WITHDRAW_CODE)
                .put("user_id", user_id)
                .put("money", money)
                .put("password", password)
                .put("pay_type", pay_type)
                .put("type", withDrawalType)
        );
    }


    /**
     * 邻里 - 咵天 - 群组 - 创建
     *
     * @param title 群组名称，1~30字符
     * @param fuid  好友ID
     * @param fuid
     */
    public static void NeighborhoodImGroupAdd(NetRequestHandler netRequestHandler, Object title, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_ADD_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_ADD_CODE)
                .put("title", title).put("fuid", fuid));
    }

    /**
     * 邻里 - 咵天 - 群组 - 置顶 - 取消
     *
     * @param id
     */
    public static void NeighborhoodImGroupIsTopCancel(NetRequestHandler netRequestHandler, Object id) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_IS_TOP_CANCEL_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_IS_TOP_CANCEL_CODE)
                .put("id", id));
    }

    /**
     * 邻里 - 咵天 - 群组 - 置顶
     *
     * @param id
     */
    public static void NeighborhoodImGroupIsTop(NetRequestHandler netRequestHandler, Object id) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_IS_TOP_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_IS_TOP_CODE)
                .put("id", id));
    }

    /**
     * 邻里 - 咵天 - 群组 - 免打扰 - 取消
     *
     * @param id
     */
    public static void NeighborhoodImGroupNotDisturbCancel(NetRequestHandler netRequestHandler, Object id) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CANCEL_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CANCEL_CODE)
                .put("id", id));
    }

    /**
     * 邻里 - 咵天 - 群组 - 免打扰
     *
     * @param id
     */
    public static void NeighborhoodImGroupNotDisturb(NetRequestHandler netRequestHandler, Object id) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CODE)
                .put("id", id));
    }

    /**
     * 邻里 - 咵天 - 群组 - 成员
     *
     * @param id
     */
    public static void NeighborhoodImGroupMemberList(NetRequestHandler netRequestHandler, Object id, int page) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_MEMBER_LIST_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_MEMBER_LIST_CODE)
                .setTypeToken(new TypeToken<List<LinkManInfo>>() {
                })
                .put("id", id)
                .put("p", page));
    }

    /**
     * 邻里 - 咵天 - 群组 - 信息
     *
     * @param id
     */
    public static void NeighborhoodImGroupInfo(NetRequestHandler netRequestHandler, Object id) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_INFO_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_INFO_CODE)
                .setTypeToken(GroupInfoBean.class)
                .put("id", id));
    }

    /**
     * 邻里 - 咵天 - 群组 - 列表
     */
    public static void NeighborhoodImGroupList(NetRequestHandler netRequestHandler, int page) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_LIST_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_LIST_CODE)
                .setTypeToken(new TypeToken<List<GroupItemBean>>() {
                })
                .put("p", page)
        );
    }

    /**
     * 邻里 - 咵天 - 群组 - 删除群成员
     *
     * @param id
     * @param uid
     */
    public static void NeighborhoodImGroupMemberDel(NetRequestHandler netRequestHandler, Object id, Object uid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_MEMBER_DEL_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_MEMBER_DEL_CODE)
                .put("id", id).put("uid", uid));
    }

    /**
     * 邻里 - 咵天 - 群组 - 退出
     *
     * @param id 群组ID
     */
    public static void NeighborhoodImGroupQuit(NetRequestHandler netRequestHandler, Object id) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_QUIT_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_QUIT_CODE)
                .put("id", id));
    }

    /**
     * 邻里 - 咵天 - 群组 - 加入
     *
     * @param id   群组ID
     * @param fuid
     */
    public static void NeighborhoodImGroupJoin(NetRequestHandler netRequestHandler, Object id, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_JOIN_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_JOIN_CODE)
                .put("id", id).put("fuid", fuid));
    }

    /**
     * 邻里 - 咵天 - 聊天信息 - 取消聊天置顶
     *
     * @param fuid
     */
    public static void NeighborhoodImIsTopCancel(NetRequestHandler netRequestHandler, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_IS_TOP_CANCEL_)
                .setRequestCode(NEIGHBORHOOD_IM_IS_TOP_CANCEL_CODE)
                .put("fuid", fuid));
    }

    /**
     * 邻里 - 咵天 - 聊天信息 - 聊天置顶
     *
     * @param fuid
     */
    public static void NeighborhoodImIsTop(NetRequestHandler netRequestHandler, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_IS_TOP_)
                .setRequestCode(NEIGHBORHOOD_IM_IS_TOP_CODE)
                .put("fuid", fuid));
    }

    /**
     * 邻里 - 咵天 - 聊天信息 - 取消免打扰
     *
     * @param fuid
     */
    public static void NeighborhoodImNotDisturbCancel(NetRequestHandler netRequestHandler, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_NOT_DISTURB_CANCEL_)
                .setRequestCode(NEIGHBORHOOD_IM_NOT_DISTURB_CANCEL_CODE)
                .put("fuid", fuid));
    }

    /**
     * 邻里 - 咵天 - 聊天信息 - 免打扰
     *
     * @param fuid
     */
    public static void NeighborhoodImNotDisturb(NetRequestHandler netRequestHandler, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_NOT_DISTURB_)
                .setRequestCode(NEIGHBORHOOD_IM_NOT_DISTURB_CODE)
                .put("fuid", fuid));
    }

    /**
     * 邻里 - 咵天 - 好友信息 - 删除
     *
     * @param fuid 好友ID
     */
    public static void NeighborhoodImFriendDel(NetRequestHandler netRequestHandler, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_FRIEND_DEL_)
                .setRequestCode(NEIGHBORHOOD_IM_FRIEND_DEL_CODE)
                .put("fuid", fuid));
    }


    /**
     * IM - 获取轮播图
     */
    public static void get_banner(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(GET_BANNER)
                .setRequestCode(GET_BANNER_CODE)
                .setTypeToken(new TypeToken<List<BannerBean>>() {
                })
        );
    }

    /**
     * 获取消息公告
     */
    public static void get_notice(NetRequestHandler netRequestHandler, int page) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(GET_NOTICE)
                .setRequestCode(GET_NOTICE_CODE)

                .setTypeToken(new TypeToken<NoticeListBean.NoticeListPage>() {
                })
                .put("p", page)
        );
    }

    /**
     * 社区 - 公告详情
     *
     * @param netRequestHandler
     */
    public static void get_notice_Details(NetRequestHandler netRequestHandler, Object id) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(GET_NOTICE_DETAIL)
                .setRequestCode(GET_NOTICE_DETAIL_CODE).
                        put("id", id)
                .setTypeToken(new TypeToken<NoticeListBean>() {
                })
        );
    }

    // 个人中心 - 用户二维码
    public static void user_qrcode(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(USER_QRCODE)
                .setRequestCode(USER_QRCODE_CODE)
                .setTypeToken(new TypeToken<UserQrCode>() {
                })
        );
    }

    /**
     * 我的钱包
     *
     * @param netRequestHandler
     * @param page
     */
    public static void getWalletIndex(NetRequestHandler netRequestHandler, int page, String type) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.GET_WALLET_INDEX)
                .put("p", page)
                .put("type", type)
                .setRequestCode(BaseQuestConfig.GET_WALLET_INDEX_CODE)
                .setTypeToken(WalletBean.class));

    }

    /**
     * 推广海报
     *
     * @param netRequestHandler
     */
    public static void getPopLog(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.GET_POP_LOG)
                .setRequestCode(BaseQuestConfig.GET_POP_LOG_CODE)
                .setTypeToken(PopLogBean.class));

    }

    /**
     * 个人中心 - 推广 - 获取下一级
     *
     * @param netRequestHandler
     * @param distance
     * @param member_id
     */
    public static void getNextLevel(NetRequestHandler netRequestHandler, int distance, String member_id, int page) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.GET_NEXT_LEVEL)
                .put("distance", distance)
                .put("member_id", member_id)
                .put("p", page)
                .setRequestCode(BaseQuestConfig.GET_NEXT_LEVEL_CODE)
                .setTypeToken(LevelBean.class));

    }

    /**
     * 钱包 - 转账
     *
     * @param netRequestHandler
     * @param uid
     * @param money
     * @param code
     */
    public static void postTransfer(NetRequestHandler netRequestHandler, String uid, String money, Object password, String code) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.POST_TRANSFER)
                .put("uid", uid)
                .put("money", money)
                .put("password", password)
                .put("code", code)
                .setRequestCode(BaseQuestConfig.POST_TRANSFER_CODE));
    }

    /**
     * 修改头像
     *
     * @param netRequestHandler
     * @param file              图形
     */
    public static void getChangePhoto(NetRequestHandler netRequestHandler, File file) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.GET_CHANGE_PHOTO_URL)
                .put("file", file)
                .setRequestCode(BaseQuestConfig.QUEST_GET_CHANGE_PHOTO_CODE));
    }

    /**
     * 保存个人资料
     *
     * @param netRequestHandler
     * @param nickname          昵称
     * @param realname          真实姓名
     * @param gender            性别
     * @param province          省
     * @param city              城市
     */
    public static void getSaveUserInfo(NetRequestHandler netRequestHandler, String nickname, String realname, String gender,
                                       String province, String city) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.GET_SAVE_USER_INFO_URL)
                .put("nickname", nickname)
                .put("realname", realname)
                .put("gender", gender)
                .put("province", province)
                .put("city", city)
                .setRequestCode(BaseQuestConfig.QUEST_GET_SAVE_USER_INFO_CODE));
    }


    // IM - 红包 - 群组
    public static void game_group_list(NetRequestHandler netRequestHandler, int p) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(GAME_GROUP_LIST)
                .setRequestCode(GAME_GROUP_LIST_CODE)
                .setTypeToken(new TypeToken<List<GroupItemBean>>() {
                })
        );
    }

    //接龙红包-红包分组
    public static void game_packet_get_group(NetRequestHandler netRequestHandler, int p) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(GAME_PACKET_GET_GROUP)
                .setRequestCode(GAME_PACKET_GET_GROUP_CODE)
                .setTypeToken(new TypeToken<List<GroupItemBean>>() {
                })
        );
    }

    /**
     * 邻里 - 咵天 - 用户信息
     *
     * @param uid 用户ID
     */
    public static NAction NeighborhoodImMemberInfo(NetRequestHandler netRequestHandler, Object uid) {
        NAction nAction = Config.UAction().setUrl(NEIGHBORHOOD_IM_MEMBER_INFO_)
                .setRequestCode(NEIGHBORHOOD_IM_MEMBER_INFO_CODE)
                .setTypeToken(MemberInfo.class)
                .put("uid", uid);
        if (netRequestHandler != null) {
            netRequestHandler.requestAsynGet(nAction);
        }
        return nAction;
    }

    /**
     * 邻里 - 咵天 - 添加好友
     *
     * @param uid     需添加为好友的用户ID
     * @param message 附加信息（留言）
     */
    public static void NeighborhoodImFriendRequest(NetRequestHandler netRequestHandler, Object uid, Object message) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_FRIEND_REQUEST_)
                .setRequestCode(NEIGHBORHOOD_IM_FRIEND_REQUEST_CODE)
                .put("uid", uid).put("message", message));
    }

    /**
     * 邻里 - 咵天 - 好友申请列表
     */
    public static void NeighborhoodImFriendRequestList(NetRequestHandler netRequestHandler, Object page) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(NEIGHBORHOOD_IM_FRIEND_REQUEST_LIST_)
                .setRequestCode(NEIGHBORHOOD_IM_FRIEND_REQUEST_LIST_CODE)
                .setTypeToken(new TypeToken<List<FriendRequestBean>>() {
                })
                .put("p", page)
        );
    }

    /**
     * 邻里 - 咵天 - 好友申请 - 拒绝
     *
     * @param fuid
     */
    public static void NeighborhoodImFriendRequestRefuse(NetRequestHandler netRequestHandler, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_FRIEND_REQUEST_REFUSE_)
                .setRequestCode(NEIGHBORHOOD_IM_FRIEND_REQUEST_REFUSE_CODE)
                .put("fuid", fuid));
    }

    /**
     * 邻里 - 咵天 - 好友申请 - 接受
     *
     * @param fuid
     */
    public static void NeighborhoodImFriendRequestAccept(NetRequestHandler netRequestHandler, Object fuid) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_FRIEND_REQUEST_ACCEPT_)
                .setRequestCode(NEIGHBORHOOD_IM_FRIEND_REQUEST_ACCEPT_CODE)
                .put("fuid", fuid));
    }

    /**
     * 邻里 - 咵天 - 通讯录
     */
    public static void NeighborhoodImFriendList(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(NEIGHBORHOOD_IM_FRIEND_LIST_)
                .setRequestCode(NEIGHBORHOOD_IM_FRIEND_LIST_CODE)
                .setTypeToken(new TypeToken<List<LinkManInfo>>() {
                })
        );
    }

    /**
     * 邻里 - 咵天 - 聊天信息
     *
     * @param fuid
     */
    public static void NeighborhoodImChatInfo(NetRequestHandler netRequestHandler, Object fuid) {
        netRequestHandler.requestAsynGet(Config.UAction().setUrl(NEIGHBORHOOD_IM_CHAT_INFO_)
                .setRequestCode(NEIGHBORHOOD_IM_CHAT_INFO_CODE)
                .setTypeToken(LinkManInfo.class)
                .put("fuid", fuid));
    }

    /**
     * IM - 红包 - 发红包
     *
     * @param money
     */
    public static void send_redpack(NetRequestHandler netRequestHandler, Object money
            , Object num, Object thunder, Object group_id, String password, Object type, Object redType
    ) {
        if ("2".equals(type)) {
            netRequestHandler.requestAsynPost(Config.UAction().setUrl(PACKET_SEND_REDPACK)
                    .setRequestCode(SEND_REDPACK_CODE)
                    .put("money", money)
                    .put("num", num)
                    .put("thunder", thunder)
                    .put("id", group_id)
                    .put("password", password)
            );
        } else {
            netRequestHandler.requestAsynPost(Config.UAction().setUrl(SEND_REDPACK)
                    .setRequestCode(SEND_REDPACK_CODE)
                    .put("money", money)
                    .put("num", num)
                    .put("thunder", thunder)
                    .put("group_id", group_id)
                    .put("password", password)
                    .put("type", redType)

            );
        }
    }

    /**
     * IM - 发福利包
     *
     * @param money
     */
    public static void sendWelfareRedPack(NetRequestHandler netRequestHandler, Object money
            , Object num, Object group_id, String password) {

        netRequestHandler.requestAsynPost(Config.UAction().setUrl(SEND_WELFARE_REDPACK)
                .setRequestCode(SEND_WELFARE_REDPACK_CODE)
                .put("money", money)
                .put("num", num)
                .put("group_id", group_id)
                .put("password", password)

        );

    }

    /**
     * IM - 红包 - 抢红包
     *
     * @param id
     */
    public static void grab_redpack(NetRequestHandler netRequestHandler, Object id
            , Object type) {
        if ("2".equals(type)) {

            netRequestHandler.requestAsynPost(Config.UAction().setUrl(PACKET_GRAB_REDPACK)
                    .setRequestCode(GRAB_REDPACK_CODE)
                    .put("id", id)
            );
        } else {
            netRequestHandler.requestAsynPost(Config.UAction().setUrl(GRAB_REDPACK)
                    .setRequestCode(GRAB_REDPACK_CODE)
                    .put("id", id)
            );
        }
    }

//    /**
//     * IM - 红包 - 发红包
//     *
//     * @param money
//     */
//    public static void send_redpack (NetRequestHandler netRequestHandler, Object money
//            , Object num, Object thunder, Object group_id, String password
//    ) {
//        netRequestHandler.requestAsynPost(Config.UAction().setUrl(IMTHUNDER_SEND_REDPACK)
//                .setRequestCode(SEND_REDPACK_CODE)
//                .put("money", money)
//                .put("num", num)
//                .put("thunder", thunder)
//                .put("group_id", group_id)
//                .put("password", password)
//
//        );
//    }
//
//    /**
//     * IM - 红包 - 抢红包
//     *
//     * @param id
//     */
//    public static void grab_redpack (NetRequestHandler netRequestHandler, Object id
//    ) {
//        netRequestHandler.requestAsynPost(Config.UAction().setUrl(IMTHUNDER_GRAB_REDPACK)
//                .setRequestCode(GRAB_REDPACK_CODE)
//                .put("id", id)
//        );
//    }

    /**
     * IM - 红包 - 红包记录
     *
     * @param id
     */
    public static void get_redpack(NetRequestHandler netRequestHandler, Object id
            , Object type
    ) {
        if ("2".equals(type)) {

            netRequestHandler.requestAsynGet(Config.UAction().setUrl(PACKET_GET_REDPACK)
                    .setRequestCode(GET_REDPACK_CODE)
                    .put("id", id)
                    .setTypeToken(RedPackInfoBean.class)
            );
        } else {
            netRequestHandler.requestAsynGet(Config.UAction().setUrl(GET_REDPACK)
                    .setRequestCode(GET_REDPACK_CODE)
                    .put("id", id)
                    .setTypeToken(RedPackInfoBean.class)
            );
        }
    }

    /**
     * //IM - 红包 - 我的红包记录
     *
     * @param p
     */
    public static void get_my_redpack_record(NetRequestHandler netRequestHandler, Object p, Object type
    ) {

        if ("2".equals(type)) {
            netRequestHandler.requestAsynGet(Config.UAction().setUrl(PACKET_GET_MY_REDPACK_RECORD)
                    .setRequestCode(GET_MY_REDPACK_RECORD_CODE)
                    .put("p", p)
                    .setTypeToken(RedPackRecord.class)
            );
        } else {
            netRequestHandler.requestAsynGet(Config.UAction().setUrl(GET_MY_REDPACK_RECORD)
                    .setRequestCode(GET_MY_REDPACK_RECORD_CODE)
                    .put("p", p)
                    .setTypeToken(RedPackRecord.class)
            );
        }
    }

//    public static String GET_MY_REDPACK_RECORD = HTTP_API + "App/IM/IM/get_my_redpack_record";
//    public static final int GET_MY_REDPACK_RECORD_CODE = 0x0240;

//    //IM - 红包 - 红包记录
//    public static String GET_REDPACK = HTTP_API + "App/IM/IM/get_redpack";
//    public static final int GET_REDPACK_CODE = 0x0239;


    /**
     * 获取验证码 （更改手机）
     *
     * @param netRequestHandler
     */
    public static void getChangePhoneVerificationCode(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.GET_CHANGE_PHONE_VERIFICATION_CODE_URL)
                .setRequestCode(BaseQuestConfig.QUEST_GET_CHANGE_PHONE_VERIFICATION_CODE));
    }

    /**
     * 短信登录
     *
     * @param netRequestHandler
     * @param mobile            手机号
     * @param code              验证码
     */
    public static void modifyPhone(NetRequestHandler netRequestHandler, String mobile, String code) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.MODIFY_PHONE_URL)
                .put("mobile", mobile)
                .put("code", code)
                .setRequestCode(BaseQuestConfig.QUEST_MODIFY_PHONE_CODE));
    }

    /**
     * 获取验证码 （重置支付密码）
     *
     * @param netRequestHandler
     */
    public static void getModifyPayPasswordVerificationCode(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.GET_MODIFY_PAY_PASSWORD_VERIFICATION_CODE_URL)
                .setRequestCode(BaseQuestConfig.QUEST_GET_MODIFY_PAY_PASSWORD_VERIFICATION_CODE));
    }

    /**
     * 重置支付密码
     *
     * @param netRequestHandler
     * @param password          密码
     * @param code              验证码
     */
    public static void modifyPayPassword(NetRequestHandler netRequestHandler, String password, String code) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.MODIFY_PAY_PASSWORD)
                .put("password", password)
                .put("code", code)
                .setRequestCode(BaseQuestConfig.QUEST_PAY_PASSWORD_CODE));
    }

    /**
     * 账号安全-是否免密支付
     *
     * @param netRequestHandler
     * @param is_secret_free
     * @param code
     */
    public static void setNoPaypwdBet(NetRequestHandler netRequestHandler, String is_secret_free, String code) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.NO_PAY_PWD_BET)
                .put("is_secret_free", is_secret_free)
                .put("code", code)
                .setRequestCode(BaseQuestConfig.QUEST_NO_PAY_PWD_BET_CODE));
    }

    /**
     * 新增银行卡
     *
     * @param netRequestHandler
     * @param password
     * @param username
     * @param account
     * @param bank_adress
     * @param account_type
     * @param code
     */
    public static void postAddBankCard(NetRequestHandler netRequestHandler, String password, String username, String account, String bank_adress, String account_type, String code, String branch_bank_adress
            , String province, String city) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.POST_ADD_BANK_CARD)
                .put("password", password)
                .put("username", username)
                .put("account", account)
                .put("bank_adress", bank_adress)
                .put("account_type", account_type)
                .put("code", code)
                .put("branch_bank_adress", branch_bank_adress)
                .put("province", province)
                .put("city", city)
                .setRequestCode(BaseQuestConfig.POST_ADD_BANK_CARD_CODE));

    }

    /**
     * 我的银行卡列表
     *
     * @param netRequestHandler
     */
    public static void getBankCardList(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(new NAction()
                .setUrl(BaseQuestConfig.GET_BANK_CARD_LIST)
                .setRequestCode(BaseQuestConfig.GET_BANK_CARD_LIST_CODE)
                .setTypeToken(new TypeToken<List<BankCradBean>>() {
                }));
    }

    /**
     * 银行卡解绑
     *
     * @param netRequestHandler
     * @param id
     */
    public static void postUnBindBankCard(NetRequestHandler netRequestHandler, String id) {
        netRequestHandler.requestAsynPost(new NAction()
                .setUrl(BaseQuestConfig.POST_UNBIND_BANK_CARD)
                .put("id", id)
                .setRequestCode(BaseQuestConfig.POST_UNBIND_BANK_CARD_CODE));

    }

    /**
     * 检查更新
     *
     * @param netRequestHandler
     */
    public static void checkUpDataVersion(NetRequestHandler netRequestHandler, String version) {
        netRequestHandler.requestAsynPost(Config.UAction()
                .setUrl(BaseQuestStart.CHECK_UP_DATA_VERSION_URL)
                .put("version", version)
                .put("type", 10)
                .setRequestCode(CHECK_UP_DATA_VERSION_CODE)
                .setTypeToken(ApkVersion.class)
        );
    }

    /**
     * 邻里 - 咵天 - 群组 - 公告
     *
     * @param notice
     */
    public static void NeighborhoodImGroupNotice(NetRequestHandler netRequestHandler, Object id, Object notice) {
        netRequestHandler.requestAsynPost(Config.UAction().setUrl(NEIGHBORHOOD_IM_GROUP_NOTICE_)
                .setRequestCode(NEIGHBORHOOD_IM_GROUP_NOTICE_CODE)
                .put("notice", notice)
                .put("id", id)
        )
        ;
    }

    /**
     * 验证支付密码
     *
     * @param netRequestHandler
     * @param dealPassword
     */
    public static void postCheckPassword(NetRequestHandler netRequestHandler, String dealPassword) {
        netRequestHandler.requestAsynPost(Config.UAction()
                .setUrl(BaseQuestConfig.POST_CHECK_PASSWORD)
                .put("password", dealPassword)
                .setRequestCode(BaseQuestConfig.POST_CHECK_PASSWORD_CODE));
    }

    /**
     * 线下充值-收款信息
     *
     * @param netRequestHandler
     */
    public static void getOffLineInfo(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(Config.UAction()
                .setUrl(BaseQuestConfig.GET_OFFLINE_INFO)
                .setRequestCode(BaseQuestConfig.GET_OFFLINE_INFO_CODE)
                .setTypeToken(OfflineInfoBean.class));
    }

    /**
     * 线下充值-提交充值信息审核
     *
     * @param netRequestHandler
     * @param user_name
     * @param total_money
     * @param remark
     * @param bank_id
     * @param pay_type
     */
    public static void postSubmitRechargeInfo(NetRequestHandler netRequestHandler, String user_name, String total_money, String remark, int bank_id, int pay_type) {
        netRequestHandler.requestAsynPost(Config.UAction()
                .setUrl(BaseQuestConfig.POST_OFFLINE_INFO)
                .put("user_name", user_name)
                .put("total_money", total_money)
                .put("remark", remark)
                .put("bank_id", bank_id)
                .put("pay_type", pay_type)
                .setRequestCode(BaseQuestConfig.POST_OFFLINE_INFO_CODE));
    }

    /**
     * 获取支付宝列表
     *
     * @param netRequestHandler
     * @param page
     */
    public static void getZhifuBaoList(NetRequestHandler netRequestHandler, int page) {
        netRequestHandler.requestAsynGet(Config.UAction()
                .setUrl(BaseQuestConfig.GET_ALI_ACCOUNT)
                .put("p", page)
                .setRequestCode(BaseQuestConfig.GET_ALI_ACCOUNT_CODE)
                .setTypeToken(new TypeToken<List<BankCradBean>>() {
                }));
    }

    /**
     * 解绑支付宝验证码
     *
     * @param netRequestHandler
     */
    public static void getVerificationCode(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynPost(Config.UAction()
                .setUrl(BaseQuestConfig.GET_VERIFICATION)
                .setRequestCode(BaseQuestConfig.QUEST_GET_VERIFICATION_CODE));
    }

    /**
     * 解绑支付宝
     *
     * @param netRequestHandler
     * @param id
     * @param s
     */
    public static void unBindZhifubaoAccount(NetRequestHandler netRequestHandler, String id, String s) {
        netRequestHandler.requestAsynPost(Config.UAction()
                .setUrl(BaseQuestConfig.UNBIND_ZHIFUBAO_ACCOUNT)
                .put("id", id)
                .put("code", s)
                .setRequestCode(BaseQuestConfig.UNBIND_ZHIFUBAO_ACCOUNT_CODE));

    }

    /**
     * 上传日志
     */
    public static void postLogcat(NetRequestHandler netRequestHandler, String Logcat) {
        if (netRequestHandler != null) {
            netRequestHandler.requestAsynPost(Config.UAction()
                    .setUrl(BaseQuestConfig.POST_LOGCAT)
                    .put("Logcat", Logcat)
                    .setRequestCode(BaseQuestConfig.POST_LOGCAT_CODE));
        }

    }

    /**
     * 公用验证码
     *
     * @param netRequestHandler
     */
    public static void getCommonSms(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynPost(Config.UAction()
                .setUrl(BaseQuestConfig.POST_COMMON_SMS)
                .setRequestCode(BaseQuestConfig.POST_COMMON_SMS_CODE));

    }

    /**
     * 佣金转余额
     *
     * @param netRequestHandler
     */
    public static void toBalance(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynPost(Config.UAction()
                .setUrl(BaseQuestConfig.POST_COMMISSION_TRANSFER)
                .setRequestCode(BaseQuestConfig.POST_COMMISSION_TRANSFER_CODE));
    }

    /**
     * 获取银行列表
     *
     * @param netRequestHandler
     */
    public static void getBankList(NetRequestHandler netRequestHandler) {
        netRequestHandler.requestAsynGet(Config.UAction()
                .setUrl(GET_BANK_LIST)
                .setRequestCode(GET_BANK_LIST_CODE)
                .setTypeToken(new TypeToken<List<BankListBean>>() {
                }));
    }
}
