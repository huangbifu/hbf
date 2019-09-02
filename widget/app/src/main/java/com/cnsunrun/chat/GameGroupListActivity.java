package com.cnsunrun.chat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatGameGroupAdapter;
import com.cnsunrun.chat.mode.GroupItemBean;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.config.RongIMHelper;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.util.GetEmptyViewUtils;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;

import static com.cnsunrun.common.quest.BaseQuestConfig.GAME_GROUP_LIST_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_GET_TOKEN_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.POST_LOGCAT_CODE;

/**
 * 扫雷游戏
 */
public class GameGroupListActivity extends LBaseActivity implements PageLimitDelegate.DataProvider{


    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;
    PageLimitDelegate<GroupItemBean> pageLimitDelegate = new PageLimitDelegate<>(this);
    private ChatGameGroupAdapter redpackInfoAdapter;
    private String imToken;

    public static void startThis(Activity that) {
        Intent intent = new Intent(that, GameGroupListActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_grouplist);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        redpackInfoAdapter = new ChatGameGroupAdapter();
        recyclerView.setAdapter(redpackInfoAdapter);
        pageLimitDelegate.attach(refreshLayout,recyclerView,redpackInfoAdapter);
        GetEmptyViewUtils.bindEmptyView(that,redpackInfoAdapter,0,"暂无群组信息",true);
        UIUtils.showLoadDialog(this);
        BaseQuestStart.NeighborhoodImGetToken(this);
        redpackInfoAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                CommonIntent.startChatUserInfoActivity(that,false);
                final GroupItemBean item = redpackInfoAdapter.getItem(position);
//                ChatBgLogic.saveChatBg((BaseActivity) that,item.chat_image,item.id,true);
                UIUtils.showLoadDialog(that,"加载中..");
//                RongIM.getInstance().deleteMessages(Conversation.ConversationType.GROUP, item.id, new RongIMClient.ResultCallback<Boolean>() {
//                    @Override
//                    public void onSuccess(Boolean aBoolean) {
//                boolean connect = RongIMHelper.isConnect();
//                Logger.E("GameGroupListActivity","IM_STATE:"+connect);
//                if (!connect){
//                    RongIMHelper.connectFocus(imToken);
//                }
                RongIM.getInstance().joinGroup(item.id,item.title, new RongIMClient.OperationCallback() {
                            @Override
                            public void onSuccess() {
                                if(!ConversationActivity.isConversationRuning()) {
                                    getSession().put("isRedpack_group"+item.id,true);
                                    RongIM.getInstance().startGroupChat(that, item.id, item.title);
                                }
                                BaseQuestStart.postLogcat(that,"JoinGrop_Success");
                                UIUtils.cancelLoadDialog();
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode errorCode) {
                                if(!ConversationActivity.isConversationRuning()) {
                                    getSession().put("isRedpack_group"+item.id,true);
                                    RongIM.getInstance().startGroupChat(that, item.id, item.title);
                                }
                                BaseQuestStart.postLogcat(that,"JoinGrop_ErrorCode:"+errorCode);
                                UIUtils.cancelLoadDialog();
                            }
                        });

//                    }

//                    @Override
//                    public void onError(RongIMClient.ErrorCode errorCode) {
//                        if(!ConversationActivity.isConversationRuning()) {
//                            RongIM.getInstance().startGroupChat(that, item.id, item.title);
//                        }
//                        UIUtils.cancelLoadDialog();
//                    }
//                });

            }
        });
        pageLimitDelegate.setPageEnable(false);

    }
    public void nofityUpdate(int requestCode, BaseBean bean){
        if(requestCode==POST_LOGCAT_CODE)return;
        switch(requestCode){
            case GAME_GROUP_LIST_CODE:
                if(bean.status==1){
                    List<GroupItemBean> data=bean.Data();
                    pageLimitDelegate.setData(data);
                }
                break;
            case NEIGHBORHOOD_IM_GET_TOKEN_CODE:
                if (bean.status == 1) {
                    JSONObject jsonObject = JsonDeal.createJsonObj(bean.toString());
                    imToken = jsonObject.optString("token");
//                    UIUtils.shortM("初始化");
                    RongIMHelper.connectFocus(imToken);

                } else {
                    if (bean.status == 0) {
                        UIUtils.shortM(bean);
                    }
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }



    @Override
    public void loadData(int page) {
        if(pageLimitDelegate.page==1) {
            UIUtils.showLoadDialog(that, "加载中..");
            BaseQuestStart.game_group_list(that, page);
//            recyclerView.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    if (redpackInfoAdapter.getData().isEmpty()) {
//                        loadData(1);
//                    }
//                }
//            },3000);
        }


    }
}
