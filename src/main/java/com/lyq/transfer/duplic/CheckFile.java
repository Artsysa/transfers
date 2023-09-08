package com.lyq.transfer.duplic;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.index.IndexElementWapper;
import com.lyq.transfer.pojo.FileMD5Info;
import com.lyq.transfer.util.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class CheckFile {

    public static void main(String[] args) {

        File root = new File(CommonConsts.tempDir);

        List<FileMD5Info> diff = Lists.newArrayList();
        List<String> diffNull = Lists.newArrayList();

        Map<String, String> map = Maps.newHashMap();

        for (File file : root.listFiles()) {
            String md5 = FileUtil.calculateMD5(file.getAbsolutePath());
            map.put(file.getName(), md5);
        }

        Set<String> set = Sets.newHashSet();
        IndexElementWapper objectFromFile = (IndexElementWapper) FileUtil.getObjectFromFile("/Users/lyq/Downloads/index.json", IndexElementWapper.class);
        Map<String, FileMD5Info> fileMD5MInfoMap = objectFromFile.getIndexElementList().stream()
                .map(indexElement -> {
                    try{
                        FileMD5Info fileMD5Info = new FileMD5Info();
                        fileMD5Info.setFilePath(indexElement.getPath().substring(indexElement.getPath().lastIndexOf("/" ) + 1));
                        fileMD5Info.setMD5(indexElement.getMD5());
                        return fileMD5Info;
                    }catch (Exception e){
                        System.out.println("error:" + JSON.toJSONString(indexElement));
                    }
                    return new FileMD5Info();
                })
                .filter((t) -> {
                    return set.add(t.getMD5());
                })
                .collect(Collectors.toMap(FileMD5Info::getFilePath, Function.identity()));

        for (Map.Entry<String, String> entry : map.entrySet()) {
            FileMD5Info md5Org = fileMD5MInfoMap.get(entry.getKey());
            if(md5Org == null){
                diffNull.add(entry.getKey());
                continue;
            }
            if(!StringUtils.equals(md5Org.getMD5(), entry.getValue())){
                md5Org.setMD5(md5Org.getMD5() + "|" + entry.getValue());
                diff.add(md5Org);
            }
        }
        System.out.println("================================null===");
        System.out.println(JSON.toJSONString(diffNull));
        System.out.println("================================diff===");
        System.out.println(JSON.toJSONString(diff));
    }
}
