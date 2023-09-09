package com.lyq.syncdata.index;

import com.alibaba.fastjson.JSON;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mp4.media.Mp4MediaDirectory;
import com.drew.metadata.mp4.media.Mp4MetaDirectory;
import com.lyq.syncdata.constant.SyncDataConsts;
import com.lyq.syncdata.index.parser.FileNameParserFactory;
import com.lyq.syncdata.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class IndexService {
    public static void main(String[] args) throws IOException {
        IndexService indexService = new IndexService();
        indexService.buildIndex();
    }

   public void buildIndex() throws IOException {

        File indexDirFile = new File(SyncDataConsts.indexDir);
        if(!indexDirFile.exists()){
            indexDirFile.mkdirs();
        }

        File rootDirFile = new File(SyncDataConsts.rootDir);
        int i = 0;
        int cacul = 0;
        int fail = 0;
        List<IndexElement> elementList = new ArrayList<>();
        Map<String, String> names = new HashMap<>();
       IndexElementWapper indexElementWapper = new IndexElementWapper();
       indexElementWapper.setRootPath(SyncDataConsts.rootDir);
       indexElementWapper.setCreateTime(System.currentTimeMillis());
        for (File storeFile : Objects.requireNonNull(rootDirFile.listFiles())) {

            long realPhotoTime = -1L;

            realPhotoTime = getRealPhotoTime(storeFile);

            if(realPhotoTime < 0){
                String fileName = storeFile.getName().substring(0, storeFile.getName().lastIndexOf("."));
                try{
                    realPhotoTime = FileNameParserFactory.obtainParser(fileName).parser(fileName);
                }catch (Exception e){
                    fail++;
                    names.put(fileName, e.getMessage());
                }
            }

            if(realPhotoTime > 0){
                IndexElement indexElement = new IndexElement();
                indexElement.setFileName(storeFile.getName());
                indexElement.setCreateTimeStamp(realPhotoTime);
                indexElement.setPath(storeFile.getAbsolutePath());
                elementList.add(indexElement);
            }
        }

        elementList = elementList.stream().sorted(Comparator.comparingLong(IndexElement::getCreateTimeStamp)).collect(Collectors.toList());
       indexElementWapper.setIndexElementList(elementList);

       File file = new File(SyncDataConsts.indexDir + "/index.json");
       file.createNewFile();
       FileWriter fileWriter = new FileWriter(file);
       fileWriter.write(JSON.toJSONString(indexElementWapper));
       fileWriter.close();

       System.out.println("总数：" +rootDirFile.listFiles().length + ",有日期的：" + i + ",通过解析获得日期的:" + cacul + ",解析失败的:" + fail);

       System.out.println(JSON.toJSONString(names));
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
