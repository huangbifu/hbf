package com.cnsunrun.chat.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatAddressBookAdapter;
import com.cnsunrun.chat.dialog.FilterPopWindow;
import com.cnsunrun.chat.logic.ChatBgLogic;
import com.cnsunrun.chat.logic.SideBarSortMode;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.chat.mode.MemberInfo;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseFragment;
import com.cnsunrun.common.config.IMUserInfoProvider;
import com.cnsunrun.common.event.RedDotEvent;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.CenterLayoutManager;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.RedDotUtil;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.flyco.tablayout.widget.MsgView;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uibase.BaseActivity;
import com.sunrun.sunrunframwork.view.ItemView;
import com.sunrun.sunrunframwork.view.sidebar.SideBar;
import com.sunrun.sunrunframwork.view.sidebar.SortModel;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.List;

import butterknife.BindColor;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_FRIEND_LIST_CODE;
import static com.cnsunrun.common.util.RedDotUtil.CODES_PROPERTY;

/**
 * 咵天-通讯录
 * Created by WQ on 2017/11/7.
 */

public class ChatAddressBookFragment extends LBaseFragment {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    SideBarSortMode sideBarSortMode = new SideBarSortMode();
    @BindView(R.id.itemNewFriends)
    ItemView itemNewFriends;
    @BindView(R.id.itemGroups)
    ItemView itemGroups;
    @BindView(R.id.txtTip)
    TextView txtTip;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    @BindColor(R.color.text4)
    int text4Color;
    @BindView(R.id.msgNumber)
    MsgView msgNumber;
    @BindView(R.id.edit_search)
    EditText editSearch;
    private ChatAddressBookAdapter selectLinkManAdapter;
    FilterPopWindow filterPopWindow;//右边功能菜单列表
    boolean isCreated=false;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initEventBus();
        selectLinkManAdapter = new ChatAddressBookAdapter(sideBarSortMode);
        recyclerView.setAdapter(selectLinkManAdapter);
        recyclerView.setLayoutManager(new CenterLayoutManager(that, LinearLayoutManager.VERTICAL, false));
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                CommonIntent.startChatSearchAddressBookActivity(that);
            }
        });
        sideBar.setTextColor(text4Color);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = sideBarSortMode.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(position, 0);
                }
            }
        });
        GetEmptyViewUtils.bindEmptyView(that, selectLinkManAdapter, 0, "暂无好友", true);
        selectLinkManAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                LinkManInfo item = selectLinkManAdapter.getItem(position);
                ChatBgLogic.saveChatBg((BaseActivity) that, item.chat_image, item.id, false);
                CommonIntent.startChatUserInfoActivity(that, item.id, true);
            }
        });
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //加人
//                CommonIntent.startChatSearchFriendsActivity(that);
                filterPopWindow.showAsDropDown(v);
            }
        });
        filterPopWindow=new FilterPopWindow(that);
//        ,"创建群组"
        filterPopWindow.setListData(TestTool.asArrayList("添加好友"), new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position==0){
                    CommonIntent.startChatSearchFriendsActivity(that);
                }else  if(position==1){
                    getSession().remove("selectedLinkMans");
                    CommonIntent.startChatSelectLinkManActivity(that,null);
                }
                filterPopWindow.dismiss();
            }
        });
        titleBar.setLeftAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonIntent.startNoticeMessageActivity(that);
            }
        });
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH){
                    //搜索
                    List<LinkManInfo> data = sideBarSortMode.getData(editSearch.getText().toString().trim());
                    selectLinkManAdapter.setNewData(data);
                }
                return true;
            }
        });
        isCreated=true;
    }

    private void loadPageData() {
        if(!isCreated)return;
        BaseQuestStart.NeighborhoodImFriendList(this);
       refreshRedDot(RedDotEvent.newInstance());
    }

    @Override
    public void onResume() {
        super.onResume();
        loadPageData();
    }

    @Override
    public void onVisible() {
        super.onVisible();
        loadPageData();
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case NEIGHBORHOOD_IM_FRIEND_LIST_CODE:
                if (bean.status == 1) {
                    List<LinkManInfo> data = bean.Data();
                    getSession().put("LinkManInfos", data);
                    setPageData(data);
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }

    private void setPageData(List<LinkManInfo> data) {
        sideBar.setTextView(txtTip);
        sideBarSortMode.setSourceDateList(data);
        selectLinkManAdapter.setNewData(data);
        for (LinkManInfo datum : data) {
            MemberInfo memberInfo = new MemberInfo();
            memberInfo.avatar = datum.avatar;
            memberInfo.id = datum.id;
            memberInfo.nickname = datum.nickname;
            memberInfo.remark = datum.remark;
            IMUserInfoProvider.refreshUserInfo(memberInfo, memberInfo.id);
        }
    }

    public static ChatAddressBookFragment newInstance() {
        Bundle args = new Bundle();
        ChatAddressBookFragment fragment = new ChatAddressBookFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_chat_addressbook;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void refreshRedDot(RedDotEvent redDotEvent) {
        RedDotUtil.setRedDot(msgNumber, RedDotUtil.CODE_50);
    }


    @OnClick({R.id.itemNewFriends, R.id.itemGroups})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.itemNewFriends:
                CommonIntent.startChatNewFriendsActivity(that);
                break;
            case R.id.itemGroups:
                CommonIntent.startChatGroupListActivity(that);
                break;
        }
    }

}
