package com.lyq.syncdata.pojo;

/**
 * created by lyq
 */
public class UploadFile {
    private String fileName;
    private byte[] contain;
    private String suffix;
    private Long totalSize;
    private Long writeIndex;
    private Boolean bigFile;
    private Integer id;
    private Boolean end;

    public UploadFile copy(){
        UploadFile newUploadFile = new UploadFile();
        newUploadFile.setFileName(fileName);
        newUploadFile.setSuffix(suffix);
        newUploadFile.setTotalSize(totalSize);
        newUploadFile.setBigFile(bigFile);
        newUploadFile.setId(id);
        return newUploadFile;
    }


    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getBigFile() {
        return bigFile;
    }

    public void setBigFile(Boolean bigFile) {
        this.bigFile = bigFile;
    }

    public Long getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(Long totalSize) {
        this.totalSize = totalSize;
    }

    public Long getWriteIndex() {
        return writeIndex;
    }

    public void setWriteIndex(Long writeIndex) {
        this.writeIndex = writeIndex;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public byte[] getContain() {
        return contain;
    }

    public void setContain(byte[] contain) {
        this.contain = contain;
    }

    public String getAbstractFileName(){
        return fileName + "." + suffix;
    }
}
