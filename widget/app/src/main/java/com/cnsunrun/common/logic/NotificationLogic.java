package com.cnsunrun.common.logic;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import com.cnsunrun.R;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * 通知栏逻辑
 * Created by WQ on 2018/3/7.
 */

public class NotificationLogic {

    int notifyMode;
    String title;
    String text;
    int iconId;
    Uri sound;
    Context mContext;
    static Set<Integer> codeSet = new HashSet<>();

    public NotificationLogic(Context mContext) {
        this.mContext = mContext;
    }

    public int showNotify(PendingIntent pendingIntent, String title, String text, int iconId, int notifyMode,int nofityId) {
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (TextUtils.isEmpty(title)) {
            title = mContext.getResources().getString(R.string.app_name);
        }
        if (iconId == 0 || iconId == -1) {
            iconId = R.mipmap.ic_launcher;
        }
        Notification notification = createNotification(mContext, pendingIntent, title, text, iconId);
        if (sound != null) {
            notification.sound = sound;
        }
        notification.defaults = notifyMode;
        int code = nofityId==0||nofityId==-1?UUID.randomUUID().hashCode():nofityId;
        notificationManager.notify(code, notification);
        return code;
    }

    public void clearNotify(int code){
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancel(code);
    }
    public void clearAllNotify(){
        NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
    }

    public static Notification createNotification(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification notification;
        if (isNotificationBuilderSupported()) {
            notification = buildNotificationWithBuilder(context, pendingIntent, title, text, iconId);
        } else {
            // 低于API 11 Honeycomb
            notification = buildNotificationPreHoneycomb(context, pendingIntent, title, text, iconId);
        }
        return notification;
    }

    public static boolean isNotificationBuilderSupported() {
        try {
            return (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) && Class.forName("android.app.Notification.Builder") != null;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }

    @SuppressWarnings("deprecation")
    private static Notification buildNotificationPreHoneycomb(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification notification = new Notification(iconId, "", System.currentTimeMillis());
        try {
            // try to call "setLatestEventInfo" if available
            Method m = notification.getClass().getMethod("setLatestEventInfo", Context.class, CharSequence.class, CharSequence.class, PendingIntent.class);
            m.invoke(notification, context, title, text, pendingIntent);
        } catch (Exception e) {
            // do nothing
        }
        return notification;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressWarnings("deprecation")
    private static Notification buildNotificationWithBuilder(Context context, PendingIntent pendingIntent, String title, String text, int iconId) {
        Notification.Builder builder = new Notification.Builder(context)
                .setContentTitle(title)
                .setContentText(text)
                .setContentIntent(pendingIntent)
                .setSmallIcon(iconId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            return builder.build();
        } else {
            return builder.getNotification();
        }
    }

}
