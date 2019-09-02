package com.cnsunrun.mine.mode;

import java.util.List;

/**
 * Created by wangchao on 2018-09-25.
 */
public class CommissionBean {
    //"count": "1",
    //        "pages": 1,
    //        "page": "    ",
    //        "list": [
    //            {
    //                "total_money": "0.36",   返佣金额
    //                "level": "1",  等级
    //                "add_time": "2018-09-13 14:01:25",  时间
    //                "from_nickname": "即时通"  昵称
    //            }
    //        ],
    //        "today_total_money": "0.36",   今日返佣总额
    //        "total_money": "0.00"    返佣总额
    public String count;
    public int pages;
    public String page;
    public List<InfoBean> list;
    public String today_total_money;
    public String total_money;

    public class InfoBean {
        //                "total_money": "0.36",   返佣金额
        //                "level": "1",  等级
        //                "add_time": "2018-09-13 14:01:25",  时间
        //                "from_nickname": "即时通"  昵称
        public String total_money;
        public String level;
        public String add_time;
        public String from_nickname;
    }
}
