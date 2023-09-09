package com.lyq.transfer.index.parser;

import com.lyq.transfer.constant.FileNamePrefixConsts;

/**
 * created by lyq
 */
public class WeChatMmexportPrefixParser implements FileNameParser {

    public static void main(String[] args) {
        WeChatMmexportPrefixParser weChatPrefixParser = new WeChatMmexportPrefixParser();
        System.out.println(weChatPrefixParser.parser("mmexport1688368200515"));
    }

    @Override
    public long parser(String fileName) {
        return Long.parseLong(fileName.substring(FileNamePrefixConsts.wx_mmexport_prefix.length()));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.wx_mmexport_prefix);
    }
}
