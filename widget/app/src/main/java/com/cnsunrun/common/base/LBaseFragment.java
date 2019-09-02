package com.cnsunrun.common.base;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnsunrun.common.model.LoginInfo;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.logic.TokenRefreshLogic;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.uibase.BaseFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Describe Fragment基类
 */

public abstract class LBaseFragment extends BaseFragment {
    @LayoutRes
    public abstract int getLayoutRes();

    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutRes = getLayoutRes();
        if (layoutRes > 0) {
            return inflater.inflate(layoutRes, container, false);
        } else {
            throw new RuntimeException("getLayoutRes should be override to provide the layout resource");
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(unbinder!=null){
            unbinder.unbind();
        }
        unbinder = ButterKnife.bind(this, view);
    }

    /**
     * 启用eventBus,不用手动关闭
     */
    public void initEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        if(EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    public void onVisible() {

    }

    public void onInVisible() {

    }


    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {
            onInVisible();
        } else {
            onVisible();
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            onVisible();
        } else {
            onInVisible();
        }
    }

    TokenRefreshLogic tokenRefreshLogic=new TokenRefreshLogic();
    @Override
    public void requestAsynGet(NAction action) {
//        tokenRefreshLogic.recordRequest(action);
//        if(!tokenRefreshLogic.isAuthCode(action.requestCode)&&!Config.getLoginInfo().isExpiresTime()){
//            BaseQuestStart.UserOauthToken(this);
//            return;
//        }
        super.requestAsynGet(action);
    }

    @Override
    public void requestAsynPost(NAction action) {
//        tokenRefreshLogic.recordRequest(action);
//        if(!tokenRefreshLogic.isAuthCode(action.requestCode)&&!Config.getLoginInfo().isExpiresTime()){
//            BaseQuestStart.UserOauthToken(this);
//            return;
//        }
        super.requestAsynPost(action);
    }

    @Override
    public void requestFinish() {
//        if (tokenRefreshLogic.isEmpty())
            super.requestFinish();
    }

    @Override
    public void loadFaild(int requestCode, BaseBean bean) {
//        tokenRefreshLogic.removeRequest(requestCode);
        super.loadFaild(requestCode, bean);
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
//        if (tokenRefreshLogic.isAuthCode(requestCode) && bean.status==1) {
//            LoginInfo.TokenBean tokenBean=bean.Data();//刷新token
//            Config.getLoginInfo().setToken(tokenBean);
//            tokenRefreshLogic.retryRequest(this);//重新请求
//        } else  if (bean.status == 2||!Config.getLoginInfo().isExpiresTime()) {//token快过期了
//            BaseQuestStart.UserOauthToken(this);
//        } else {
//            tokenRefreshLogic.removeRequest(requestCode);
//        }
        super.nofityUpdate(requestCode, bean);
    }
}
