package com.cnsunrun.common.base;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.cnsunrun.commonui.widget.FixedPopupWindow;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.NetRequestHandler;
import com.sunrun.sunrunframwork.http.NetServer;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.http.utils.UIUpdateHandler;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * PopupWindow基类
 * Created by WQ on 2017/9/19.
 */

public abstract class LBasePopupWindow extends FixedPopupWindow implements
        NetRequestHandler, UIUpdateHandler {
    private Unbinder unbinder;
    protected NetServer mNServer;
    protected Activity that;
    public LBasePopupWindow(Context context) {
        super(context);
        inited();
    }

    public void inited() {

    }

    public LBasePopupWindow(Context context, AttributeSet attrs) {
        super(context, attrs);
        inited();
    }

    public LBasePopupWindow(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        inited();
    }

    public LBasePopupWindow(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        inited();
    }

    public LBasePopupWindow(View contentView) {
        super(contentView);
        inited();
    }

    public LBasePopupWindow() {
    }

    public LBasePopupWindow(int width, int height) {
        super(width, height);
        inited();
    }

    public LBasePopupWindow(View contentView, int width, int height, boolean focusable) {
        super(contentView, width, height, focusable);
        inited();
    }

    public LBasePopupWindow(View contentView, int width, int height) {
        super(contentView, width, height);
        inited();
    }

    @Override
    public void setContentView(View contentView) {
        super.setContentView(contentView);
        if(unbinder!=null){
            unbinder.unbind();
        }
        that= (Activity) contentView.getContext();
        unbinder = ButterKnife.bind(this, contentView);
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        onViewCreated(getContentView());
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff) {
        super.showAsDropDown(anchor, xoff, yoff);
        onViewCreated(getContentView());
    }

    @Override
    public void showAsDropDown(View anchor, int xoff, int yoff, int gravity) {
        super.showAsDropDown(anchor, xoff, yoff, gravity);
        onViewCreated(getContentView());
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        super.showAtLocation(parent, gravity, x, y);
        onViewCreated(getContentView());
    }

     public abstract void onViewCreated(View view) ;


    /**
     * 初始化网络请求模块,由子类在有需要时调用
     */
    protected void initNetServer() {
        if (mNServer == null)
            mNServer = new NetServer(that, this);
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
        return NetSession.instance(that);
    }
}
