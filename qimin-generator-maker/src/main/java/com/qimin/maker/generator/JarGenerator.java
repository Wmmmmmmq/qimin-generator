package com.qimin.maker.generator;

import java.io.*;

public class JarGenerator {

    public static void doGenerator(String projectStr) throws IOException, InterruptedException {
        //构建命令
        //打包前先清理之前的构建
        String winMavenCommand = "mvn.cmd clean package -DskipTests=true";
        String otherMavenCommand = "mvn clean package -DskipTests=true";

        String mavenCommand = winMavenCommand;

        //一定要先将命令拆分 不然会直接以一个整个字符串去执行命令 会出错
        ProcessBuilder processBuilder = new ProcessBuilder(mavenCommand.split(" "));
        processBuilder.directory(new File(projectStr));

        Process process = processBuilder.start();

        //读取命令的输出
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine())!=null){
            System.out.println(line);
        }

        //等待命令完成
        int exitCode = process.waitFor();
        System.out.println("执行命令结束，退出码：" + exitCode);
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        doGenerator("D:\\newStage\\qimin-generator\\qimin-generator-basic");
    }
}
