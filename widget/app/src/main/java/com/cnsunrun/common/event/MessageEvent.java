package com.cnsunrun.common.event;

/**
 * Created by cnsunrun on 2017/7/14.
 * <p>
 * EventBus模型类
 */

public class MessageEvent {
    private String type;
    private String content;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public MessageEvent(String action, String content) {
        this.type = action;
        this.content = content;
    }

    public MessageEvent(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
    public MessageEvent() {
    }
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
