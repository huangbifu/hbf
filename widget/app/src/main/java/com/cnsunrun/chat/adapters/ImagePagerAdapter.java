//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.cnsunrun.chat.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.cnsunrun.R;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import java.util.List;

public abstract class ImagePagerAdapter<T> extends PagerAdapter implements OnClickListener {
    protected Context mContext;
    private List<T> imageIdList;
    private int size;
    private boolean isInfiniteLoop;
    ImagePagerAdapter.OnBannerClickListener onBannerClickListener;

    public ImagePagerAdapter(Context context, List<T> imageIdList) {
        this.mContext = context;
        this.imageIdList = imageIdList;
        if (imageIdList != null) {
            this.size = imageIdList.size();
        }

        this.isInfiniteLoop = false;
    }

    public ImagePagerAdapter<T> setOnBannerClickListener(ImagePagerAdapter.OnBannerClickListener onBannerClickListener) {
        this.onBannerClickListener = onBannerClickListener;
        return this;
    }

    public int getCount() {
        return this.isInfiniteLoop ? 2147483647 : this.imageIdList.size();
    }

    public int getPosition(int position) {
        return this.isInfiniteLoop ? position % this.size : position;
    }

    public int getSize() {
        return this.imageIdList == null ? 0 : this.imageIdList.size();
    }

    public View getView(int index, View view, ViewGroup container) {
        ImagePagerAdapter.ViewHolder holder;
        if (view == null) {
            holder = new ImagePagerAdapter.ViewHolder();
            view = View.inflate(this.mContext, R.layout.item_group_viewpage_img, (ViewGroup)null);
            holder.imageView = (ImageView)view.findViewById(R.id.img);
            view.setTag(holder);
//            holder.imageView.setTag(holder);
        } else {
            holder = (ImagePagerAdapter.ViewHolder)view.getTag();
        }

        int position = this.getPosition(index);
        holder.position = position;
        this.setImage(this.imageIdList.get(position), holder.imageView);
//        holder.imageView.setOnClickListener(this);
        view.setOnClickListener(this);
        return view;
    }

    public void setImage(T mItem, ImageView img) {
        if (mItem instanceof Integer) {
            img.setImageResource((Integer)mItem);
        } else if (String.valueOf(mItem).startsWith("http")) {
            this.loadImage(img, String.valueOf(mItem));
        } else {
            img.setImageBitmap(UIUtils.getBitmapForasses(this.mContext, String.valueOf(mItem)));
        }

    }

    protected abstract void loadImage(@NonNull ImageView var1, @NonNull String var2);

    public boolean isInfiniteLoop() {
        return this.isInfiniteLoop;
    }

    public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
        this.isInfiniteLoop = isInfiniteLoop;
        return this;
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public final Object instantiateItem(ViewGroup container, int position) {
        View view = null;
        view = this.getView(position, view, container);
        container.addView(view);
        return view;
    }

    public final void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View)object;
        container.removeView(view);
    }

    public final boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    public void onClick(View v) {
        ImagePagerAdapter.ViewHolder holder = (ImagePagerAdapter.ViewHolder)v.getTag();
        if (holder != null && this.onBannerClickListener != null) {
            this.onBannerClickListener.onBannerClick(holder.position, this.imageIdList.get(holder.position));
        }

    }

    public interface OnBannerClickListener {
        void onBannerClick(int var1, Object var2);
    }

    private static class ViewHolder {
        int position;
        ImageView imageView;

        private ViewHolder() {
        }
    }
}
