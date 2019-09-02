package com.cnsunrun.common.model;

/**
 * Created by cnsunrun on 2017/5/27.
 */

public class SelectTypeInfo {
    private String id;
    private String title;
    private boolean isCheck;

    public SelectTypeInfo() {
    }

    public SelectTypeInfo(String id, String title, boolean isCheck) {
        this.id = id;
        this.title = title;
        this.isCheck = isCheck;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isCheck() {
        return isCheck;
    }

    public void setCheck(boolean check) {
        isCheck = check;
    }
}
