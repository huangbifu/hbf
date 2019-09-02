package com.cnsunrun.chat.mode;

import com.cnsunrun.common.util.selecthelper.Selectable;
import com.sunrun.sunrunframwork.view.sidebar.SortModel;

import java.io.Serializable;

import static com.cnsunrun.common.util.TestTool.vaildVal;

/**
 * 联系人信息
 * Created by WQ on 2017/11/9.
 */

public class LinkManInfo extends SortModel implements Selectable.SelectableEntity,Serializable{

    /**
     * id : 7
     * nickname : aichen
     * mobile : 13971383849
     * remark :
     * not_disturb : 0
     * is_top : 0
     * chat_image :
     * avatar : http://localhost/wisdom/Uploads/Avatar/Default/default.png?time=1512526375
     */

    public String id;
    public String nickname;
    public String mobile;
    public String remark;
    public String not_disturb;
    public String is_top;
    public String chat_image;
    public String avatar;
    public String adminId;
    public String id_num;
    public String uid;

    public LinkManInfo() {
    }

    public LinkManInfo(String nickname) {
        this.nickname=nickname;
    }

    public String getNickname() {
        return vaildVal(remark,nickname);
    }

    @Override
    public String toString() {
        return getNickname();
    }

    @Override
    public int getUniqueCode() {
        return String.valueOf(id).hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof LinkManInfo){
            return getUniqueCode()==((LinkManInfo) obj).getUniqueCode();
        }
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return getUniqueCode();
    }
}
