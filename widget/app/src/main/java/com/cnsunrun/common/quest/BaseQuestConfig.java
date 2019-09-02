package com.cnsunrun.common.quest;


/**
 * 接口参数帮助类
 */
public class BaseQuestConfig implements NetQuestConfig {


    public static final String LUCKDRAW_URL = HTTP_API+"App/User/LuckDraw/index?member_id=";
    public static final String GAME_RULE_URL = HTTP_API+"App/User/Game/index";

    public static final String SIGN_RULE_URL = HTTP_API+"App/User/Game/sign_rule";



    public static String TESTPAY = HTTP_API + "Community/Parking/pay";
    public static final int TESTPAY_CODE = 0x110;
    public static String TEST_IMAGE = "http://s9.knowsky.com/bizhi/l/35001-45000/200952904241438473283.jpg";

    //获取图形验证码（注册，短信登录）
    public static String GET_REGISTERED_PIC_VERIFICATION_CODE_URL = HTTP_API + "App/User/Register/get_code_verification";
    public static final int QUEST_GET_REGISTERED_PIC_VERIFICATION_CODE = 0X001;

    //获取验证码（注册）
    public static String GET_REGISTERED_VERIFICATION_CODE_URL = HTTP_API + "App/User/Register/register_password_sms";
    public static final int QUEST_GET_REGISTERED_VERIFICATION_CODE = 0X002;

    //注册
    public static String REGISTER_URL = HTTP_API + "App/User/Register/register";
    public static final int QUEST_REGISTER_CODE = 0X003;

    //登录
    public static String LOGIN_URL = HTTP_API + "App/User/Login/login";
    public static final int QUEST_LOGIN_CODE = 0X004;

    //获取验证码（忘记密码）
    public static String GET_FORGET_PASSWORD_VERIFICATION_CODE_URL = HTTP_API + "App/User/Login/forget_password_sms";
    public static final int QUEST_GET_FORGET_PASSWORD_VERIFICATION_CODE = 0X005;

    //修改密码
    public static String MODIFY_PASSWORD_URL = HTTP_API + "App/User/Login/forget_password";
    public static final int MODIFY_PASSWORD_CODE = 0X006;

    //获取验证码（注册）
    public static String GET_SMS_LOGIN_VERIFICATION_CODE_URL = HTTP_API + "App/User/Login/get_login_sms";
    public static final int QUEST_GET_SMS_LOGIN_VERIFICATION_CODE = 0X007;

    //短信登录
    public static String SMS_LOGIN_URL = HTTP_API + "App/User/Login/login_sms";
    public static final int QUEST_SMS_LOGIN_CODE = 0X008;

    //钱包 - 充值
    public static final String USER_RECHARGE = HTTP_API + "App/User/Wallet/recharge";
    public static final int USER_RECHARGE_CODE = 0X009;

    //个人中心-获取用户信息
    public static final String GET_USER_DATA = HTTP_API + "App/User/Info/info";
    public static final int GET_USER_DATA_CODE = 0X010;

    //获取充值金额列表
    public static final String GET_RECHAR_INFO = HTTP_API + "App/User/Wallet/recharge_info_v2";
    public static final int GET_RECHAR_INFO_CODE = 0X011;

    //个人中心 - 设置邀请人
    public static final String POST_INVITE = HTTP_API + "App/User/Info/set_invite_code";
    public static final int POST_INVITE_CODE = 0X012;

    //个人中心 - 返佣记录
    public static final String GET_COMMOISSION =HTTP_API + "App/User/Info/get_commission_record";
    public static final int GET_COMMOISSION_CODE =0X013;

    //提现 - 提现信息
    public static final String GET_WITH_DRAWAL_INFO =HTTP_API + "App/User/Wallet/withdraw_info";
    public static final int GET_WITH_DRAWAL_INFO_CODE =0X014;
    //我的 - 设置 - 账户与安全
    public static String USER_SAFE_INDEX_ = HTTP_API + "App/User/Safe/index";
    public static final int USER_SAFE_INDEX_CODE = 0x0142;
    //我的 - 设置 - 账户与安全 - 支付密码 - 设置
    public static String USER_SAFE_SET_DEAL_PASSWORD_ = HTTP_API + "App/User/Safe/set_deal_password";
    public static final int USER_SAFE_SET_DEAL_PASSWORD_CODE = 0x0130;
    //我的 - 钱包 - 提现-获取授权信息
    public static String USER_WALLET_LOGIN_ALIPAY_ = HTTP_API + "App/User/Wallet/login_alipay";
    public static final int USER_WALLET_LOGIN_ALIPAY_CODE = 0x0151;
    //我的 - 钱包 - 提现
    public static String USER_WALLET_WITHDRAW_ = HTTP_API + "App/User/Wallet/withdraw_v2";
    public static final int USER_WALLET_WITHDRAW_CODE = 0x0152;
    //修改头像
    public static String GET_CHANGE_PHOTO_URL = HTTP_API + "App/User/Info/avatar";
    public static final int QUEST_GET_CHANGE_PHOTO_CODE = 0X017;
    //保存个人资料
    public static String GET_SAVE_USER_INFO_URL = HTTP_API + "App/User/Info/info";
    public static final int QUEST_GET_SAVE_USER_INFO_CODE = 0X018;
    //我的钱包
    public static final String GET_WALLET_INDEX =HTTP_API + "App/User/Wallet/index";
    public static final int GET_WALLET_INDEX_CODE =0X015;


    //推广海报
    public static final String GET_POP_LOG =HTTP_API + "App/User/Info/pop_posters";
    public static final int GET_POP_LOG_CODE =0X016;
    //个人中心 - 推广 - 获取下一级
    public static final String GET_NEXT_LEVEL =HTTP_API + "App/User/Info/get_commission_list";
    public static final int GET_NEXT_LEVEL_CODE = 0X017;
    //钱包 - 转账
    public static final String POST_TRANSFER =HTTP_API + "App/User/Wallet/transfer";
    public static final int POST_TRANSFER_CODE =0X018;

    //签到明细
    public static final String SIGN_RECORD =HTTP_API +"App/User/Info/sign_record";
    public static final int SIGN_RECORD_CODE =0X022;

    //签到
    public static final String SIGN_IN =HTTP_API +"App/User/Info/sign_in";
    public static final int SIGN_IN_CODE =0X023;

    //签到信息
    //GET - App/User/Info/sign_info
    public static final String SIGN_INFO =HTTP_API +"App/User/Info/sign_info";
    public static final int SIGN_INFO_CODE =0X024;





    //社区 - 门禁 - 呼叫记录
    public static String COMMUNITY_DOOR_CALL_ = HTTP_API + "Community/Door/call";
    public static final int COMMUNITY_DOOR_CALL_CODE = 0x0163;

    //社区 - 门禁 - 开门记录
    public static String COMMUNITY_DOOR_ACCESS_GET_ = HTTP_API + "Community/Door/access_get";
    public static final int COMMUNITY_DOOR_ACCESS_GET_CODE = 0x0162;

    //社区 - 门禁 - 提交开门记录
    public static String COMMUNITY_DOOR_ACCESS_ = HTTP_API + "Community/Door/access_post";
    public static final int COMMUNITY_DOOR_ACCESS_CODE = 0x0160;

    //社区 - 门禁 - 切换房屋
    public static String COMMUNITY_DOOR_SWITCH_UNIT_ = HTTP_API + "Community/Door/switch_unit";
    public static final int COMMUNITY_DOOR_SWITCH_UNIT_CODE = 0x0159;

    //社区 - 门禁 - 我的房屋
    public static String COMMUNITY_DOOR_UNIT_LIST_ = HTTP_API + "Community/Door/unit_list";
    public static final int COMMUNITY_DOOR_UNIT_LIST_CODE = 0x0158;

    //社区 - 门禁 - 首页
    public static String COMMUNITY_DOOR_INDEX_ = HTTP_API + "Community/Door/index";
    public static final int COMMUNITY_DOOR_INDEX_CODE = 0x0157;

    //社区 - 门禁 - 登录
    public static String COMMUNITY_DOOR_LOGIN_ = HTTP_API + "Community/Door/login";
    public static final int COMMUNITY_DOOR_LOGIN_CODE = 0x0156;

    //引导页
    public static String HOME_INDEX_GUIDE_PAGE_ = HTTP_API + "Home/Index/guide_page";
    public static final int HOME_INDEX_GUIDE_PAGE_CODE = 0x0153;

    //我的 - 我的收藏 - 删除
    public static String USER_COLLECT_DELETE_ = HTTP_API + "User/Collect/delete";

    public static final int USER_COLLECT_DELETE_CODE = 0x0150;

    //邻里 - 咵天 - 动态 - 个人动态 - 背景修改
    public static String NEIGHBORHOOD_IM_MEMBER_UPDATES_IMAGE_ = HTTP_API + "Neighborhood/IM/member_updates_image";

    public static final int NEIGHBORHOOD_IM_MEMBER_UPDATES_IMAGE_CODE = 0x01154;

    //我的 - 设置,帮助及版本信息
    public static String USER_INDEX_INDEX_SET_ = HTTP_API + "User/Index/index_set";
    public static final int USER_INDEX_INDEX_SET_CODE = 0x0153;






    //我的 - 找回密码 - 验证短信验证码
    public static String USER_LOGIN_VERIFY_MOBILE_SMS_ = HTTP_API + "User/Login/verify_mobile_sms";

    public static final int USER_LOGIN_VERIFY_MOBILE_SMS_CODE = 0x0149;
    //邻里 - 咵天 - 群组 - 群名称
    public static String NEIGHBORHOOD_IM_GROUP_TITLE_ = HTTP_API + "App/IM/IM/group_title";

    public static final int NEIGHBORHOOD_IM_GROUP_TITLE_CODE = 0x0148;

    //邻里 - 咵天 - 群组 - 设置名片
    public static String NEIGHBORHOOD_IM_GROUP_REMARK_ = HTTP_API + "App/IM/IM/group_remark";

    public static final int NEIGHBORHOOD_IM_GROUP_REMARK_CODE = 0x0147;

    //我的 - 钱包
    public static String USER_WALLET_INDEX_ = HTTP_API + "User/Wallet/index";

    public static final int USER_WALLET_INDEX_CODE = 0x0138;

    //我的 - 设置 - 帮助 - 详情
    public static String USER_HELP_DETAIL_ = HTTP_API + "User/Help/detail";

    public static final int USER_HELP_DETAIL_CODE = 0x0137;

    //我的 - 设置 - 帮助 - 分类问题列表
    public static String USER_HELP_LIST_CATEGORY_ = HTTP_API + "User/Help/list_category";

    public static final int USER_HELP_LIST_CATEGORY_CODE = 0x0136;

    //我的 - 设置 - 帮助
    public static String USER_HELP_INDEX_ = HTTP_API + "User/Help/index";

    public static final int USER_HELP_INDEX_CODE = 0x0135;

    //我的 - 设置 - 账户与安全 - 支付密码 - 忘记 - 重置
    public static String USER_SAFE_RESET_DEAL_PASSWORD_ = HTTP_API + "User/Safe/reset_deal_password";

    public static final int USER_SAFE_RESET_DEAL_PASSWORD_CODE = 0x0134;

    //我的 - 设置 - 账户与安全 - 支付密码 - 忘记 - 校验验证码
    public static String USER_SAFE_VERIFY_DEAL_PASSWORD_SMS_ = HTTP_API + "User/Safe/verify_deal_password_sms";

    public static final int USER_SAFE_VERIFY_DEAL_PASSWORD_SMS_CODE = 0x0133;

    //我的 - 设置 - 账户与安全 - 支付密码 - 忘记 - 短信验证码
    public static String USER_SAFE_FORGET_DEAL_PASSWORD_SMS_ = HTTP_API + "User/Safe/forget_deal_password_sms";

    public static final int USER_SAFE_FORGET_DEAL_PASSWORD_SMS_CODE = 0x0132;

    //我的 - 设置 - 账户与安全 - 密码 - 忘记 - 短信验证码
    public static String USER_LOGIN_FORGET_PASSWORD_SMS_ = HTTP_API + "User/Login/forget_password_sms";

    public static final int USER_LOGIN_FORGET_PASSWORD_SMS_CODE = 0x0140;

    //我的 - 设置 - 账户与安全 - 密码 - 设置密码
    public static String USER_LOGIN_FORGET_PASSWORD_ = HTTP_API + "User/Login/forget_password";

    public static final int USER_LOGIN_FORGET_PASSWORD_CODE = 0x0141;

    //我的 - 设置 - 账户与安全 - 支付密码 - 修改
    public static String USER_SAFE_CHANGE_DEAL_PASSWORD_ = HTTP_API + "User/Safe/change_deal_password";

    public static final int USER_SAFE_CHANGE_DEAL_PASSWORD_CODE = 0x0131;




    //我的 - 设置 - 账户与安全 - 修改手机号码
    public static String USER_SAFE_CHANGE_MOBILE_ = HTTP_API + "User/Safe/change_mobile";

    public static final int USER_SAFE_CHANGE_MOBILE_CODE = 0x0128;

    //我的 - 设置 - 账户与安全 - 修改手机号码 - 新手机验证码
    public static String USER_SAFE_NEW_MOBILE_SMS_ = HTTP_API + "User/Safe/new_mobile_sms";

    public static final int USER_SAFE_NEW_MOBILE_SMS_CODE = 0x0127;

    //我的 - 设置 - 账户与安全 - 修改手机号码 - 校验验证码
    public static String USER_SAFE_VERIFY_MOBILE_SMS_ = HTTP_API + "User/Safe/verify_mobile_sms";

    public static final int USER_SAFE_VERIFY_MOBILE_SMS_CODE = 0x0126;

    //我的 - 设置 - 账户与安全 - 修改手机号码 - 原手机验证码
    public static String USER_SAFE_CHANGE_MOBILE_SMS_ = HTTP_API + "User/Safe/change_mobile_sms";

    public static final int USER_SAFE_CHANGE_MOBILE_SMS_CODE = 0x0125;




    //我的 - 我的收藏 - 详情
    public static String USER_COLLECT_DETAIL_ = HTTP_API + "User/Collect/detail";

    public static final int USER_COLLECT_DETAIL_CODE = 0x0124;

    //我的 - 我的收藏
    public static String USER_COLLECT_INDEX_ = HTTP_API + "User/Collect/index";

    public static final int USER_COLLECT_INDEX_CODE = 0x0123;

    //我的 - 个人资料 - 用户名
    public static String USER_INFO_USERNAME_EXISTS_ = HTTP_API + "User/Info/username_exists";

    public static final int USER_INFO_USERNAME_EXISTS_CODE = 0x0122;

    //我的 - 个人资料
    public static String USER_INFO_ = HTTP_API + "User/Info/info";

    public static final int USER_INFO_CODE = 0x0121;

    //我的 - 个人资料 - 头像
    public static String USER_INFO_AVATAR_ = HTTP_API + "User/Info/avatar";

    public static final int USER_INFO_AVATAR_CODE = 0x0120;

    //我的 - 个人资料 - 头像
    public static String USER_INFO_AVATAR_1 = HTTP_API + "User/Info/avatar";

    public static final int USER_INFO_AVATAR_1CODE = 0x0119;

    //我的 - 个人资料
    public static String USER_INFO_1 = HTTP_API + "User/Info/info";

    public static final int USER_INFO_1CODE = 0x0118;

    //邻里 - 咵天 - 动态 - 删除
    public static String NEIGHBORHOOD_IM_UPDATES_DELETE_ = HTTP_API + "App/IM/IM/updates_delete";

    public static final int NEIGHBORHOOD_IM_UPDATES_DELETE_CODE = 0x0145;


    //邻里 - 咵天 - 动态 - 收藏 - 附件
    public static String NEIGHBORHOOD_IM_COLLECT_UPDATES_ATTACHMENT_ = HTTP_API + "App/IM/IM/collect_updates_attachment";

    public static final int NEIGHBORHOOD_IM_COLLECT_UPDATES_ATTACHMENT_CODE = 0x0141;

    //邻里 - 咵天 - 动态 - 收藏 - 文字
    public static String NEIGHBORHOOD_IM_COLLECT_UPDATES_CONTENT_ = HTTP_API + "App/IM/IM/collect_updates_content";

    public static final int NEIGHBORHOOD_IM_COLLECT_UPDATES_CONTENT_CODE = 0x0140;

    //邻里 - 咵天 - 动态 - 动态详情
    public static String NEIGHBORHOOD_IM_UPDATES_DETAIL_ = HTTP_API + "App/IM/IM/updates_detail";

    public static final int NEIGHBORHOOD_IM_UPDATES_DETAIL_CODE = 0x0117;

    //邻里 - 咵天 - 动态 - 个人动态
    public static String NEIGHBORHOOD_IM_UPDATES_HOME_ = HTTP_API + "App/IM/IM/updates_home";

    public static final int NEIGHBORHOOD_IM_UPDATES_HOME_CODE = 0x0116;

    //邻里 - 咵天 - 动态 - 列表
    public static String NEIGHBORHOOD_IM_UPDATES_LIST_ = HTTP_API + "App/IM/IM/updates_list";

    public static final int NEIGHBORHOOD_IM_UPDATES_LIST_CODE = 0x0115;

    //邻里 - 咵天 - 动态 - 点赞 - 取消
    public static String NEIGHBORHOOD_IM_UPDATES_LIKE_CANCEL_ = HTTP_API + "App/IM/IM/updates_like_cancel";

    public static final int NEIGHBORHOOD_IM_UPDATES_LIKE_CANCEL_CODE = 0x0114;

    //邻里 - 咵天 - 动态 - 点赞
    public static String NEIGHBORHOOD_IM_UPDATES_LIKE_ = HTTP_API + "App/IM/IM/updates_like";

    public static final int NEIGHBORHOOD_IM_UPDATES_LIKE_CODE = 0x0113;

    //邻里 - 咵天 - 动态 - 评论 - 回复
    public static String NEIGHBORHOOD_IM_UPDATES_COMMENT_REPLY_ = HTTP_API + "App/IM/IM/updates_comment_reply";

    public static final int NEIGHBORHOOD_IM_UPDATES_COMMENT_REPLY_CODE = 0x0112;

    //邻里 - 咵天 - 动态 - 评论
    public static String NEIGHBORHOOD_IM_UPDATES_COMMENT_ = HTTP_API + "App/IM/IM/updates_comment";

    public static final int NEIGHBORHOOD_IM_UPDATES_COMMENT_CODE = 0x0111;

    //邻里 - 咵天 - 动态 - 发布
    public static String NEIGHBORHOOD_IM_UPDATES_ADD_ = HTTP_API + "App/IM/IM/updates_add";

    public static final int NEIGHBORHOOD_IM_UPDATES_ADD_CODE = 0x0110;

    //邻里 - 咵天 - 收藏 - 附件
    public static String NEIGHBORHOOD_IM_COLLECT_ATTACHMENT_ = HTTP_API + "App/IM/IM/collect_attachment";

    public static final int NEIGHBORHOOD_IM_COLLECT_ATTACHMENT_CODE = 0x0109;

    //邻里 - 咵天 - 收藏 - 文字
    public static String NEIGHBORHOOD_IM_COLLECT_CONTENT_ = HTTP_API + "App/IM/IM/collect_content";

    public static final int NEIGHBORHOOD_IM_COLLECT_CONTENT_CODE = 0x0108;

    //邻里 - 咵天 - 群组 - 公告
    public static String NEIGHBORHOOD_IM_GROUP_NOTICE_ = HTTP_API + "App/IM/IM/group_notice";

    public static final int NEIGHBORHOOD_IM_GROUP_NOTICE_CODE = 0x0107;

    //邻里 - 咵天 - 群组 - 设置聊天背景
    public static String NEIGHBORHOOD_IM_GROUP_IMAGE_ = HTTP_API + "App/IM/IM/group_image";

    public static final int NEIGHBORHOOD_IM_GROUP_IMAGE_CODE = 0x0106;

    //邻里 - 咵天 - 群组 - 置顶 - 取消
    public static String NEIGHBORHOOD_IM_GROUP_IS_TOP_CANCEL_ = HTTP_API + "App/IM/IM/group_is_top_cancel";

    public static final int NEIGHBORHOOD_IM_GROUP_IS_TOP_CANCEL_CODE = 0x0105;

    //邻里 - 咵天 - 群组 - 置顶
    public static String NEIGHBORHOOD_IM_GROUP_IS_TOP_ = HTTP_API + "App/IM/IM/group_is_top";

    public static final int NEIGHBORHOOD_IM_GROUP_IS_TOP_CODE = 0x0104;

    //邻里 - 咵天 - 群组 - 免打扰 - 取消
    public static String NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CANCEL_ = HTTP_API + "App/IM/IM/group_not_disturb_cancel";

    public static final int NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CANCEL_CODE = 0x0103;

    //邻里 - 咵天 - 群组 - 免打扰
    public static String NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_ = HTTP_API + "App/IM/IM/group_not_disturb";

    public static final int NEIGHBORHOOD_IM_GROUP_NOT_DISTURB_CODE = 0x0102;

    //邻里 - 咵天 - 群组 - 成员
    public static String NEIGHBORHOOD_IM_GROUP_MEMBER_LIST_ = HTTP_API + "App/IM/IM/group_member_list";

    public static final int NEIGHBORHOOD_IM_GROUP_MEMBER_LIST_CODE = 0x0101;

    //邻里 - 咵天 - 群组 - 信息
    public static String NEIGHBORHOOD_IM_GROUP_INFO_ = HTTP_API + "App/IM/IM/group_info";

    public static final int NEIGHBORHOOD_IM_GROUP_INFO_CODE = 0x0100;

    //邻里 - 咵天 - 群组 - 列表
    public static String NEIGHBORHOOD_IM_GROUP_LIST_ = HTTP_API + "App/IM/IM/group_list";

    public static final int NEIGHBORHOOD_IM_GROUP_LIST_CODE = 0x0099;

    //邻里 - 咵天 - 群组 - 删除群成员
    public static String NEIGHBORHOOD_IM_GROUP_MEMBER_DEL_ = HTTP_API + "App/IM/IM/group_member_del";

    public static final int NEIGHBORHOOD_IM_GROUP_MEMBER_DEL_CODE = 0x0098;

    //邻里 - 咵天 - 群组 - 退出
    public static String NEIGHBORHOOD_IM_GROUP_QUIT_ = HTTP_API + "App/IM/IM/group_quit";

    public static final int NEIGHBORHOOD_IM_GROUP_QUIT_CODE = 0x0097;

    //邻里 - 咵天 - 群组 - 加入
    public static String NEIGHBORHOOD_IM_GROUP_JOIN_ = HTTP_API + "App/IM/IM/group_join";

    public static final int NEIGHBORHOOD_IM_GROUP_JOIN_CODE = 0x0096;

    //邻里 - 咵天 - 群组 - 创建
    public static String NEIGHBORHOOD_IM_GROUP_ADD_ = HTTP_API + "App/IM/IM/group_add";

    public static final int NEIGHBORHOOD_IM_GROUP_ADD_CODE = 0x0095;

    //邻里 - 咵天 - 聊天信息 - 设置聊天背景
    public static String NEIGHBORHOOD_IM_CHAT_IMAGE_ = HTTP_API + "App/IM/IM/chat_image";

    public static final int NEIGHBORHOOD_IM_CHAT_IMAGE_CODE = 0x0094;

    //邻里 - 咵天 - 聊天信息 - 取消聊天置顶
    public static String NEIGHBORHOOD_IM_IS_TOP_CANCEL_ = HTTP_API + "App/IM/IM/is_top_cancel";

    public static final int NEIGHBORHOOD_IM_IS_TOP_CANCEL_CODE = 0x0093;

    //邻里 - 咵天 - 聊天信息 - 聊天置顶
    public static String NEIGHBORHOOD_IM_IS_TOP_ = HTTP_API + "App/IM/IM/is_top";

    public static final int NEIGHBORHOOD_IM_IS_TOP_CODE = 0x0092;

    //邻里 - 咵天 - 聊天信息 - 取消免打扰
    public static String NEIGHBORHOOD_IM_NOT_DISTURB_CANCEL_ = HTTP_API + "App/IM/IM/not_disturb_cancel";

    public static final int NEIGHBORHOOD_IM_NOT_DISTURB_CANCEL_CODE = 0x0091;

    //邻里 - 咵天 - 聊天信息 - 免打扰
    public static String NEIGHBORHOOD_IM_NOT_DISTURB_ = HTTP_API + "App/IM/IM/not_disturb";

    public static final int NEIGHBORHOOD_IM_NOT_DISTURB_CODE = 0x0090;

    //邻里 - 咵天 - 聊天信息
    public static String NEIGHBORHOOD_IM_CHAT_INFO_ = HTTP_API + "App/IM/IM/chat_info";

    public static final int NEIGHBORHOOD_IM_CHAT_INFO_CODE = 0x0089;
    //邻里 - 咵天 - 好友信息 - 删除
    public static String NEIGHBORHOOD_IM_FRIEND_DEL_ = HTTP_API + "App/IM/IM/friend_del";

    public static final int NEIGHBORHOOD_IM_FRIEND_DEL_CODE = 0x0088;

    //邻里 - 咵天 - 好友信息 - 设置备注
    public static String NEIGHBORHOOD_IM_FRIEND_REMARK_ = HTTP_API + "App/IM/IM/friend_remark";

    public static final int NEIGHBORHOOD_IM_FRIEND_REMARK_CODE = 0x0087;

    //邻里 - 咵天 - 通讯录
    public static String NEIGHBORHOOD_IM_FRIEND_LIST_ = HTTP_API + "App/IM/IM/friend_list";

    public static final int NEIGHBORHOOD_IM_FRIEND_LIST_CODE = 0x0086;

    //邻里 - 咵天 - 好友申请 - 拒绝
    public static String NEIGHBORHOOD_IM_FRIEND_REQUEST_REFUSE_ = HTTP_API + "App/IM/IM/friend_request_refuse";

    public static final int NEIGHBORHOOD_IM_FRIEND_REQUEST_REFUSE_CODE = 0x0084;

    //邻里 - 咵天 - 好友申请 - 接受
    public static String NEIGHBORHOOD_IM_FRIEND_REQUEST_ACCEPT_ = HTTP_API + "App/IM/IM/friend_request_accept";

    public static final int NEIGHBORHOOD_IM_FRIEND_REQUEST_ACCEPT_CODE = 0x0083;

    //邻里 - 咵天 - 好友申请列表
    public static String NEIGHBORHOOD_IM_FRIEND_REQUEST_LIST_ = HTTP_API + "App/IM/IM/friend_request_list";

    public static final int NEIGHBORHOOD_IM_FRIEND_REQUEST_LIST_CODE = 0x0082;

    //邻里 - 咵天 - 添加好友
    public static String NEIGHBORHOOD_IM_FRIEND_REQUEST_ = HTTP_API + "App/IM/IM/friend_request";

    public static final int NEIGHBORHOOD_IM_FRIEND_REQUEST_CODE = 0x0081;

    //邻里 - 咵天 - 用户信息
    public static String NEIGHBORHOOD_IM_MEMBER_INFO_ = HTTP_API + "App/IM/IM/member_info";

    //邻里 - 咵天 - 用户信息
    public static String NEIGHBORHOOD_IM_IM_MEMBER_INFO_ = HTTP_API + "App/IM/IM/im_member_info";


    public static final int NEIGHBORHOOD_IM_MEMBER_INFO_CODE = 0x0080;

    //邻里 - 咵天 - 添加好友 - 搜索
    public static String NEIGHBORHOOD_IM_MEMBER_SEARCH_ = HTTP_API + "App/IM/IM/member_search";

    public static final int NEIGHBORHOOD_IM_MEMBER_SEARCH_CODE = 0x0085;

    //邻里 - 咵天 - 获取 token
    public static String NEIGHBORHOOD_IM_GET_TOKEN_ = HTTP_API + "App/IM/IM/get_token";

    public static final int NEIGHBORHOOD_IM_GET_TOKEN_CODE = 0x0079;

    //我的 - 钱包 - 充值
    public static String USER_WALLET_RECHARGE_ = HTTP_API + "User/Wallet/recharge";


    //我的 - 登录
    public static String USER_LOGIN = HTTP_API + "User/Login/login";
    public static final int USER_LOGIN_CODE = 0X002;

    //我的 - 修改初始密码
    public static String CHANGE_PASSWORD = HTTP_API + "User/Safe/change_password";
    public static final int CHANGE_PASSWORD_CODE = 0X005;


    public static final int COMMUNITY_PROPERTY_SUGGESTIONS_TYPE_CODE = 0x0018;








    //我的 - 修改初始密码
    public static String USER_SAFE_CHANGE_PASSWORD_ = HTTP_API + "User/Safe/change_password";

    public static final int USER_SAFE_CHANGE_PASSWORD_CODE = 0x0005;

    //我的 - 刷新 token
    public static String USER_OAUTH_REFRESH_ = HTTP_API + "User/OAuth/refresh";

    public static final int USER_OAUTH_REFRESH_CODE = 0x0004;

    //我的 - 获取 token
    public static String USER_OAUTH_TOKEN_ = HTTP_API + "App/User/OAuth/token";

    public static final int USER_OAUTH_TOKEN_CODE = 0x0000;


    //IM - 获取轮播图
    public static String GET_BANNER = HTTP_API + "App/IM/IM/get_banner";
    public static final int GET_BANNER_CODE = 0x0210;
    //IM - 获取系统公告
    public static String GET_NOTICE = HTTP_API + "App/IM/IM/get_notice";
    public static final int GET_NOTICE_CODE = 0x0211;
    //社区 - 公告详情
    public static String GET_NOTICE_DETAIL = HTTP_API + "App/IM/IM/get_notice_detail";

    public static final int GET_NOTICE_DETAIL_CODE = 0x0009;

    // 个人中心 - 用户二维码
    public static String USER_QRCODE = HTTP_API + "App/User/Info/user_qrcode";
    public static final int USER_QRCODE_CODE = 0x0212;

    // IM - 红包 - 群组
    public static String GAME_GROUP_LIST = HTTP_API + "App/IM/IM/game_group_list";
    public static final int GAME_GROUP_LIST_CODE = 0x0213;
    // IM - 接龙红包 - 群组
    public static String GAME_PACKET_GET_GROUP = HTTP_API + "App/IM/Packet/get_group";
    public static final int GAME_PACKET_GET_GROUP_CODE = 0x0213;
    //获取验证码（更改手机）
    public static String GET_CHANGE_PHONE_VERIFICATION_CODE_URL = HTTP_API + "App/User/Info/change_mobile_sms";
    public static final int QUEST_GET_CHANGE_PHONE_VERIFICATION_CODE = 0X0214;


    //修改手机
    public static String MODIFY_PHONE_URL = HTTP_API + "App/User/Info/change_mobile";
    public static final int QUEST_MODIFY_PHONE_CODE = 0X0215;

    //获取验证码（重置支付密码）
    public static String GET_MODIFY_PAY_PASSWORD_VERIFICATION_CODE_URL = HTTP_API + "App/User/Safe/forget_deal_password_sms";
    public static final int QUEST_GET_MODIFY_PAY_PASSWORD_VERIFICATION_CODE = 0X0216;

    //重置支付密码
    public static String MODIFY_PAY_PASSWORD = HTTP_API + "App/User/Safe/reset_deal_password";
    public static final int QUEST_PAY_PASSWORD_CODE = 0X0217;

    //免密支付
    public static String NO_PAY_PWD_BET = HTTP_API + "App/User/Safe/secret_free";
    public static final int QUEST_NO_PAY_PWD_BET_CODE = 0X0218;

    //新增银行卡
    public static final String POST_ADD_BANK_CARD =HTTP_API + "App/User/Wallet/add_bank_card";
    public static final int POST_ADD_BANK_CARD_CODE =0X0219;

    //我的银行卡列表
    public static final String GET_BANK_CARD_LIST =HTTP_API + "App/User/Wallet/get_bank_card";
    public static final int GET_BANK_CARD_LIST_CODE =0X0220 ;

    //银行卡解绑
    public static final String POST_UNBIND_BANK_CARD =HTTP_API + "App/User/Wallet/bank_card_del";
    public static final int POST_UNBIND_BANK_CARD_CODE =0X0221;



    //    IM - 红包 - 发红包
    public static String SEND_REDPACK = HTTP_API + "App/IM/IM/send_redpack";
    public static final int SEND_REDPACK_CODE = 0x0237;
    //     发福利红包
    public static String SEND_WELFARE_REDPACK = HTTP_API + "App/IM/IM/send_welfare_redpack";
    public static final int SEND_WELFARE_REDPACK_CODE = 0x0239;
    // 接龙红包-发红包
    public static String PACKET_SEND_REDPACK = HTTP_API + "App/IM/Packet/send_pack";

    //  IM - 红包 - 抢红包
    public static String GRAB_REDPACK = HTTP_API + "App/IM/IM/grab_redpack";
    public static final int GRAB_REDPACK_CODE = 0x0238;
    /**
     * 抢接龙红包
     */
    public static String PACKET_GRAB_REDPACK = HTTP_API + "App/IM/Packet/grab_pack";
//    //  IM - 红包 - 抢红包-接龙
//    public static String IMTHUNDER_GRAB_REDPACK = HTTP_API + "App/IM/IMThunder/grab_redpack";
//    public static final int IMTHUNDER_GRAB_REDPACK_CODE = 0x0238;
//    //    IM - 红包 - 发红包
//    public static String IMTHUNDER_SEND_REDPACK = HTTP_API + "App/IM/IMThunder/send_redpack";
//    public static final int IMTHUNDER_SEND_REDPACK_CODE = 0x0237;

    //IM - 红包 - 红包记录
    public static String GET_REDPACK = HTTP_API + "App/IM/IM/get_redpack";
    public static final int GET_REDPACK_CODE = 0x0239;
    ///接龙红包-红包记录
    public static String PACKET_GET_REDPACK = HTTP_API + "App/IM/Packet/get_redpack";


    //我的红包记录
    public static String GET_MY_REDPACK_RECORD = HTTP_API + "App/IM/IM/get_my_redpack_record";
    public static final int GET_MY_REDPACK_RECORD_CODE = 0x0240;
    /**
     *     接龙红包-我的红包
     */
    public static String PACKET_GET_MY_REDPACK_RECORD = HTTP_API + "App/IM/Packet/get_my_redpack_record";




    //版本更新
    public static final String CHECK_UP_DATA_VERSION_URL = HTTP_API + "App/User/Info/check_version";
    public static final int CHECK_UP_DATA_VERSION_CODE = 0X0133;
    //验证支付密码
    public static final String POST_CHECK_PASSWORD =HTTP_API + "App/User/Wallet/pay_password";
    public static final int POST_CHECK_PASSWORD_CODE =0x0241;
    //线下充值-收款信息
    public static final String GET_OFFLINE_INFO =HTTP_API + "App/User/Wallet/offline_info";
    public static final int GET_OFFLINE_INFO_CODE =0x0242;
    //线下充值-提交充值信息审核
    public static final String POST_OFFLINE_INFO =HTTP_API + "App/User/Wallet/offline_recharge";
    public static final int POST_OFFLINE_INFO_CODE =0x0243;
    //获取支付宝列表
    public static final String GET_ALI_ACCOUNT =HTTP_API + "App/User/Wallet/get_ali_account";
    public static final int GET_ALI_ACCOUNT_CODE =0x0244;
    //解绑支付宝验证码
    public static final String GET_VERIFICATION = HTTP_API + "App/User/Wallet/ali_account_del_sms";
    public static final int QUEST_GET_VERIFICATION_CODE = 0x0245;
    //解绑支付宝
    public static final String UNBIND_ZHIFUBAO_ACCOUNT = HTTP_API + "App/User/Wallet/ali_account_del";
    public static final int UNBIND_ZHIFUBAO_ACCOUNT_CODE = 0x0246;
    //上传日志
    public static final String POST_LOGCAT = HTTP_API + "App/IM/IM/debug_info";
    public static final int POST_LOGCAT_CODE =0x0247;
    //公用验证码
    public static final String POST_COMMON_SMS = HTTP_API + "App/User/Wallet/common_sms";
    public static final int POST_COMMON_SMS_CODE = 0x0248 ;
    //佣金转余额
    public static final String POST_COMMISSION_TRANSFER =HTTP_API + "App/User/Info/commission_transfer";
    public static final int POST_COMMISSION_TRANSFER_CODE =0x0249;
    //银行卡列表
    public static final String GET_BANK_LIST =HTTP_API + "App/User/Wallet/bank_list";
    public static final int GET_BANK_LIST_CODE =0x0250;

}



