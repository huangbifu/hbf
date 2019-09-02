package com.cnsunrun.common.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * 推送消息接收器
 *
 * @author WQ 下午1:07:41
 */
public class AppPushReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null && intent.getAction() != null) {
            new DealPushAction(context, intent);
        }
    }

    private void dipatchMessageToService(Context context, Intent intent) {
    }

}
