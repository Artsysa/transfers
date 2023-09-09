package com.lyq.syncdata.netty;

import com.alibaba.fastjson.JSON;
import com.lyq.syncdata.constant.CommandEnum;
import com.lyq.syncdata.constant.SyncDataConsts;
import com.lyq.syncdata.pojo.SyncDataCommand;
import com.lyq.syncdata.pojo.UploadFile;
import com.lyq.syncdata.service.BigDataService;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.concurrent.ThreadPoolExecutor;

/**
 * created by lyq
 */
public class SaveDataProcessor implements SyncDataCommandProcessor{
    private static final Logger log = LoggerFactory.getLogger("SaveDataProcessor");

    ///Users/lyq/Downloads
    //private static final String rootDir = System.getProperty("user.dir") + "/syncData/";
    private final CommandEnum pictureEnum = CommandEnum.UPLOADFILE;
    private final ThreadPoolExecutor saveFileWorks;
    private final BigDataService bigDataService;

    public SaveDataProcessor(ThreadPoolExecutor commonWorks, BigDataService bigDataService) {
        File rootDirFile = new File(SyncDataConsts.rootDir);
        if(!rootDirFile.exists()){
            rootDirFile.mkdirs();
        }
        this.saveFileWorks = commonWorks;
        this.bigDataService = bigDataService;
    }

    @Override
    public void processor(ChannelHandlerContext ctx, SyncDataCommand command){
        saveFileWorks.execute(() -> {
            try{
                UploadFile picture = JSON.parseObject(command.getBody(), UploadFile.class);
                File file = new File(SyncDataConsts.rootDir + picture.getAbstractFileName());
                if(!file.exists()){
                    file.createNewFile();
                }
                if(picture.getBigFile() != null && picture.getBigFile()){
                    bigDataService.saveBigData(ctx, command, picture, file);
                }else{
                    RandomAccessFile origin = new RandomAccessFile(file, "rw");
                    origin.getChannel().write(ByteBuffer.wrap(picture.getContain()));
                    origin.close();
                    ctx.writeAndFlush(SyncDataCommand.buildResponse(command, true));
                }
            }catch (Exception e){
                e.printStackTrace();
                ctx.writeAndFlush(SyncDataCommand.buildResponse(command, false));
            }
        });
    }

    @Override
    public boolean match(SyncDataCommand command) {
        return pictureEnum.getCode().equals(command.getCode());
    }

}
