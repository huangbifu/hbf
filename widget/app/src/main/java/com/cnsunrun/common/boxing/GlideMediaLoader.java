package com.cnsunrun.common.boxing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.GradientDrawable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.DrawableRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.Resource;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.load.model.LazyHeaders;
import com.bumptech.glide.load.resource.bitmap.BitmapResource;
import com.cnsunrun.R;
import com.cnsunrun.common.CommonApp;
import com.sunrun.sunrunframwork.utils.EmptyDeal;


/**
 * Created by WQ on 2017/5/5.
 */

public class GlideMediaLoader {
    public static void load(Object context, View imgview, String path, int placeholder) {
        if (!String.valueOf(path).startsWith("http")) {
//             path = "file://" + path;
        }
        if(EmptyDeal.isEmpy(path))return;

        GlideUrl cookie = new GlideUrl(path, new LazyHeaders.Builder().addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 RedOne (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36")
.build());
        DrawableRequestBuilder<GlideUrl> stringDrawableRequestBuilder = with(context)
                .load(cookie).dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL).skipMemoryCache(false);
        if (placeholder != 0) {
            stringDrawableRequestBuilder
                    .placeholder(placeholder);
        } ;
        stringDrawableRequestBuilder.into((ImageView) imgview);
    }

    public static void load(Object context, View imgview, String path) {
        load(context, imgview, path, 0);
    }

    public static void loadHead(Object context, View imgview, String path) {
        load(context, imgview, path, R.drawable.ic_def_head);
    }

    static RequestManager with(Object context) {
        if (context instanceof Activity) {
            return Glide.with((Activity) context);
        } else if (context instanceof Fragment) {
            return Glide.with((Fragment) context);
        } else if (context instanceof Context) {
            return Glide.with((Context) context);
        }
        return null;
    }


}
