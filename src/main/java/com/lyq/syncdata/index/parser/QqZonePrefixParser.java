package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;

/**
 * created by lyq
 */
public class QqZonePrefixParser implements FileNameParser{

    public static void main(String[] args) {
        QqZonePrefixParser weChatPrefixParser = new QqZonePrefixParser();
        System.out.println(weChatPrefixParser.parser("QQ空间视频_20191029113033"));
    }

    @Override
    public long parser(String fileName) {
        int QqZonePrefixLenght = FileNamePrefixConsts.qq_zone_prefix.length();
        String data = fileName.substring(QqZonePrefixLenght);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(data);
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.qq_zone_prefix);
    }
}
