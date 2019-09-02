package com.cnsunrun.common.intent;

/**
 * 数据存储器
 * Created by WQ on 2017/11/6.
 */
public interface DataStorage {
    void put(String key, Object value);
    Object get(String key, Class valueType);
}
