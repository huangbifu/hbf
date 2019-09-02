package com.cnsunrun.common.widget.dialog;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBaseDialogFragment;
import com.sunrun.sunrunframwork.adapter.SelectableAdapter;
import com.sunrun.sunrunframwork.adapter.ViewHodler;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * 列表选择样式的对话框
 * Created by WQ on 2017/5/8.
 */

public class ListSelectDialogFragment extends LBaseDialogFragment {
    @BindView(R.id.list)
    ListView list;
    /**
     * 数据源
     */
    ArrayList<String> mData;
    /**
     * 选择监听器
     */
    OnItemSelectedListener onItemSelectedListener;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mData = getArguments().getStringArrayList("list");
        int selectPosition = getArguments().getInt("position");
        /**
         * 设置适配器
         */
        final SelectableAdapter selectableAdapter = new SelectableAdapter<String>(that, mData, R.layout.item_dialog_list_select) {
            @Override
            public void fillView(ViewHodler viewHodler, String s, int i) {
                boolean isSelected = isSelected(i);
                TextView itemTitle = viewHodler.getView(R.id.item_title);
                viewHodler.setVisibility(R.id.item_icon, isSelected);
                viewHodler.setText(R.id.item_title, s);
                itemTitle.getPaint().setFakeBoldText(isSelected);
                viewHodler.setTextColorId(R.id.item_title, isSelected ? R.color.text2 : R.color.text3);
            }

        };
        list.setAdapter(selectableAdapter);
        selectableAdapter.select(selectPosition);
        list.setSelection(selectPosition);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectableAdapter.select(position);
                dismiss();
                if (onItemSelectedListener != null) {
                    onItemSelectedListener.onItemSelected(position, mData.get(position));
                }
            }
        });
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_list_select;
    }


    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = new Dialog(getActivity(), R.style.NoTitleDialog);
        Window dialogWindow = dialog.getWindow();
        //设置对话框从底部进入
        dialogWindow.setWindowAnimations(R.style.bottomInWindowAnim);
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.width = ViewGroup.LayoutParams.MATCH_PARENT;
        p.height = ViewGroup.LayoutParams.WRAP_CONTENT;//高度自己设定
        dialogWindow.setAttributes(p);
        dialogWindow.setGravity(Gravity.BOTTOM);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        return dialog;
    }

    /**
     * 显示对话框
     *
     * @param fragmentManager
     * @param mData
     * @param selectPosition
     * @param onItemSelectedListener
     */
    public static void showListDialog(FragmentManager fragmentManager, ArrayList<String> mData, int selectPosition, OnItemSelectedListener onItemSelectedListener) {
        ListSelectDialogFragment listSelectDialogFragment = new ListSelectDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("list", mData);
        bundle.putInt("position", selectPosition);
        listSelectDialogFragment.setOnItemSelectedListener(onItemSelectedListener);
        listSelectDialogFragment.setArguments(bundle);
        listSelectDialogFragment.show(fragmentManager, ListSelectDialogFragment.class.getName());
    }

    public void setOnItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    public interface OnItemSelectedListener {
        void onItemSelected(int position, String item);
    }

}
