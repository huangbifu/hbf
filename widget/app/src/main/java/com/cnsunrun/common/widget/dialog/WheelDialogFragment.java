package com.cnsunrun.common.widget.dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.BaseDialogFragment;
import com.cnsunrun.common.util.ScreenUtil;
import com.cnsunrun.common.widget.WheelView;



import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnsunrun on 2017-12-12.
 */

public class WheelDialogFragment extends BaseDialogFragment implements WheelView.OnValueChangeListener {
    public static final String DIALOG_LEFT = "dialog_left";
    public static final String DIALOG_RIGHT = "dialog_right";
    public static final String DIALOG_WHEEL = "dialog_wheel";
    TextView tvWheelDialogLeft;
    TextView tvWheelDialogRight;
    WheelView WheelViewDialog;

    private WheelView wheelView;
    private TextView tvLeft, tvRight;
    List device_type;
    private String[] dialogWheel;
    private String dialogLeft, dialogRight;
    private OnWheelDialogListener onWheelDialogListener;
    private int defIndex;


    @Override
    public void onStart() {
        super.onStart();
        //设置对话框显示在底部
        getDialog().getWindow().setGravity(Gravity.BOTTOM);
        //设置对话框弹出动画，从底部滑入，从底部滑出
        getDialog().getWindow().getAttributes().windowAnimations = R.style.Dialog_Animation;
        //设置让对话框宽度充满屏幕
        getDialog().getWindow().setLayout(ScreenUtil.getScreenWidth(activity), getDialog().getWindow().getAttributes().height);
    }

    @Override
    protected void initView(View view) {
        tvLeft = (TextView) view.findViewById(R.id.tv_wheel_dialog_left);
        tvRight = (TextView) view.findViewById(R.id.tv_wheel_dialog_right);
        wheelView = (WheelView) view.findViewById(R.id.WheelView_dialog);

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        dialogWheel = bundle.getStringArray(DIALOG_WHEEL);
        dialogLeft = bundle.getString(DIALOG_LEFT);
        dialogRight = bundle.getString(DIALOG_RIGHT);
    }

    @Override
    protected int getLayoutID() {
        return R.layout.view_dialog_wheel;
    }


    @Override
    protected void setSubView() {
        tvLeft.setText(dialogLeft);
        tvRight.setText(dialogRight);

        wheelView.refreshByNewDisplayedValues(dialogWheel);
        wheelView.setValue(defIndex);
        //设置是否可以上下无限滑动
        wheelView.setWrapSelectorWheel(false);
        wheelView.setDividerColor(R.color.submit_btn_bg);
        wheelView.setSelectedTextColor(R.color.home_search_bg);
        wheelView.setNormalTextColor(R.color.hint_text_color);
    }

    public void setDefIndex(int defIndex) {
        this.defIndex = defIndex;

    }

    @Override
    protected void initEvent() {
        //左边按钮
        tvLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onWheelDialogListener != null) {
                    onWheelDialogListener.onClickLeft(WheelDialogFragment.this, "");
                }
            }
        });

        //右边按钮
        tvRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onWheelDialogListener != null) {
                    onWheelDialogListener.onClickRight(WheelDialogFragment.this, getWheelValue());
                }
            }
        });
    }

    @Override
    protected void onCancel() {

    }

    /**
     * 获取当前值
     *
     * @return
     */
    private Object getWheelValue() {
//        String[] content = wheelView.getDisplayedValues();
        return this.device_type.get(wheelView.getValue() - wheelView.getMinValue());
    }

    @Override
    public void onValueChange(WheelView picker, int oldVal, int newVal) {
        String[] content = wheelView.getDisplayedValues();
        if (content != null && onWheelDialogListener != null) {
            onWheelDialogListener.onValueChanged(WheelDialogFragment.this, content[newVal - wheelView.getMinValue()]);
        }
    }

    public void setData(List device_type) {
        List<String> strings=new ArrayList<>();
        this.device_type=device_type;
        for (int i = 0; i < device_type.size(); i++) {

            strings.add(device_type.get(i).toString());
        }
        dialogWheel = strings.toArray(new String[strings.size()]);
    }


    public interface OnWheelDialogListener<T> {
        /**
         * 左边按钮单击事件回调
         *
         * @param dialog
         * @param value
         */
        void onClickLeft(DialogFragment dialog, String value);

        /**
         * 右边按钮单击事件回调
         *
         * @param dialog
         * @param value
         */
        void onClickRight(DialogFragment dialog, T value);

        /**
         * 滑动停止时的回调
         *
         * @param dialog
         * @param value
         */
        void onValueChanged(DialogFragment dialog, String value);
    }

    /**
     * 对外开放的方法
     *
     * @param onWheelDialogListener
     */
    public void setWheelDialogListener(OnWheelDialogListener onWheelDialogListener) {
        this.onWheelDialogListener = onWheelDialogListener;
    }
}
