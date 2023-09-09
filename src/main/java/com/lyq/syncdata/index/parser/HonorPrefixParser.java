package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * created by lyq
 */
public class HonorPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        HonorPrefixParser weChatPrefixParser = new HonorPrefixParser();
        System.out.println(weChatPrefixParser.parser("Videoframe_20210808_132621_com.huawei.himovie.jpg"));
    }

    @Override
    public long parser(String fileName) {
        int honorPrefixLength = FileNamePrefixConsts.honor_prefix.length();
        int simpleDate = honorPrefixLength + 8;
        int time = simpleDate + 1;
        String simpleDataStr = fileName.substring(honorPrefixLength, simpleDate);
        String timeStr = fileName.substring(time, time + 6);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(StringUtils.join(simpleDataStr, timeStr));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.honor_prefix);
    }
}
