package com.cnsunrun.chat.mode;

import com.cnsunrun.common.util.TestTool;

import java.util.List;

/**
 * 群组信息
 * Created by WQ on 2017/12/7.
 */

public class GroupInfoBean {

    /**
     * id : 1
     * title : 世界和平
     * notice : null
     * notice_time : 0000-00-00 00:00:00
     * remark :
     * not_disturb : 0
     * is_top : 0
     * member_list : [{"id":"2","nickname":"xudong","remark":"","avatar":"http://localhost/wisdom/Uploads/Avatar/000/00/00/02_avatar_big.jpg?time=1512611214"},{"id":"3","nickname":"xufei","remark":"","avatar":"http://localhost/wisdom/Uploads/Avatar/Default/default.png?time=1512611214"},{"id":"4","nickname":"zhangyijuan","remark":"","avatar":"http://localhost/wisdom/Uploads/Avatar/Default/default.png?time=1512611214"},{"id":"5","nickname":"chenmeng","remark":"","avatar":"http://localhost/wisdom/Uploads/Avatar/Default/default.png?time=1512611214"},{"id":"6","nickname":"zhangjinliang","remark":"","avatar":"http://localhost/wisdom/Uploads/Avatar/Default/default.png?time=1512611214"},{"id":"7","nickname":"aichen","remark":"","avatar":"http://localhost/wisdom/Uploads/Avatar/Default/default.png?time=1512611214"}]
     */

    public String id;
    public String title;
    public String notice;
    public String notice_time;
    public String remark;
    public String not_disturb;
    public String is_top;
    public String _uid;
    public String activity_id;
    public String is_disable;//是否禁言,0否,1是
    public String type;//0普通群,1红包群
    public String money_range;
    public int redpack_num;// 默认红包个数
    public int max_thunder_num;// 雷数
    public String more_thunder_title;
    public  List<MoreThunderRatioBean> more_thunder_ratio;
    public static class MoreThunderRatioBean{
        // "id": "1",
        //                "num": "1",
        //                "ratio": "1.11"
        public String id;
        public int num;
        public String ratio;
    }

    /**
     * 是否禁言
     * @return
     */
    public boolean isDisable(){
        return "1".equals(is_disable);
    }
    public boolean isRedpackGroup(){
        return "1".equals(type);
    }
    /**
     * id : 2
     * nickname : xudong
     * remark :
     * avatar : http://localhost/wisdom/Uploads/Avatar/000/00/00/02_avatar_big.jpg?time=1512611214
     */

    public List<LinkManInfo> member_list;

    public String getTitle() {
        return TestTool.vaildVal(title);
    }
}
