package com.lyq.transfer.pojo;

import java.util.List;

public class SendFileVO {

    private List<String> paths;

    private Long clientId;

    public List<String> getPaths() {
        return paths;
    }

    public void setPaths(List<String> paths) {
        this.paths = paths;
    }

    public Long getClientId() {
        return clientId;
    }

    public void setClientId(Long clientId) {
        this.clientId = clientId;
    }
}
