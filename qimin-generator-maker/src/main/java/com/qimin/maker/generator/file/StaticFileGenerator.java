package com.qimin.maker.generator.file;

import cn.hutool.core.io.FileUtil;

/**
 * 静态代码生成器
 */
public class StaticFileGenerator {

    /**
     * hutool工具复制文件
     *
     * @param inputPath
     * @param outputPath
     */
    public static void copyFilesByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }
}