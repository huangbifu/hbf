package com.cnsunrun.chat.mode;

import java.util.List;

import static com.cnsunrun.common.util.TestTool.vaildVal;

/**
 * 用户信息
 * Created by WQ on 2017/12/6.
 */

public class MemberInfo {

    /**
     * id : 2
     * nickname : xudong
     * description : 一生只爱一个人
     * gender : 1
     * updates_image :
     * avatar : http://localhost/wisdom/Uploads/Avatar/000/00/00/02_avatar_big.jpg?time=1512524610
     * is_friend : 0
     * remark :
     * updates_list : [{"attachment":"http://localhost/wisdom/Uploads/Neighborhood/IM/Updates/2017-11-16/5a0cfd740b4fd.mp4","width":"0","height":"0"},{"attachment":"http://localhost/wisdom/Uploads/Neighborhood/IM/Updates/2017-11-16/5a0cfd740d35f.jpg","width":"1080","height":"1920"}]
     */

    public String id;
    public String nickname;
    public String description;
    public int gender;
    public String updates_image;
    public String avatar;
    public int is_friend;
    public String remark;
    public String id_num;

    public String getNickname(){
        return vaildVal(remark,nickname);
    }
    public boolean getIsFriend() {
        return is_friend==1;
    }

    /**
     * attachment : http://localhost/wisdom/Uploads/Neighborhood/IM/Updates/2017-11-16/5a0cfd740b4fd.mp4
     * width : 0
     * height : 0
     */

    public List<UpdatesListBean> updates_list;

    public static class UpdatesListBean {
        public String attachment;
        public String width;
        public String height;
    }
}
