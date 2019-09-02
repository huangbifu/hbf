package com.cnsunrun.common.base;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Build;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;

import com.cnsunrun.R;
import com.sunrun.sunrunframwork.utils.log.Logger;

import java.lang.reflect.Method;

/**
 * Created by cnsunrun on 2017-07-12.
 * <p>
 * 解决沉浸式状态栏问题
 */

public class ConversationHeightFix {

    // For more information, see https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    private Context context;

    public static void assistActivity(Activity activity) {
        new ConversationHeightFix(activity);
    }

    //根布局               ,输入框所在内容的布局
    private View mRootView,contentView;
    private int usableHeightPrevious;
    private ViewGroup.LayoutParams frameLayoutParams;
    View titleBar;
    private ConversationHeightFix(Activity activity) {
        this.context = activity;
        // FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        // mRootView = content.getChildAt(0);
        mRootView = ((ViewGroup) activity.getWindow().getDecorView()).getChildAt(0);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

//               if(OSUtils.getRomType()== OSUtils.ROM_TYPE.EMUI){
//                   possiblyResizeChildOfContentHw();
//               }else {
                possiblyResizeChildOfContent();
//               }
            }
        });
        frameLayoutParams = (FrameLayout.LayoutParams) mRootView.getLayoutParams();
    }

    /**
     *
     * @param activity
     * @param contentView 聊天界面所在布局
     */
    public ConversationHeightFix(Activity activity, ViewGroup contentView) {
        this.context = activity;
        mRootView = activity.getWindow().getDecorView();
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            public void onGlobalLayout() {

                possiblyResizeChildOfContent();
            }
        });
        this.contentView=contentView;
        frameLayoutParams =  contentView.getLayoutParams();
         titleBar = mRootView.findViewById(R.id.titleBar);//标题区域

    }

    private void possiblyResizeChildOfContent() {
        int usableHeightNow = computeUsableHeight();//计算可用高度, (从屏幕最上面开始到输入法的高度)
        int realUsableHeight = usableHeightNow - titleBar.getHeight();//聊天区域在屏幕上的可见高度- 标题栏到输入法那块区域,
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mRootView.getHeight();//获取跟布局高度,
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;//计算差值,判断键盘是否展示中
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // 软键盘弹出
                int navigationBarHeight = getNavigationBarHeight();
//                if(!isNavigationBarShow())navigationBarHeight=0;
                frameLayoutParams.height = realUsableHeight;//+navigationBarHeight;//(int) (usableHeightSansKeyboard - heightDifference + (navigationBarHeight / 2)- DisplayUtil.dpToPx(50,context));

            } else {
                // 软键盘隐藏
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    if (checkDeviceHasNavigationBarByLollipop(context)) {
                        int navigationBarHeight = getNavigationBarHeight();
                        if(!isNavigationBarShow())navigationBarHeight=0;
                        frameLayoutParams.height = usableHeightNow-navigationBarHeight;//usableHeightSansKeyboard - navigationBarHeight;
                    } else {
                        int navigationBarHeight = getNavigationBarHeight();
                        frameLayoutParams.height =usableHeightNow-navigationBarHeight;// usableHeightSansKeyboard;
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
            contentView.requestLayout();

            usableHeightPrevious = usableHeightNow;
        }
    }

    private void printView(String viewName,View view){
        int pW=0,pH=0;
        if(view.getLayoutParams()!=null){
            pW=view.getLayoutParams().width;
            pH=view.getLayoutParams().height;
        }
        Logger.E(viewName+"-- W:"+view.getWidth()+" H:"+view.getHeight()+" pW:"+pW+" pH:"+pH);
    }

    private void possiblyResizeChildOfContentHw() {
        int usableHeightNow = computeUsableHeight();
        int navigationBarHeight = getNavigationBarHeight();
        if (usableHeightNow != usableHeightPrevious) {
            int usableHeightSansKeyboard = mRootView.getRootView().getHeight();
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                // keyboard probably just became visible
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference + navigationBarHeight;
            } else {
                // keyboard probably just became hidden
                frameLayoutParams.height = usableHeightNow + navigationBarHeight;
            }
            mRootView.requestLayout();
            usableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mRootView.getWindowVisibleDisplayFrame(r);
        //return (r.bottom - r.top);// 全屏模式下： return r.bottom
        return r.bottom;
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
