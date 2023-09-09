package com.lyq.syncdata.constant;

/**
 * created by lyq
 */
public enum CommandEnum {
    SERVER_RESPONSE(4, "服务器响应数据"),
    CLIENT_RESPONSE(9, "客户端响应数据"),
    UPLOADFILE(5, "上传文件"),
    DOWONLOAD_FILE(8, "下载所有文件"),
    DOWONLOAD_PICTURE(10, "下载图片"),
    SEND_CLIENT_PROGRESS(7,"客户端向服务器发送当前最新进度"),
    TIMEOUT(11,"超时"),
    COPY_DATA(12,"粘贴数据"),
    CREATE_FILE(13,"预先创建文件"),
    ORDER_FILE(14,"获取文件顺序"),
    GET_SYNC_PROGRESS(6, "获取最新数据同步进度");


    private final Integer code;
    private final String description;

    CommandEnum(Integer code, String description) {
        this.code = code;
        this.description = description;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public static CommandEnum getCommandEnum(Integer code){
        for (CommandEnum commandEnum : CommandEnum.values()) {
            if(commandEnum.getCode().equals(code)){
                return commandEnum;
            }
        }
        return null;
    }
}
