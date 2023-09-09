package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;

/**
 * created by lyq
 */
public class DjiExportPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        DjiExportPrefixParser weChatPrefixParser = new DjiExportPrefixParser();
        System.out.println(weChatPrefixParser.parser("dji_export_1662210550122"));
    }


    @Override
    public long parser(String fileName) {
        return Long.parseLong(fileName.substring(FileNamePrefixConsts.dji_export_prefix.length()));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.dji_export_prefix);
    }
}
