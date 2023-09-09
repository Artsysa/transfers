package com.lyq.syncdata.service;

import com.lyq.syncdata.pojo.SyncDataCommand;
import com.lyq.syncdata.pojo.UploadFile;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Map;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * created by lyq
 */
public class BigDataService {

    private final Map<Integer, RandomAccessFile> fileMap;

    private final ThreadPoolExecutor works;

    public BigDataService() {
        this.fileMap = new ConcurrentHashMap<>();
        this.works = new ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() >> 1, Runtime.getRuntime().availableProcessors() >> 1, 60, TimeUnit.SECONDS, new ArrayBlockingQueue<>(20000),
                (r) -> {
                    Thread thread = new Thread(r);
                    thread.setName("bigData-thread");
                    return thread;
                }
        );
    }

    public void saveBigData(ChannelHandlerContext ctx, SyncDataCommand command, UploadFile uploadFile, File file){
        works.execute(() -> {
            try {
                RandomAccessFile randomAccessFile = fileMap.get(uploadFile.getId());
                if(randomAccessFile == null){
                    randomAccessFile = new RandomAccessFile(file, "rw");
                    fileMap.put(uploadFile.getId(), randomAccessFile);
                }
                randomAccessFile.seek(uploadFile.getWriteIndex().intValue());
                randomAccessFile.write(uploadFile.getContain());
                if(uploadFile.getEnd()){
                    fileMap.remove(uploadFile.getId());
                    randomAccessFile.close();
                }
                ctx.writeAndFlush(SyncDataCommand.buildResponse(command, true));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public Map<Integer, RandomAccessFile> getFileMap() {
        return fileMap;
    }
}
