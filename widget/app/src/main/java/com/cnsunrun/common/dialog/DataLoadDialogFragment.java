package com.cnsunrun.common.dialog;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.NAction;
import com.sunrun.sunrunframwork.http.NetUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;

/**
 * 数据加载的Fragment,用于先加载数据在跳转
 */
public class DataLoadDialogFragment extends LBaseDialogFragment {
    public NAction nAction;
    public onDataLoadeListener onDataLoadeListener;
    public onErrorListener onErrorListener;
    boolean needLoadDialog = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

	@Override
	public Dialog getDialog() {
        if(needLoadDialog){
            super.getDialog();
        }
		return new Dialog(getActivity(), R.style.CustomDialog);
	}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//		getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
//		getDialog().getWindow().setBackgroundDrawable(
//				new ColorDrawable(Color.TRANSPARENT));
//		getDialog().getWindow().setWindowAnimations(R.style.bottomInWindowAnim);
//		getDialog().setCanceledOnTouchOutside(false);
//		getDialog().setCancelable(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(!needLoadDialog){
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        loadData();
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_data_load;
    }

    public void closeDialog() {

    }

    private void loadData() {
        if(needLoadDialog) {
            UIUtils.showLoadDialog(that, "正在加载数据...");
        }
        if (nAction.getRequestType() == POST) {
            NetUtils.setTimeOut(20*1000);//超时20秒
            requestAsynPost(nAction.setRequestCode(1));
        } else {
            requestAsynGet(nAction.setRequestCode(1));
        }

        // .setTypeToken(ExamTopBean.class)
    }

    @Override
    public void nofityUpdate(int requestCode, BaseBean bean) {
        if (bean.status == 1) {
            onDataLoadeListener.onDataLoaded(bean);
            dismissAllowingStateLoss();
        } else {
            dismissAllowingStateLoss();
            UIUtils.shortM(bean);
            if(this.onErrorListener!=null){
                this.onErrorListener.onLoadeError(bean);
            }
        }
        super.nofityUpdate(requestCode, bean);
    }

    public static DataLoadDialogFragment getInstance() {
        return new DataLoadDialogFragment();
    }

    public static DataLoadDialogFragment getInstance(FragmentManager fm, NAction action, onDataLoadeListener onDataLoadeListener) {
        DataLoadDialogFragment dataLoadDialogFragment = new DataLoadDialogFragment();
        dataLoadDialogFragment.setAction(action);
        dataLoadDialogFragment.setOnDataLoadeListener(onDataLoadeListener);
        dataLoadDialogFragment.show(fm, "DataLoadDialogFragment");
        return dataLoadDialogFragment;
    }

    public static DataLoadDialogFragment getInstance(FragmentManager fm, NAction action,boolean needLoadDialog, onDataLoadeListener onDataLoadeListener) {
        DataLoadDialogFragment dataLoadDialogFragment = new DataLoadDialogFragment();
        dataLoadDialogFragment.setAction(action);
        dataLoadDialogFragment.setNeedLoadDialog(needLoadDialog);
        dataLoadDialogFragment.setOnDataLoadeListener(onDataLoadeListener);
        dataLoadDialogFragment.show(fm, "DataLoadDialogFragment");
        return dataLoadDialogFragment;
    }

    public DataLoadDialogFragment setNeedLoadDialog(boolean needLoadDialog) {
        this.needLoadDialog = needLoadDialog;
        return this;
    }

    public DataLoadDialogFragment setAction(NAction nAction) {
        this.nAction = nAction;
        return this;
    }

    public DataLoadDialogFragment setOnDataLoadeListener(DataLoadDialogFragment.onDataLoadeListener onDataLoadeListener) {
        this.onDataLoadeListener = onDataLoadeListener;
        return this;
    }

    public DataLoadDialogFragment setOnErrorListener(DataLoadDialogFragment.onErrorListener onErrorListener) {
        this.onErrorListener = onErrorListener;
        return this;
    }

    @Override
    public void loadFaild(int requestCode, BaseBean bean) {
        super.loadFaild(requestCode, bean);

        if(this.onErrorListener!=null){
            UIUtils.shortM(bean);
            this.onErrorListener.onLoadeError(bean);
        }
    }

    public interface onDataLoadeListener {
        void onDataLoaded(BaseBean bean);
    }
    public interface onErrorListener {
        void onLoadeError(BaseBean bean);
    }

    @Override
    public void requestFinish() {
       dismissAllowingStateLoss();
        super.requestFinish();
    }
}
