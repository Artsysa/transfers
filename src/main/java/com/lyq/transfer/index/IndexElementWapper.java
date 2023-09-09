package com.lyq.transfer.index;

import java.util.List;

/**
 * created by lyq
 */
public class IndexElementWapper {
    private String rootPath;
    List<IndexElement> indexElementList;
    private Long createTime;

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public String getRootPath() {
        return rootPath;
    }

    public void setRootPath(String rootPath) {
        this.rootPath = rootPath;
    }

    public List<IndexElement> getIndexElementList() {
        return indexElementList;
    }

    public void setIndexElementList(List<IndexElement> indexElementList) {
        this.indexElementList = indexElementList;
    }
}
