package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * created by lyq
 */
public class OneplusRecordPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        OneplusRecordPrefixParser weChatPrefixParser = new OneplusRecordPrefixParser();
        System.out.println(weChatPrefixParser.parser("Record_2023-07-06-11-59-42"));
    }

    @Override
    public long parser(String fileName) {
        int OneplusRecordPrefixParserLenght = FileNamePrefixConsts.oneplus_record_prefix.length();
        String year = fileName.substring(OneplusRecordPrefixParserLenght, OneplusRecordPrefixParserLenght + 4);
        String month = fileName.substring(OneplusRecordPrefixParserLenght + 4 + 1, OneplusRecordPrefixParserLenght + 4 + 1 + 2);
        String day = fileName.substring(OneplusRecordPrefixParserLenght + 4 + 1 + 2 + 1, OneplusRecordPrefixParserLenght + 4 + 1 + 2 + 1 + 2);
        String hours = fileName.substring(OneplusRecordPrefixParserLenght + 4 + 1 + 2 + 1 + 2 + 1, OneplusRecordPrefixParserLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2);
        String minutes = fileName.substring(OneplusRecordPrefixParserLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1, OneplusRecordPrefixParserLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2);
        String seconds = fileName.substring(OneplusRecordPrefixParserLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2 + 1, OneplusRecordPrefixParserLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(StringUtils.join(year, month, day, hours, minutes, seconds));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.oneplus_record_prefix);
    }
}
