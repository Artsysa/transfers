package com.lyq.syncdata.index.parser;

/**
 * created by lyq
 */
public interface FileNameParser {

    long parser(String fileName);

    boolean match(String fileName);
}
