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

/**
 * 游戏测试页面
 */
public class GameTestActivity extends LBaseActivity {


    @BindView(R.id.titleBar)
    TitleBar titleBar;


    public static void startThis(Activity that) {
        Intent intent = new Intent(that, GameTestActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_test);

    }



}
