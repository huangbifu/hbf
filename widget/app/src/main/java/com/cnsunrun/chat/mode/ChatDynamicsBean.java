package com.cnsunrun.chat.mode;

import java.util.List;

/**
 * 动态
 * Created by WQ on 2017/12/8.
 */

public class ChatDynamicsBean {

    /**
     * description : 一生只爱一个人
     * updates_image : http://localhost/wisdom/Api/Neighborhood/IM/updates_home
     * nickname : xudong
     */

    public InfoBean info;
    /**
     * id : 1
     * uid : 2
     * content : 人生如戏，全靠演技
     * comments : 0
     * like_list : []
     * likes : 0
     * avatar : http://localhost/wisdom/Uploads/Avatar/000/00/00/02_avatar_big.jpg?time=1512633740
     * nickname : xudong
     * attachment_list : [{"id":"1","updates_id":"1","uid":"2","title":"","attachment":"http://localhost/wisdom/Uploads/Neighborhood/IM/Updates/2017-11-16/5a0cfd740b4fd.mp4","filesize":"1560064","width":"0","height":"0","add_time":"2017-11-16 10:52:36"},{"id":"2","updates_id":"1","uid":"2","title":"","attachment":"http://localhost/wisdom/Uploads/Neighborhood/IM/Updates/2017-11-16/5a0cfd740d35f.jpg","filesize":"823786","width":"1080","height":"1920","add_time":"2017-11-16 10:52:36"}]
     * comment_list : []
     */

    public List<ListBean> list;

    public static class InfoBean {
        public String description;
        public String updates_image;
        public String nickname;
        public String avatar;
        public String is_friend="1";

        public boolean isFriends() {
            return "1".equals(is_friend);
        }
    }

    public static class ListBean {
        public String id;
        public String uid;
        public String content;
        public String comments;
        public String likes;
        public String avatar;
        public String nickname;

        /**
         * id : 1
         * updates_id : 1
         * uid : 2
         * title :
         * attachment : http://localhost/wisdom/Uploads/Neighborhood/IM/Updates/2017-11-16/5a0cfd740b4fd.mp4
         * filesize : 1560064
         * width : 0
         * height : 0
         * add_time : 2017-11-16 10:52:36
         */

        public List<AttachmentListBean> attachment_list;

        /**
         * id : 1
         * updates_id : 2
         * uid : 2
         * nickname : xudong
         * to_uid : 0
         * to_nickname :
         * content : 什么鬼？
         * add_time : 2017-12-07 15:38:39
         */

        public List<CommentListBean> comment_list;
        /**
         * like_list : [{"uid":"8","nickname":"魏泉"}]
         * add_time : 2017-12-08 15:57:47
         */

        public String add_time;
        /**
         * uid : 8
         * nickname : 魏泉
         */

        public List<LikeListBean> like_list;

        public static class AttachmentListBean {
            public String id;
            public String updates_id;
            public String uid;
            public String title;
            public String attachment;
            public String filesize;
            public String width;
            public String height;
            public String add_time;

            @Override
            public String toString() {
                return String.valueOf(attachment);
            }
        }

        public static class CommentListBean {
            public String id;
            public String updates_id;
            public String uid;
            public String nickname;
            public String to_uid;
            public String to_nickname;
            public String content;
            public String add_time;
            public String avatar;
        }

        public static class LikeListBean {
            public String uid;
            public String nickname;

            @Override
            public String toString() {
                return String.valueOf(nickname);
            }
        }
    }
}
