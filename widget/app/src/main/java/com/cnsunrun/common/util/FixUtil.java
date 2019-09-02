package com.cnsunrun.common.util;

import android.app.Dialog;
import android.content.Context;

import com.sunrun.sunrunframwork.uiutils.UIAlertDialogUtil;

/**
 * Created by WQ on 2018/1/12.
 */

public class FixUtil {
    public static Dialog showLoadDialog(Context context) {
        try {
            return UIAlertDialogUtil.getInstences().showLoadDialog(context, "正在加载");
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public static Dialog showLoadDialog(Context context, String msg) {
        try {
            return UIAlertDialogUtil.getInstences().showLoadDialog(context, msg);
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
}
