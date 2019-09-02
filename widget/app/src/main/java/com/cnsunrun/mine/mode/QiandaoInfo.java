package com.cnsunrun.mine.mode;

import java.util.List;

/**
 * Created by weiquan on 2018/12/4.
 */
public class QiandaoInfo {

    /**
     * today_sign : 1
     * sgin_list : [{"date":"12.03","reward_money":"0.10","is_sign":1},{"date":"12.04","reward_money":"0.20","is_sign":0},{"date":"12.05","reward_money":"0.30","is_sign":1},{"date":"今日","reward_money":"0.40","is_sign":1},{"date":"12.07","reward_money":"0.50","is_sign":0},{"date":"12.08","reward_money":"0.70","is_sign":0},{"date":"12.09","reward_money":"1.00","is_sign":0}]
     * sign_num : 3
     * sign_reward_money : 0.00
     * sign_reward_record : [{"total_money":"0.10","add_time":"2018-12-06 19:56:05"},{"total_money":"0.10","add_time":"2018-12-05 19:55:19"},{"total_money":"0.10","add_time":"2018-12-03 19:50:54"}]
     */

    public int today_sign;
    public int sign_num;
    public String sign_reward_money;
    public List<SginListBean> sgin_list;
    public List<QiandaoRecord> sign_reward_record;

    public static class SginListBean {
        /**
         * date : 12.03
         * reward_money : 0.10
         * is_sign : 1
         */

        public String date;
        public String reward_money;
        public int is_sign;
    }


}
