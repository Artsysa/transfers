package com.lyq.transfer.util;

import cn.hutool.core.collection.CollectionUtil;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lyq.transfer.constant.CommonConsts;
import org.apache.tomcat.util.buf.HexUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class FileUtil {
    private static MessageDigest digest;

    static {
        try {
            digest = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            //
        }
    }


    public static String calculateMD5(String filePath)  {
        byte[] buffer = new byte[8192];

        try (FileInputStream fis = new FileInputStream(filePath)) {
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                FileUtil.digest.update(buffer, 0, bytesRead);
            }
        }catch (Exception e){
            //
        }

        byte[] digest = FileUtil.digest.digest();
        return HexUtils.toHexString(digest);
    }

    public static List<File> getAllFile(){
        List<File> fileList = Lists.newArrayList();

        File oneplusFile = new File(CommonConsts.oneplusDir);
        if(oneplusFile.isDirectory()){
            CollectionUtil.addAll(fileList, oneplusFile.listFiles());
        }

        File rootFile = new File(CommonConsts.rootDir);
        if(rootFile.isDirectory()){
            CollectionUtil.addAll(fileList, rootFile.listFiles());
        }

        File tempFile = new File(CommonConsts.tempDir);
        if(tempFile.isDirectory()){
            CollectionUtil.addAll(fileList, tempFile.listFiles());
        }

        return fileList.stream().filter(file -> !Objects.equals(file.getName(), ".DS_Store")).collect(Collectors.toList());
    }

    public static Object getObjectFromFile(String path, Class<?> cla){
        File file = new File(path);
        char[] b = new char[(int) file.length()];
        try(FileReader fileReader = new FileReader(file)){
            fileReader.read(b);
        } catch (IOException e) {
            //
        }
        return JSON.parseObject(new String(b), cla);
    }

    public static void writeFile(String path, Object object) throws IOException {
        File file = new File(path);
        file.createNewFile();
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(JSON.toJSONString(object));
        fileWriter.close();
    }

    public static void saveFile(){

    }

    public static void mkdirParent(File file){
        if(!file.exists()){
            file.mkdirs();
        }
    }

    public static void mkdirFile(File file){
        try {
            file.createNewFile();
        } catch (IOException e) {
            //
        }
    }
}
