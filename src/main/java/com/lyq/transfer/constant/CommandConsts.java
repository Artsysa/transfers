package com.lyq.transfer.constant;

/**
 * created by lyq
 */
public enum CommandConsts {
    COMMAND_REQUEST(100, "请求"),
    COMMAND_REQUEST_TIMEOUT(101, "请求超时"),


    COMMAND_RESPONSE(200, "响应"),
    COMMAND_RESPONSE_SUCCESS(201, "请求成功且获取到正常的响应信息"),
    COMMAND_RESPONSE_TIMEOUT(202, "超时"),
    COMMAND_RESPONSE_ERROR(203, "失败"),

    COMMAND_INNER_ERROR(300, "内部错误"),

    GET_FILE_INCREMENT(1024, "获取文件增量信息(需要同步到对端的文件)"),
    UPLOAD_FILE(1025, "发送文件数据"),
    REGISTER_CLIENT(1026, "注册客户端"),
    CLIENT_DOWNLOAD_INCREMENT(1027, "客户端下载增量数据"),
    CLIENT_DOWNLOAD_ALL(1028, "客户端下载全部数据"),
    GET_SERVER_INDEX_FILE(1029, "获取服务器index索引数据"),
    GET_SERVER_FILE_LIST(1030, "从服务器下载指定文件"),



    UNKNOWN(2048, "未知"),
    ;

    private final Integer code;
    private final String descritpion;

    CommandConsts(Integer code, String descritpion) {
        this.code = code;
        this.descritpion = descritpion;
    }

    public static CommandConsts getCommandByCode(Integer code){
        for (CommandConsts commandConst : CommandConsts.values()) {
            if(commandConst.getCode().equals(code)){
                return commandConst;
            }
        }
        return UNKNOWN;
    }

    public Integer getCode() {
        return code;
    }

    public String getDescritpion() {
        return descritpion;
    }
}
