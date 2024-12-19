package com.qimin.maker.generator.main;

/**
 * 生成代码生成器
 */
public class MainGenerator extends GenerateTemplate{

    @Override
    protected void buildDist(String outputPath, String sourceCopyDestPath, String jarPath, String shellOutputFilePath){
        System.out.println("不用再生成Dist版");
    }

}
