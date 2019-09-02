package com.cnsunrun.common.base;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import com.cnsunrun.R;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.NetRequestHandler;
import com.sunrun.sunrunframwork.http.NetServer;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.http.utils.UIUpdateHandler;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.StatisticsUtil;
import com.sunrun.sunrunframwork.utils.log.Logger;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;

/**
 * 对话框Fragment基类
 */
public abstract class LBaseDialogFragment extends DialogFragment implements
        NetRequestHandler, UIUpdateHandler {
    protected FragmentActivity mAct;
    protected NetServer mNServer;
    protected Activity that;
    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutRes(), container, false);
        bind = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = getDialog();
        Window dialogWindow = dialog.getWindow();
        applyCompat(dialogWindow);
        dialogWindow.setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN | SOFT_INPUT_ADJUST_RESIZE);
        //设置对话框从底部进入
        dialogWindow.setWindowAnimations(R.style.bottomInWindowAnim);
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.MATCH_PARENT;//高度自己设定
        dialogWindow.setAttributes(p);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));
        dialogWindow.setGravity(Gravity.CENTER);
        dialog.setCancelable(true);

        dialog.setCanceledOnTouchOutside(true);
        //修复状态栏变黑的问题
        int screenHeight = getScreenHeight(getActivity());
        int statusBarHeight = getStatusBarHeight(getContext());
        int dialogHeight = 0;//screenHeight - statusBarHeight;
        dialogWindow.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, dialogHeight == 0 ? ViewGroup.LayoutParams.MATCH_PARENT : dialogHeight);
        return dialog;
    }
    private void applyCompat( Window dialogWindow) {
        if (Build.VERSION.SDK_INT <= 19) {
            return;
        }
        dialogWindow.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        dialogWindow.setStatusBarColor(Color.TRANSPARENT);

    }
    private static int getScreenHeight(Activity activity) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        return displaymetrics.heightPixels;
    }
    public Dialog getDialog(){
        return new Dialog(getActivity(), R.style.NoTitleDialog);
    }
    private static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        Resources res = context.getResources();
        int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            statusBarHeight = res.getDimensionPixelSize(resourceId);
        }
        return statusBarHeight;
    }
    protected abstract int getLayoutRes();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        that = mAct = getActivity();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        StatisticsUtil.pageStart(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        StatisticsUtil.pageEnd(this);
    }

    /**
     * 初始化网络请求模块,由子类在有需要时调用
     */
    protected void initNetServer() {
        if (mNServer == null)
            mNServer = new NetServer(mAct, this);
    }

    @Override
    public void requestAsynGet(NAction action) {
        initNetServer();
        mNServer.requestAsynGet(action);

    }

    @Override
    public void useCache(boolean useCache) {
        initNetServer();
        mNServer.useCache(useCache);
    }

    @Override
    public void requestAsynPost(NAction action) {
        initNetServer();
        mNServer.requestAsynPost(action);
    }

    @Override
    public void cancelRequest(int requestCode) {
        if (mNServer != null)
            mNServer.cancelRequest(requestCode);
    }

    @Override
    public void cancelAllRequest() {
        if (mNServer != null)
            mNServer.cancelAllRequest();
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {

    }

    @Override
    public void nofityUpdate(int requestCode, float progress) {

    }

    @Override
    public void dealData(int requestCode, BaseBean bean) {

    }

    @Override
    public void loadFaild(int requestCode, BaseBean bean) {

    }

    @Override
    public void emptyData(int requestCode, BaseBean bean) {

    }

    @Override
    public void requestStart() {

    }

    @Override
    public void requestCancel() {
        UIUtils.cancelLoadDialog();
    }

    @Override
    public void requestFinish() {
        UIUtils.cancelLoadDialog();
    }


    @Override
    public NetSession getSession() {
        return NetSession.instance(mAct);
    }

    @Override
    public void onDestroyView() {
        cancelAllRequest();
        bind.unbind();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.D("视图销毁 " + this);
        that = null;
    }

    @Override
    public void dismissAllowingStateLoss() {
        try {
            super.dismissAllowingStateLoss();
        }catch (Error e){
            e.printStackTrace();
        }

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        }catch (Throwable e){
            e.printStackTrace();
            //
        }

    }

    public void finish() {
        if (mAct != null)
            mAct.finish();
    }
}