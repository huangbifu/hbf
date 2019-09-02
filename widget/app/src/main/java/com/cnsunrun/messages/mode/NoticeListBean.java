package com.cnsunrun.messages.mode;

import java.util.List;

/**
 * Created by WQ on 2017/11/24.
 */

public class NoticeListBean {

    /**
     * id : 2
     * community_id : 1
     * title : 停电公告
     * is_top : 0
     * add_time : 2017-11-07 15:56:52
     */

    public String id;
    public String community_id;
    public String title;
    public String is_top;
    public String add_time;
    public String content;
    public String h5;
    public String is_read;

    @Override
    public String toString() {
        return title;
    }

    public static class NoticeListPage{
        public List<NoticeListBean>list;
    }
}
