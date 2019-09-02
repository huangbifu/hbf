package com.cnsunrun.chat.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.config.BoxingConfig;
import com.bilibili.boxing.model.config.BoxingCropOption;
import com.bilibili.boxing.utils.BoxingFileHelper;
import com.bilibili.boxing_impl.ui.BoxingActivity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatSearchDynamicsAdapter;
import com.cnsunrun.chat.logic.DynamicsHeadViewLogic;
import com.cnsunrun.chat.mode.ChatDynamicsBean;
import com.cnsunrun.chat.mode.ChatDynamicsBean.ListBean;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseFragment;
import com.cnsunrun.common.boxing.BoxingUcrop;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.event.DefaultEvent;
import com.cnsunrun.common.logic.ImgDealLogic;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.util.RedDotUtil;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.common.config.Const.EVENT_CHAT_COMMENT;
import static com.cnsunrun.common.config.Const.EVENT_CHAT_REFRESH_HOME_PAGE;
import static com.cnsunrun.common.config.Const.EVENT_DELETE_DYNAMICS;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_MEMBER_UPDATES_IMAGE_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_HOME_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_LIKE_CANCEL_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_LIKE_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_LIST_CODE;

/**
 * 咵天-个人主页
 * Created by WQ on 2017/11/7.
 */

public class ChatHomePageFragment extends LBaseFragment {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    @BindView(R.id.editContent)
    EditText editContent;
    @BindView(R.id.layEmpty)
    View layEmpty;
    PageLimitDelegate<ListBean> pageLimitDelegate = new PageLimitDelegate<>(new PageLimitDelegate.DataProvider() {
        @Override
        public void loadData(int page) {
            if (id == null) {
//                BaseQuestStart.NeighborhoodImUpdatesList(ChatHomePageFragment.this, page, null);
            } else {
//                BaseQuestStart.NeighborhoodImUpdatesHome(ChatHomePageFragment.this, id, page);
            }
        }
    });
    DynamicsHeadViewLogic dynaHeadLogic;
    private String id;
    ChatSearchDynamicsAdapter repairsRecordAdapter;
    boolean isFriends=true;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        id = getArguments().getString("id");
        initEventBus();
        if (id != null) {
            titleBar.setLeftText("个人主页");
            titleBar.setRightVisible(View.GONE);
            titleBar.setLeftIcon(R.drawable.icon_goback_white);
            titleBar.setLeftAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } else {
            titleBar.setRightAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    CommonIntent.startChatAddDynamicsActivity(that);
                }
            });
        }
        repairsRecordAdapter = new ChatSearchDynamicsAdapter(null, false);
        View homepageHead = LayoutInflater.from(that).inflate(R.layout.view_chat_dynamics_homepage_head, (ViewGroup) view, false);
        repairsRecordAdapter.addHeaderView(homepageHead);

//        dynaHeadLogic = new DynamicsHeadViewLogic(homepageHead);
        recyclerView.setAdapter(repairsRecordAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        pageLimitDelegate.attach(refreshLayout, recyclerView, repairsRecordAdapter);
        repairsRecordAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                if(!isFriends)return;
                ListBean item = repairsRecordAdapter.getItem(position);
//                CommonIntent.startChatDynamicsDetailsActivity(that, item.id);
            }
        });
        repairsRecordAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.txtLike) {
                    ListBean item = repairsRecordAdapter.getItem(position);
                    UIUtils.showLoadDialog(that, "操作中..");
                    if (!repairsRecordAdapter.containtLikes(item.like_list)) {
//                        BaseQuestStart.NeighborhoodImUpdatesLike(ChatHomePageFragment.this, item.id);
                    } else {
//                        BaseQuestStart.NeighborhoodImUpdatesLikeCancel(ChatHomePageFragment.this, item.id);
                    }

                }
            }
        });
        editContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    //搜索
                }
                return true;
            }
        });
//        dynaHeadLogic.setBgClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(id!=null &&!Config.getLoginInfo().getId().equals(id))return;
//                //修改头像
//                String cachePath = BoxingFileHelper.getCacheDir(that);
//                Uri destUri = new Uri.Builder()
//                        .scheme("file")
//                        .appendPath(cachePath)
//                        .appendPath(String.format(Locale.US, "%s.jpg", System.currentTimeMillis()))
//                        .build();
//                //这里设置的config的mode是sngle_img  就是单选图片的模式  支持相机
//                BoxingConfig singleCropImgConfig = new BoxingConfig(BoxingConfig.Mode.SINGLE_IMG)
//                        .needCamera()
//                        .withCropOption(new BoxingCropOption(destUri).withMaxResultSize(720, 400).aspectRatio(720, 400));
//                Boxing.of(singleCropImgConfig).
//                        withIntent(that, BoxingActivity.class).
//                        start(ChatHomePageFragment.this, 0x0120);
//            }
//        });
        RedDotUtil.setNumber(0, RedDotUtil.CODES_DYNAMICS);//清空动态红点数
    }


    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case NEIGHBORHOOD_IM_UPDATES_LIKE_CANCEL_CODE:
            case NEIGHBORHOOD_IM_UPDATES_LIKE_CODE:
                UIUtils.shortM(bean);
                pageLimitDelegate.refreshPage();
                break;
            case NEIGHBORHOOD_IM_UPDATES_HOME_CODE:
            case NEIGHBORHOOD_IM_UPDATES_LIST_CODE:
                if (bean.status == 1) {
                    ChatDynamicsBean dynamicsBean = bean.Data();
                    setPageData(dynamicsBean);
                }
                break;
            case NEIGHBORHOOD_IM_MEMBER_UPDATES_IMAGE_CODE:
                UIUtils.shortM(bean);
                break;

        }
        super.nofityUpdate(requestCode, bean);
    }

    private void setPageData(ChatDynamicsBean dynamicsBean) {
//        dynaHeadLogic.setPageData(dynamicsBean);
        pageLimitDelegate.setData(dynamicsBean.list);
        isFriends=dynamicsBean.info.isFriends();
        repairsRecordAdapter.setIsFriend(isFriends);//设置是否是好友
        pageLimitDelegate.setPageEnable(isFriends);//不是好友,禁用分页
        layEmpty.setVisibility(EmptyDeal.isEmpy(repairsRecordAdapter.getData()) ? View.VISIBLE : View.GONE);
    }
    File bgImgFile;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String pic = ImgDealLogic.getPic(that, data);
        if(!EmptyDeal.isEmpy(pic)) {
            bgImgFile = new File(pic);
//            dynaHeadLogic.setImageBg(pic);
            UIUtils.showLoadDialog(that, "图片上传中..");
//            BaseQuestStart.neighborhoodImMemberUpdatesImage(this, bgImgFile);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //pageLimitDelegate.refreshPage();
    }

    public static ChatHomePageFragment newInstance() {

        Bundle args = new Bundle();
        ChatHomePageFragment fragment = new ChatHomePageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chat_dynamics_homepage;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(DefaultEvent event) {
        switch (event.getAction()) {
            case EVENT_CHAT_REFRESH_HOME_PAGE:
            case EVENT_CHAT_COMMENT:
                pageLimitDelegate.refreshPage();
                break;
        }
    }
}
