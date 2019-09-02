package com.cnsunrun.common.event;

/**
 * Created by cnsunrun on 2017/7/14.
 * <p>
 * EventBus模型类
 */

public class RefreshIMTokenEvent {
    static RefreshIMTokenEvent _INSTACE=new RefreshIMTokenEvent();
    public static RefreshIMTokenEvent newInstance() {
        return _INSTACE;
    }
}
