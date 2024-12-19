package com.qimin.maker.generator.file;

import com.qimin.maker.model.DataModel;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

/**
 * 核心生成器
 */
public class FileGenerator {

    /**
     * 生成
     *
     * @param model 数据模型
     * @throws TemplateException
     * @throws IOException
     */
    public static void doGenerate(DataModel model) throws Exception {
        String projectPath = System.getProperty("user.dir");
        // 整个项目的根路径
        File parentFile = new File(projectPath).getParentFile();
        // 输入路径
        String inputPath = new File(parentFile, "qimin-generator-demo-projects/acm-template").getAbsolutePath();
        String outputPath = projectPath;
        // 生成静态文件
        StaticFileGenerator.copyFilesByHutool(inputPath, outputPath);
        // 生成动态文件
        String inputDynamicFilePath = projectPath + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String outputDynamicFilePath = outputPath + File.separator + "acm-template/src/com/qimin/acm/MainTemplate.java";
        DynamicFileGenerator.doGenerate(inputDynamicFilePath, outputDynamicFilePath, model);

        boolean needGit = model.isNeedGit();
        if(needGit){
            inputPath = new File(inputPath,".gitignore").getAbsolutePath();
            outputPath = new File(outputPath,".gitignore").getAbsolutePath();
            StaticFileGenerator.copyFilesByHutool(inputPath,outputPath);
        }

    }
}

