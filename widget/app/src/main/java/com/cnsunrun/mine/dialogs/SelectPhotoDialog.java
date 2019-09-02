package com.cnsunrun.mine.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.cnsunrun.R;


/**
 * Created by yepeng on 2017/8/25.
 * Effect: 选择图片的diaLog
 */

public class SelectPhotoDialog extends Dialog {

    private TextView tv_select_item_one, tv_select_item_two, tv_cancel;
    private OnSelectItemClickListener onSelectItemClickListener;

    public SelectPhotoDialog(Context context) {
        super(context);
        Window window = this.getWindow();
        window.requestFeature(Window.FEATURE_NO_TITLE);
        View popupView = View.inflate(context, R.layout.select_item_photo_view, null);
        window.setContentView(popupView);
        initViews(popupView);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.windowAnimations = R.style.bottomInWindowAnim;
        lp.gravity = Gravity.BOTTOM;
        window.setAttributes(lp);
        window.setBackgroundDrawableResource(android.R.color.transparent);
        SelectPhotoDialog.this.show();
    }

    private void initViews(View popupView) {
        tv_select_item_one = (TextView) popupView.findViewById(R.id.tv_select_item_one);
        tv_select_item_two = (TextView) popupView.findViewById(R.id.tv_select_item_two);
        tv_cancel = (TextView) popupView.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SelectPhotoDialog.this.dismiss();
            }
        });

        tv_select_item_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectItemClickListener != null) {
                    onSelectItemClickListener.selectItemOne(v);
                }
                SelectPhotoDialog.this.dismiss();
            }
        });
        tv_select_item_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelectItemClickListener != null) {
                    onSelectItemClickListener.selectItemTwo(v);
                }
                SelectPhotoDialog.this.dismiss();
            }
        });

    }

    /**
     * 设置选项一
     *
     * @param text
     */
    public void setPhotoAlbumsText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_select_item_one.setText(text);
        }

    }

    /**
     * 设置选项一文字颜色
     *
     */
    public void setPhotoAlbumsTextColor(int color) {
        tv_select_item_one.setTextColor(color);
    }

    /**
     * 设置选项二
     *
     * @param text
     */
    public void setSelectTakeText(String text) {
        if (!TextUtils.isEmpty(text)) {
            tv_select_item_two.setText(text);
        }

    }

    /**
     * 设置选项二文字颜色
     *
     */
    public void setSelectTakeTextColor(int color) {
        tv_select_item_two.setTextColor(getContext().getResources().getColor(color));

    }

    /**
     * 设置取消文字颜色
     *
     */
    public void setCancelTextColor(int color) {
       tv_cancel.setTextColor(getContext().getResources().getColor(color));

    }


    public interface OnSelectItemClickListener {

        void selectItemOne(View view);

        void selectItemTwo(View view);


    }

    public void setOnSelectItemClickListener(OnSelectItemClickListener onSelectItemClickListener) {
        this.onSelectItemClickListener = onSelectItemClickListener;
    }
}
