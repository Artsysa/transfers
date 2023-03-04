package com.lyq.syncdata.netty;

import com.alibaba.fastjson.JSON;
import com.lyq.syncdata.constant.CommandEnum;
import com.lyq.syncdata.constant.ServerResponseEnum;
import com.lyq.syncdata.pojo.ServerResponse;
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
    private static final String rootDir = "/Users/lyq/Downloads/syncData/";
    private final CommandEnum pictureEnum = CommandEnum.UPLOADFILE;
    private final ThreadPoolExecutor saveFileWorks;
    private final BigDataService bigDataService;

    public SaveDataProcessor(ThreadPoolExecutor commonWorks, BigDataService bigDataService) {
        File rootDirFile = new File(rootDir);
        if(!rootDirFile.exists()){
            rootDirFile.mkdirs();
        }
        log.info("store path: {}", rootDir);
        this.saveFileWorks = commonWorks;
        this.bigDataService = bigDataService;
    }

    @Override
    public void processor(ChannelHandlerContext ctx, SyncDataCommand command){
        saveFileWorks.execute(() -> {
            try{
                UploadFile picture = JSON.parseObject(command.getBody(), UploadFile.class);
                File file = new File(rootDir + picture.getAbstractFileName());
                if(!file.exists()){
                    file.createNewFile();
                }
                if(picture.getBigFile() != null && picture.getBigFile()){
                    bigDataService.saveBigData(ctx, command, picture, file);
                }else{
                    RandomAccessFile origin = new RandomAccessFile(file, "rw");
                    origin.getChannel().write(ByteBuffer.wrap(picture.getContain()));
                    origin.close();
                    ctx.writeAndFlush(buildResponse(command, true));
                }
            }catch (Exception e){
                e.printStackTrace();
                ctx.writeAndFlush(buildResponse(command, false));
            }
        });
    }

    @Override
    public boolean match(SyncDataCommand command) {
        return pictureEnum.getCode().equals(command.getCode());
    }

    public static SyncDataCommand buildResponse(SyncDataCommand clientCommand, boolean success){
        SyncDataCommand syncDataCommand = new SyncDataCommand();
        syncDataCommand.setCommandId(clientCommand.getCommandId());
        syncDataCommand.setCode(CommandEnum.SERVER_RESPONSE.getCode());
        ServerResponse serverResponse = new ServerResponse();
        if(success){
            serverResponse.setCode(ServerResponseEnum.SAVE_SUCCESS.getCode());
        }else{
            serverResponse.setCode(ServerResponseEnum.SAVE_ERROR.getCode());
            serverResponse.setDescription(ServerResponseEnum.SAVE_ERROR.getDescription());
        }
        syncDataCommand.setBody(JSON.toJSONBytes(serverResponse));
        syncDataCommand.setLength(syncDataCommand.getBody().length);
        return syncDataCommand;
    }

    public static String getRootDir() {
        return rootDir;
    }
}
