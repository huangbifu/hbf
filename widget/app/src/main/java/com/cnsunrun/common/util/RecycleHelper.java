package com.cnsunrun.common.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * Created by WQ on 2017/10/12.
 */

public class RecycleHelper {
    public static void setLinearLayoutManager(RecyclerView recyclerView,int orientation){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext(),orientation,false));
    }
}
