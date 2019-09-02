package com.cnsunrun.common.util;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


import com.cnsunrun.common.widget.dialog.WheelDialogFragment;
import com.sunrun.sunrunframwork.uibase.BaseActivity;

import java.util.List;

/**
 * Created by cnsunrun on 2017-12-14.
 */

public class SelectHelprUtils {

    public static void dealResult(final FragmentActivity that, List device_type, WheelDialogFragment.OnWheelDialogListener onItemSelectListener){
        Bundle bundle = new Bundle();
        bundle.putBoolean(WheelDialogFragment.DIALOG_BACK, true);
        bundle.putBoolean(WheelDialogFragment.DIALOG_CANCELABLE, true);
        bundle.putBoolean(WheelDialogFragment.DIALOG_CANCELABLE_TOUCH_OUT_SIDE, true);
        bundle.putString(WheelDialogFragment.DIALOG_LEFT, "取消");
        bundle.putString(WheelDialogFragment.DIALOG_RIGHT, "确定");
        WheelDialogFragment dialogFragment = WheelDialogFragment.newInstance(WheelDialogFragment.class, bundle);
        dialogFragment.setData(device_type);
        dialogFragment.setWheelDialogListener(onItemSelectListener);
        dialogFragment.show(that.getSupportFragmentManager(), "");

    }
    public static void dealDefResult(final BaseActivity that, List device_type, int pos, WheelDialogFragment.OnWheelDialogListener onItemSelectListener){
        Bundle bundle = new Bundle();
        bundle.putBoolean(WheelDialogFragment.DIALOG_BACK, true);
        bundle.putBoolean(WheelDialogFragment.DIALOG_CANCELABLE, true);
        bundle.putBoolean(WheelDialogFragment.DIALOG_CANCELABLE_TOUCH_OUT_SIDE, true);
        bundle.putString(WheelDialogFragment.DIALOG_LEFT, "取消");
        bundle.putString(WheelDialogFragment.DIALOG_RIGHT, "确定");
        WheelDialogFragment dialogFragment = WheelDialogFragment.newInstance(WheelDialogFragment.class, bundle);
        dialogFragment.setData(device_type);
        dialogFragment.setDefIndex(pos);
        dialogFragment.setWheelDialogListener(onItemSelectListener);
        dialogFragment.show(that.getSupportFragmentManager(), "");

    }


}
