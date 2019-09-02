package com.cnsunrun.common.util.selecthelper;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.common.base.LBaseAdapter;

import java.util.List;



/**
 * 提供选择操作的适配器
 * Created by WQ on 2017/5/27.
 */

public abstract class SelectableLBaseAdapter<T extends Selectable.SelectableEntity, K extends BaseViewHolder> extends LBaseAdapter<T, K > implements Selectable {
    public SelectableHelper<T> selectableHelper = new SelectableHelper<>();

    public SelectableLBaseAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);

    }

    public SelectableLBaseAdapter(@Nullable List<T> data) {
        super(data);
    }

    public SelectableLBaseAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    @Override
    public int getItemCount() {
        selectableHelper.setData(getData());
        return super.getItemCount();
    }

    //多选事务逻辑
    @Override
    public int getSelectPosition() {
        return selectableHelper.getSelectPosition();
    }

    @Override
    public int getSelectCount() {
        return selectableHelper.getSelectCount();
    }

    @Override
    public T getSelectItem() {
        return selectableHelper.getSelectItem();
    }

    @Override
    public void setSelectPosition(int position) {
        selectableHelper.setSelectPosition(position);
        notifyItemChanged(position);
    }

    @Override
    public boolean isSelected(int position) {
        return selectableHelper.isSelected(position);
    }

    @Override
    public <T extends SelectableEntity> boolean isSelected(T item) {
        return selectableHelper.isSelected(item);
    }

    @Override
    public void selectAll(boolean isSelect) {
        selectableHelper.selectAll(isSelect);
        notifyDataSetChanged();
    }

    @Override
    public void selectMode(int mode) {
        selectableHelper.selectMode(mode);
    }

    @Override
    public List<T > getAllCheckData() {
        return selectableHelper.getAllCheckData();
    }

}
