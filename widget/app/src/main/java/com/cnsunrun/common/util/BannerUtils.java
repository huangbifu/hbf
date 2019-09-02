package com.cnsunrun.common.util;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.adapter.CBPageAdapter;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.view.CBLoopViewPager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.cnsunrun.R;

import java.util.List;

/**
 * @时间: 2016/11/24
 * @功能描述: 用于显示Benner的工具类
 */

public class BannerUtils {


    public static void setBannerNoIndicator(ConvenientBanner convenientBanner, List<?> info) {
        CBLoopViewPager viewPager = convenientBanner.getViewPager();
        CBPageAdapter adapter = viewPager.getAdapter();
        if (adapter == null || viewPager.getTag() == null) {
            convenientBanner.setCanLoop(false);
            convenientBanner.startTurning(5000);
            viewPager.setTag(info);
            //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
            convenientBanner.setPages(
                    new CBViewHolderCreator<NetImageHolderView>() {
                        @Override
                        public NetImageHolderView createHolder() {
                            return new NetImageHolderView();
                        }
                    }, info);
            convenientBanner.setManualPageable(true);//设置不能手动影响
        } else {
            List tmpInfo = (List) viewPager.getTag();
            tmpInfo.clear();
            tmpInfo.addAll(info);
            adapter.notifyDataSetChanged();
        }
    }


    public static void addView(ConvenientBanner banner, View view) {

    }

    /**
     * 填充网络轮播图数据
     *
     * @param convenientBanner
     * @param info
     * @param isAuto
     */
    public static void setNetBanner(ConvenientBanner convenientBanner, List info, boolean isAuto) {
        //自定义你的Holder，实现更多复杂的界面，不一定是图片翻页，其他任何控件翻页亦可。
        CBLoopViewPager viewPager = convenientBanner.getViewPager();
        CBPageAdapter adapter = viewPager.getAdapter();
        if (adapter == null || viewPager.getTag() == null) {
            if (isAuto) {
                convenientBanner.startTurning(5000);
                convenientBanner.setCanLoop(true);
            } else {
                convenientBanner.setCanLoop(false);
            }
            viewPager.setTag(info);
            convenientBanner.setPages(
                    new CBViewHolderCreator<NetImageHolderView>() {
                        @Override
                        public NetImageHolderView createHolder() {
                            return new NetImageHolderView();
                        }
                    }, info);
            //设置不能手动影响
            convenientBanner.setManualPageable(true);
            convenientBanner.setPageIndicator(new int[]{R.drawable.shape_indicator_huan, R.drawable.shape_indicator_yuan})
                    //设置指示器的方向
                    .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL);
        } else {
            List tmpInfo = (List) viewPager.getTag();
            tmpInfo.clear();
            tmpInfo.addAll(info);
            adapter.notifyDataSetChanged();
        }

    }

    public static class NetImageHolderView implements Holder {
        private ImageView imageView;

        @Override
        public View createView(Context context) {
            imageView = new ImageView(context);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return imageView;
        }

        @Override
        public void UpdateUI(Context context, final int position, Object data) {
                Glide.with(context)
                        .load(String.valueOf(data))
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .dontAnimate()
                        .into(imageView);
                imageView.setTag(String.valueOf(data));

        }
    }

}
