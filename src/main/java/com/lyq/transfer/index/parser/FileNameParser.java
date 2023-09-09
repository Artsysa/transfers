package com.lyq.transfer.index.parser;

/**
 * created by lyq
 */
public interface FileNameParser {

    long parser(String fileName);

    boolean match(String fileName);
}
