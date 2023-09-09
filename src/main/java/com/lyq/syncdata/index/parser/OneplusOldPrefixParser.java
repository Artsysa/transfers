package com.lyq.syncdata.index.parser;

import com.lyq.syncdata.index.consts.FileNamePrefixConsts;
import com.lyq.syncdata.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * created by lyq
 */
public class OneplusOldPrefixParser implements FileNameParser{

    public static void main(String[] args) {
        OneplusOldPrefixParser weChatPrefixParser = new OneplusOldPrefixParser();
        System.out.println(weChatPrefixParser.parser("VID_20200923_120142"));
        System.out.println(weChatPrefixParser.parser("VID_20220413221952"));
    }

    @Override
    public long parser(String fileName) {
        int oneplusOldPrefixLength = FileNamePrefixConsts.oneplus_old_prefix.length();
        if(fileName.length() == 19){
            String simpleData = fileName.substring(oneplusOldPrefixLength, oneplusOldPrefixLength + 8);
            String timeData = fileName.substring(oneplusOldPrefixLength + 8 + 1, oneplusOldPrefixLength + 8 + 1 + 6);
            return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(StringUtils.join(simpleData, timeData));
        }
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(fileName.substring(oneplusOldPrefixLength));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.oneplus_old_prefix);
    }
}
