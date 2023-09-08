package com.lyq.transfer.pojo;

/**
 * created by lyq
 */
public class Command {
    private Integer length;

    private byte[] body;

    private Integer code;

    private Long commandId;

    private Integer type;

    private Long clientUnique;

    public Long getClientUnique() {
        return clientUnique;
    }

    public void setClientUnique(Long clientUnique) {
        this.clientUnique = clientUnique;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Long getCommandId() {
        return commandId;
    }

    public void setCommandId(Long commandId) {
        this.commandId = commandId;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }
}
