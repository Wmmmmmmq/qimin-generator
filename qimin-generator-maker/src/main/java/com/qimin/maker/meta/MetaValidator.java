package com.qimin.maker.meta;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

/**
 * 元信息校验
 */
public class MetaValidator {

    public static void doValidAndFill(Meta meta) {

        validAndFillMetaRoot(meta);

        validAndFillFileConfig(meta);

        validAndFillModelConfig(meta);
    }

    private static void validAndFillModelConfig(Meta meta) {
        //modelConfig 校验和默认值
        Meta.ModelConfig modelConfig = meta.getModelConfig();

        if (modelConfig == null) {
            return;
        }

        //modelConfig 默认值
        List<Meta.ModelConfig.ModelsInfo> modelsInfoList = modelConfig.getModels();
        if (!CollectionUtil.isNotEmpty(modelsInfoList)) {
            return;
        }

        for (Meta.ModelConfig.ModelsInfo modelInfo : modelsInfoList) {
            //输入路径默认值
            String fieldName = modelInfo.getFieldName();
            if (StrUtil.isEmpty(fieldName)) {
                throw new MetaException("未填写 fieldName");
            }
            String modelInfoType = modelInfo.getType();
            if (StrUtil.isEmpty(modelInfoType)) {
                modelInfo.setType(modelInfoType);
            }
        }

    }

    private static void validAndFillFileConfig(Meta meta) {
        //FileConfig 校验和默认值
        Meta.FileConfig fileConfig = meta.getFileConfig();
        if (fileConfig == null) {
            return;
        }
        //sourceRootPath 必填
        String sourceRootPath = fileConfig.getSourcesRootPath();
        if (StrUtil.isBlank(sourceRootPath)) {
            throw new MetaException("未填写 sourceRootPath");
        }
        //inputRootPath: .source + sourceRootPath 的最后一个层级路径
        String inputRootPath = fileConfig.getInputRootPath();
        String defaultInputRootPath = ".source/" + FileUtil.getLastPathEle(Paths.get(sourceRootPath)).getFileName().toString();
        if (StrUtil.isEmpty(inputRootPath)) {
            fileConfig.setInputRootPath(defaultInputRootPath);
        }
        //outputRootPath 默认设置为当前路径下的generated
        String outputRootPath = fileConfig.getOutputRootPath();
        String defaultOutputRootPath = "generated";
        if (StrUtil.isEmpty(outputRootPath)) {
            fileConfig.setOutputRootPath(defaultOutputRootPath);
        }
        String fileConfigType = fileConfig.getType();
        String defaultType = "dir";
        if (StrUtil.isEmpty(fileConfigType)) {
            fileConfig.setType(defaultType);
        }

        //fileInfo 默认值
        List<Meta.FileConfig.FileInfo> fileInfoList = fileConfig.getFiles();
        if (!CollectionUtil.isNotEmpty(fileInfoList)) {
            return;
        }
        for (Meta.FileConfig.FileInfo fileInfo : fileInfoList) {
            //inputPath 必填
            String inputPath = fileInfo.getInputPath();
            if (StrUtil.isBlank(inputPath)) {
                throw new MetaException("未填写 inputPath");
            }
            //outputPath 默认等于inputPath
            String outputPath = fileInfo.getOutputPath();
            if (StrUtil.isEmpty(outputPath)) {
                fileInfo.setOutputPath(inputPath);
            }
            //type 默认为inputPath有文件后缀（如：.java）为file，否则为dir
            String type = fileInfo.getType();
            if (StrUtil.isBlank(type)) {
                //无文件后缀
                if (StrUtil.isBlank(FileUtil.getSuffix(inputPath))) {
                    fileInfo.setType("dir");
                } else {
                    fileInfo.setType("file");
                }
            }
            //generateType 如果文件结尾不为 Ftl,generateType 默认为static，否则为dynamic
            String generateType = fileInfo.getGenerateType();
            if (StrUtil.isBlank(generateType)) {
                //为动态模板
                if (inputPath.endsWith(".ftl")) {
                    fileInfo.setGenerateType("dynamic");
                } else {
                    fileInfo.setGenerateType("static");
                }
            }
        }
    }

    private static void validAndFillMetaRoot(Meta meta) {
        //基础信息校验和默认值
        String name = StrUtil.blankToDefault(meta.getName(), "my-generator");
        String description = StrUtil.emptyToDefault(meta.getDescription(), "我的模板代码生成器");
        String author = StrUtil.emptyToDefault(meta.getAuthor(), "qimin");
        String basePackage = StrUtil.blankToDefault(meta.getBasePackage(), "com.qimin");
        String version = StrUtil.emptyToDefault(meta.getVersion(), "1.0");
        String createTime = StrUtil.emptyToDefault(meta.getCreateTime(), DateUtil.now());
        meta.setName(name);
        meta.setDescription(description);
        meta.setAuthor(author);
        meta.setBasePackage(basePackage);
        meta.setVersion(version);
        meta.setCreateTime(createTime);
    }
}