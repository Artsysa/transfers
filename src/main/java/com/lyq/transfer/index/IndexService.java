package com.lyq.transfer.index;

import cn.hutool.core.collection.CollectionUtil;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.mp4.media.Mp4MediaDirectory;
import com.drew.metadata.mp4.media.Mp4MetaDirectory;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.lyq.syncdata.util.TimeUtil;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.index.parser.FileNameParserFactory;
import com.lyq.transfer.util.FileUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class IndexService {

    public static void main(String[] args) throws IOException, InterruptedException {
        buildIndexJsonFile();
    }

    public static IndexElementWapper getIndexElementWapper(){
        return (IndexElementWapper)FileUtil.getObjectFromFile(CommonConsts.indexFile, IndexElementWapper.class);
    }

    public static void buildIndexJsonFile() throws IOException, InterruptedException {

        File indexDirFile = new File(CommonConsts.indexDir);
        if(!indexDirFile.exists()){
            indexDirFile.mkdirs();
        }

        Map<String, IndexElement> indexElementMap = Optional.ofNullable(getIndexElementWapper())
                .map(IndexElementWapper::getIndexElementList)
                .filter(CollectionUtil::isNotEmpty)
                .map((list) -> list.stream().collect(Collectors.toMap(IndexElement::getPath, Function.identity())))
                .orElse(Maps.newHashMap());

        List<File> allFile = FileUtil.getAllFile();
        final List<IndexElement> elementList = Lists.newArrayList();
        for (File storeFile : allFile) {

            if(storeFile.isDirectory()){
                continue;
            }

            IndexElement indexElementOrg = indexElementMap.get(storeFile.getAbsolutePath());
            if(Objects.nonNull(indexElementOrg)){
                elementList.add(indexElementOrg);
                continue;
            }

            long realPhotoTime = -1L;

            realPhotoTime = getRealPhotoTime(storeFile);

            if(realPhotoTime < 0){
                String fileName = storeFile.getName().substring(0, storeFile.getName().lastIndexOf("."));
                try{
                    realPhotoTime = FileNameParserFactory.obtainParser(fileName).parser(fileName);
                }catch (Exception e){
                    //
                }
            }

            IndexElement indexElement = new IndexElement();
            indexElement.setFileName(storeFile.getName());
            indexElement.setCreateTimeStamp(realPhotoTime);
            indexElement.setPath(storeFile.getAbsolutePath());
            indexElement.setMD5(FileUtil.calculateMD5(storeFile.getAbsolutePath()));
            elementList.add(indexElement);
        }


        List<IndexElement> elementSortList = elementList.stream().sorted(Comparator.comparingLong(IndexElement::getCreateTimeStamp)).collect(Collectors.toList());

       IndexElementWapper indexElementWapper = new IndexElementWapper();
       indexElementWapper.setRootPath(CommonConsts.rootDir);
       indexElementWapper.setCreateTime(System.currentTimeMillis());
       indexElementWapper.setIndexElementList(elementSortList);

       FileUtil.writeFile(CommonConsts.indexFile, indexElementWapper);
    }

    private static long getRealPhotoTime(File file){
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
