package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * created by lyq
 */
public class ImgOldPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        ImgOldPrefixParser weChatPrefixParser = new ImgOldPrefixParser();
        System.out.println(weChatPrefixParser.parser("IMG_20220614_200928"));
    }

    @Override
    public long parser(String fileName) {
        int imgOldPrefixLength = FileNamePrefixConsts.img_old_prefix.length();
        String simpleData = fileName.substring(imgOldPrefixLength, imgOldPrefixLength + 8);
        String timeData = fileName.substring(imgOldPrefixLength + 8 + 1, imgOldPrefixLength + 8 + 1 + 6);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(StringUtils.join(simpleData, timeData));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.img_old_prefix);
    }
}
