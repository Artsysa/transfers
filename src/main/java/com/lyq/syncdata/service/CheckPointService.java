package com.lyq.syncdata.service;

import com.lyq.syncdata.netty.SaveDataProcessor;
import com.lyq.syncdata.pojo.CheckPointFileInfo;
import com.lyq.syncdata.pojo.FileInfo;
import com.lyq.syncdata.pojo.Progress;
import javafx.util.Pair;

import java.io.File;
import java.util.*;
import java.util.concurrent.RecursiveTask;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * created by lyq
 */
public class CheckPointService {

    public static List<CheckPointFileInfo> getNeedUploadFiles(Progress progress, List<String> dataPath){
        List<CheckPointFileInfo> needSaveDataPath = new ArrayList<>();

        Map<String, Pair<String, Integer>> localFilePathMap = new HashMap<>(dataPath.size());
        for (int i = 0; i < dataPath.size(); i++) {
            String path = dataPath.get(i);
            localFilePathMap.put(path.substring(path.lastIndexOf("/") + 1), new Pair<>(path, i));
        }

        Map<String, FileInfo> fileInfoMap = progress.getFileName();
        if(fileInfoMap == null){
            fileInfoMap = new HashMap<>();
        }
        for (Map.Entry<String, Pair<String, Integer>> entry : localFilePathMap.entrySet()) {
            String localFileName = entry.getKey();
            String path = entry.getValue().getKey();
            FileInfo fileInfo = fileInfoMap.get(localFileName);
            //缺失整个文件
            if(fileInfo == null){
                CheckPointFileInfo checkPointFileInfo = new CheckPointFileInfo();
                checkPointFileInfo.setPath(path);
                checkPointFileInfo.setCompleteFile(true);
                needSaveDataPath.add(checkPointFileInfo);
                continue;
            }

            Long fileSize = fileInfo.getFileSize();
            File file = new File(path);
            //部分文件部分分片还未上传完毕
            if(fileSize < file.length()){
                CheckPointFileInfo checkPointFileInfo = new CheckPointFileInfo();
                checkPointFileInfo.setPath(path);
                checkPointFileInfo.setStartIndex(fileSize);
                checkPointFileInfo.setCompleteFile(false);
                needSaveDataPath.add(checkPointFileInfo);
            }

        }

        return needSaveDataPath;
    }

    public Progress getNewProgress(){
        File storeRootDir = new File(SaveDataProcessor.getRootDir());
        Progress progress = new Progress();
        if(storeRootDir.exists() && storeRootDir.isDirectory()){
            List<FileInfo> fileInfos = Optional.of(storeRootDir).map(File::listFiles).map((files) -> Arrays.stream(files).map(file -> {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setFileName(file.getName());
                fileInfo.setFileSize(file.length());
                return  fileInfo;
            }).collect(Collectors.toList())).orElse(new ArrayList<>());

            if(fileInfos.size() > 0){
                progress.setFileName(fileInfos.stream().collect(Collectors.toMap(FileInfo::getFileName, Function.identity())));
            }
        }
        return progress;
    }

    static class CheckPointTastk extends RecursiveTask<List<FileInfo>>{
        int partition = 30;
        File[] files;
        int startIndex;
        int endIndex;
        public CheckPointTastk(File[] files, int startIndex, int endIndex) {
            this.files = files;
            this.startIndex = startIndex;
            this.endIndex = endIndex;
        }

        @Override
        protected List<FileInfo> compute() {
            if((endIndex - startIndex) <= partition){
                List<FileInfo> fileInfos = new ArrayList<>();
                for(int i = startIndex; i <endIndex; i++){
                    File file = files[i];
                    FileInfo fileInfo = new FileInfo();
                    fileInfo.setFileName(file.getName());
                    fileInfo.setFileSize(file.length());
                    fileInfos.add(fileInfo);
                }
                return fileInfos;
            }

            int mid = files.length / 2;

            CheckPointTastk leftTask = new CheckPointTastk(files, startIndex, mid);
            CheckPointTastk rightTask = new CheckPointTastk(files, mid + 1, endIndex);

            leftTask.fork();
            rightTask.fork();

            List<FileInfo> resultLeft = leftTask.join();
            List<FileInfo> resultRight = rightTask.join();
            resultLeft.addAll(resultRight);
            return resultLeft;
        }
    }

}
