package com.cnsunrun.mine.mode;

import java.util.List;

/**
 * Created by wangchao on 2018-09-21.
 */
public class RechargeInfo {

    public List<MoneyListBean> money_list;
    public List<PayListBean> pay_list;
    public List<PayChannelBean> pay_channel;

    public static class MoneyListBean {
        /**
         * id : 1
         * money : 10.1
         */

        public int id;
        public String money;


    }

    public static class PayListBean {
        /**
         * title : 支付宝转账(最低100起充)
         * type : 1
         */

        public String title;
        public int type;

    }

    public static class PayChannelBean {
        /**
         *   "id": "2",
            "title": "通道2"
         */
        public String title;
        public String id;
        public String is_show;
        public List<MoneyListBean> money_list;

    }
}
