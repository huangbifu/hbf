package com.cnsunrun.common.widget;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.LayoutRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;


/**
 * @时间: 2017/5/23
 * @功能描述:
 */


public class EmptyView {
    private ImageView imgNoData;
    private TextView txtNoData;
    private View noDataView;

    public EmptyView(View view) {
        this.noDataView = view;
        imgNoData = (ImageView) view.findViewById(R.id.imgNoData);
        txtNoData = (TextView) view.findViewById(R.id.txtNoData);
    }

    public EmptyView setTips(String tips) {
        txtNoData.setText(tips);
        return this;
    }

    public EmptyView setTipIcon(@DrawableRes int icon) {
        imgNoData.setImageResource(icon);
        return this;
    }

    public View getView() {
        return noDataView;
    }


    public static EmptyView EmptyView(Context context, @LayoutRes int layoutId, String tips, @DrawableRes int icon) {
        EmptyView emptyView = new EmptyView(View.inflate(context, layoutId,null));
        return emptyView.setTipIcon(icon).setTips(tips);
    }

}
