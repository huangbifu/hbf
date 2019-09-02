package com.cnsunrun.common.util;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.widget.TextView;

import com.sunrun.sunrunframwork.utils.EmptyDeal;

/**
 * Created by wangchao on 2018-08-16.
 * 设置字体的工具类
 */
public class FontUtils {
    /**
     * 设置字体平方
     * @param view
     * @param context
     */
    public static void setFonts(TextView view, Context context) {
        AssetManager mgr = context.getAssets();
        Typeface fromAsset = Typeface.createFromAsset(mgr, "fonts/medium.ttf");
        view.setTypeface(fromAsset);
    }
    public static String setChangeLine(String text){
        if (EmptyDeal.isEmpy(text)) return "";
        StringBuilder stringBuilder=new StringBuilder(text);
        stringBuilder.replace(10,11,"\n");
        return stringBuilder.toString();
    }
}
