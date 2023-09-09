package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;

/**
 * created by lyq
 */
public class JianYing0PrefixParser implements FileNameParser{

    public static void main(String[] args) {
        JianYing0PrefixParser weChatPrefixParser = new JianYing0PrefixParser();
        System.out.println(weChatPrefixParser.parser("lv_0_20211024202134"));
    }

    @Override
    public long parser(String fileName) {
        int JianYing0PrefixLenght = FileNamePrefixConsts.jianying_0_prefix.length();
        String data = fileName.substring(JianYing0PrefixLenght);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(data);
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.jianying_0_prefix);
    }
}
