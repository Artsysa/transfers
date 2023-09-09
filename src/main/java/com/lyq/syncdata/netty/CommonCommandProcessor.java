package com.lyq.syncdata.netty;

import com.alibaba.fastjson.JSON;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mp4.media.Mp4MediaDirectory;
import com.drew.metadata.mp4.media.Mp4MetaDirectory;
import com.lyq.syncdata.constant.CommandEnum;
import com.lyq.syncdata.constant.SyncDataConsts;
import com.lyq.syncdata.index.parser.FileNameParserFactory;
import com.lyq.syncdata.pojo.CopyData;
import com.lyq.syncdata.pojo.OrderFile;
import com.lyq.syncdata.pojo.Progress;
import com.lyq.syncdata.pojo.SyncDataCommand;
import com.lyq.syncdata.service.CheckPointService;
import com.lyq.syncdata.util.TimeUtil;
import io.netty.channel.ChannelHandlerContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class CommonCommandProcessor implements SyncDataCommandProcessor{

    private static final Logger log = LoggerFactory.getLogger("CommonCommandProcessor");


    private static final CommandEnum progressEnum = CommandEnum.GET_SYNC_PROGRESS;
    private static final CommandEnum serverResponseEnum = CommandEnum.CLIENT_RESPONSE;
    private static final CommandEnum copyDataEnum = CommandEnum.COPY_DATA;
    private static final CommandEnum createFileEnum = CommandEnum.CREATE_FILE;
    private static final CommandEnum orderFileEnum = CommandEnum.ORDER_FILE;
    private final ThreadPoolExecutor commonWorks;
    private final CheckPointService checkPointService;
    private final RequestManager requestManager;


    public CommonCommandProcessor(ThreadPoolExecutor commonWorks, RequestManager requestManager) {
        this.commonWorks = commonWorks;
        this.requestManager = requestManager;
        checkPointService = new CheckPointService();
    }

    @Override
    public void processor(ChannelHandlerContext ctx, SyncDataCommand command) {
        switch (Objects.requireNonNull(CommandEnum.getCommandEnum(command.getCode()))){
            case GET_SYNC_PROGRESS :
                commonWorks.execute(() -> {
                    getNewProgress(ctx, command);
                });
            break;
            case CLIENT_RESPONSE :
                Integer commandId = command.getCommandId();
                RequestFuture requestFuture = requestManager.getByCommandId(commandId);
                requestFuture.setResponse(command);
                if(requestFuture.getCommandCallable() != null){
                    requestManager.asyncInvoke(() -> {
                        try {
                            requestFuture.invoke();
                        } catch (IOException e) {
                            log.error(e.getMessage());
                        }finally {
                            requestManager.remove(commandId);
                        }
                    });
                }
                break;
            case COPY_DATA:
                CopyData copyData = JSON.parseObject(command.getBody(), CopyData.class);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(new StringSelection(copyData.getContain()), null);
                break;
            case CREATE_FILE:
                OrderFile orderFile = JSON.parseObject(command.getBody(), OrderFile.class);
                orderFile.getFileName().forEach(name -> {
                    File file = new File(SyncDataConsts.rootDir + name);
                    if(!file.exists()){
                        try {
                            file.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
                ctx.writeAndFlush(SyncDataCommand.buildResponse(command, true));
                break;
            case ORDER_FILE:
                File storeRootDir = new File(SyncDataConsts.rootDir);
                File oneplusDir = new File(SyncDataConsts.oneplusDir);
                File[] oneplusFiles = oneplusDir.listFiles();
                File[] storeRootFiles = storeRootDir.listFiles();
                List<File> fileList = Arrays.stream(storeRootFiles).collect(Collectors.toList());
                fileList.addAll(Arrays.stream(oneplusFiles).collect(Collectors.toList()));
                fileList = fileList.stream()
                        .filter(file -> !file.getAbsolutePath().contains(".DS_Store"))
                    .sorted(
                            (fist, last) -> {
                                long fistlong = getDate(new File(fist.getPath()));
                                long lastlong = getDate(new File(last.getPath()));
                                return Long.compare(fistlong, lastlong);
                            })
                    .collect(Collectors.toList());
                OrderFile storeFile = new OrderFile();
                storeFile.setFileName(fileList.stream().map(checkPointFileInfo -> checkPointFileInfo.getPath().substring(checkPointFileInfo.getPath().lastIndexOf("/"))).collect(Collectors.toList()));
                SyncDataCommand syncDataCommand = new SyncDataCommand();
                syncDataCommand.setCommandId(command.getCommandId());
                syncDataCommand.setCode(CommandEnum.ORDER_FILE.getCode());
                syncDataCommand.setBody(JSON.toJSONBytes(storeFile));
                syncDataCommand.setLength(syncDataCommand.getBody().length);
                ctx.writeAndFlush(syncDataCommand);
                break;
            default:
                log.error("不支持该指令,command:{}" + JSON.toJSONString(command));
        }
    }

    @Override
    public boolean match(SyncDataCommand command) {
        return progressEnum.getCode().equals(command.getCode())
                || serverResponseEnum.getCode().equals(command.getCode())
                || copyDataEnum.getCode().equals(command.getCode())
                || orderFileEnum.getCode().equals(command.getCode())
                || createFileEnum.getCode().equals(command.getCode());
    }

    public void getNewProgress(ChannelHandlerContext ctx, SyncDataCommand command){
        Progress progress = checkPointService.getNewProgress();
        SyncDataCommand syncDataCommand = new SyncDataCommand();
        syncDataCommand.setCommandId(command.getCommandId());
        syncDataCommand.setCode(CommandEnum.GET_SYNC_PROGRESS.getCode());
        syncDataCommand.setBody(JSON.toJSONBytes(progress));
        syncDataCommand.setLength(syncDataCommand.getBody().length);
        ctx.writeAndFlush(syncDataCommand);
    }

    public long getDate(File file){
        long realPhotoTime = -1L;
        try{
            realPhotoTime = getRealPhotoTime(file);

            if(realPhotoTime < 0){
                String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
                realPhotoTime = FileNameParserFactory.obtainParser(fileName).parser(fileName);
            }

        }catch (Exception e){
            //continue
        }
        return realPhotoTime;
    }

    private long getRealPhotoTime(File file){
        String timeString = null;
        try{
            Metadata metadata = ImageMetadataReader.readMetadata(file);
            if(file.getName().contains("mp4")){
                Mp4MetaDirectory firstDirectoryOfType = metadata.getFirstDirectoryOfType(Mp4MetaDirectory.class);
                if(firstDirectoryOfType != null){
                    timeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(firstDirectoryOfType.getDate(Mp4MediaDirectory.TAG_CREATION_TIME));
                }
            }else{
                ExifSubIFDDirectory directory = metadata.getFirstDirectoryOfType(ExifSubIFDDirectory.class);
                if(directory != null){
                    timeString = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(directory.getDateOriginal());
                }
            }
        }catch (Exception e){
            //continue
        }
        return StringUtils.isNotBlank(timeString) ? TimeUtil.yyyyMMddHHmmss2TimeStamp(timeString) : -1L;
    }
}
