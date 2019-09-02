package com.cnsunrun.chat.dialog;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.base.LBasePopupWindow;
import com.sunrun.sunrunframwork.adapter.ViewHodler;
import com.sunrun.sunrunframwork.adapter.ViewHolderAdapter;

import java.util.List;

import butterknife.BindView;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

/**
 * Created by WQ on 2017/11/3.
 */

public class FilterPopWindow extends LBasePopupWindow {


    @BindView(R.id.typeList)
    ListView typeList;
    AdapterView.OnItemClickListener onItemClickListener;
    public FilterPopWindow(Context context) {
        super(context);
        LayoutInflater from = LayoutInflater.from(context);
        View inflate = from.inflate(R.layout.pop_more_filter, null);
        setContentView(inflate);
        setWidth(MATCH_PARENT);
        setHeight(MATCH_PARENT);
        getContentView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        setBackgroundDrawable(new ColorDrawable(Color.parseColor("#33000000")));
        setFocusable(true);
    }


    @Override
    public void onViewCreated(View view) {
        setOutsideTouchable(true);
    }

    public void setListData(List<?> data, AdapterView.OnItemClickListener onItemClickListener){
        typeList.setAdapter(new ViewHolderAdapter(getContentView().getContext(),data, R.layout.item_filter_text) {
            @Override
            public void fillView(ViewHodler viewHodler, Object s, int i) {
                viewHodler.setVisibility(R.id.layLine,i!=0);
                viewHodler.setText(R.id.item_title, String.valueOf(s));
                TextView itemTitle = viewHodler.getView(R.id.item_title);
                itemTitle.setGravity(Gravity.CENTER);
            }
        });
        typeList.setOnItemClickListener(onItemClickListener);
    }
}


