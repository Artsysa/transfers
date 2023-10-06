package com.lyq.transfer.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.index.IndexService;
import com.lyq.transfer.netty.service.CommonThreadService;
import com.lyq.transfer.netty.service.RemotingServiceWapper;
import com.lyq.transfer.pojo.Command;
import com.lyq.transfer.pojo.FileMD5Info;
import com.lyq.transfer.pojo.UploadFile;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class SaveFileService {

    private static final Map<String, RandomAccessFile> bigFileMap = Maps.newConcurrentMap();


    static {
        File file = new File(CommonConsts.tempDir );
        if(!file.exists()){
            file.mkdirs();
        }
    }


    public static void processorFileIncrement(Command command){
        List<FileMD5Info> clientFileMD5InfoList = JSON.parseArray(new String(command.getBody()), FileMD5Info.class);
        Set<String> set = Sets.newHashSet();
        Map<String, FileMD5Info> localFileMD5Map = IndexService.getIndexElementWapper()
                .getIndexElementList()
            .stream()
                .map(indexElement -> {
                    FileMD5Info fileMD5Info = new FileMD5Info();
                    fileMD5Info.setFilePath(indexElement.getPath());
                    fileMD5Info.setMD5(indexElement.getMD5());
                    return fileMD5Info;
                })
                .filter(fileMD5Info -> set.add(fileMD5Info.getMD5()))
                .collect(Collectors.toMap(FileMD5Info::getMD5, Function.identity()));

        List<String> needFilePath = Lists.newArrayList();
        if(CollectionUtil.isNotEmpty(clientFileMD5InfoList)){
            for (FileMD5Info fileMD5Info : clientFileMD5InfoList) {
                FileMD5Info needFileMD5Info = localFileMD5Map.get(fileMD5Info.getMD5());
                if(Objects.isNull(needFileMD5Info)){
                    needFilePath.add(fileMD5Info.getFilePath());
                }
            }
        }


        RemotingServiceWapper.responseSuccess(command, needFilePath);
    }

    public static void saveFile(Command command){
        CommonThreadService.submitTask(() -> {
            UploadFile uploadFile = JSON.parseObject(command.getBody(), UploadFile.class);
            if(uploadFile.getFileSizeType()){
                try{
                    RandomAccessFile randomAccessFile = bigFileMap.get(uploadFile.getFileName());
                    if(Objects.isNull(randomAccessFile)){
                        String fileName = uploadFile.getFileName();
                        File file = new File(CommonConsts.tempDir + "/" + fileName);
                        if(!file.exists()){
                            file.createNewFile();
                        }
                        randomAccessFile = new RandomAccessFile(file, "rw");
                        bigFileMap.put(uploadFile.getFileName(), randomAccessFile);
                    }
                    randomAccessFile.seek(uploadFile.getWriteIndex());
                    randomAccessFile.write(uploadFile.getFileContent());
                    if(uploadFile.getFinish()){
                        bigFileMap.remove(uploadFile.getFileName());
                    }
                    RemotingServiceWapper.responseSuccess(command);
                }catch (Exception e){
                    e.printStackTrace();
                    RemotingServiceWapper.responseFail(command);
                }
            }else{
                try{
                    String fileName = uploadFile.getFileName();
                    File file = new File(CommonConsts.tempDir + "/" + fileName);
                    if(!file.exists()){
                        file.createNewFile();
                    }
                    RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
                    randomAccessFile.write(uploadFile.getFileContent());
                    randomAccessFile.close();
                    RemotingServiceWapper.responseSuccess(command);
                }catch (Exception e){
                    e.printStackTrace();
                    RemotingServiceWapper.responseFail(command);
                }
            }
        });
    }
}
