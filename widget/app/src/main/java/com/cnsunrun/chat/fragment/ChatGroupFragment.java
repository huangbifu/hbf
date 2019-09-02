package com.cnsunrun.chat.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.GameTestActivity;
import com.cnsunrun.chat.adapters.ChatPageIndicatorAdapter;
import com.cnsunrun.chat.adapters.ImagePagerAdapter;
import com.cnsunrun.chat.dialog.MessageTipDialog;
import com.cnsunrun.chat.mode.BannerBean;
import com.cnsunrun.chat.mode.GroupItemBean;
import com.cnsunrun.chat.mode.NoticeInfoBean;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseFragment;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.event.DefaultEvent;
import com.cnsunrun.common.intent.StartIntent;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.RedDotUtil;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.common.widget.MultipleViewPager;
import com.cnsunrun.common.widget.VerticalSwitchLayout;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.cnsunrun.messages.mode.NoticeListBean;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.LayoutUtil;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.Utils;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static com.cnsunrun.common.config.Const.EVENT_CHAT_COMMENT;
import static com.cnsunrun.common.config.Const.EVENT_CHAT_REFRESH_HOME_PAGE;
import static com.cnsunrun.common.quest.BaseQuestConfig.GAME_GROUP_LIST_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.GAME_RULE_URL;
import static com.cnsunrun.common.quest.BaseQuestConfig.GET_BANNER_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.GET_NOTICE_CODE;

/**
 * 扫雷-首页
 * Created by WQ on 2017/11/7.
 */

public class ChatGroupFragment extends LBaseFragment {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.vslNotices)
    VerticalSwitchLayout vslNotices;
    @BindView(R.id.multiViewPager)
    MultipleViewPager multiViewPager;
    @BindView(R.id.rlvIndicator)
    RecyclerView rlvIndicator;
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.tvRule)
    TextView tvRule;
    @BindView(R.id.btnStartGame)
    QMUIRoundButton btnStartGame;
    @BindView(R.id.imgCode)
    ImageView imgCode;
    @BindView(R.id.activity_search)
    LinearLayout activitySearch;
    private ChatPageIndicatorAdapter chatPageIndicatorAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RedDotUtil.setNumber(0, RedDotUtil.CODES_DYNAMICS);//清空动态红点数
        rlvIndicator.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.HORIZONTAL, false));
        chatPageIndicatorAdapter = new ChatPageIndicatorAdapter(null);
        rlvIndicator.setAdapter(chatPageIndicatorAdapter);
        BaseQuestStart.get_banner(this);
        BaseQuestStart.get_notice(this,1);
        multiViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                chatPageIndicatorAdapter.setSelectIndex(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        titleBar.setLeftAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonIntent.startNoticeMessageActivity(that);
            }
        });
        BaseQuestStart.game_group_list(this,1);
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case GET_BANNER_CODE:
                final List<BannerBean> data=bean.Data();
                List<String> bannerList = TestTool.list2StringList(data);
                multiViewPager.setAdapter(new ImagePagerAdapter<String>(that,bannerList) {
                    @Override
                    public View getView(int index, View view, ViewGroup container) {
                        View view1 = super.getView(index, view, container);
                        LayoutUtil.setMargin(view1.findViewById(R.id.img),20,0,20,0);
                        return view1;
                    }

                    @Override
                    protected void loadImage(@NonNull ImageView imageView, @NonNull String s) {
                        GlideMediaLoader.load(that,imageView,s);
                    }
                }.setOnBannerClickListener(new ImagePagerAdapter.OnBannerClickListener() {
                    @Override
                    public void onBannerClick(int i, Object o) {
                        if(EmptyDeal.isEmpy(data.get(i).url))return;
                        CommonIntent.OpenUrl(that,data.get(i).url);
                    }
                }));
                chatPageIndicatorAdapter.setNewData(bannerList);
                multiViewPager.setCurrentItem(0);
                chatPageIndicatorAdapter.setSelectIndex(0);
                if(bean.status!=1){
                    UIUtils.shortM(bean);
                }
                break;
            case GAME_GROUP_LIST_CODE:
                if(bean.status==1){
                    List<GroupItemBean> groupItemBeans=bean.Data();
                    if(!EmptyDeal.isEmpy(groupItemBeans))
                    {
                        for (GroupItemBean groupItemBean : groupItemBeans) {
                            RongIM.getInstance().removeConversation(Conversation.ConversationType.GROUP, groupItemBean.id, new RongIMClient.ResultCallback<Boolean>() {
                                @Override
                                public void onSuccess(Boolean aBoolean) {

                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                            RongIMClient.getInstance().quitGroup(groupItemBean.id, new RongIMClient.OperationCallback() {
                                @Override
                                public void onSuccess() {

                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {

                                }
                            });
                        }
                    }

                }
                break;
            case GET_NOTICE_CODE:
                NoticeListBean.NoticeListPage listPage=bean.Data();
               final List<NoticeListBean>notices;
                if(listPage!=null) {
                    notices=listPage.list;
                }else {
                    notices=new ArrayList<>();
                }

                List<String> noticeList = TestTool.list2StringList(notices);
                if(!EmptyDeal.isEmpy(noticeList)){
                    String tip = noticeList.get(0);
                    MessageTipDialog.newInstance().setContentTxt(tip)
                            .setTitleTxt("公告")
                            .show(getChildFragmentManager(),"MessageTipDialog");
                }
                vslNotices.setContent(noticeList);
                vslNotices.setOnItemClickListener(new VerticalSwitchLayout.OnItemClickListener() {
                    @Override
                    public void onClick(int index) {
                        NoticeListBean noticeInfoBean = notices.get(index);
                        CommonIntent.startNoticeMessageDetailsActivity(that, noticeInfoBean.id, noticeInfoBean.h5);
                    }
                });
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public static ChatGroupFragment newInstance() {

        Bundle args = new Bundle();
        ChatGroupFragment fragment = new ChatGroupFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chat_group;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DefaultEvent event) {
        switch (event.getAction()) {
            case EVENT_CHAT_REFRESH_HOME_PAGE:
            case EVENT_CHAT_COMMENT:
//                pageLimitDelegate.refreshPage();
                break;
        }
    }


    @OnClick({R.id.tvTitle, R.id.tvRule,R.id.btnStartGame,R.id.imgGame2,R.id.imgGame3})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tvTitle:
                break;
            case R.id.tvRule:
                CommonIntent.startWebDetailsActivity(that,GAME_RULE_URL,"游戏规则");
                break;
            case R.id.btnStartGame:
                CommonIntent.startGameGroupListActivity(that);
                break;
            case R.id.imgGame2:
//                CommonIntent.startJielongGameGroupListActivity(that);
                CommonIntent.startGameTestActivity(that );
                break;
            case R.id.imgGame3:
                CommonIntent.startGameTestActivity(that );
                break;

        }
    }
}
