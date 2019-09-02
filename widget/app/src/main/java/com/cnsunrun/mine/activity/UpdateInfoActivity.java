package com.cnsunrun.mine.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.widget.titlebar.TitleBar;
import com.sunrun.sunrunframwork.bean.BaseBean;
import com.sunrun.sunrunframwork.uiutils.ToastUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的个人资料修改
 */
public class UpdateInfoActivity extends LBaseActivity {

    @BindView(R.id.titleBar)
    TitleBar titleBar;
    @BindView(R.id.edit_title)
    EditText editTitle;

    private String title;
    private String content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        ButterKnife.bind(this);
        if (getIntent() != null) {
            title = getIntent().getStringExtra("title");
            content = getIntent().getStringExtra("content");
            titleBar.setTitle(title);
            editTitle.setText(content);
        }
        //输入类型为文本
        editTitle.setInputType(InputType.TYPE_CLASS_TEXT);
        //限制长度为10
        editTitle.setFilters(new InputFilter[]{new InputFilter.LengthFilter(11)});
        editTitle.setSelection(content.length());

        titleBar.setRightAction(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //修改昵称/姓名
                setInput();
                Intent intent = new Intent();
                intent.putExtra("title", title);
                intent.putExtra("content", editTitle.getText().toString());
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    private void setInput() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

}
