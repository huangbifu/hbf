package com.cnsunrun.common.logic;

import android.util.SparseArray;

import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.NetRequestHandler;

import static com.cnsunrun.common.quest.BaseQuestConfig.USER_OAUTH_REFRESH_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.USER_OAUTH_TOKEN_CODE;
import static com.sunrun.sunrunframwork.http.NetRequestHandler.POST;

/**
 * Created by WQ on 2017/12/5.
 */

public class TokenRefreshLogic {
    SparseArray<NAction>requestSet=new SparseArray<>();
    public void recordRequest(NAction action){
        if(isAuthCode(action.requestCode))return;
        requestSet.put(action.requestCode,action);
    }
    public void removeRequest(NAction action){
        requestSet.remove(action.requestCode);
    }
    public void removeRequest(int requestCode){
        requestSet.remove(requestCode);
    }

    /**
     * 重新发起请求
     * @param requestHandler
     */
    public void retryRequest(NetRequestHandler requestHandler){
        requestHandler.cancelAllRequest();//取消掉原有的所有请求
        for (int i = 0; i < requestSet.size(); i++) {
            int keyAt = requestSet.keyAt(i);
            NAction action = requestSet.get(keyAt);

            if(action.getRequestType()==POST){
                requestHandler.requestAsynPost(action);
            }else {
                requestHandler.requestAsynGet(action);
            }
        }
    }

    public boolean isAuthCode(int requestCode){
        return (requestCode==USER_OAUTH_TOKEN_CODE||requestCode==USER_OAUTH_REFRESH_CODE);
    }

    public boolean isEmpty(){
        return requestSet.size()==0;
    }
}
