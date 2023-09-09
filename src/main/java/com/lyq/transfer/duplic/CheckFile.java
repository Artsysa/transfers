package com.lyq.transfer.duplic;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.lyq.transfer.constant.CommonConsts;
import com.lyq.transfer.netty.service.CommonThreadService;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.util.List;

/**
 * created by lyq
 */
public class CheckFile {

    public static void main(String[] args) {
        List<String> check = check();
        System.out.println(JSON.toJSONString(check));
    }

    public static List<String> check(){
        File tempFile = new File(CommonConsts.tempDir);
        File[] files = tempFile.listFiles();
        List<String> fileNameList = Lists.newArrayList();

        File duplicDir = new File(CommonConsts.duplicDir);
        if(!duplicDir.exists()){
            duplicDir.mkdirs();
        }

        for (File file : files) {
            if(file.getName().contains(".mp4")){
                continue;
            }
            CommonThreadService.submitTask(() ->{
                if(!isImageCorrupted(file)){
                    DuplicateService.transfer(file.getAbsolutePath(), CommonConsts.duplicDir + "/" + file.getName());
                }
            });
        }
        return fileNameList;

    }

    public static boolean isImageCorrupted(File file) {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 读取图像文件
        Mat image = Imgcodecs.imread(file.getAbsolutePath());
        return !image.empty();
    }

    private static boolean isImageValid1(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();

        // 创建一个新的BufferedImage对象来防止修改原始图像
        BufferedImage copyImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        copyImage.getGraphics().drawImage(image, 0, 0, null);

        // 尝试解码图像数据
        Image decodedImage = new ImageIcon(copyImage).getImage();

        // 校验解码后的图像内容
        if (decodedImage.getWidth(null) < 0 || decodedImage.getHeight(null) < 0) {
            return false;
        }

        return true;
    }

    private static boolean isImageValid(BufferedImage image) {
        int width = image.getWidth();
        int height = image.getHeight();
        int[] pixels = new int[width * height];

        // 通过PixelGrabber获取图像的像素数据
        PixelGrabber pg = new PixelGrabber(image, 0, 0, width, height, pixels, 0, width);

        try {
            pg.grabPixels();
        } catch (InterruptedException e) {
            return false;
        }

        // 检查图像的每个像素值，如果有非空像素，则说明图像有效
        for (int pixel : pixels) {
            if ((pixel >> 24) != 0x00) {
                return true;
            }
        }

        return false;
    }

}
