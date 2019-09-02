package com.cnsunrun.mine.dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.CommonIntent;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.cnsunrun.common.dialog.DataLoadDialogFragment;
import com.cnsunrun.common.quest.BaseQuestStart;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.http.utils.JsonDeal;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.InputMethodUtil;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

import static android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN;
import static android.view.WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE;

/**
 * 输入支付密码
 * Created by WQ on 2017/10/27.
 */

public class InputPasswordDialog extends LBaseDialogFragment {
    @BindView(R.id.editPwd)
    EditText editPwd;
    @BindView(R.id.editPwd2)
    TextView editPwd2;
    @BindView(R.id.txtForget)
    TextView txtForget;
    boolean isSetDealPwd = true;
    View.OnClickListener listener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editPwd.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (editPwd.length() != 6) {
                        UIUtils.shortM("请输入6位支付密码");
                        return true;
                    }
                    if (listener != null) {
                        listener.onClick(v);
                    }
                    dismissAllowingStateLoss();
                    return true;
                }
                return false;
            }
        });
        editPwd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editPwd.length() == 6) {
                    if (listener != null) {
                        listener.onClick(editPwd);
                        dismissAllowingStateLoss();
                    }
                }


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
            }
        });
//        InputMethodUtil.ShowKeyboard(editPwd);
//        editPwd.set
        DataLoadDialogFragment.getInstance(getChildFragmentManager(), BaseQuestStart.UserSafeIndex(), new DataLoadDialogFragment.onDataLoadeListener() {
            @Override
            public void onDataLoaded(BaseBean bean) {
                try {
                    JSONObject jsonObj = JsonDeal.createJsonObj(bean.toString());
                    isSetDealPwd = "1".equals(jsonObj.optString("is_set_deal_password"));
//                    isSetDealPwd = false;
                    txtForget.setText(isSetDealPwd ? "忘记密码" : "设置支付密码");
                    editPwd.setFocusable(isSetDealPwd);
                    editPwd.setHint(isSetDealPwd ? "请输入6位支付密码" : "请设置支付密码");
                    if (!isSetDealPwd) {
                        editPwd2.setVisibility(View.VISIBLE);
                        InputMethodUtil.HideKeyboard(editPwd);
                    }else {
                        editPwd.setVisibility(View.VISIBLE);
                        InputMethodUtil.ShowKeyboard(editPwd);
                    }
                } catch (Error e) {
                    e.printStackTrace();
                }
//                CommonIntent.startModifyPwdActivity(that,"1".equals(isSetDealPwd)?1:2);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_input_pay_password;
    }

    public static InputPasswordDialog newInstance() {

        Bundle args = new Bundle();

        InputPasswordDialog fragment = new InputPasswordDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.getWindow().setSoftInputMode(SOFT_INPUT_STATE_ALWAYS_HIDDEN | SOFT_INPUT_ADJUST_RESIZE);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    public InputPasswordDialog setOnSubmitAction(View.OnClickListener listener) {
        this.listener = listener;
        return this;
    }

    @OnClick({R.id.txtForget,R.id.editPwd2})
    public void onClick() {
        if (!isSetDealPwd) {
            CommonIntent.startSettingPayPasswordActivity(that);
        } else {
            CommonIntent.startModifyPayPasswordActivity(that);
        }

        dismissAllowingStateLoss();
    }
}
