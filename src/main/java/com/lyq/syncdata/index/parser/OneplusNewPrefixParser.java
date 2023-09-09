package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;

/**
 * created by lyq
 */
public class OneplusNewPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        OneplusNewPrefixParser weChatPrefixParser = new OneplusNewPrefixParser();
        System.out.println(weChatPrefixParser.parser("VID20230622185644"));
    }

    @Override
    public long parser(String fileName) {
        int oneplusNewPrefixLength = FileNamePrefixConsts.oneplus_new_prefix.length();
        String data = fileName.substring(oneplusNewPrefixLength);
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(data);
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.oneplus_new_prefix);
    }
}
