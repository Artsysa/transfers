package com.lyq.transfer.controller;

import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.index.IndexElement;
import com.lyq.transfer.index.IndexService;
import com.lyq.transfer.util.FFmpegUtil;
import com.lyq.transfer.util.VideoThumbnailGenerator;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.coobird.thumbnailator.Thumbnails;
import org.jcodec.common.io.IOUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/store")
public class FileController {

    static Map<String, List<IndexElement>> localIndexElemetnList = null;


    static {
        localIndexElemetnList =  IndexService.getIndexElementWapper().getIndexElementList().stream().collect(
                Collectors.groupingBy(IndexElement::getMD5));
    }

    @RequestMapping(value = "/{md5}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getFile(@PathVariable("md5") String md5) throws IOException {

        IndexElement indexElement = localIndexElemetnList.get(md5).get(0);
        InputStream inputStream = new FileInputStream(indexElement.getPath());
        byte[] bytes = IOUtils.toByteArray(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
    @RequestMapping(value = "/zip/{md5}", produces = MediaType.IMAGE_JPEG_VALUE)
    public ResponseEntity<byte[]> getZipFile(@PathVariable("md5") String md5) throws IOException {

        IndexElement indexElement = localIndexElemetnList.get(md5).get(0);
        File file = null;
        if(FFmpegUtil.isVideo(indexElement.getPath())){
            String fileZipPath = CommonConsts.cacheDir + "/" + indexElement.getFileName().substring(0, indexElement.getFileName().lastIndexOf(".") + 1) +"jpg";
            file = new File(fileZipPath);
            if(!file.exists()){
                VideoThumbnailGenerator.generateThumbnails(indexElement.getPath(), fileZipPath, 100, 100, 10.0);
            }
        }else{
            String fileZipPath = CommonConsts.cacheDir + "/" + indexElement.getFileName();
            file = new File(fileZipPath);
            if(!file.exists()) {
                file.createNewFile();
                int width = 100;
                int height = 90;

                // 使用 Thumbnailator 库生成缩略图
                Thumbnails.of(indexElement.getPath())
                        .size(width, height)
                        .toFile(fileZipPath);
            }
        }

        InputStream inputStream = new FileInputStream(file);
        byte[] bytes = IOUtils.toByteArray(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        headers.setContentLength(bytes.length);
        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }
}
