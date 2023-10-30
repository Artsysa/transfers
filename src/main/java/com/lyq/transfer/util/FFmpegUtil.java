package com.lyq.transfer.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.imageio.ImageIO;
import org.bytedeco.ffmpeg.global.avcodec;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;

public class FFmpegUtil {
    /**
     * @throws
     * @Title: getTempPath
     * @Description: 生成视频的首帧图片方法
     * @author: Zing
     * @param: @param tempPath 生成首帧图片的文件地址
     * @param: @param filePath 传进来的线上文件
     * @param: @return
     * @param: @throws Exception
     * @return: boolean
     */
    public static Video getTempPath(File targetFile, File file2) throws Exception {
        Video video = new Video(false,"00:00:00:00");
        if (!targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }
        //判断文件是否为视频
        if (isVideo(file2.getAbsolutePath())) {
            System.out.println("确认成功！");
            //判断文件是否存在
            if (file2.getParentFile().exists()) {
                FFmpegFrameGrabber ff = new FFmpegFrameGrabber(file2);
                //获取时长
                video.setDuration(durationlFormat(ff));
                //转缩略图
                video.setGetThumbnail(getThumbnail(file2, targetFile));
                ff.close();
            } else {
            }
        } else {
        }
        return video;
    }

    public static void zipVido(String videoPath, String thumbnailPath){
        try {
            // 构建 FFmpeg 命令
            String command = "ffmpeg -i " + videoPath + " -ss 00:00:05 -vframes 1 -s 320x240 " + thumbnailPath;

            // 执行命令
            Process process = Runtime.getRuntime().exec(command);

            // 获取命令执行的输出结果
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            // 等待命令执行完成
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                System.out.println("缩略图生成成功！");
            } else {
                System.out.println("缩略图生成失败！");
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
    public static Boolean getThumbnail(File sourceFile, File targetFile) throws FrameGrabber.Exception {
        FFmpegFrameGrabber ff = null;
        Frame frame = null;
        try {
            ff = new FFmpegFrameGrabber(sourceFile);
            ff.setVideoCodec(avcodec.AV_CODEC_ID_MJPEG);
            ff.start();
            //转缩略图
            int length = ff.getLengthInFrames();
            int i = 0;
            while (i < length) {
                // 过滤前20帧，避免出现全黑的图片（）
                frame = ff.grabFrame().clone();
                if ((i > 20) && (frame.image != null)) {
                    break;
                }
                i++;
            }
            int tmpWidth = frame.imageWidth;
            int tmpHeight = frame.imageHeight;
            // 对截取的帧进行等比例缩放
            int width = 300;
            int height = (int) (((double) width / tmpWidth) * tmpHeight );
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            BufferedImage renderedImage = new Java2DFrameConverter().getBufferedImage(frame);
            bi.getGraphics().drawImage(renderedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            if (ImageIO.write(bi, "png", targetFile)) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ff != null) {
                try {
                    ff.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (frame != null) {
                frame.close();
            }
        }
    }




    /**
     * @throws
     * @Title: isVideo
     * @Description:判断是不是视频
     * @author: Zing
     * @param: @param path 文件路径
     * @param: @return
     * @return: boolean       true是视频 false非视频
     */
    public static boolean isVideo(String path) {
        //设置视频后缀
        List<String> typeList = new ArrayList<>();
        typeList.add("mp4");
        typeList.add("flv");
        typeList.add("avi");
        typeList.add("rmvb");
        typeList.add("rm");
        typeList.add("wmv");
        //获取文件名和后缀
        String suffix = path.substring(path.lastIndexOf(".") + 1);
        for (String type : typeList) {
            //统一为大写作比较
            if (type.toUpperCase().equals(suffix.toUpperCase())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 格式化时长
     * @return 00:00:00:00
     */
    private static String durationlFormat(FFmpegFrameGrabber ff) throws FrameGrabber.Exception {
        try{
            ff.start();
            long duration = ff.getLengthInTime() / 1000;
            ff.close();
            return String.format("%02d:%02d:%02d",
                    TimeUnit.MILLISECONDS.toHours(duration),
                    TimeUnit.MILLISECONDS.toMinutes(duration) % TimeUnit.HOURS.toMinutes(1),
                    TimeUnit.MILLISECONDS.toSeconds(duration) % TimeUnit.MINUTES.toSeconds(1));
        }catch (Exception e){
            throw e;
        }finally {
            ff.close();
        }
    }

    public static class Video{
        private Boolean getThumbnail;
        private String duration;

        public Video(Boolean getThumbnail, String duration) {
            this.getThumbnail = getThumbnail;
            this.duration = duration;
        }

        public Boolean getGetThumbnail() {
            return getThumbnail;
        }

        public void setGetThumbnail(Boolean getThumbnail) {
            this.getThumbnail = getThumbnail;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

    }
}
