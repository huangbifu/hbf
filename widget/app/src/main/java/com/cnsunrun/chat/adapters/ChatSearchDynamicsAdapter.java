package com.cnsunrun.chat.adapters;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ListPopupWindow;
import android.text.TextUtils;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.cnsunrun.R;
import com.cnsunrun.chat.dialog.CommentDialog;
import com.cnsunrun.chat.mode.ChatDynamicsBean;
import com.cnsunrun.common.base.LBaseActivity;
import com.cnsunrun.common.boxing.GlideMediaLoader;
import com.cnsunrun.common.quest.Config;
import com.cnsunrun.common.util.TestTool;
import com.cnsunrun.common.util.VideoTool;
import com.cnsunrun.common.widget.MomentPicView;
import com.sunrun.sunrunframwork.adapter.FuncAdapter;
import com.sunrun.sunrunframwork.adapter.ViewHodler;
import com.sunrun.sunrunframwork.adapter.ViewHolderAdapter;
import com.sunrun.sunrunframwork.http.cache.NetSession;
import com.sunrun.sunrunframwork.uibase.BaseActivity;
import com.sunrun.sunrunframwork.uiutils.PictureShow;
import com.sunrun.sunrunframwork.uiutils.TextColorUtils;
import com.sunrun.sunrunframwork.uiutils.UIUtils;
import com.sunrun.sunrunframwork.utils.AHandler;
import com.sunrun.sunrunframwork.utils.EmptyDeal;
import com.sunrun.sunrunframwork.utils.Utils;
import com.sunrun.sunrunframwork.utils.log.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by cnsunrun on 2017/11/2.
 * 咵天-动态适配器
 */

public class ChatSearchDynamicsAdapter extends BaseQuickAdapter<ChatDynamicsBean.ListBean, BaseViewHolder> {
    SparseBooleanArray expandSet = new SparseBooleanArray();
    boolean isDetails = false;
    boolean isFriend=true;
    ListPopupWindow listPopupWindow;//搜索类型切换窗

    public ChatSearchDynamicsAdapter(@Nullable List<ChatDynamicsBean.ListBean> data) {
        super(R.layout.item_chat_dynamics, data);
    }

    public ChatSearchDynamicsAdapter(@Nullable List<ChatDynamicsBean.ListBean> data, boolean isDetails) {
        super(R.layout.item_chat_dynamics, data);
        this.isDetails = isDetails;
    }

    @Override
    protected void convert(BaseViewHolder helper, final ChatDynamicsBean.ListBean item) {
        final int layoutPosition = helper.getLayoutPosition();

        GlideMediaLoader.load(mContext, helper.getView(R.id.imgUserIcon), item.avatar);
        helper.setText(R.id.txtUserName, item.nickname);
        helper.setText(R.id.txtContent, item.content);
        helper.setText(R.id.txtAddTime, item.add_time);
        final MomentPicView imgPics = helper.getView(R.id.imgPics);
        imgPics.setImageUrls(TestTool.list2StringList(item.attachment_list));
        imgPics.setOnClickItemListener(new MomentPicView.OnClickItemListener() {
            @Override
            public void onClick(int i, final ArrayList<String> url) {
                PictureShow pictureShow = new PictureShow(mContext, url, i);

                pictureShow.setImageOperate(TestTool.asArrayList(new FuncAdapter.FuncItem("收藏")), new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                       /* UIUtils.showLoadDialog(mContext, "收藏中..");
                        String attachUrl = url.get((int) id);
                        NAction nAction = attachUrl == null ? BaseQuestStart.NeighborhoodImCollectUpdatesContent(null, item.id) :
                                BaseQuestStart.NeighborhoodImCollectUpdatesAttachment(null, item.id, item.attachment_list.get((int) id).id);
                        DataLoadDialogFragment.getInstance(((LBaseActivity) mContext).getSupportFragmentManager(), nAction, new DataLoadDialogFragment.onDataLoadeListener() {
                            @Override
                            public void onDataLoaded(BaseBean bean) {
                                UIUtils.shortM(bean);
                            }
                        });*/
                    }
                });
                pictureShow.show();
            }
        });
        helper.setOnLongClickListener(R.id.txtContent, new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOperationMenu(item, null);
                return true;
            }
        });
        imgPics.setOnLongClickItemListener(new MomentPicView.OnLongClickItemListener() {
            @Override
            public void onLongClick(int i, ArrayList<String> url) {
                showOperationMenu(item, item.attachment_list.get(i).id);
            }
        });


        String likeInfo = Utils.listToString(EmptyDeal.dealNull(item.like_list), new Utils.DefaultToString<ChatDynamicsBean.ListBean.LikeListBean>(", ", "", " 等" + item.likes + "人觉得赞"));
        helper.setText(R.id.txtLikeInfo, likeInfo);
        helper.setVisible(R.id.layLike, !EmptyDeal.isEmpy(item.like_list));
        final List<ChatDynamicsBean.ListBean.CommentListBean> comment_list = item.comment_list;
        helper.setVisible(R.id.layComments, !EmptyDeal.isEmpy(comment_list));
        helper.setVisible(R.id.viewHandleLine, !(EmptyDeal.isEmpy(item.like_list) && EmptyDeal.isEmpy(comment_list)));
        helper.setVisible(R.id.layCommGap, !EmptyDeal.isEmpy(comment_list));
        final ListView moreCommentList = helper.getView(R.id.moreCommentList);
        moreCommentList.setFocusable(false);
        int commentLayoutId = isDetails ? R.layout.item_chat_dynamics_comment_details : R.layout.item_chat_dynamics_comment;
        moreCommentList.setAdapter(new ViewHolderAdapter<ChatDynamicsBean.ListBean.CommentListBean>(mContext, comment_list, commentLayoutId) {
            @Override
            public void fillView(ViewHodler viewHodler, ChatDynamicsBean.ListBean.CommentListBean locItem, int i) {
                if (isDetails) {
                    String nick = EmptyDeal.isEmpy(locItem.to_nickname) ? "" : (" 回复 " + locItem.to_nickname + ":");
                    viewHodler.setText(R.id.txtContent, String.format("%s %s", nick, locItem.content));
                    viewHodler.setText(R.id.txtTime, locItem.add_time);
                    viewHodler.setText(R.id.txtUserName, locItem.nickname);

                    GlideMediaLoader.load(mContext, viewHodler.getView(R.id.imgUserIcon), locItem.avatar);
                    viewHodler.setVisibility(R.id.bottomLine,i!=getCount()-1);
                } else {
                    String nick = EmptyDeal.isEmpy(locItem.to_nickname) ? locItem.nickname : (locItem.nickname + " 回复 " + locItem.to_nickname + "");
                    viewHodler.setText(R.id.txtTitle, String.format("%s: %s", nick, locItem.content));

                }
            }
        });

        moreCommentList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChatDynamicsBean.ListBean.CommentListBean commentListBean = comment_list.get(position);
                if (TextUtils.equals(commentListBean.uid, Config.getLoginInfo().getId())) {
                    return;
                }
                CommentDialog.newInstance().setId(item.id).setComment_id(commentListBean.id)
                        .setContentHintTxt("回复:" + commentListBean.nickname)
                        .show(((BaseActivity) mContext).getSupportFragmentManager(), "CommentDialog");
            }
        });
        helper.setOnClickListener(R.id.txtComment, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommentDialog.newInstance().setId(item.id)
                        .show(((BaseActivity) mContext).getSupportFragmentManager(), "CommentDialog");
            }
        });
        helper.addOnClickListener(R.id.txtLike);
        helper.setText(R.id.txtLike, containtLikes(item.like_list) ? "已赞" : "赞");
        helper.setText(R.id.txtCommentInfo, String.format("%s人评论", item.comments));
        final TextView txtCommentExpand = helper.getView(R.id.txtCommentExpand);
        boolean isExpand = expandSet.get(layoutPosition, false);
        txtCommentExpand.setText(isExpand ? "收起更多" : "展开更多");
        TextColorUtils.setCompoundDrawables(txtCommentExpand, 0, 0, isExpand ? R.drawable.ic_chat_comment_more_up : R.drawable.ic_chat_comment_more, 0);
        helper.setVisible(R.id.moreCommentList, isExpand);
        helper.setOnClickListener(R.id.txtCommentExpand, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isExpand = expandSet.get(layoutPosition, false);
                isExpand = !isExpand;
                expandSet.put(layoutPosition, isExpand);
                notifyDataSetChanged();

            }
        });
        dealVideo(helper, item);
        dealDelete(helper, item);
        if(!isFriend){
            helper.setVisible(R.id.layLike, false);
            helper.setVisible(R.id.layComments, false);
            helper.setVisible(R.id.viewHandleLine, false);
            helper.setVisible(R.id.txtDelete, false);
            helper.setVisible(R.id.txtLike, false);
            helper.setVisible(R.id.txtComment, false);
        }
    }

    private void dealVideo(BaseViewHolder helper, final ChatDynamicsBean.ListBean item) {
        if (EmptyDeal.isEmpy(item.attachment_list)) {
            helper.setVisible(R.id.layVideo, false);
        } else {
            final ChatDynamicsBean.ListBean.AttachmentListBean attachmentListBean = item.attachment_list.get(0);
            final String attachment = attachmentListBean.attachment;
            boolean isVideo = TestTool.checkSuffix(attachment, ".mp4", ".mov");
            ImageView imgCover = helper.getView(R.id.imgCover);
            if (isVideo) {
                createVideoThumbnail(imgCover, attachment, helper.getLayoutPosition() - getHeaderLayoutCount(), 720, 300);
                imgCover.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        showOperationMenu(item, attachmentListBean.id);
                        return false;
                    }
                });
                imgCover.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        SmallVideoDetailsActivity.start(mContext, attachment, true);
                    }
                });
            }
            helper.setVisible(R.id.layVideo, isVideo);
            helper.setVisible(R.id.imgPics, !isVideo);
        }
    }

    private void showOperationMenu(final ChatDynamicsBean.ListBean item, final String url) {
        final List<String> attrStrs = Arrays.asList("收藏");
        AlertDialog alertDialog = new AlertDialog.Builder(mContext).setAdapter(new ViewHolderAdapter<String>(mContext, attrStrs, R.layout.item_simple_list_text) {
            @Override
            public void fillView(ViewHodler viewHodler, String item, int i) {
                viewHodler.setText(R.id.text, item);
                viewHodler.setVisibility(R.id.limiteLine, false);
            }
        }, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                UIUtils.showLoadDialog(mContext, "收藏中..");
//                NAction nAction = url == null ? BaseQuestStart.NeighborhoodImCollectUpdatesContent(null, item.id) :
//                        BaseQuestStart.NeighborhoodImCollectUpdatesAttachment(null, item.id, url);
//                DataLoadDialogFragment.getInstance(((LBaseActivity) mContext).getSupportFragmentManager(), nAction, new DataLoadDialogFragment.onDataLoadeListener() {
//                    @Override
//                    public void onDataLoaded(BaseBean bean) {
//                        UIUtils.shortM(bean);
//                    }
//                });
            }
        }).create();
        alertDialog.show();
    }


    public void addExpand(int position) {
        expandSet.put(position, true);
    }

    /**
     * 处理删除事件
     *
     * @param helper
     * @param item
     */
    private void dealDelete(BaseViewHolder helper, final ChatDynamicsBean.ListBean item) {
        boolean isAuther = TextUtils.equals(item.uid, Config.getLoginInfo().getId());
        final int position = helper.getLayoutPosition() - getHeaderLayoutCount();
        helper.setVisible(R.id.txtDelete, isAuther);
        final FragmentManager supportFragmentManager = ((LBaseActivity) mContext).getSupportFragmentManager();
        helper.setOnClickListener(R.id.txtDelete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                MessageTipDialog.newInstance().setContentTxt("是否删除该动态?")
//                        .setOnSubmitAction(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                DataLoadDialogFragment.getInstance(supportFragmentManager, BaseQuestStart.NeighborhoodImUpdatesDelete(null, item.id), new DataLoadDialogFragment.onDataLoadeListener() {
//                                    @Override
//                                    public void onDataLoaded(BaseBean bean) {
//                                        EventBus.getDefault().post(DefaultEvent.createEvent(Const.EVENT_DELETE_DYNAMICS, item));
//                                        UIUtils.shortM(bean);
//                                        remove(position);
//                                    }
//                                });
//                            }
//                        }).show(supportFragmentManager, "MessageTipDialog");

            }
        });


    }

    //判断当前用户在不在里面
    public boolean containtLikes(List<ChatDynamicsBean.ListBean.LikeListBean> like_list) {
        if (like_list == null) return false;
        String uid = Config.getLoginInfo().getId();
        for (ChatDynamicsBean.ListBean.LikeListBean likeListBean : like_list) {
            if (TextUtils.equals(uid, likeListBean.uid))
                return true;
        }
        return false;
    }

    public void setIsFriend(boolean isfriend) {
        isFriend = isfriend;
        notifyDataSetChanged();
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