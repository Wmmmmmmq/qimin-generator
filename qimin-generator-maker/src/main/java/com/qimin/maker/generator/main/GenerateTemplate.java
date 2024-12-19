package com.qimin.maker.generator.main;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.StrUtil;
import com.qimin.maker.generator.JarGenerator;
import com.qimin.maker.generator.ScriptGenerator;
import com.qimin.maker.generator.file.DynamicFileGenerator;
import com.qimin.maker.meta.Meta;
import com.qimin.maker.meta.MetaManager;
import freemarker.template.TemplateException;

import java.io.File;
import java.io.IOException;

public abstract class GenerateTemplate {

    public void doGenerate() throws TemplateException, IOException, InterruptedException {
        Meta meta = MetaManager.getObjectMeta();

        //输出的根路径
        String projectPath = System.getProperty("user.dir");
        String outputPath = projectPath + File.separator + "generated" + File.separator + meta.getName();
        if (!FileUtil.exist(outputPath)) {
            FileUtil.mkdir(outputPath);
        }

        //1.复制原始文件
        String sourceCopyDestPath = copySource(meta, outputPath);

        //2.代码生成
        generateCode(meta, outputPath);

        //3.构建jar包
        String jarPath = buildJar(outputPath, meta);

        //4.封装脚本
        String shellOutputFilePath = shellOutputFilePath(outputPath, jarPath);

        //5.生成精简版的程序
        buildDist(outputPath, sourceCopyDestPath, jarPath, shellOutputFilePath);
    }


    /**
     * 生成精简版的程序
     * @param outputPath
     * @param sourceCopyDestPath
     * @param jarPath
     * @param shellOutputFilePath
     */
    protected  void buildDist(String outputPath,String sourceCopyDestPath, String jarPath, String shellOutputFilePath) {
        // 生成精简版的程序（产物包）
        String distOutputPath = outputPath + "-dist";
        // - 拷贝 jar 包
        String targetAbsolutePath = distOutputPath + File.separator + "target";
        FileUtil.mkdir(targetAbsolutePath);
        String jarAbsolutePath = outputPath + File.separator + jarPath;
        FileUtil.copy(jarAbsolutePath, targetAbsolutePath, true);
        // - 拷贝脚本文件
        FileUtil.copy(shellOutputFilePath, distOutputPath, true);
        // - 拷贝源模板文件
        FileUtil.copy(sourceCopyDestPath, distOutputPath, true);
    }

    /**
     * 封装脚本
     * @param outputPath
     * @param jarPath
     * @return
     */
    protected  String shellOutputFilePath(String outputPath, String jarPath) {
        String shellOutputFilePath = outputPath + File.separator + "generator";
        ScriptGenerator.doGenerate(shellOutputFilePath, jarPath);
        return shellOutputFilePath;
    }

    /**
     * 构建jar包
     * @param outputPath
     * @param meta
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    protected  String buildJar(String outputPath, Meta meta) throws IOException, InterruptedException {
        JarGenerator.doGenerator(outputPath);
        String jarName = String.format("%s-%s-jar-with-dependencies.jar", meta.getName(), meta.getVersion());
        String jarPath = "target/" + jarName;
        return jarPath;
    }

    /**
     * 代码生成
     * @param meta
     * @param outputPath
     * @throws IOException
     * @throws TemplateException
     */
    protected  void generateCode(Meta meta, String outputPath) throws IOException, TemplateException {
        //读取 resources 目录
        ClassPathResource classPathResource = new ClassPathResource("");
        String inputResourcePath = classPathResource.getAbsolutePath();

        //java包的基础路径
        //com.qimin
        String outputBasePackage = meta.getBasePackage();
        //com/qimin
        String outputBasePackagePath = StrUtil.join("/", StrUtil.split(outputBasePackage, "."));
        //generated/src/main/java/com/qimin
        String outputJavaBasePackagePath = outputPath + File.separator + "src/main/java" + File.separator + outputBasePackagePath;

        String inputFilePath;
        String outputFilePath;
        //model.DataModel
        inputFilePath = inputResourcePath + File.separator + "templates/java/model/DataModel.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/model/DataModel.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //cli.command.ConfigCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ConfigCommand.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/cli/command/ConfigCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //cli.command.GenerateCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/GenerateCommand.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/cli/command/GenerateCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //cli.command.ListCommand
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/command/ListCommand.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/cli/command/ListCommand.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //cli.CommandExecutor
        inputFilePath = inputResourcePath + File.separator + "templates/java/cli/CommandExecutor.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/cli/CommandExecutor.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);


        //Main
        inputFilePath = inputResourcePath + File.separator + "templates/java/Main.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/Main.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //generator.MainGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/MainGenerator.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/generator/file/MainGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //generator.DynamicGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/DynamicGenerator.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/generator/file/DynamicGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        //generator.StaticGenerator
        inputFilePath = inputResourcePath + File.separator + "templates/java/generator/StaticGenerator.java.ftl";
        outputFilePath = outputJavaBasePackagePath + "/generator/file/StaticGenerator.java";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        inputFilePath = inputResourcePath + File.separator + "templates/pom.xml.ftl";
        outputFilePath = outputPath + File.separator + "pom.xml";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);

        inputFilePath = inputResourcePath + File.separator + "templates/README.md.ftl";
        outputFilePath = outputPath + File.separator + "README.md";
        DynamicFileGenerator.doGenerate(inputFilePath, outputFilePath, meta);
    }

    /**
     * 复制原始文件
     * @param meta
     * @param outputPath
     * @return
     */
    protected  String copySource(Meta meta, String outputPath) {
        String sourcesRootPath = meta.getFileConfig().getSourcesRootPath();
        String destPath = outputPath + File.separator + ".source";
        FileUtil.copy(sourcesRootPath, destPath, false);
        return sourcesRootPath;
    }

}
