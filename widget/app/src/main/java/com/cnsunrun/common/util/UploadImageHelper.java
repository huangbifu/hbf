package com.cnsunrun.common.util;

import com.cnsunrun.common.widget.MomentPicView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Created by WQ on 2017/11/2.
 */

public class UploadImageHelper {
    String addImgResId = "-1";
    int maxImg=9;
    MomentPicView momentPicView;
    boolean isAddImgInStart=false;
    List<String>currentData=new ArrayList<>();
    public UploadImageHelper(int addImgResId) {
        this.addImgResId = String.valueOf(addImgResId);
    }
    public void attachMomentPicView(MomentPicView momentPicView){
        this.momentPicView=momentPicView;
        momentPicView.setImageUrls(Arrays.asList(addImgResId));
    }

    public void addImgUrl(List<String> imageUrls){
        currentData.addAll(imageUrls);
        if(imageUrls.size()<maxImg){
            if(isAddImgInStart){
                imageUrls.add(0,addImgResId);
            }else {
                imageUrls.add(addImgResId);
            }
            momentPicView.setImageUrls(imageUrls);
        }
    }
}
