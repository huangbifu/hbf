package com.cnsunrun.common.model;

/**
 * Created by WQ on 2017/6/2.
 */

public class AppConfigInfo {

    /**
     * hysjymti : 会员升级页面文字提示&nbsp;
     * tgyhjs : 推广用户页面文字介绍
     * dhsm : 兑换说明&nbsp;
     */

    private String hysjymti="";
    private String tgyhjs="";
    private String dhsm="";

    public String getHysjymti() {
        return hysjymti;
    }

    public void setHysjymti(String hysjymti) {
        this.hysjymti = hysjymti;
    }

    public String getTgyhjs() {
        return tgyhjs;
    }

    public void setTgyhjs(String tgyhjs) {
        this.tgyhjs = tgyhjs;
    }

    public String getDhsm() {
        return dhsm;
    }

    public void setDhsm(String dhsm) {
        this.dhsm = dhsm;
    }
}
