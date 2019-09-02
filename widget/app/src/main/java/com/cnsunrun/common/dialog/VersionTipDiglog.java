package com.cnsunrun.common.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by wangchao on 2018-10-09.
 * 版本提示框
 */
public class VersionTipDiglog extends LBaseDialogFragment {
    @BindView(R.id.submit)
    QMUIRoundButton submit;
    View.OnClickListener onSubmitAction;
    DialogInterface.OnDismissListener onDismissListener;
    String contentStr;
    String btnStr;
    boolean isUpdate=false;
    @BindView(R.id.tvContent)
    TextView tvContent;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(contentStr!=null){
            tvContent.setText(contentStr);
            tvContent.setGravity(Gravity.CENTER);
        }
        if(btnStr!=null){
            submit.setText(btnStr);
        }
        getDialog().setCancelable(false);
        getDialog().setCanceledOnTouchOutside(false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        DialogInterface.OnKeyListener keylistener = new DialogInterface.OnKeyListener(){
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode==KeyEvent.KEYCODE_BACK&&event.getRepeatCount()==0)
                {
                    return true;
                }
                else
                {
                    return false;
                }
            }
        } ;
        dialog.setOnKeyListener(keylistener);
        return dialog;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_version_tip;
    }


    public static VersionTipDiglog newInstance() {
        Bundle args = new Bundle();
        VersionTipDiglog fragment = new VersionTipDiglog();
        fragment.setArguments(args);
        return fragment;
    }

    public VersionTipDiglog setOnSubmitAction(View.OnClickListener onSubmitAction) {
        this.onSubmitAction = onSubmitAction;
        return this;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
            onDismissListener.onDismiss(dialog);
        }

    }

    public VersionTipDiglog setTvContent(String tvContent) {
        this.contentStr = tvContent;
        return this;
    }

    public VersionTipDiglog setBtnStr(String btnStr) {
        this.btnStr = btnStr;
        return this;
    }

    public VersionTipDiglog setUpdate(boolean update) {
        isUpdate = update;
        return this;
    }

    @OnClick(R.id.submit)
    public void onViewClicked(View view) {
        if (onSubmitAction != null) {
            onSubmitAction.onClick(view);
        }
        if(!isUpdate) {
            dismissAllowingStateLoss();
        }
    }


}
