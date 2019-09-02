package com.cnsunrun.common.widget.popupwindow;


public class DropFilterInfo {
    private String title;
    private String id;
    private boolean isChecked;

    public DropFilterInfo(String title, String id, boolean isChecked) {
        this.title = title;
        this.id = id;
        this.isChecked = isChecked;
    }


    public DropFilterInfo(String title, String id) {
        this(title, id, false);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
