package com.qimin;


import com.qimin.generator.DynamicGenerator;
import com.qimin.generator.StaticGenerator;
import com.qimin.model.MainTemplateConfig;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws TemplateException, IOException {

        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + File.separator + "qimin-generator-demo-projects" + File.separator + "acm-template";
        String outputPath = projectPath;
        StaticGenerator.copyFileByHutool(inputPath, outputPath);

        String DynamicInputPath = projectPath + File.separator + "qimin-generator-basic" + File.separator + "src/main/resources/templates/MainTemplate.java.ftl";
        String DynamicOutputPath = projectPath + File.separator + "acm-template/src/com/qimin/acm/MainTemplate.java";
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        mainTemplateConfig.setAuthor("qiminmmmmmm");
        mainTemplateConfig.setOutputText("输出结果：");
        mainTemplateConfig.setLoop(false);
        DynamicGenerator.doGenerate(DynamicInputPath, DynamicOutputPath, mainTemplateConfig);

    }
}