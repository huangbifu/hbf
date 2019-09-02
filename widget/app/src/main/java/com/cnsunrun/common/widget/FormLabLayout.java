package com.cnsunrun.common.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.InputFilter;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;


/**
 * 表单输入行控件,封装简单的左边标签,右边文字/输入框的样式
 * 功能待完善,只实现一部分结果
 * Created by WQ on 2017/9/4.
 */

public class FormLabLayout extends FrameLayout {
    public final static int MODE_TXT=0,MODE_INPUT=1,MODE_IMG=2;
    ImageView imgLeftHint;
    TextView txtImportant;
    TextView txtLab;
    EditText editInput;
    TextView txtContent;
    ImageView imgRightAction;
    private String labTxtStr;
    private String labInputStr;
    private String labContentStr;
    private String labInputHintStr;
    private Drawable labRightIcon;

    boolean labImportant;
    int labMode=0;
    private int labContentColor;
    private int labColor;
    private int labInputLen;

    public FormLabLayout(Context context) {
        super(context);
    }

    public FormLabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.FormLabLayout));
    }

    public FormLabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        parseAttributes(context.obtainStyledAttributes(attrs, R.styleable.FormLabLayout));
    }

    /**
     * Parse the attributes passed to the view from the XML
     *
     * @param a the attributes to parse
     */
    private void parseAttributes(TypedArray a) {
        labTxtStr = a.getString(R.styleable.FormLabLayout_lab_Txt);
        labInputStr = a.getString(R.styleable.FormLabLayout_lab_Input);
        labContentStr = a.getString(R.styleable.FormLabLayout_lab_Content);
        labInputHintStr = a.getString(R.styleable.FormLabLayout_lab_InputHint);
        labImportant = a.getBoolean(R.styleable.FormLabLayout_lab_Important, labImportant);

        labContentColor = a.getColor(R.styleable.FormLabLayout_lab_ContentColor, Color.parseColor("#333333"));
        labColor=a.getColor(R.styleable.FormLabLayout_lab_Color, Color.parseColor("#333333"));
        labMode=a.getInt(R.styleable.FormLabLayout_lab_Mode,0);
        labRightIcon=a.getDrawable(R.styleable.FormLabLayout_lab_RightIcon);
        labInputLen=a.getInt(R.styleable.FormLabLayout_lab_InputLen,labInputLen);
        initView();
    }

    private void initView() {
        inflate(getContext(), R.layout.view_form_lab_row, this);
        txtLab=get(R.id.txtLab);
        editInput=get(R.id.editInput);
        imgLeftHint=get(R.id.imgLeftHint);
        txtImportant=get(R.id.txtImportant);
        txtContent=get(R.id.txtContent);
        imgRightAction=get(R.id.imgRightAction);
        txtImportant.setVisibility(labImportant?VISIBLE:GONE);
        setLabMode(labMode);
        if(labInputLen!=0) {
            editInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(labInputLen)});
        }
        txtLab.setTextColor(labColor);
        txtContent.setTextColor(labContentColor);
        imgRightAction.setImageDrawable(labRightIcon);
        setLabTxtStr(labTxtStr);
        setLabContentStr(labContentStr);
        setLabInputStr(labInputStr);
        setLabInputHintStr(labInputHintStr);
    }

    public void setLabMode(int labMode) {
        this.labMode=labMode;
        txtContent.setVisibility(this.labMode ==0?VISIBLE:GONE);
        editInput.setVisibility(this.labMode ==1?VISIBLE:GONE);
    }

    private int strDp2Px(String dp) {
        final Resources resources = getResources();
        final DisplayMetrics metrics = resources.getDisplayMetrics();
        final int unit = TypedValue.COMPLEX_UNIT_DIP;
        return (int) TypedValue.applyDimension(unit, Integer.parseInt(dp), metrics);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    public String getLabTxtStr() {
        return txtLab.getText().toString();
    }

    public void setLabTxtStr(String labTxtStr) {
        txtLab.setText(labTxtStr);
        this.labTxtStr = labTxtStr;
    }

    public String getLabInputStr() {
        return editInput.getText().toString();
    }

    public void setLabInputStr(String labInputStr) {
        this.labInputStr = labInputStr;
        editInput.setText(labInputStr);
    }

    public String getLabContentStr() {
        return txtContent.getText().toString();
    }

    public void setLabContentStr(String labContentStr) {
        this.labContentStr = labContentStr;
        txtContent.setText(labContentStr);
    }

    public EditText getEditInput() {
        return editInput;
    }

    public void setLabInputHintStr(String labInputHintStr) {
        this.labInputHintStr = labInputHintStr;
        editInput.setHint(labInputHintStr);
    }

    public <T extends View> T get(int id) {
        return (T) findViewById(id);
    }

}
