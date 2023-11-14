package com.lyq.transfer.index.cache;

import com.lyq.transfer.index.IndexElement;
import com.lyq.transfer.index.IndexService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class IndexCacheManager {

    static Map<String, List<IndexElement>> localIndexElemetnList = null;


    static {
        localIndexElemetnList =  IndexService.getIndexElementWapper().getIndexElementList().stream().collect(
                Collectors.groupingBy(IndexElement::getMD5));
    }

    public static Map<String, List<IndexElement>> getLocalIndexElemetnList(){
        return localIndexElemetnList;
    }

    public static void notifly(){
        localIndexElemetnList =  IndexService.getIndexElementWapper(false).getIndexElementList().stream().collect(
                Collectors.groupingBy(IndexElement::getMD5));
    }

}
