package com.lyq.syncdata.pojo;

/**
 * created by lyq
 */
public class ClientMonitor {
    //服务器文件总数
    Integer serverFileCount = 0;
    //本次需要传输的总文件数
    Integer fileCount = 0;
    //总共需要下载文件的大小
    Long totalFileSize = 0L;
    //断点续传文件数量
    Integer notCompleteFileCount = 0;
    //断点续传文件总大小
    Long notCompleteFileSize = 0L;

    public Integer getServerFileCount() {
        return serverFileCount;
    }

    public void setServerFileCount(Integer serverFileCount) {
        this.serverFileCount = serverFileCount;
    }

    public Integer getFileCount() {
        return fileCount;
    }

    public void setFileCount(Integer fileCount) {
        this.fileCount = fileCount;
    }

    public Long getTotalFileSize() {
        return totalFileSize;
    }

    public void setTotalFileSize(Long totalFileSize) {
        this.totalFileSize = totalFileSize;
    }

    public Integer getNotCompleteFileCount() {
        return notCompleteFileCount;
    }

    public void setNotCompleteFileCount(Integer notCompleteFileCount) {
        this.notCompleteFileCount = notCompleteFileCount;
    }

    public Long getNotCompleteFileSize() {
        return notCompleteFileSize;
    }

    public void setNotCompleteFileSize(Long notCompleteFileSize) {
        this.notCompleteFileSize = notCompleteFileSize;
    }
}
