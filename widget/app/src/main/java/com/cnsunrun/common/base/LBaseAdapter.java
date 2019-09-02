package com.cnsunrun.common.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

abstract public class LBaseAdapter<T, K extends BaseViewHolder> extends BaseQuickAdapter<T, K> {


    public LBaseAdapter(@LayoutRes int layoutResId, @Nullable List<T> data) {
        super(layoutResId, data);
    }

    public LBaseAdapter(@Nullable List<T> data) {
        super(data);
    }

    public LBaseAdapter(@LayoutRes int layoutResId) {
        super(layoutResId);
    }

    public void setNewData(List<T> data, boolean notifyChange) {
        if (notifyChange) {
            setNewData(data);
        } else {
            this.mData = data == null ? new ArrayList<T>() : data;
        }
    }
}
    