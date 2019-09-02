package com.cnsunrun.mine.mode;

import java.util.List;

/**
 * Created by wangchao on 2018-09-26.
 */
public class WalletBean {
    // "balance": "0.00",
    //        "deal_password": "",
    //        "frozen" : "0.00",
    //        "is_set_deal_password": 0,
    //        "list": [
    //            {
    //                "id": "1598",
    //                "order_no": "2016110456539948",
    //                "from_member_id": "1",
    //                "to_member_id": "713",
    //                "money": "+0.01元",
    //                "add_time": "2016-11-04 17:17:12",
    //                "status": "10",
    //                "pay_title": "待付款"
    //            }
    //        ]
    public String balance;
    public String deal_password;
    public String frozen;
    public String is_set_deal_password;
    public List<WalletInfoBean> list;

    public class WalletInfoBean {
        //                "id": "1598",
        //                "order_no": "2016110456539948",
        //                "from_member_id": "1",
        //                "to_member_id": "713",
        //                "money": "+0.01元",
        //                "add_time": "2016-11-04 17:17:12",
        //                "status": "10",
        //                "pay_title": "待付款"
        public String id;
        public String order_no;
        public String from_member_id;
        public String to_member_id;
        public String money;
        public String add_time;
        public String status;
        public String pay_title;
    }
}
