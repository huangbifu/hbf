package com.cnsunrun.mine.mode;

import java.util.List;

/**
 * Created by weiquan on 2018/12/4.
 */
public class QiandaoRecord {

    /**
     * total_money : 0.10
     * add_time : 2018-12-07 20:47:26
     */

    public String total_money;
    public String add_time;

    /**
     * count : 2
     * pages : 1
     * page :
     * list : [{"total_money":"0.10","add_time":"2018-12-07 20:47:26"},{"total_money":"0.10","add_time":"2018-12-06 20:47:16"}]
     */
    public String count;
    public int pages;
    public String page;
    public List<QiandaoRecord> list;


    public static List<QiandaoRecord> getList(QiandaoRecord qiandaoRecord) {
        return qiandaoRecord==null?null:qiandaoRecord.list;
    }
}
