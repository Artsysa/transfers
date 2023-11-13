package com.lyq.transfer.service;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lyq.transfer.adapter.FileUploadAdapter;
import com.lyq.transfer.constant.CommandConsts;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.index.IndexElement;
import com.lyq.transfer.index.IndexElementWapper;
import com.lyq.transfer.index.IndexService;
import com.lyq.transfer.netty.service.CommonThreadService;
import com.lyq.transfer.netty.service.LimitService;
import com.lyq.transfer.netty.service.RemotingServiceWapper;
import com.lyq.transfer.pojo.Command;
import com.lyq.transfer.pojo.FileMD5Info;
import com.lyq.transfer.pojo.Response;
import com.lyq.transfer.pojo.UploadFile;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class DownloadFileService {


    public static void getServerIndexFile(Command requestCommand, ChannelHandlerContext ctx){
        IndexElementWapper indexElementWapper = IndexService.getIndexElementWapper();
        RemotingServiceWapper.responseSuccess(requestCommand, indexElementWapper.getIndexElementList(), ctx);
    }


    public static void downloadFileList(Command requestCommand, ChannelHandlerContext ctx){

        List<String> clientMD5InfoList = JSON.parseArray(new String(requestCommand.getBody()), String.class);

        Map<String, List<IndexElement>> localIndexElemetnList = IndexService.getIndexElementWapper().getIndexElementList().stream().collect(Collectors.groupingBy(IndexElement::getMD5));

        List<IndexElement> incrementFileList = Lists.newArrayList();

        for (String md5 : clientMD5InfoList) {
            List<IndexElement> indexElements = localIndexElemetnList.get(md5);
            if(CollectionUtil.isNotEmpty(indexElements)){
                incrementFileList.add(indexElements.get(0));
            }
        }

        RemotingServiceWapper.responseSuccess(requestCommand, incrementFileList.size(), ctx);

        CommonThreadService.submitTask(() ->{
            doUploadIncrement(incrementFileList.stream().map(IndexElement::getPath).collect(Collectors.toList()), ctx);
        });
    }

    public static void downloadIncrement(Command requestCommand, ChannelHandlerContext ctx){

        List<FileMD5Info> clientFileMD5InfoList = JSON.parseArray(new String(requestCommand.getBody()), FileMD5Info.class);
        Map<String, List<FileMD5Info>> clientFileMap = clientFileMD5InfoList.stream().collect(Collectors.groupingBy(FileMD5Info::getMD5));

        List<IndexElement> localIndexElemetnList = IndexService.getIndexElementWapper().getIndexElementList();

        List<IndexElement> incrementFileList = Lists.newArrayList();
        for (IndexElement indexElement : localIndexElemetnList) {
            List<FileMD5Info> md5InfoList = clientFileMap.get(indexElement.getMD5());
            if(CollectionUtil.isEmpty(md5InfoList)){
                incrementFileList.add(indexElement);
            }
        }

        RemotingServiceWapper.responseSuccess(requestCommand, incrementFileList.size(), ctx);

        CommonThreadService.submitTask(() ->{
            doUploadIncrement(incrementFileList.stream().map(IndexElement::getPath).collect(Collectors.toList()), ctx);
        });
    }



    public static void doUploadIncrement(List<String> incrementFileList, ChannelHandlerContext ctx){
        for (String abstractPath : incrementFileList) {
            try{

                File file = new File(abstractPath);

                if(file.length() <= CommonConsts.slice_max){
                    UploadFile smallUploadFile = FileUploadAdapter.buildSmallFileUpload(file);
                    doUploadFile(smallUploadFile, ctx);
                }else{
                    doUploadBigFile(file, ctx);
                }

            }catch (Exception e){
                //
                e.printStackTrace();
            }
        }
    }

    private static void doUploadBigFile(File file, ChannelHandlerContext ctx) throws IOException {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")){

            long fileTotalSize = file.length();

            int sliceMax = CommonConsts.slice_max;

            int loopCount = (int) Math.ceil(fileTotalSize / (sliceMax * 1.0));

            for(int index = 0; index < loopCount; index++){
                UploadFile uploadFile = FileUploadAdapter.buildBigFileUpload(file, randomAccessFile, index == (loopCount - 1), index);
                doUploadFile(uploadFile, ctx);
            }

        }
    }

    public static void doUploadFile(UploadFile uploadFile, ChannelHandlerContext ctx) {
        LimitService.acquireRequest();
        RemotingServiceWapper.uploadFile(uploadFile, ctx, (serverResponseCommand) -> processorResopnse(serverResponseCommand, uploadFile, ctx));
    }

    private static void dealyDoUploadFile(UploadFile uploadFile, ChannelHandlerContext ctx){
        CommonThreadService.submitTaskDefaultDelay(() -> {
            try {
                doUploadFile(uploadFile, ctx);
            } catch (Exception e) {
                //
                e.printStackTrace();
            }
        });
    }

    private static void processorResopnse(Command serverResponseCommand, UploadFile uploadFile, ChannelHandlerContext ctx){
        Response response = JSON.parseObject(serverResponseCommand.getBody(), Response.class);
        CommandConsts commandResponseCode = CommandConsts.getCommandByCode(response.getCode());
        LimitService.releaseRequest();
        switch (commandResponseCode){
            case COMMAND_INNER_ERROR:
            case COMMAND_RESPONSE_ERROR:
            case COMMAND_RESPONSE_TIMEOUT:
                dealyDoUploadFile(uploadFile, ctx);
                break;
            case COMMAND_RESPONSE_SUCCESS:
                //TODO 统计信息

                break;
            default:
        }
    }
}
