package com.lyq.transfer.constant;

import org.apache.commons.lang3.StringUtils;

import cn.hutool.core.util.IdUtil;

/**
 * created by lyq
 */
public class CommonConsts {

    public static final String baseDir = "/Users/lyq/Downloads/syncData/";

    public static final String oneplusDir = "/Users/lyq/Downloads/未命名文件夹/";

    public static final String rootDir = StringUtils.join(baseDir, "data");

    public static final String indexDir = StringUtils.join(baseDir, "index");

    public static final String indexFile = StringUtils.join(indexDir, "/index.json");

    public static final String tempDir = StringUtils.join(baseDir, "temp");
    public static final String cacheDir = StringUtils.join(baseDir, "cache");
    public static final String duplicDir = StringUtils.join(baseDir, "duplicate");

    public static final Long client_unique = IdUtil.getSnowflakeNextId();

    //单次传输最大数据量1m
    public static final Integer slice_max = 1 << 20;

    public static final String symmetry_key = "1999012211111111";

}
