package com.lyq.transfer.zip;

import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.index.IndexElement;
import com.lyq.transfer.index.IndexService;
import com.lyq.transfer.util.FFmpegUtil;
import com.lyq.transfer.util.VideoThumbnailGenerator;
import java.io.File;
import java.io.IOException;
import java.util.List;
import net.coobird.thumbnailator.Thumbnails;

public class FileZipService {

    public static void main(String[] args) {
        prepareAllFile();
    }

    public static void prepareAllFile(){
        List<IndexElement> indexElementList = IndexService.getIndexElementWapper()
                .getIndexElementList();
        for (IndexElement indexElement : indexElementList) {
            try{
                prepareFile(indexElement.getPath(), indexElement.getFileName());
            }catch (Exception e){
                System.out.println(indexElement.getPath());
            }
        }
    }

    public static void prepareFile(String path, String fileName){
        File file = null;
        if(FFmpegUtil.isVideo(path)){
            String fileZipPath = CommonConsts.cacheDir + "/" + fileName.substring(0, fileName.lastIndexOf(".") + 1) +"jpg";
            file = new File(fileZipPath);
            if(!file.exists()){
                VideoThumbnailGenerator.generateThumbnails(path, fileZipPath, 100, 100, 10.0);
            }
        }else{
            String fileZipPath = CommonConsts.cacheDir + "/" + fileName;
            file = new File(fileZipPath);
            if(!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                int width = 100;
                int height = 90;

                // 使用 Thumbnailator 库生成缩略图
                try {
                    Thumbnails.of(path)
                            .size(width, height)
                            .toFile(fileZipPath);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

}
