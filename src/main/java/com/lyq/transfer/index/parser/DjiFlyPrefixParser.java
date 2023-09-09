package com.lyq.transfer.index.parser;

import com.lyq.syncdata.util.TimeUtil;
import com.lyq.transfer.constant.FileNamePrefixConsts;
import org.apache.commons.lang3.StringUtils;

/**
 * created by lyq
 */
public class DjiFlyPrefixParser implements FileNameParser {

    public static void main(String[] args) {
        DjiFlyPrefixParser weChatPrefixParser = new DjiFlyPrefixParser();
        System.out.println(weChatPrefixParser.parser("dji_fly_20230610_133814_542_1687079173583_photo_optimized"));
    }

    @Override
    public long parser(String fileName) {
        int djiFlyPrefixLength = FileNamePrefixConsts.dji_fly_prefix.length();
        String simpleData = fileName.substring(djiFlyPrefixLength, djiFlyPrefixLength + 8);
        String timeData = fileName.substring(djiFlyPrefixLength + 8 + 1, djiFlyPrefixLength + 8 + 1 + 6);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(StringUtils.join(simpleData, timeData));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.dji_fly_prefix);
    }
}
