package com.cnsunrun.common.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.cnsunrun.common.util.OSUtils;

import java.lang.reflect.Method;

/**
 * Created by cnsunrun on 2017-07-12.
 * <p>
 * 解决沉浸式状态栏问题
 */

public class StatusBarConfig {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    private Context context;

    public static void assistActivity(Activity activity) {
        new StatusBarConfig(activity);
    }

    private View mChildOfContent;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;

    private StatusBarConfig(Activity activity) {
        this.context = activity;
        // FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        // mChildOfContent = content.getChildAt(0);
        mChildOfContent = ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

//               if(OSUtils.getRomType()== OSUtils.ROM_TYPE.EMUI){
//                   possiblyResizeChildOfContentHw();
//               }else {
                possiblyResizeChildOfContent();
//               }
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    public StatusBarConfig(Activity activity,ViewGroup contentView) {
        this.context = activity;
        // FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        // mChildOfContent = content.getChildAt(0);
        mChildOfContent = ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0);
        mChildOfContent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

//               if(OSUtils.getRomType()== OSUtils.ROM_TYPE.EMUI){
//                   possiblyResizeChildOfContentHw();
//               }else {
                possiblyResizeChildOfContent();
//               }
            }
        });

        frameLayoutParams =  contentView.getLayoutParams();
    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // 软键盘弹出
                int navigationBarHeight = getNavigationBarHeight();
//                if(!isNavigationBarShow())navigationBarHeight=0;
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + (navigationBarHeight / 2);

            } else {
                // 软键盘隐藏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (checkDeviceHasNavigationBarByLollipop(context)) {
                        int navigationBarHeight = getNavigationBarHeight();
                        if(!isNavigationBarShow())navigationBarHeight=0;
                        frameLayoutParams.height = usableHeightSansKeyboard - navigationBarHeight;
                    } else {
                        frameLayoutParams.height = usableHeightSansKeyboard;
                    }
                } else {
                    if (checkDeviceHasNavigationBarByKitkat(context)) {
                        int navigationBarHeight = getNavigationBarHeight();
                        if(!isNavigationBarShow())navigationBarHeight=0;
                        frameLayoutParams.height = usableHeightSansKeyboard - navigationBarHeight;
                    } else {
                        frameLayoutParams.height = usableHeightSansKeyboard;
                    }
                }
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private void possiblyResizeChildOfContentHw() {
        int usableHeightNow = computeUsableHeight();
        int navigationBarHeight = getNavigationBarHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mChildOfContent.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + navigationBarHeight;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightNow + navigationBarHeight;
            }
            mChildOfContent.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);// 全屏模式下： return r.bottom
    }

    private int getNavigationBarHeight() {
        Resources resources = context.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    //NavigationBar状态是否是显示
    public boolean isNavigationBarShow() {
        Activity mContext = (Activity) context;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = mContext.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(context).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            if (menu || back) {
                return false;
            } else {
                return true;
            }
        }
    }


    public static boolean checkDeviceHasNavigationBarByLollipop(Context context) {
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {

        }
        return hasNavigationBar;

    }

    @SuppressLint("NewApi")
    public static boolean checkDeviceHasNavigationBarByKitkat(Context activity) {
        //通过判断设备是否有返回键、菜单键(不是虚拟键,是手机屏幕外的按键)来确定是否有navigation bar
        boolean hasMenuKey = ViewConfiguration.get(activity)
                .hasPermanentMenuKey();
        boolean hasBackKey = KeyCharacterMap
                .deviceHasKey(KeyEvent.KEYCODE_BACK);

        if (!hasMenuKey && !hasBackKey) {
            // 做任何你需要做的,这个设备有一个导航栏
            return true;
        }
        return false;
    }

}
