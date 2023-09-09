package com.lyq.transfer.index.parser;

import com.lyq.transfer.constant.FileNamePrefixConsts;

/**
 * created by lyq
 */
public class ImgMinPrefix implements FileNameParser {

    public static void main(String[] args) {
        ImgMinPrefix weChatPrefixParser = new ImgMinPrefix();
        System.out.println(weChatPrefixParser.parser("img-1649120529288d22df57cc9ffafa8f29f5adcb6339b9e830d7ed36fcc4bba7c30c0362c85c8fd.jpg"));
    }

    @Override
    public long parser(String fileName) {
        return Long.parseLong(fileName.substring(FileNamePrefixConsts.img_min_prefix.length(), FileNamePrefixConsts.img_min_prefix.length() + 13));
    }

    @Override
    public boolean match(String fileName) {
        return fileName.startsWith(FileNamePrefixConsts.img_min_prefix);
    }
}
