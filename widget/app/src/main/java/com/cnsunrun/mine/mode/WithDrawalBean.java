package com.cnsunrun.mine.mode;

/**
 * Created by wangchao on 2018-09-25.
 */
public class WithDrawalBean {
    //"balance": "991.92",
    //        "enbale_balance": 991.92,
    //        "info": "提现说明"
    public String balance;
    public String commission_balance;
    public String enbale_balance;
    public String info;
    public String withdraw_explain;
    public String max_money;
    public String min_money;
    public int status;
    public BankCardBean bank_card;
    public  ZhifubaoBean ali_account;

    public class BankCardBean {
        // "account": "6212264100011335373",
        //  "bank_adress": "工商银行",
        // "username": "陈英",
        //  "id": "40"
        public String account;
        public String bank_adress;
        public String username;
        public String id;
        public String type;
    }
    public class ZhifubaoBean{
        // "account": "188***1813",
        //            "bank_adress": "支付宝",
        //            "id": "68",
        //            "username": "王超"
        public String account;
        public String bank_adress;
        public String username;
        public String id;

    }
}
