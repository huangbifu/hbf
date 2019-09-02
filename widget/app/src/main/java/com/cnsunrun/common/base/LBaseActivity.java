package com.cnsunrun.common.base;

/**
 * @时间: 2017/5/19
 * @功能描述:
 */

import android.os.Bundle;

import com.cnsunrun.common.model.LoginInfo;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.logic.TokenRefreshLogic;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.cache.NetSession;

import org.greenrobot.eventbus.EventBus;

import static com.cnsunrun.common.quest.BaseQuestConfig.POST_LOGCAT_CODE;

/**
 * 预留后面会增加的空间
 */

public class LBaseActivity extends TranslucentActivity {
    /**
     * 启用eventBus,不用手动关闭
     */
    public void initEventBus() {
        EventBus.getDefault().register(this);
    }

    @Override
    protected boolean isStatusContentDark() {
        return true;
    }

    @Override
    protected boolean isTranslucent() {
        return true;
    }

    @Override
    protected void onDestroy() {
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        super.onDestroy();
    }

    TokenRefreshLogic tokenRefreshLogic = new TokenRefreshLogic();

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
        if(requestCode==POST_LOGCAT_CODE)return;
        super.loadFaild(requestCode, bean);
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
//        if (tokenRefreshLogic.isAuthCode(requestCode) && bean.status==1) {
//            LoginInfo.TokenBean tokenBean=bean.Data();//刷新token
//            Config.getLoginInfo().setToken(tokenBean);
//            tokenRefreshLogic.retryRequest(this);//重新请求
//        } else if (bean.status == 2||!Config.getLoginInfo().isExpiresTime()) {
//
//        } else {
//            tokenRefreshLogic.removeRequest(requestCode);
//        }
        super.nofityUpdate(requestCode, bean);
    }

    @Override
    public NetSession getSession() {
        return super.getSession();
    }

    @Override
    public void dealLongTimeBackGround(Bundle arg0) {
        super.dealLongTimeBackGround(arg0);
        if (arg0 != null) {
            finish();

        }
    }
}
