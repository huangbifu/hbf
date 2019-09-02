package com.cnsunrun.chat.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 消息提示对话框
 * Created by WQ on 2017/10/30.
 */

public class MessageTipDialog extends LBaseDialogFragment {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_content)
    TextView tvContent;
    @BindView(R.id.cancel)
    QMUIRoundButton cancel;
    @BindView(R.id.center_line)
    View centerLine;
    @BindView(R.id.submit)
    QMUIRoundButton submit;
    View.OnClickListener onSubmitAction, onCancelAction;
    DialogInterface.OnDismissListener onDismissListener;
    CharSequence titleTxt="温馨提示", contentTxt, leftBtnTxt="取消", rightBtnTxt="确认";

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle.setText(titleTxt);
        tvContent.setText(contentTxt);
        cancel.setText(leftBtnTxt);
        submit.setText(rightBtnTxt);
        if(this.leftBtnTxt==null){
            centerLine.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_message;
    }


    public static MessageTipDialog newInstance() {
        Bundle args = new Bundle();
        MessageTipDialog fragment = new MessageTipDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public MessageTipDialog setOnSubmitAction(View.OnClickListener onSubmitAction) {
        this.onSubmitAction = onSubmitAction;
        return this;
    }

    public MessageTipDialog setOnCancelAction(View.OnClickListener onCancelAction) {
        this.onCancelAction = onCancelAction;
        return this;
    }

    public MessageTipDialog setTitleTxt(CharSequence titleTxt) {
        this.titleTxt = titleTxt;
        return this;
    }

    public MessageTipDialog setContentTxt(CharSequence contentTxt) {
        this.contentTxt = contentTxt;
        return this;
    }

    public MessageTipDialog setLeftBtnTxt(CharSequence leftBtnTxt) {
        this.leftBtnTxt = leftBtnTxt;
        return this;
    }

    public MessageTipDialog setRightBtnTxt(CharSequence rightBtnTxt) {
        this.rightBtnTxt = rightBtnTxt;
        return this;
    }

    public MessageTipDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }


    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if(onDismissListener!=null){
            onDismissListener.onDismiss(dialog);
        }

    }

    @OnClick({R.id.cancel, R.id.submit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel:
                if (onCancelAction != null) {
                    onCancelAction.onClick(view);
                }
                dismissAllowingStateLoss();
                break;
            case R.id.submit:
                if (onSubmitAction != null) {
                    onSubmitAction.onClick(view);
                }
                dismissAllowingStateLoss();
                break;
        }
    }


}
