package com.lyq.transfer.index.parser;

import com.lyq.transfer.constant.FileNamePrefixConsts;
import com.lyq.transfer.util.TimeUtil;
import org.apache.commons.lang3.StringUtils;

/**
 * created by lyq
 */
public class OneplusScreenshotOldPrefixParser implements FileNameParser {

    public static void main(String[] args) {
        OneplusScreenshotOldPrefixParser weChatPrefixParser = new OneplusScreenshotOldPrefixParser();
        System.out.println(weChatPrefixParser.parser("Screenshot_2022-04-19-22-28-42-33"));
        System.out.println(weChatPrefixParser.parser("Screenshot_20220103_211852_com.ss.android.ugc.aweme"));
    }

    @Override
    public long parser(String fileName) {
        int oneplusScreenshotOldPrefixLenght = FileNamePrefixConsts.oneplus_screenshot_old_prefix.length();
        String simpleDataStr = fileName.substring(oneplusScreenshotOldPrefixLenght, oneplusScreenshotOldPrefixLenght + 8);
        String dataStr = null;
        if(simpleDataStr.contains("-")){
            String year = fileName.substring(oneplusScreenshotOldPrefixLenght, oneplusScreenshotOldPrefixLenght + 4);
            String month = fileName.substring(oneplusScreenshotOldPrefixLenght + 4 + 1, oneplusScreenshotOldPrefixLenght + 4 + 1 + 2);
            String day = fileName.substring(oneplusScreenshotOldPrefixLenght + 4 + 1 + 2 + 1, oneplusScreenshotOldPrefixLenght + 4 + 1 + 2 + 1 + 2);
            String hours = fileName.substring(oneplusScreenshotOldPrefixLenght + 4 + 1 + 2 + 1 + 2 + 1, oneplusScreenshotOldPrefixLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2);
            String minutes = fileName.substring(oneplusScreenshotOldPrefixLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1, oneplusScreenshotOldPrefixLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2);
            String seconds = fileName.substring(oneplusScreenshotOldPrefixLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2 + 1, oneplusScreenshotOldPrefixLenght + 4 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2 + 1 + 2);
            dataStr = StringUtils.join(year, month, day, hours, minutes, seconds);
        }else{
            dataStr = simpleDataStr;
            String timeDataStr = fileName.substring(oneplusScreenshotOldPrefixLenght + 8 + 1, oneplusScreenshotOldPrefixLenght + 8 + 1 + 6);
            dataStr = StringUtils.join(dataStr, timeDataStr);
        }
        return TimeUtil.yyyyMMddHHmmssSimple2TimeStamp(dataStr);
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.oneplus_screenshot_old_prefix);
    }
}
