package com.cnsunrun.common.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by ZhouBin on 2017/6/12.
 * Effect: 引导页的adapter
 */

public class BootPagerViewPagerAdapter extends PagerAdapter {

    private List<ImageView> imageViews;
    private Context context;

    public BootPagerViewPagerAdapter(List<ImageView> imageViews, Context context) {
        this.imageViews = imageViews;
        this.context = context;
    }

    @Override
    public int getCount() {
        return imageViews == null ? 0 : imageViews.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    //添加imageview
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        //从集合中得到imageview
        ImageView imageView = imageViews.get(position);
        //添加到容器中
        container.addView(imageView);
        return imageView;

    }

    //删除imageview
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
