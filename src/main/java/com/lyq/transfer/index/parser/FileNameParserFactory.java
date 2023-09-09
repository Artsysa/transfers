package com.lyq.transfer.index.parser;

import java.util.ArrayList;
import java.util.List;

/**
 * created by lyq
 */
public class FileNameParserFactory {

    private static List<FileNameParser> fileNameParserList;

    private static final FileNameParser DEFAULT_NAME_PARSER = new CommonParser();

    static {
        fileNameParserList = new ArrayList<>();
        fileNameParserList.add(new WeChatPrefixParser());
        fileNameParserList.add(new WeChatMmexportPrefixParser());
        fileNameParserList.add(new HonorPrefixParser());
        fileNameParserList.add(new OneplusOldPrefixParser());
        fileNameParserList.add(new OneplusNewPrefixParser());
        fileNameParserList.add(new OneplusScreenshotOldPrefixParser());
        fileNameParserList.add(new OneplusRecordPrefixParser());
        fileNameParserList.add(new DouyinOldPrefixParser());
        fileNameParserList.add(new QqZonePrefixParser());
        fileNameParserList.add(new JianYing0PrefixParser());
        fileNameParserList.add(new JianYingPrefixParser());
        fileNameParserList.add(new ImgOldPrefixParser());
        fileNameParserList.add(new ImgPrefixParser());
        fileNameParserList.add(new DjiFlyPrefixParser());
        fileNameParserList.add(new DjiExportPrefixParser());
        fileNameParserList.add(new ImgMinPrefix());
    }
    public FileNameParserFactory() {

    }

    public static FileNameParser obtainParser(String fileName){
        for (FileNameParser fileNameParser : fileNameParserList) {
            if(fileNameParser.match(fileName)){
                return fileNameParser;
            }
        }
        return DEFAULT_NAME_PARSER;
    }

}
