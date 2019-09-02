package com.cnsunrun.login.bean;

import android.app.Activity;
import android.widget.TextView;

import com.sunrun.sunrunframwork.utils.AHandler;

/**
 * 验证码按钮状态管理
 * Created by cnsunrun on 2018-03-15.
 */

public class CaptchaStateMode {
    private Activity activity;
    private TextView codeTextView;
    private String normalTxt = "获取验证码", countDownTxt = "%ds";
    private int maxTime = 60;
    private AHandler.Task task;

    public CaptchaStateMode(Activity activity, TextView codeTextView) {
        this.activity = activity;
        this.codeTextView = codeTextView;
    }

    /**
     * 设置默认显示的文字 eg:  获取验证码
     *
     * @param normalTxt
     * @return
     */
    public CaptchaStateMode setNormalTxt(String normalTxt) {
        this.normalTxt = normalTxt;
        return this;
    }

    /**
     * 设置倒计时中显示的文字 eg: 重新发送%s秒
     *
     * @param countDownTxt 倒计时文字,需要包含一个%d占位符
     * @return
     */
    public CaptchaStateMode setCountDownTxt(String countDownTxt) {
        this.countDownTxt = countDownTxt;
        return this;
    }

    /**
     * 设置最大时间,默认60s
     *
     * @param time 最大时间
     * @return
     */
    public CaptchaStateMode setMaxTime(int time) {
        this.maxTime = time;
        return this;
    }

    /**
     * 开始倒计时
     *
     * @return 操作结果, 成功/失败
     */
    public boolean beginCountDown() {
        if (task == null) {
            codeTextView.setEnabled(false);
            runCountDown();
            return true;
        }
        return false;
    }

    /**
     * 停止倒计时
     */
    public void stopCountDown() {
        codeTextView.setEnabled(true);
        codeTextView.setText(normalTxt);
        AHandler.cancel(task);
    }


    /**
     * 开启倒计时,内部实现
     */
    private void runCountDown() {
        task = new AHandler.Task() {
            int time = maxTime;

            @Override
            public void update() {
                if (isDestory()) {
                    cancel();
                    return;
                }
                if (time <= 0) {
                    task.cancel();
                } else {
                    codeTextView.setEnabled(false);
                    codeTextView.setText(String.format(countDownTxt, time--));
                }
            }

            @Override
            public boolean cancel() {
                boolean flag = super.cancel();
                boolean isDestory = codeTextView == null || activity == null || activity.isFinishing() || activity.isDestroyed();
                if (isDestory) return true;
                codeTextView.setEnabled(true);
                codeTextView.setText(normalTxt);
                time = maxTime;
                task = null;
                return flag;
            }

        };
        AHandler.runTask(task, 0, 1000);
    }

    /**
     * 是否已经消亡
     *
     * @return
     */
    private boolean isDestory() {
        boolean isDestory = codeTextView == null || activity == null || activity.isFinishing() || activity.isDestroyed();
        if (isDestory) {
            codeTextView = null;
            activity = null;
            AHandler.cancel(task);
        }
        return isDestory;
    }
}

