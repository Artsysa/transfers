package com.lyq.syncdata;

import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.lyq.syncdata.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.Collection;

@SpringBootTest
class SyncDataApplicationTests {


    @Test
    void contextLoads() {
      // getRealCreateTime2(new File("/Users/lyq/Downloads/syncData/mmexport1653553988774.jpg"));
//        String rootDir = "/Users/lyq/Downloads/syncData/";
//        File storeRootDir = new File(rootDir);
//        File[] files = storeRootDir.listFiles();
//        List<String> paths = Arrays.stream(files).map(File::getAbsolutePath).filter(path -> !path.contains(".DS_Store"))
//                .sorted((fist, last) -> {
//                    long fistlong = getRealCreateTime(new File(fist));
//                    long lastlong = getRealCreateTime(new File(last));
//                            return Long.compare(fistlong, lastlong);
//                }
//                ).collect(Collectors.toList());
//        for (String path : paths) {
//            File file = new File(path);
//            System.out.println("filename:" + file.getName() + ",time:" +getRealCreateTime1(file));
//        }

    }

    public String getRealCreateTime2(File file){
        Metadata metadata = null;
        String realCreateTime = "";
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            for (Directory next : metadata.getDirectories()) {
                Collection<Tag> tags = next.getTags();
                for (Tag tag : tags) {
                    System.out.println(tag.getTagName() + "," + tag.getDescription());
                }
            }
        } catch (Exception e) {
            System.out.println(file.getAbsolutePath());
            e.printStackTrace();
        }
        return realCreateTime;
    }

    public String getRealCreateTime1(File file){
        Metadata metadata = null;
        String realCreateTime = "";
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            for (Directory next : metadata.getDirectories()) {
                Collection<Tag> tags = next.getTags();
                boolean finash = false;
                for (Tag tag : tags) {
                    if (tag.getTagName().contains("Date/Time")) {
                        realCreateTime = tag.getDescription();
                        finash = true;
                        break;
                    }
                }
                if(finash){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(file.getAbsolutePath());
            e.printStackTrace();
        }
        return realCreateTime;
    }
    public long getRealCreateTime(File file){
        Metadata metadata = null;
        long realCreateTime = 0L;
        try {
            metadata = ImageMetadataReader.readMetadata(file);
            for (Directory next : metadata.getDirectories()) {
                Collection<Tag> tags = next.getTags();
                boolean finash = false;
                for (Tag tag : tags) {
                    if (tag.getTagName().contains("Date/Time") && StringUtils.isNotBlank(tag.getDescription())) {
                        String timeString = tag.getDescription();
                        realCreateTime = TimeUtil.yyyyMMddHHmmss2TimeStamp(timeString);
                        finash = true;
                        break;
                    }
                }
                if(finash){
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println(file.getAbsolutePath());
            e.printStackTrace();
        }
        return realCreateTime;
    }
}
