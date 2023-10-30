package com.lyq.transfer.util;

import java.io.IOException;

public class VideoThumbnailGenerator {

    public static void generateThumbnails(String videoPath, String thumbnailPath, int thumbnailWidth, int thumbnailHeight, double thumbnailInterval) {

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "/Users/lyq/Downloads/ffmpeg",
                    "-i", videoPath,
                    "-vf", "fps=1/" + thumbnailInterval + ",scale=" + thumbnailWidth + ":" + thumbnailHeight,
                    thumbnailPath
            );
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}