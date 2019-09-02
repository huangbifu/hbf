package com.cnsunrun.chat.mode;

import java.util.List;

/**
 * Created by weiquan on 2018/9/26.
 */
public class RedPackInfoBean {

    /**
     * title : 5-3
     * total_money : 5.00
     * num : 7
     * nickname : chen
     * member_id : 713
     * grab_money : 0.81
     * list : [{"money":"1.05","member_id":"713","is_thunder":"0","is_optimum":"1","id":"713","nickname":"chen","update_time":""}]
     */

    public String title;
    public String total_money;
    public String num;
    public String nickname;
    public int member_id;
    public String grab_money;
    public String avatar;
    public String is_thunder;

    public List<ListBean> list;

    public boolean isThunder(){
        return "1".equals(is_thunder);
    }
    public static class ListBean {
        /**
         * money : 1.05
         * member_id : 713
         * is_thunder : 0
         * is_optimum : 1
         * id : 713
         * nickname : chen
         * update_time :
         */

        public String money;
        public String member_id;
        public String is_thunder;
        public String is_optimum;
        public String id;
        public String nickname;
        public String update_time;
        public String avatar;
    }
}
