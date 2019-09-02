package com.cnsunrun.chat.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.cnsunrun.common.event.DefaultEvent;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.cnsunrun.commonui.widget.roundwidget.QMUIRoundButton;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.InputMethodUtil;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.OnClick;

import static android.text.InputType.TYPE_TEXT_FLAG_MULTI_LINE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
import static com.cnsunrun.common.config.Const.EVENT_CHAT_COMMENT;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_COMMENT_CODE;
import static com.cnsunrun.common.quest.BaseQuestConfig.NEIGHBORHOOD_IM_UPDATES_COMMENT_REPLY_CODE;

/**
 * 评论对话框
 * Created by WQ on 2017/10/30.
 */

public class CommentDialog extends LBaseDialogFragment {
    @BindView(R.id.editContent)
    EditText editContent;
    @BindView(R.id.btnSend)
    Button btnSend;
    DialogInterface.OnDismissListener onDismissListener;
    CharSequence  contentTxt, contentHint="说点什么...";
    String id, comment_id;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        InputMethodUtil.ShowKeyboard(editContent);
        editContent.setText(contentTxt);
        editContent.setHint(contentHint);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
        editContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    sendComment();
                }
                return false;
            }
        });
        editContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btnSend.setEnabled(!TextUtils.isEmpty(editContent.getText().toString().trim()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                saveRecord();
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodUtil.HideKeyboard(mAct.getWindow().getDecorView());
                dismissAllowingStateLoss();
            }
        });
        loadRecord();
    }
    private void sendComment() {
        if(EmptyDeal.isEmpy(editContent)){
            UIUtils.shortM("内容不能为空");
            return ;
        }
        //发送
        if(comment_id!=null){
            UIUtils.showLoadDialog(that, "提交回复..");
//            BaseQuestStart.NeighborhoodImUpdatesCommentReply(this, id,comment_id, editContent);
        }else {
            UIUtils.showLoadDialog(that, "提交评论..");
//            BaseQuestStart.NeighborhoodImUpdatesComment(this, id, editContent);
        }
    }

    public void nofityUpdate(int requestCode,BaseBean bean){
        switch(requestCode){
            case NEIGHBORHOOD_IM_UPDATES_COMMENT_CODE:
            case NEIGHBORHOOD_IM_UPDATES_COMMENT_REPLY_CODE:
                UIUtils.shortM(bean);
                if(bean.status==1){
                    EventBus.getDefault().post(DefaultEvent.createEvent(EVENT_CHAT_COMMENT,null));//通知界面刷新
                    editContent.getText().clear();
                    saveRecord();
                    InputMethodUtil.HideKeyboard(mAct.getWindow().getDecorView());
                    dismissAllowingStateLoss();
                }
                break;
        }
        super.nofityUpdate(requestCode,bean);
    }

    void saveRecord(){
        getSession().put(String.format("%s_%s",id,comment_id),editContent.getText().toString());
    }
    void loadRecord(){
        String string = getSession().getString(String.format("%s_%s", id, comment_id));
        if(!EmptyDeal.isEmpy(string)) {
            editContent.setText(string);
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_comment;
    }


    public static CommentDialog newInstance() {
        Bundle args = new Bundle();
        CommentDialog fragment = new CommentDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public CommentDialog setContentTxt(CharSequence contentTxt) {
        this.contentTxt = contentTxt;
        return this;
    }

    public CommentDialog setContentHintTxt(CharSequence contentHint) {
        this.contentHint = contentHint;
        return this;
    }

    public CommentDialog setId(String id) {
        this.id = id;
        return this;
    }

    public CommentDialog setComment_id(String comment_id) {
        this.comment_id = comment_id;
        return this;
    }

    public CommentDialog setOnDismissListener(DialogInterface.OnDismissListener onDismissListener) {
        this.onDismissListener = onDismissListener;
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public Dialog getDialog(){
        return new Dialog(getActivity(), R.style.Comment_Dialog);
    }

}
