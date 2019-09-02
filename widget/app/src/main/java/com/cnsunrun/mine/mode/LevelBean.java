package com.cnsunrun.mine.mode;

import java.util.List;

/**
 * Created by wangchao on 2018-09-26.
 */
public class LevelBean {
    //  "count": "1",
    //        "pages": 1,
    //        "page": "    ",
    //        "list": [
    //            {
    //               "member_id": "713",
    //                "member_pid": "2",
    //                "distance": "1",
    //                "add_time": "2018-09-14 11:46:42",
    //                "nickname": "11111",
    //                "avatar"
    //            }
    //        ]
    public String count;
    public int pages;
    public String page;
    public List<Info> list;

    public static class Info {
        public String member_id;
        public String member_pid;
        public int distance;
        public String nickname;
        public String add_time;
        public String avatar;
        public String num;
    }
}
