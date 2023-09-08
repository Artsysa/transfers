package com.lyq.transfer.adapter;

import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.pojo.UploadFile;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class FileUploadAdapter {


    public static UploadFile buildBigFileUpload(File file, RandomAccessFile randomAccessFile, boolean finash, int index) throws IOException {
        UploadFile uploadFile = new UploadFile();
        uploadFile.setFileName(file.getName());
        uploadFile.setWriteIndex(randomAccessFile.getFilePointer());
        if(finash){
            int residueSize = (int) (file.length() - (CommonConsts.slice_max * index));
            uploadFile.setFileContent(getFileContent(randomAccessFile, residueSize));
        }else{
            uploadFile.setFileContent(getFileContent(randomAccessFile));
        }
        uploadFile.setFileSizeType(true);
        uploadFile.setFinish(finash);
        return uploadFile;
    }

    public static UploadFile buildSmallFileUpload(File file) throws IOException {
        UploadFile uploadFile = new UploadFile();
        uploadFile.setFileName(file.getName());
        uploadFile.setFileContent(getFileContent(file));
        uploadFile.setFileSizeType(false);
        uploadFile.setFinish(true);
        return uploadFile;
    }

    public static byte[] getFileContent(RandomAccessFile randomAccessFile) throws IOException {
        return getFileContent(randomAccessFile, CommonConsts.slice_max);
    }

    public static byte[] getFileContent(RandomAccessFile randomAccessFile, Integer size) throws IOException {
        byte[] b = new byte[size];
        randomAccessFile.read(b);
        return b;
    }

    public static byte[] getFileContent(File file) throws IOException {
        try(RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")){
            byte[] b = new byte[(int) file.length()];
            randomAccessFile.read(b);
            randomAccessFile.close();
            return b;
        }
    }
}
