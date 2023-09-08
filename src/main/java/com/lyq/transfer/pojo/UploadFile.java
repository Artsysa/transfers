package com.lyq.transfer.pojo;

public class UploadFile {

    //文件名称 包含文件类型后缀,例如：log.txt
    private String fileName;

    private byte[] fileContent;

    //false 表示当前文件是小于一个数据包，即当前是小文件传输
    //true 表示当前文件是大文件，需要分片传输
    private Boolean fileSizeType;

    //如果当前是大文件，需要指明写入文件的位置
    private Long writeIndex;

    //表示当前文件传输是否结束了
    private Boolean finish;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getFileContent() {
        return fileContent;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public Boolean getFileSizeType() {
        return fileSizeType;
    }

    public void setFileSizeType(Boolean fileSizeType) {
        this.fileSizeType = fileSizeType;
    }

    public Long getWriteIndex() {
        return writeIndex;
    }

    public void setWriteIndex(Long writeIndex) {
        this.writeIndex = writeIndex;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }
}
