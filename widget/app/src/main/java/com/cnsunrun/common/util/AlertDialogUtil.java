package com.cnsunrun.common.util;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.cnsunrun.R;
import com.cnsunrun.common.widget.gridpasswordview.GridPasswordView;
import com.cnsunrun.mine.dialogs.SelectPhotoDialog;
import com.sunrun.sunrunframwork.uiutils.UIUtils;


/**
 * @version V1.0
 * @功能描述: Dialog对话框总工具类
 */
public final class AlertDialogUtil {
    /**
     * 单例模式
     */
    private static AlertDialogUtil AlertDialogUtil;

    /**
     * 显示正在加载的对话框
     */
    private Dialog LoadDialog;

    private AlertDialogUtil() {
    }

    public static AlertDialogUtil getInstences() {
        if (AlertDialogUtil == null) {
            AlertDialogUtil = new AlertDialogUtil();
        }
        return AlertDialogUtil;
    }



    /**
     * 确认对话框
     * @param context
     * @param content
     * @return
     */
    public static Dialog showConfimDialog(final Context context, CharSequence content, int iconId, final View.OnClickListener onConfimListener,final View.OnClickListener onCancelListener) {
        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
        View dialogView = View.inflate(context, R.layout.dialog_delete_confim, null);
        dialog.setContentView(dialogView);
        TextView contentView = (TextView) dialogView.findViewById(R.id.tv_content);
        ImageView iv_icon = (ImageView) dialogView.findViewById(R.id.iv_icon);
        if (iconId != 0 && iconId != -1) {
            iv_icon.setImageResource(iconId);
        }
        if (content != null) {
            contentView.setText(content);
        }
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(onCancelListener!=null){
                    onCancelListener.onClick(v);
                }
            }
        });
        dialogView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (onConfimListener != null) {
                    onConfimListener.onClick(v);
                }
            }
        });
        dialog.show();
        return dialog;
    }


    public static void setDialogBtnText(Dialog dialog, String submitStr, String cancelStr) {
        TextView submit = (TextView) dialog.findViewById(R.id.submit);
        TextView cancel = (TextView) dialog.findViewById(R.id.cancel);
        submit.setText(submitStr);
        cancel.setText(cancelStr);
    }


    /**
     * 确认对话框
     * @param context
     * @return
     */
    public static Dialog showAccountDialog(final Context context,final View.OnClickListener onConfimListener) {
        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
        View dialogView = View.inflate(context, R.layout.dialog_input_account, null);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        dialog.setContentView(dialogView);
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (onConfimListener != null) {
                    onConfimListener.onClick(v);
                }
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }
    /**
     * 密码对话框
     * @param context
     * @return
     */
    public static Dialog showPwdDialog(final Context context, GridPasswordView.OnPasswordChangedListener listener) {
        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
        View dialogView = View.inflate(context, R.layout.dialog_input_password, null);
        dialog.setContentView(dialogView);
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE|WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        GridPasswordView pwd_layout= (GridPasswordView) dialogView.findViewById(R.id.pwd_layout);
        pwd_layout.setOnPasswordChangedListener(listener);
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }

    /**
     * 密码对话框
     * @param context
     * @return
     */
    public static Dialog showCashDialog(final Context context,String cashSize) {
        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
        View dialogView = View.inflate(context, R.layout.dialog_cashandabout, null);
        dialog.setContentView(dialogView);
        TextView cash_memry= (TextView) dialogView.findViewById(R.id.cash_memry);
        cash_memry.setText("已经清除"+cashSize+"缓存");
        TextView mCashSubmit= (TextView) dialogView.findViewById(R.id.cash_submit);
        mCashSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }

    /**
     * 关于我们对话框
     * @param context
     * @return
     */
    public static Dialog showAboutDialog(final Context context,String about) {
        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
        View dialogView = View.inflate(context, R.layout.dialog_about, null);
        dialog.setContentView(dialogView);
        TextView cash_memry= (TextView) dialogView.findViewById(R.id.cash_about);
        cash_memry.setText(about);
        TextView mCashSubmit= (TextView) dialogView.findViewById(R.id.about_submit);
        mCashSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;
    }





//    /**
//     * 显示我要提问输入打赏金额对话框
//     *
//     * @param context
//     * @param onClickListener
//     * @return
//     */
//    public static Dialog showGiveRewardDialog(Context context, String averageMoney, final View.OnClickListener onClickListener) {
//        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
//        View dialogView = View.inflate(context, R.layout.dialog_give_reward, null);
//        dialog.setContentView(dialogView);
//        EditText editTextMoney = (EditText) dialogView.findViewById(R.id.edit_money);
//        TextView tvAverageMoney = (TextView) dialog.findViewById(R.id.tv_average_money);
//        if (averageMoney != null) {
//            tvAverageMoney.setText(averageMoney);
//        }
//        dialogView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//            }
//        });
//        dialogView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dialog.dismiss();
//                if (onClickListener != null) {
//                    onClickListener.onClick(v);
//                }
//            }
//        });
//        dialog.show();
//        return dialog;
//    }


    /**
     * 显示收费标准
     *
     * @param context
     * @param money
     * @return
     */
    public static Dialog showShowFeiDialog(Context context, String money, final ShouFeiListener shouFeiListener) {
        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
        View dialogView = View.inflate(context, R.layout.dialog_shoufei, null);
        dialog.setContentView(dialogView);
        final EditText editTextMoney = (EditText) dialogView.findViewById(R.id.edit_money);
        if (TextUtils.isEmpty(money)) {
            editTextMoney.setText(money);
        }
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String money = editTextMoney.getText().toString().trim();
                if (TextUtils.isEmpty(money)) {
                    UIUtils.shortM("请设置收费标准");
                    return;
                }
                if (Float.valueOf(money) < 1.0f) {
                    UIUtils.shortM("咨询费用最少设置为1元");
                    editTextMoney.setText("");
                    return;
                } else {
                    dialog.dismiss();
                    if (shouFeiListener != null) {
                        shouFeiListener.showFeiyongMsg(money);
                    }
                }
            }
        });
        dialog.show();
        return dialog;
    }


    //收费标准的接口
    public interface ShouFeiListener {
        void showFeiyongMsg(String money);
    }

    public  ShouFeiListener shouFeiListener;

    public void setShouFeiListener(ShouFeiListener shouFeiListener) {
        this.shouFeiListener = shouFeiListener;
    }


    /**
     * 显示保存用户资料的对话框
     *
     * @param context
     * @return
     */
    public static Dialog showSaveDialog(Context context) {
        View rootView = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        TextView msgText = (TextView) rootView.findViewById(R.id.msg);
        msgText.setText("保存中...");
        Dialog LoadDialog = new Dialog(context, R.style.CustomDialog);
        LoadDialog.setCanceledOnTouchOutside(false);
        LoadDialog.setContentView(rootView);
        LoadDialog.show();
        return LoadDialog;
    }

    /**
     * 显示保存用户资料的对话框
     *
     * @param context
     * @return
     */
    public static Dialog shopConsultDialog(Context context, final View.OnClickListener onClickListener) {
        final Dialog consultDialog = new Dialog(context, R.style.CustomDialog);
        View rootView = LayoutInflater.from(context).inflate(R.layout.stop_consult_dialog, null);
        Button mConsultCancel = (Button) rootView.findViewById(R.id.stop_btn_consult_cancel);
        Button mConsultConfirm = (Button) rootView.findViewById(R.id.stop_btn_consult_confirm);
        mConsultCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                consultDialog.dismiss();
            }
        });

        mConsultConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onClickListener != null) {
                    onClickListener.onClick(v);
                }
            }
        });

        consultDialog.setCanceledOnTouchOutside(false);
        consultDialog.setContentView(rootView);
        consultDialog.show();
        return consultDialog;
    }


    /**
     * 选项控件(相册/录像)
     *
     * @param context                   上下文
     * @param textOne                   选项一
     * @param textTwo                   选项二
     * @param onSelectItemClickListener 监听
     * @return
     */
    public static SelectPhotoDialog selectPhotoDialog(Context context, String textOne, String textTwo, final SelectPhotoDialog.OnSelectItemClickListener onSelectItemClickListener) {
        final SelectPhotoDialog selectPhotoDialog = new SelectPhotoDialog(context);
        if (!TextUtils.isEmpty(textOne)) {
            selectPhotoDialog.setPhotoAlbumsText(textOne);
        }
        if (!TextUtils.isEmpty(textTwo)) {
            selectPhotoDialog.setSelectTakeText(textTwo);
        }
        selectPhotoDialog.setOnSelectItemClickListener(new SelectPhotoDialog.OnSelectItemClickListener() {
            @Override
            public void selectItemOne(View view) {
                if (onSelectItemClickListener != null) {
                    onSelectItemClickListener.selectItemOne(view);
                }

            }

            @Override
            public void selectItemTwo(View view) {
                if (onSelectItemClickListener != null) {
                    onSelectItemClickListener.selectItemTwo(view);
                }
            }
        });
        return selectPhotoDialog;

    }

    /**
     * 退出确认框
     * @param context
     * @param title
     * @param content
     * @param submit
     * @param cancel
     * @return
     */
    public static Dialog showCommonConfirm(Context context,String title,String content, final View.OnClickListener submit, final View.OnClickListener cancel) {
        final Dialog dialog = new Dialog(context, R.style.NoTitleDialog);
        View dialogView = View.inflate(context, R.layout.layout_dialog_common_confirm, null);
        dialog.setContentView(dialogView);
        TextView tvTitle = (TextView) dialogView.findViewById(R.id.tv_title);
        TextView tvContent = (TextView) dialogView.findViewById(R.id.tv_content);
        TextView tvSubmit = (TextView) dialogView.findViewById(R.id.submit);
        tvTitle.setText(title);
        tvContent.setText(content);
        if(content.equals("是否退出登录？")){
            tvSubmit.setText("退出");
        }
        if(content.equals("未设置交易密码")){
            tvSubmit.setText("去设置");
        }
        dialogView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialogView.findViewById(R.id.submit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (submit != null) {
                    submit.onClick(v);
                }
                dialog.cancel();
            }
        });
        dialogView.findViewById(R.id.cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cancel != null) {
                    cancel.onClick(v);
                }
                dialog.cancel();
            }
        });
        dialog.show();
        return dialog;
    }

}
