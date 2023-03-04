package com.lyq.syncdata.constant;

public enum ServerResponseEnum {

    SAVE_SUCCESS(100, "服务器存储成功"),
    SAVE_ERROR(101, "服务器存储失败"),
    SAFEPOINT(102, "获取当前最新同步进度");

    private Integer code;
    private String description;

    ServerResponseEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
