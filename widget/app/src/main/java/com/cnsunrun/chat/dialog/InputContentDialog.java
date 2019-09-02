package com.cnsunrun.chat.dialog;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.cnsunrun.commonui.widget.SwitchButton.SwitchButton;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 单信息输入对话框
 * Created by WQ on 2017/10/30.
 */

public class InputContentDialog extends LBaseDialogFragment {
    @BindView(R.id.tvTitle)
    TextView tvTitle;
    @BindView(R.id.cancel)
    QMUIRoundButton cancel;
    @BindView(R.id.submit)
    QMUIRoundButton submit;
    @BindView(R.id.editContent)
    EditText editContent;
    @BindView(R.id.tvContent)
    TextView tvContent;

    @BindView(R.id.layBotton)
    View layBotton;
    @BindView(R.id.sb_no_password_bet)
    SwitchButton sb_no_password_bet;
    View.OnClickListener onSubmitAction, onCancelAction;
    SwitchButton.OnCheckedChangeListener onCheckedChangeListener;
    DialogInterface.OnDismissListener onDismissListener;
    CharSequence titleTxt = "提示", contentTxt, contentHint, leftBtnTxt = "取消", rightBtnTxt = "确认",infoContentTxt;
    boolean isInputPwd=false;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tvTitle.setText(titleTxt);
        cancel.setText(leftBtnTxt);
        submit.setText(rightBtnTxt);
        editContent.setText(contentTxt);
        editContent.setHint(contentHint);
        tvContent.setText(infoContentTxt);
        tvContent.setVisibility(EmptyDeal.isEmpy(infoContentTxt)?View.GONE:View.VISIBLE);
        if (isInputPwd) {
            editContent.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editContent.setInputType(InputType.TYPE_CLASS_NUMBER);
        } else {
//            editContent.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        }
        layBotton.setVisibility(onCheckedChangeListener==null?View.GONE:View.VISIBLE);
        sb_no_password_bet.setOnCheckedChangeListener(onCheckedChangeListener);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_input_content;
    }


    public static InputContentDialog newInstance() {
        Bundle args = new Bundle();
        InputContentDialog fragment = new InputContentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public InputContentDialog setOnSubmitAction(View.OnClickListener onSubmitAction) {
        this.onSubmitAction = onSubmitAction;
        return this;
    }

    public InputContentDialog setOnCancelAction(View.OnClickListener onCancelAction) {
        this.onCancelAction = onCancelAction;
        return this;
    }

    public InputContentDialog setTitleTxt(CharSequence titleTxt) {
        this.titleTxt = titleTxt;
        return this;
    }

    public InputContentDialog setContentTxt(CharSequence contentTxt) {
        this.contentTxt = contentTxt;
        return this;
    }

    public InputContentDialog setContentHintTxt(CharSequence contentHint) {
        this.contentHint = contentHint;
        return this;
    }

    public InputContentDialog setLeftBtnTxt(CharSequence leftBtnTxt) {
        this.leftBtnTxt = leftBtnTxt;
        return this;
    }

    public InputContentDialog setRightBtnTxt(CharSequence rightBtnTxt) {
        this.rightBtnTxt = rightBtnTxt;
        return this;
    }

    public InputContentDialog setInfoContentTxt(CharSequence infoContentTxt) {
        this.infoContentTxt = infoContentTxt;
        return this;
    }

    public InputContentDialog setInputPwd(boolean inputPwd) {
        isInputPwd = inputPwd;
        return this;
    }

    public InputContentDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
        return this;
    }
    public InputContentDialog setOnSwitchBtnAction(SwitchButton.OnCheckedChangeListener onCheckedChangeListener) {

        this.onCheckedChangeListener = onCheckedChangeListener;
        return this;
    }
    public String getInputContent() {
        return editContent.getText().toString();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (onDismissListener != null) {
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
//                dismissAllowingStateLoss();
                break;
        }
    }


}
