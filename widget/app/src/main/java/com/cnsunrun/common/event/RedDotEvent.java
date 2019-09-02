package com.cnsunrun.common.event;

import android.os.Bundle;

/**
 * 红点标示,仅作为指示为刷新红点显示
 * Created by WQ on 2017/12/19.
 */

public class RedDotEvent {
    static RedDotEvent _INSTACE=new RedDotEvent();
    public static RedDotEvent newInstance() {
        return _INSTACE;
    }
}
