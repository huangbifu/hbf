package com.cnsunrun.chat.mode;

import java.util.List;

/**
 * Created by weiquan on 2018/9/26.
 */
public class RedPackRecord {

    /**
     * count : 12
     * pages : 2
     * list : [{"id":"25","money":"1.32","is_thunder":"0","is_optimum":"0","from_nickname":"chen","from_member_id":"713"}]
     * total_money : 12.82
     * total_num : 12
     * member_id : 713
     * nickname : chen
     */

    public String count;
    public int pages;
    public String total_money;
    public String total_num;
    public String member_id;
    public String nickname;
    public String avatar;
    public List<ListBean> list;

    public static class ListBean {
        /**
         * id : 25
         * money : 1.32
         * is_thunder : 0
         * is_optimum : 0
         * from_nickname : chen
         * from_member_id : 713
         */

        public String id;
        public String money;
        public String is_thunder;
        public String is_optimum;
        public String from_nickname;
        public String from_member_id;
        public String update_time;

    }
}
