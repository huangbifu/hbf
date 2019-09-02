package com.cnsunrun.common.event;

/**
 * Created by WQ on 2017/12/8.
 */

public class DefaultEvent {
    String action;
    Object extra;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public <T> T getExtra() {
        return (T) extra;
    }

    public void setExtra(Object extra) {
        this.extra = extra;
    }
    public static DefaultEvent createEvent(String action,Object extra){
        DefaultEvent defaultEvent = new DefaultEvent();
        defaultEvent.action=action;
        defaultEvent.extra=extra;
        return defaultEvent;
    }

    public boolean match(String action){
        return this.action.equals(action);
    }
}
