package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;

/**
 * created by lyq
 */
public class WeChatPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        WeChatPrefixParser weChatPrefixParser = new WeChatPrefixParser();
        System.out.println(weChatPrefixParser.parser("wx_camera_1688828871283"));
    }

    @Override
    public long parser(String fileName) {
        return Long.parseLong(fileName.substring(FileNamePrefixConsts.wx_prefix.length()));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.wx_prefix);
    }
}
