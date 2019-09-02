package com.cnsunrun.common.logic;

import android.content.Context;
import android.content.Intent;

import com.bilibili.boxing.Boxing;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.model.entity.impl.ImageMedia;
import com.bilibili.boxing.utils.ImageCompressor;
import com.cnsunrun.common.widget.MomentPicView;
import com.sunrun.sunrunframwork.utils.EmptyDeal;

import java.util.ArrayList;

/**
 * Created by WQ on 2017/11/30.
 */

public class ImgDealLogic {
    /**
     * 设置数据到图片展示控件上
     * @param imgPics
     * @param data
     */
    public static void setPicsData(MomentPicView imgPics,Intent data){
        ArrayList<BaseMedia> images = Boxing.getResult(data);
        ImageCompressor compressor=new ImageCompressor(imgPics.getContext());
        if (images != null) {
            ArrayList<String> imgPath = new ArrayList<>();
            for (BaseMedia image : images) {
                ImageMedia imageMedia = (ImageMedia) image;
//                imageMedia.compress(compressor);
                imageMedia.compress(compressor, 300 * 1024);
                imgPath.add(imageMedia.getThumbnailPath());
            }
            imgPics.setImageUrls(imgPath);
        }
    }

    /**
     * 获取单张图片路径
     * @param context
     * @param data
     * @return
     */
    public static String getPic(Context context,Intent data){
        ImageCompressor compressor=new ImageCompressor(context);
        ArrayList<BaseMedia> result = Boxing.getResult(data);
        if (!EmptyDeal.isEmpy(result)) {
            ImageMedia imageMedia = (ImageMedia) result.get(0);
//            imageMedia.compress(compressor);
            imageMedia.compress(compressor, 300 * 1024);
            return  imageMedia.getThumbnailPath();
        }
        return "";
    }
}
