package com.cnsunrun.chat.adapters;

import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.mode.MemberInfo.UpdatesListBean;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.common.util.VideoTool;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.Utils;
import com.sunrun.sunrunframwork.utils.log.Logger;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cnsunrun on 2017/11/2.
 * 咵天-详细资料,个人动态图片
 */

public class ChatUserInfoPicAdapter extends BaseQuickAdapter<UpdatesListBean, BaseViewHolder> {
    public ChatUserInfoPicAdapter(@Nullable List<UpdatesListBean> data) {
        super(R.layout.item_chat_userdynamics_pic_tag, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, UpdatesListBean item) {
        int layoutPosition = helper.getLayoutPosition();
        String attachment = item.attachment;

        boolean isVideo = TestTool.checkSuffix(attachment, ".mp4", ".mov");
        ImageView imgUserIcon = helper.getView(R.id.imgUserIcon);
        if (isVideo) {
            createVideoThumbnail(imgUserIcon, attachment, helper.getLayoutPosition() - getHeaderLayoutCount(), 720, 300);
        }else {
            GlideMediaLoader.load(mContext,helper.getView(R.id.imgUserIcon), attachment);
        }
    }


    Set<String> taskSet = new HashSet<>();

    public void createVideoThumbnail(ImageView img, final String url, final int position, final int width, final int height) {
        final String asName = Utils.getMD5(String.valueOf(url)) + ".jpg";
        final NetSession session = NetSession.instance(mContext);
        String path = session.getString(asName);

        if (!EmptyDeal.isEmpy(path) && new File(path).exists()) {
            if (!EmptyDeal.isEmpy(path)) {
                GlideMediaLoader.load(mContext, img, path);
            }
        } else {
            if (taskSet.contains(url)) return;
            taskSet.add(url);
            Logger.D("" + url + "  " + asName);
            AHandler.Task task = new AHandler.Task() {
                Bitmap videoThumbnail = null;

                @Override
                public void task() {
                    File cacheDir = mContext.getCacheDir();
                    videoThumbnail = VideoTool.createVideoThumbnail(url, width, height);
                    if (videoThumbnail != null) {
                        String absolutePath = new File(cacheDir, asName).getAbsolutePath();
                        boolean result = UIUtils.saveBitmapToFile(videoThumbnail, absolutePath);
                        session.put(asName, absolutePath);
                    }
                    super.task();
                }

                @Override
                public void update() {
                    super.update();
                    taskSet.remove(url);
                    if (videoThumbnail != null) {
                        notifyDataSetChanged();
//                        notifyItemChanged(position);
                    }

                }
            };
            AHandler.runTask(task);
        }
        return;
    }
}