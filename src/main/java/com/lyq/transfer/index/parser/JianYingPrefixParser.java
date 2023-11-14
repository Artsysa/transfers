package com.lyq.transfer.index.parser;

import com.lyq.transfer.constant.FileNamePrefixConsts;
import com.lyq.transfer.util.TimeUtil;

/**
 * created by lyq
 */
public class JianYingPrefixParser implements FileNameParser {

    public static void main(String[] args) {
        JianYingPrefixParser weChatPrefixParser = new JianYingPrefixParser();
        System.out.println(weChatPrefixParser.parser("lv_6897879829350305036_20210101164836"));
    }

    @Override
    public long parser(String fileName) {
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(fileName.substring(fileName.lastIndexOf("_") + 1));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.jianying_prefix);
    }
}
