package com.cnsunrun.chat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.SelectLinkManAdapter;
import com.cnsunrun.chat.dialog.InputContentDialog;
import com.cnsunrun.chat.logic.SideBarSortMode;
import com.cnsunrun.chat.mode.LinkManInfo;
import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.CenterLayoutManager;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.common.util.selecthelper.Selectable;
import com.cnsunrun.commonui.widget.titlebar.TitleBar;
import com.cnsunrun.mine.activity.TransferActivity;
import com.google.gson.reflect.TypeToken;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.InputMethodUtil;
import com.sunrun.sunrunframwork.utils.JsonDeal;
import com.sunrun.sunrunframwork.utils.Utils;
import com.sunrun.sunrunframwork.view.sidebar.SideBar;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_ADD_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GROUP_JOIN_CODE;

/**
 * 咵天,搜索选择联系人
 * Created by WQ on 2017/10/30.
 */

public class ChatSelectLinkManActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.edit_search)
    EditText editSearch;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    @BindView(R.id.txtTip)
    TextView txtTip;
    SelectLinkManAdapter selectLinkManAdapter;
    SideBarSortMode sideBarSortMode = new SideBarSortMode();
    @BindView(R.id.selectedPeople)
    RecyclerView selectedPeople;
    @BindView(R.id.imgShowSearch)
    ImageView imgShowSearch;
    @BindView(R.id.laySelected)
    LinearLayout laySelected;
    List<LinkManInfo> linkManInfos;
    String groupId;//有id 为加人,没有则为创建
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_select_linkman);
        groupId = getIntent().getStringExtra("groupId");
        linkManInfos =  TestTool.safeList(getSession().getBean("LinkManInfos", new TypeToken<List<LinkManInfo>>() {
        }));

        //过滤已选择的
        List<LinkManInfo> selectedLinkMans = TestTool.safeList(getSession().getBean("selectedLinkMans", new TypeToken<List<LinkManInfo>>() {
        }));
        for (Iterator<LinkManInfo> iterator=linkManInfos.iterator();iterator.hasNext();){
            LinkManInfo next = iterator.next();
            if(selectedLinkMans.contains(next)){
                iterator.remove();
            }
        }
        if(getIntent().hasExtra("title")){
            title = getIntent().getStringExtra("title");
            titleBar.setTitle(title);
        }


        selectLinkManAdapter = new SelectLinkManAdapter(sideBarSortMode,isSingleSelect()) {
            @Override
            public void onItemSelected(int position, List<LinkManInfo> selectedData) {
                setSelectedPeople(selectedData);
                if(isSingleSelect()) {
                    addOrCreateGroup();
                }
            }
        };
        if(isSingleSelect()){
            selectLinkManAdapter.selectMode(Selectable.RADIO);
            titleBar.setRightVisible(View.GONE);
        }else {
            titleBar.setRightAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addOrCreateGroup();

                }
            });
        }
        GetEmptyViewUtils.bindEmptyView(that,selectLinkManAdapter,0,"暂无联系人",true);
        recyclerView.setAdapter(selectLinkManAdapter);
        recyclerView.setLayoutManager(new CenterLayoutManager(that, LinearLayoutManager.VERTICAL, false));

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
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                List<LinkManInfo> data = sideBarSortMode.getData(editSearch.getText().toString());
                selectLinkManAdapter.setNewData(data);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        sideBar.setTextView(txtTip);
        sideBarSortMode.setSourceDateList(linkManInfos);
        selectLinkManAdapter.setNewData(linkManInfos);


    }

    private boolean isSingleSelect() {
        return getIntent().getBooleanExtra("isSingleSelect",false);
    }

    public void nofityUpdate(int requestCode, BaseBean bean) {
        switch (requestCode) {
            case NEIGHBORHOOD_IM_GROUP_ADD_CODE:
                UIUtils.shortM(bean);
                if (bean.status == 1) {
                    String groupId= JsonDeal.createJsonObj(bean.toString()).optString("id");
                    CommonApp.getInstance().closeActivitys(ChatSetInfoActivity.class,ConversationActivity.class);
                    RongIM.getInstance().startGroupChat(that,groupId,groupName);
                    finish();
                }
                break;
            case NEIGHBORHOOD_IM_GROUP_JOIN_CODE:
                UIUtils.shortM(bean);
                if (bean.status == 1) {
                    CommonApp.getInstance().closeActivitys(ChatGroupPeoplesActivity.class);
                    finish();
                }
                break;
        }
        super.nofityUpdate(requestCode, bean);
    }

    String groupName;
    private void addOrCreateGroup() {
        int selectCount = selectLinkManAdapter.getSelectCount();
        if(isSingleSelect()){
            if (TextUtils.equals(title,"转账")){
                LinkManInfo selectItem = selectLinkManAdapter.getSelectItem();
                TransferActivity.startThis(that,selectItem);
            }else{
                LinkManInfo selectItem = selectLinkManAdapter.getSelectItem();
                Intent intent=new Intent();
                intent.putExtra("linkman",selectItem);
                setResult(RESULT_OK,intent);
                finish();
            }

            return;
        }
        if (groupId == null&&selectCount < 2) {
            UIUtils.shortM("请选择两个以上数量的好友");
            return;
        }else if(groupId!=null){
            UIUtils.shortM("请选择联系人");
        }
        List<LinkManInfo> allCheckData = selectLinkManAdapter.getAllCheckData(linkManInfos);

        final String[] fuids = Utils.listToString(allCheckData, new Utils.DefaultToString<LinkManInfo>(",") {
            @Override
            public String getString(LinkManInfo applyPeople) {
                return applyPeople.id;
            }
        }).split(",");


        if (groupId == null) {
            final InputContentDialog inputContentDialog = InputContentDialog.newInstance();
            inputContentDialog.setTitleTxt("建群组").setContentHintTxt("请输入群名称").setOnSubmitAction(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String inputContent = inputContentDialog.getInputContent();
                    if (EmptyDeal.isEmpy(inputContent)) {
                        UIUtils.shortM("群名称不能为空");
                        return;
                    }
                    UIUtils.showLoadDialog(that, "创建中..");
                    BaseQuestStart.NeighborhoodImGroupAdd(that, groupName=inputContent, fuids);
                    inputContentDialog.dismissAllowingStateLoss();
                }
            });
            inputContentDialog.show(getSupportFragmentManager(), "InputContentDialog");
        } else {
            UIUtils.showLoadDialog(this);
            BaseQuestStart.NeighborhoodImGroupJoin(this, groupId, fuids);
        }
    }

    private void setSelectedPeople(final List<LinkManInfo> selectedData) {
//        laySelected.setVisibility(EmptyDeal.isEmpy(selectedData) ? View.GONE : View.VISIBLE);
//        selectedPeople.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
//        SelectedTagAdapter adapter = new SelectedTagAdapter();
//        selectedPeople.setAdapter(adapter);
//        adapter.setNewData(selectedData);
//        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//                int realPosition = selectLinkManAdapter.getData().indexOf(adapter.getItem(position));
//                adapter.remove(position);
//                selectLinkManAdapter.setSelectPosition(realPosition);
//                if (EmptyDeal.isEmpy(adapter.getData())) {
//                    laySelected.setVisibility(View.GONE);
//                }
//            }
//        });
    }

    @OnClick(R.id.imgShowSearch)
    public void onClick() {
        laySelected.setVisibility(View.GONE);
    }
    @Override
    protected void onPause() {
        super.onPause();
        InputMethodUtil.HideKeyboard(this);
    }

}
