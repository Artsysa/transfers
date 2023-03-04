package com.lyq.syncdata.pojo;

public class CheckPointFileInfo {
    private String path;
    private Boolean completeFile;
    private Long startIndex;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean getCompleteFile() {
        return completeFile;
    }

    public void setCompleteFile(Boolean completeFile) {
        this.completeFile = completeFile;
    }

    public Long getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(Long startIndex) {
        this.startIndex = startIndex;
    }
}
