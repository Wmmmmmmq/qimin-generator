package com.qimin.generator;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ArrayUtil;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

/**
 * 静态代码生成器
 */
public class StaticGenerator {

    public static void main(String[] args) {
        String projectPath = System.getProperty("user.dir");
        String inputPath = projectPath + File.separator + "qimin-generator-demo-projects" + File.separator + "acm-template";
        String outputPath = projectPath;
        copyFileByHutool(inputPath, outputPath);
    }

    /**
     * hutool工具复制文件
     *
     * @param inputPath
     * @param outputPath
     */
    public static void copyFileByHutool(String inputPath, String outputPath) {
        FileUtil.copy(inputPath, outputPath, false);
    }

    /**
     * 递归拷贝文件
     *
     * @param inputPath
     * @param outputPath
     * @throws Exception
     */
    public static void copyFileByRecursive(String inputPath, String outputPath) throws Exception {
        File inputFile = new File(inputPath);
        File outputFile = new File(outputPath);
        try {
            copyFileByRecursive(inputFile, outputFile);
        } catch (Exception e) {
            System.out.println("文件复制失败");
            e.printStackTrace();
        }
    }

    /**
     * 递归实现文件复制
     *
     * @param inputFile
     * @param outputFile
     * @throws Exception
     */
    public static void copyFileByRecursive(File inputFile, File outputFile) throws Exception {
        //区分是文件还是目录
        if (inputFile.isDirectory()) {
            File destOutputFile = new File(outputFile, inputFile.getName());
            //如果是目录，则首先创建目标目录
            if (!destOutputFile.exists()) {
                destOutputFile.mkdirs();
            }
            //获取目录下的所有文件和子目录
            File[] files = destOutputFile.listFiles();
            //无子文件则直接结束
            if (ArrayUtil.isEmpty(files)) {
                return;
            }
            for (File file : files) {
                //递归拷贝下一层文件
                copyFileByRecursive(file, destOutputFile);
            }
        } else {
            //是文件，则直接复制到目标目录下
            Path resolve = outputFile.toPath().resolve(inputFile.getName());
            Files.copy(inputFile.toPath(), resolve, StandardCopyOption.REPLACE_EXISTING);
        }
    }
}
