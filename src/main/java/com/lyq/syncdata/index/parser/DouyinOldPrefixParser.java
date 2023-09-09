package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * created by lyq
 */
public class DouyinOldPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        DouyinOldPrefixParser weChatPrefixParser = new DouyinOldPrefixParser();
        System.out.println(weChatPrefixParser.parser("TG-2022-04-26-231931129"));
    }

    @Override
    public long parser(String fileName) {
        int DouyinOldPrefixLength = FileNamePrefixConsts.douyin_old_prefix.length();
        String year = fileName.substring(DouyinOldPrefixLength, DouyinOldPrefixLength + 4);
        String month = fileName.substring(DouyinOldPrefixLength + 4 + 1, DouyinOldPrefixLength + 4 + 1 + 2);
        String day = fileName.substring(DouyinOldPrefixLength + 4 + 1 + 2 + 1, DouyinOldPrefixLength + 4 + 1 + 2 + 1 + 2);
        String timeData = fileName.substring(DouyinOldPrefixLength + 4 + 1 + 2 + 1 + 2 + 1, DouyinOldPrefixLength + 4 + 1 + 2 + 1 + 2 + 1 + 6);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(StringUtils.join(year, month, day, timeData));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.douyin_old_prefix);
    }
}
