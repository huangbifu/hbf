package com.cnsunrun.chat;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.Toast;

import com.cnsunrun.BuildConfig;
import com.cnsunrun.R;
import com.cnsunrun.chat.fragment.ChatAddressBookFragment;
import com.cnsunrun.chat.fragment.ChatGroupFragment;
import com.cnsunrun.chat.fragment.ChatMessageFragment;
import com.cnsunrun.chat.mode.MemberInfo;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.base.ViewPagerFragmentAdapter;
import com.cnsunrun.common.config.RongIMHelper;
import com.cnsunrun.common.dialog.VersionTipDiglog;
import com.cnsunrun.common.event.MessageEvent;
import com.cnsunrun.common.event.RedDotEvent;
import com.cnsunrun.common.event.RefreshIMTokenEvent;
import com.cnsunrun.common.model.ApkVersion;
import com.cnsunrun.common.model.BottomTab;
import com.cnsunrun.common.model.LoginInfo;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.quest.TokenRequestPreProccess;
import com.cnsunrun.common.util.LoginUtil;
import com.cnsunrun.common.util.RedDotUtil;
import com.cnsunrun.common.util.UpdateUtils;
import com.cnsunrun.mine.fragment.NewUserMineFragment;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;
import com.flyco.tablayout.widget.MsgView;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;
import com.sunrun.sunrunframwork.utils.BaseStartIntent;
import com.sunrun.sunrunframwork.utils.DataCleanManager;
import com.sunrun.sunrunframwork.utils.Utils;
import com.sunrun.sunrunframwork.utils.log.Logger;
import com.tencent.bugly.beta.Beta;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import static com.cnsunrun.common.config.RongIMHelper.isConnect;
import static com.cnsunrun.common.quest.BaseQuestConfig.CHECK_UP_DATA_VERSION_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GET_TOKEN_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_MEMBER_INFO_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.USER_OAUTH_TOKEN_CODE;
import static com.cnsunrun.common.util.RedDotUtil.CODE_50;
import static com.cnsunrun.common.util.RedDotUtil.CODE_IM;
import static com.sunrun.sunrunframwork.uiutils.DisplayUtil.dp2px;
import static io.rong.imlib.RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED;


/**
 * 咵天导航
 */
public class ChatNavigatorActivity extends LBaseActivity {
    private List<Fragment> baseFragments;
    ViewPagerFragmentAdapter mVPAdapter;
    private String[] mTitles = {"群组", "消息", "通讯录", "我的"};
    private int[] mIconUnselectIds = {
            R.drawable.common_group_icon_normal,
            R.drawable.common_chat_icon_normal,
            R.drawable.common_addressbook_icon_normal,
            R.drawable.common_mine_icon_normal
    };
    private int[] mIconSelectIds = {
            R.drawable.common_group_icon_active,
            R.drawable.common_chat_icon_active,
            R.drawable.common_addressbook_icon_active,
            R.drawable.common_mine_icon_activel
    };
    @BindView(R.id.vp_main_center)
    ViewPager mViewPager;
    @BindView(R.id.tab_layout)
    CommonTabLayout mTabLayout;
    AHandler.Task refreshTokenTask,refreshImToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Logger.E("初始化");
        initEventBus();
        setContentView(R.layout.activity_chat_navigator);
        baseFragments = new ArrayList<>();
        baseFragments.add(ChatGroupFragment.newInstance());
        baseFragments.add(ChatMessageFragment.newInstance());
        baseFragments.add(ChatAddressBookFragment.newInstance());
        baseFragments.add(NewUserMineFragment.newInstance());
        mVPAdapter = new ViewPagerFragmentAdapter(this.getSupportFragmentManager());
        mVPAdapter.setFragments(baseFragments);
        mViewPager.setAdapter(mVPAdapter);
        mViewPager.setOffscreenPageLimit(baseFragments.size());
        setTabData(mTabLayout, mTitles, mIconSelectIds, mIconUnselectIds);
        bindTabEvent(mTabLayout, mViewPager);
        mViewPager.setCurrentItem(0);
        setMsgViewMargin(mTabLayout);
        LoginInfo loginInfo = Config.getLoginInfo();
//        BaseQuestStart.NeighborhoodImMemberInfo(that,loginInfo.getId());




//        if(!RongIMHelper.isConnect()){
//            EventBus.getDefault().post(RefreshIMTokenEvent.newInstance());//刷新Token
//        }

        RongIMClient.ConnectionStatusListener.ConnectionStatus currentConnectionStatus = RongIM.getInstance().getCurrentConnectionStatus();
        refreshIMToken(RefreshIMTokenEvent.newInstance());
        if (currentConnectionStatus != CONNECTED) {
            RongIMHelper.connectFocus();
        }
        BaseQuestStart.checkUpDataVersion(this, UpdateUtils.getVersionName(that));

        DataCleanManager.clearAllCache(that);
//        AHandler.runTask( refreshTokenTask=new AHandler.Task() {
//            @Override
//            public void update() {
//                super.update();
//                requestToken();
//            }
//        },10*1000,60*1000*5);

        AHandler.runTask( refreshImToken=new AHandler.Task() {
            @Override
            public void update() {
                super.update();
                Logger.D("检查融云连接状态: "+RongIMHelper.isConnect());
                refreshIMToken(RefreshIMTokenEvent.newInstance());
            }
        },0,10*1000);


    }



    public void requestToken(){
        BaseQuestStart.UserOauthToken(this);
    }
    /**
     * 检查更新
     */
    protected void checkUpData(final ApkVersion apkversion,String msg) {
//
        if("21".equals(msg))return;
        VersionTipDiglog.newInstance()
                .setUpdate(true)
                .setOnSubmitAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 下载APK并替换安装
                if (Environment.getExternalStorageState().equals(
                        Environment.MEDIA_MOUNTED)) {
                    BaseStartIntent.OpenUrl(that,apkversion.path);
//                    Intent service = new Intent(that, DownLoadService.class);
//                    service.putExtra(DOWNLOAD_URL, apkversion.path);
//                    that.startService(service);
                } else {
                    Toast.makeText(that, "未检测到SD卡，请插入SD卡再运行",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }).show(getSupportFragmentManager(), "ChatNavigatorActivity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        RedDotUtil.setRedDot(mTabLayout, 1, CODE_IM);
        RedDotUtil.setRedDot(mTabLayout, 2, CODE_50);
//        RedDotUtil.setRedDot(mTabLayout,2,CODES_DYNAMICS);
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case USER_OAUTH_TOKEN_CODE:
                if(bean.status==1){
                    LoginInfo.TokenBean tokenBean=bean.Data();//刷新token
                    Config.getLoginInfo().setToken(tokenBean);
                }else {
                }
                break;
            case NEIGHBORHOOD_IM_MEMBER_INFO_CODE:
                if (bean.status == 1) {
                    MemberInfo memberInfo = bean.Data();
                    Uri parse = Uri.parse(String.valueOf(memberInfo.avatar));
                    UserInfo userInfo = new UserInfo(memberInfo.id, memberInfo.getNickname(), parse);
                    RongIM.getInstance().setCurrentUserInfo(userInfo);
                    RongIM.getInstance().refreshUserInfoCache(userInfo);
                }
                break;
            case NEIGHBORHOOD_IM_GET_TOKEN_CODE:
                if (bean.status == 1) {
                    JSONObject jsonObject = JsonDeal.createJsonObj(bean.toString());
                    String imToken = jsonObject.optString("token");
//                    UIUtils.shortM("初始化");
                    RongIMHelper.connectFocus(imToken);

                } else {
                    if (bean.status == 0) {
                        UIUtils.shortM(bean);
                    }
                }
                break;
            case CHECK_UP_DATA_VERSION_CODE: //检查更新

                if (bean.status == 1) {
                    ApkVersion apkversion = (ApkVersion) bean.data;
                    if (apkversion != null) {
                        checkUpData(apkversion,bean.msg);
                    }
                } else {
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }


    @Override
    public void onBackPressed() {
        if (!Utils.isQuck(2000)) {
            UIUtils.shortM("再按一次退出程序");
        } else {
            finish();
        }
    }


    /**
     * 设置标签视图内容,
     *
     * @param tabLayout
     * @param titles        文字
     * @param selDrawable   图标/选中
     * @param unselDrawable 图标/未选中
     */
    public void setTabData(CommonTabLayout tabLayout, String titles[], int selDrawable[], int unselDrawable[]) {
        ArrayList<CustomTabEntity> customTabEntities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            customTabEntities.add(BottomTab.createTab(titles[i], selDrawable[i], unselDrawable[i]));
        }
        tabLayout.setTabData(customTabEntities);
        setMsgViewMargin(tabLayout);
    }

    void setMsgViewMargin(CommonTabLayout tabLayout) {

        for (int i = 0, len = tabLayout.getTabCount(); i < len; i++) {
            tabLayout.setMsgMargin(i, -3, 0);
            MsgView msgView = tabLayout.getMsgView(i);
            msgView.setBackgroundColor(Color.parseColor("#FF3F4E"));
            UIUtils.setViewWH(msgView, dp2px(this, 7), dp2px(this, 7));
        }
    }

    int currentIndex;

    /**
     * 绑定标签视图和viewPager 事件
     *
     * @param tabLayout
     * @param viewPager
     */
    void bindTabEvent(final CommonTabLayout tabLayout, final ViewPager viewPager) {
        tabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                viewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tabLayout.setCurrentTab(position);


            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void setCurrnetPage(int index) {
        mViewPager.setCurrentItem(index);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshIMToken(RefreshIMTokenEvent refreshIMTokenEvent) {
        if (!isConnect()) {
//            UIUtils.showLoadDialog(that,"加载中...");
            Logger.D("刷新Token尝试重连 ");
            BaseQuestStart.NeighborhoodImGetToken(this);
        }
    }

    @Override
    protected void onDestroy() {
        AHandler.cancel(refreshTokenTask);
        AHandler.cancel(refreshImToken);
        super.onDestroy();
    }

    @Subscribe
    public void eventBusMethod(MessageEvent event) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRedDot(RedDotEvent redDotEvent) {
        RedDotUtil.setRedDot(mTabLayout, 2, CODE_50);
    }

}
