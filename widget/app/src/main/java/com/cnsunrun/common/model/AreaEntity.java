package com.cnsunrun.common.model;

import java.util.List;

/**
 * 中国省市区模型类
 */
public class AreaEntity {

    /**
     * id : 1
     * pid : 0
     * title : 北京
     * _child : [{"id":"2","pid":"1","title":"北京市","_child":[{"id":"3","pid":"2","title":"东城区"},{"id":"4","pid":"2","title":"西城区"},{"id":"5","pid":"2","title":"崇文区"},{"id":"6","pid":"2","title":"宣武区"},{"id":"7","pid":"2","title":"朝阳区"},{"id":"8","pid":"2","title":"丰台区"},{"id":"9","pid":"2","title":"石景山区"},{"id":"10","pid":"2","title":"海淀区"},{"id":"11","pid":"2","title":"门头沟区"},{"id":"12","pid":"2","title":"房山区"},{"id":"13","pid":"2","title":"通州区"},{"id":"14","pid":"2","title":"顺义区"},{"id":"15","pid":"2","title":"昌平区"},{"id":"16","pid":"2","title":"大兴区"},{"id":"17","pid":"2","title":"怀柔区"},{"id":"18","pid":"2","title":"平谷区"},{"id":"19","pid":"2","title":"密云县"},{"id":"20","pid":"2","title":"延庆县"}]}]
     */

    public AreaEntity() {
    }

    public AreaEntity(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getTitle();
    }

    private String id;
    private String pid;
    private String title;
    private List<ChildBeanX> _child;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<ChildBeanX> get_child() {
        return _child;
    }

    public void set_child(List<ChildBeanX> _child) {
        this._child = _child;
    }

    public static class ChildBeanX {
        public ChildBeanX(String id) {
            this.id = id;
        }

        /**
         * id : 2
         * pid : 1
         * title : 北京市
         * _child : [{"id":"3","pid":"2","title":"东城区"},{"id":"4","pid":"2","title":"西城区"},{"id":"5","pid":"2","title":"崇文区"},{"id":"6","pid":"2","title":"宣武区"},{"id":"7","pid":"2","title":"朝阳区"},{"id":"8","pid":"2","title":"丰台区"},{"id":"9","pid":"2","title":"石景山区"},{"id":"10","pid":"2","title":"海淀区"},{"id":"11","pid":"2","title":"门头沟区"},{"id":"12","pid":"2","title":"房山区"},{"id":"13","pid":"2","title":"通州区"},{"id":"14","pid":"2","title":"顺义区"},{"id":"15","pid":"2","title":"昌平区"},{"id":"16","pid":"2","title":"大兴区"},{"id":"17","pid":"2","title":"怀柔区"},{"id":"18","pid":"2","title":"平谷区"},{"id":"19","pid":"2","title":"密云县"},{"id":"20","pid":"2","title":"延庆县"}]
         */



        private String id;
        private String pid;
        private String title;
        private List<ChildBean> _child;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<ChildBean> get_child() {
            return _child;
        }

        public void set_child(List<ChildBean> _child) {
            this._child = _child;
        }

        @Override
        public String toString() {
            return getTitle();
        }

        public static class ChildBean {
            public ChildBean(String id) {
                this.id = id;
            }

            /**
             * id : 3
             * pid : 2
             * title : 东城区
             */



            private String id;
            private String pid;
            private String title;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getPid() {
                return pid;
            }

            public void setPid(String pid) {
                this.pid = pid;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }

            @Override
            public String toString() {
                return getTitle();
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof AreaEntity) {
            AreaEntity ch = (AreaEntity) o;
            return String.valueOf(ch.id).equals(id);
        }
        return super.equals(o);
    }

    @Override
    public int hashCode() {
        return String.valueOf(id).hashCode();
    }

}
