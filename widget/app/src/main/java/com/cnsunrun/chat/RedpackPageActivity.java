package com.cnsunrun.chat;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.cnsunrun.R;
import com.cnsunrun.chat.adapters.ChatRedpackInfoAdapter;
import com.cnsunrun.chat.logic.RedPageHeadLogic;
import com.cnsunrun.chat.mode.RedPackInfoBean;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.PageLimitDelegate;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.cnsunrun.chat.ChatGroupSetInfoActivity.isNotify;
import static com.cnsunrun.common.quest.BaseQuestConfig.GET_REDPACK_CODE;

/**
 * 红包页面
 */
public class RedpackPageActivity extends LBaseActivity implements PageLimitDelegate.DataProvider{
public static boolean DEBUGGGG=true;

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.refreshLayout)
    SwipeRefreshLayout refreshLayout;

    @BindView(R.id.imgBoom)
    ImageView imgBoom;
    boolean isLoad=false;
    SoundPool soundPool;
    int soundId;
    PageLimitDelegate<RedPackInfoBean.ListBean> pageLimitDelegate = new PageLimitDelegate<>(this);
    private ChatRedpackInfoAdapter redpackInfoAdapter;
    View headView;
    RedPageHeadLogic redPageHeadLogic;
    public static void startThis(Activity that) {
        Intent intent = new Intent(that, RedpackPageActivity.class);
        that.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redpack_page);
        headView=getLayoutInflater().inflate(R.layout.include_redpack_page_head,null);
        redPageHeadLogic=new RedPageHeadLogic(headView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        redpackInfoAdapter = new ChatRedpackInfoAdapter();
        redpackInfoAdapter.setHeaderView(headView);
        recyclerView.setAdapter(redpackInfoAdapter);
        pageLimitDelegate.attach(refreshLayout,recyclerView,redpackInfoAdapter);
        pageLimitDelegate.setPageEnable(false);
        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CommonIntent.startRedpackRecordActivity(that,getIntent().getStringExtra("type"));
            }
        });
        tryLoadSound();
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        super.nofityUpdate(requestCode, bean);
        switch (requestCode){
            case GET_REDPACK_CODE:
                RedPackInfoBean redPackInfoBean=bean.Data();
                if(redPackInfoBean!=null){
                    redPageHeadLogic.setPageData(redPackInfoBean);
                    pageLimitDelegate.setData(redPackInfoBean.list);
                    if(!isLoad) {
                        isLoad=true;
                        String id = getIntent().getStringExtra("id");
                        boolean isFirst = !getSession().getBoolean(Config.KEY(id));
                        boolean hasThunder = redPackInfoBean.isThunder() && isFirst;
                        imgBoom.setVisibility(hasThunder ? View.VISIBLE : View.GONE);
                        getSession().put(Config.KEY(id), true);
                        if ( hasThunder&&soundPool != null) {
                            if(isNotify( getIntent().getStringExtra("targetId")))
                            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
                        }
                    }
                }else {
                    UIUtils.shortM(bean);
                }
                break;
        }
    }
    private void tryLoadSound() {
        try {
            soundPool = new SoundPool(2, AudioManager.STREAM_NOTIFICATION, 0);
            soundId = soundPool.load(getAssets().openFd("bomb.wav"), 1);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onDestroy() {
        if (soundPool != null) {
            soundPool.release();

        }
        super.onDestroy();
    }

    @Override
    public void loadData(int page) {
        BaseQuestStart.get_redpack(this,getIntent().getStringExtra("id"),getIntent().getStringExtra("type"));
    }
}
