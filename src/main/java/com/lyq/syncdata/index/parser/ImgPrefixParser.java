package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;

/**
 * created by lyq
 */
public class ImgPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        ImgPrefixParser weChatPrefixParser = new ImgPrefixParser();
        System.out.println(weChatPrefixParser.parser("IMG20220702195627"));
    }

    @Override
    public long parser(String fileName) {
        int imgPrefixLenght = FileNamePrefixConsts.img_prefix.length();
        String data = fileName.substring(imgPrefixLenght);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(data);
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.img_prefix);
    }
}