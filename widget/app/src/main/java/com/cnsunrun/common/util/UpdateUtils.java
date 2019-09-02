package com.cnsunrun.common.util;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

import com.cnsunrun.common.model.ApkVersion;
import com.cnsunrun.common.service.DownLoadService;

import static com.cnsunrun.common.service.DownLoadService.DOWNLOAD_URL;


/**
 * 作者：ZhouBin  2017/4/6 10:43
 * 邮箱：1021237228@qq.com
 * 作用：版本更新
 */

public class UpdateUtils {


    /**
     * 检查是否需要更新
     *
     * @param context
     * @param callback
     */
    public static void checkUpdate(final Context context, ApkVersion response, final UpdateCallback callback) {
        if (response.version!=null&&String.valueOf(response.version).compareTo(getVersionName(context)) > 0) {
            callback.result(true);
        } else {
            callback.result(false);

        }
    }

    /***
     * 执行更新
     * @param context
     */
    public static void update(Activity context, ApkVersion response) {
        showUpdateDialog(context, response);
    }

    private static void showUpdateDialog(final Activity context, final ApkVersion response) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        AlertDialog checkVersion = builder.setTitle("发现新版本:" + response.version)
                .setMessage(response.content)
                .setNegativeButton(null, null)
                .setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 下载APK并替换安装
                        if (Environment.getExternalStorageState().equals(
                                Environment.MEDIA_MOUNTED)) {
                            Intent service = new Intent(context, DownLoadService.class);
                            service.putExtra(DOWNLOAD_URL, response.path);
                            context.startService(service);
                        } else {
                            Toast.makeText(context, "未检测到SD卡，请插入SD卡再运行",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }

                }).create();
        checkVersion.setCanceledOnTouchOutside(false);
        checkVersion.show();
    }



    /*
     * 获得应用版本名称
     */
    public static String getVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            // 获得清单文件的信息
            PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 检查更新结果回调
     */
    public interface UpdateCallback {
        /**
         * 更新结果
         *
         * @param update 是否需要更新
         */
        void result(boolean update);

        /***
         * 取消更新
         */
        void cancel();
    }

}
