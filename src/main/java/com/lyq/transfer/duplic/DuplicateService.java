package com.lyq.transfer.duplic;

import cn.hutool.core.collection.CollectionUtil;
import com.google.common.collect.Maps;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.index.IndexElement;
import com.lyq.transfer.index.IndexService;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class DuplicateService {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        duplicateFile();
    }

    public static void duplicateFile() {
        Map<String, List<IndexElement>> indexElementMD5Map = Optional.ofNullable(IndexService.getIndexElementWapper().getIndexElementList())
                .filter(CollectionUtil::isNotEmpty)
                .map((list) -> list.stream().collect(Collectors.groupingBy(IndexElement::getMD5)))
                .orElse(Maps.newHashMap());

        Map<String, List<IndexElement>> indexElementNameMap = Optional.ofNullable(IndexService.getIndexElementWapper().getIndexElementList())
                .filter(CollectionUtil::isNotEmpty)
                .map((list) -> list.stream().collect(Collectors.groupingBy(IndexElement::getFileName)))
                .orElse(Maps.newHashMap());

        indexElementNameMap.forEach((name, indexElementList) -> {
            if(indexElementList.size() > 1){
                int i = 0;
                for (IndexElement indexElement : indexElementList) {
                    File file = new File(indexElement.getPath());
                    File duplicDir = new File(CommonConsts.duplicDir + "/" + indexElement.getFileName());
                    if(!duplicDir.exists()){
                        duplicDir.mkdirs();
                    }
                    transfer(file.getAbsolutePath(), CommonConsts.duplicDir + "/" + indexElement.getFileName() + "/" + (i++ + "-" + file.getName()));
                }
            }
        });

        indexElementMD5Map.forEach((name, indexElementList) -> {
            if(indexElementList.size() > 1){
                for (IndexElement indexElement : indexElementList) {
                    File file = new File(indexElement.getPath());
                    File duplicDir = new File(CommonConsts.duplicDir + "/" + indexElement.getMD5());
                    if(!duplicDir.exists()){
                        duplicDir.mkdirs();
                    }
                    transfer(file.getAbsolutePath(), CommonConsts.duplicDir + "/" + indexElement.getMD5() + "/" + file.getName());
                }
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

        try(InputStream is = Files.newInputStream(fileTemp.toPath());
           OutputStream os = Files.newOutputStream(fileAim.toPath())){
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            fileTemp.delete();
        }
    }
}
