package com.lyq.transfer.duplic;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.index.IndexElement;
import com.lyq.transfer.index.IndexService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class DuplicateService {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        duplicateFile();
    }

    public static void duplicateFile() {
        Map<String, IndexElement> indexElementMap = Optional.ofNullable(IndexService.getIndexElementWapper().getIndexElementList())
                .filter(CollectionUtil::isNotEmpty)
                .map((list) -> list.stream().collect(Collectors.toMap(IndexElement::getPath, Function.identity())))
                .orElse(Maps.newHashMap());

        Set<String> md5Set = Sets.newHashSet();
        Map<String, List<String>> duplicFileAbstractMap = Maps.newHashMap();
        AtomicInteger duplicateCount = new AtomicInteger();
        indexElementMap.forEach((path, indexElement) -> {
            if(!md5Set.add(indexElement.getMD5())){
                List<String> duplicAbstractPathList = duplicFileAbstractMap.get(indexElement.getMD5());
                if(org.apache.commons.collections4.CollectionUtils.isEmpty(duplicAbstractPathList)){
                    duplicAbstractPathList = Lists.newArrayList();
                    duplicFileAbstractMap.put(indexElement.getMD5(), duplicAbstractPathList);
                }
                duplicAbstractPathList.add(path);
                duplicateCount.getAndIncrement();
            }
        });

        File duplicDir = new File(CommonConsts.duplicDir);
        if(!duplicDir.exists()){
            duplicDir.mkdirs();
        }

        duplicFileAbstractMap.forEach((md5, duplicFileAbstractPathList) -> {
            for (String path : duplicFileAbstractPathList) {
                File file = new File(path);
                transfer(file.getAbsolutePath(), CommonConsts.duplicDir + "/" + file.getName());
                file.delete();
            }
        });

    }

    public static void transfer(String oriPath, String aimPaht){
        File fileAim = new File(aimPaht);
        File fileTemp = new File(oriPath);
        if(!fileAim.exists()){
            try {
                fileAim.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(fileTemp);
            os = new FileOutputStream(fileAim);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
