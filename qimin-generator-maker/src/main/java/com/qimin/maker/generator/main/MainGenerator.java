package com.qimin.maker.generator.main;

/**
 * 生成代码生成器
 */
public class MainGenerator extends GenerateTemplate{

    @Override
    protected String buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath){
        return "不用再生成Dist版";
    }

}
