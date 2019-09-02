package com.cnsunrun.common.receiver;

import android.content.Context;

import com.cnsunrun.common.CommonApp;
import com.cnsunrun.common.CommonIntent;
import com.sunrun.sunrunframwork.utils.log.Logger;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

/**
 * Created by cnsunrun on 2017-08-01.
 */

public class CharNotificationReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        Logger.E(message+"");
        return true; // 返回 false, 会弹出融云 SDK 默认通知; 返回 true, 融云 SDK 不会弹通知, 通知需要由您自定义。
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        Logger.E(message+"");
        return false; // 返回 false, 会走融云 SDK 默认处理逻辑, 即点击该通知会打开会话列表或会话界面; 返回 true, 则由您自定义处理逻辑。
    }

}
