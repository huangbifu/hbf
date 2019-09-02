package com.cnsunrun.common.util;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by cnsunrun on 2017-07-10.
 */

public class OpenActivityUtil {


    /**
     * 通过类名启动Activity之后不用finish
     *
     * @param pClass
     */
    public static void openActivity(Activity activity,Class<?> pClass) {
        openActivity(activity,pClass, null,false);
    }

    /**
     *  打开一个activity之后finish
     * @param pClass
     */
    public static void openActivityThenKill(Activity activity,Class<?> pClass) {
        openActivity(activity,pClass, null,true);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    public static void openActivity(Activity activity,Class<?> pClass, Bundle pBundle, boolean finish) {
        Intent intent = new Intent(activity, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        activity.startActivity(intent);
        if (finish) {
            activity.finish();
        }
    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    protected static void openActivity(Activity activity,String pAction) {
        openActivity(activity,pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    public static void openActivity(Activity activity, String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        activity.startActivity(intent);

    }


}
