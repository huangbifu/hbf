package com.cnsunrun.common.intent;


import android.app.Activity;

import com.cnsunrun.common.model.LoginInfo;

/**
 * 所有的Activity启动定义
 * Created by WQ on 2017/8/24.
 */

public interface StartIntent {
    void startLoginActivity(Activity fromAct,@Key("info")LoginInfo loginInfo);

}
