package com.qimin.maker.cli.command;

import cn.hutool.core.io.FileUtil;
import picocli.CommandLine.Command;

import java.io.File;
import java.util.List;

@Command(name = "list", description = "遍历所有要生成的文件", mixinStandardHelpOptions = true)
public class ListCommand implements Runnable {

    @Override
    public void run() {
        String projectPath = System.getProperty("user.dir");
        //整个项目的根目录
        File parentFile = new File(projectPath + File.separator + "qimin-generator-maker").getParentFile();
        //输入路径
        String inputPath = new File(parentFile, "qimin-generator-demo-projects/acm-template").getAbsolutePath();
        List<File> files = FileUtil.loopFiles(inputPath);
        for (File file : files) {
            System.out.println(file);
        }

    }
}
