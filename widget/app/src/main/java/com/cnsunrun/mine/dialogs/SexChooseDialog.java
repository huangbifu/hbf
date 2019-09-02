package com.cnsunrun.mine.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.view.View;

import com.cnsunrun.R;


/**
 * Created by : yepeng on 2018/9/26.
 * function :  性别选择
 */

public class SexChooseDialog {
    private OnViewClickListener onViewClickListener;

    public SexChooseDialog(Context context, OnViewClickListener onViewClickListeners) {
       this.onViewClickListener =onViewClickListeners;
        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
        View dialogView = View.inflate(context, R.layout.layout_dialog_sex, null);
        dialog.setContentView(dialogView);
        dialog.show();
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.tv_boy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClickListener.onItemClick("男");
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.tv_girl).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClickListener.onItemClick("女");
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.tv_secret).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onViewClickListener.onItemClick("保密");
                dialog.dismiss();
            }
        });
    }




    public interface OnViewClickListener {
        void onItemClick(String sex);
    }

    public void setOnRecyclerViewClickListener(OnViewClickListener onRecyclerViewClickListener) {
        this.onViewClickListener = onViewClickListener;
    }
}
