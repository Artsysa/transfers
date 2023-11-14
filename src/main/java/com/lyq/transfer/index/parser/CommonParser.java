package com.lyq.transfer.index.parser;

import com.lyq.transfer.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * created by lyq
 */
public class CommonParser implements FileNameParser {

    public static void main(String[] args) {
        CommonParser weChatPrefixParser = new CommonParser();
        System.out.println(weChatPrefixParser.parser("2022_11_09_14_38_01_B3150958"));
        System.out.println(weChatPrefixParser.parser("20211009_23075109_avatar"));
        System.out.println(weChatPrefixParser.parser("1607236460269"));
        System.out.println(weChatPrefixParser.parser("165089276578855692217050540adbf1bb407161c5e742.jpg"));
    }

    @Override
    public long parser(String fileName) {
        if(fileName.length() == 13){
            return Long.parseLong(fileName);
        }
        if(fileName.contains("_") || fileName.contains("-")){
            String subFileNameStr = fileName.substring(0, 8);
            if(subFileNameStr.contains("_") || subFileNameStr.contains("-")){
                String year = fileName.substring(0, 4);
                String month = fileName.substring(4 + 1, 4 + 1 + 2);
                String day = fileName.substring( 4 + 1 + 2 + 1,  4 + 1 + 2 + 1 + 2);
                String hours = fileName.substring( 4 + 1 + 2 + 1 + 2 + 1,  4 + 1 + 2 + 1 + 2 + 1 + 2);
                String minutes = fileName.substring( 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1,  4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2);
                String seconds = fileName.substring( 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2 + 1,  4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2);
                return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(StringUtils.join(year, month, day, hours, minutes, seconds));
            }else{
                String timeDataStr = fileName.substring(8 + 1,  8 + 1 + 6);
                return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(StringUtils.join(subFileNameStr, timeDataStr));
            }
        }

        return Long.parseLong(fileName.substring(0, 13));
    }

    @Override
    public boolean match(String fileName) {
        return false;
    }
}
