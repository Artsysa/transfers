package com.lyq.syncdata.duplic;

import com.alibaba.fastjson.JSON;
import com.lyq.syncdata.constant.SyncDataConsts;
import org.apache.tomcat.util.buf.HexUtils;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * created by lyq
 */
public class DuplicateService {

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        Map<String, String> duplicateMap = duplicateFile(SyncDataConsts.oneplusDir);
        Map<String, List<String>> map = new HashMap<>();
        duplicateMap.forEach((key, value) -> {
            List<String> duplicateName = map.get(value);
            if (CollectionUtils.isEmpty(duplicateName)) {
                List<String> list = new ArrayList<>();
                list.add(key);
                map.put(value, list);
            } else {
                duplicateName.add(key);
            }
        });
        System.out.println(JSON.toJSONString(map));
    }

    public static Map<String, String> duplicateFile(String path) throws NoSuchAlgorithmException, IOException {
        Map<String, String> duplicateMap = new HashMap<>();
        Map<String, String> duplicate = new HashMap<>();
        File duplicateDir = new File(path);
        File[] hasDuplicateFile = duplicateDir.listFiles();
        MessageDigest digest = MessageDigest.getInstance("MD5");

        for (File file : hasDuplicateFile) {
            if(file.isDirectory()){
                continue;
            }
            byte[] md5 = digest.digest(Files.readAllBytes(Paths.get(file.getAbsolutePath())));

            String unique = HexUtils.toHexString(md5);

            if(duplicateMap.containsKey(unique)){
                duplicate.put(file.getName(), duplicateMap.get(unique));
                transfer(file.getAbsolutePath(), path + "/duplicate/" + file.getName());
                file.delete();
                continue;
            }

            duplicateMap.put(unique, file.getName());

        }
        return duplicate;
    }

    private static void transfer(String oriPath, String aimPaht){
        File fileAim = new File(aimPaht);
        File fileTemp = new File(oriPath);
        if(!fileAim.exists()){
            try {
                fileAim.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        InputStream is = null;
        OutputStream os = null;
        try {
            is = new FileInputStream(fileTemp);
            os = new FileOutputStream(fileAim);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
