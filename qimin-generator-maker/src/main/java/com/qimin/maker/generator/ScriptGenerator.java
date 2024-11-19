package com.qimin.maker.generator;

import cn.hutool.core.io.FileUtil;

import java.io.File;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Set;

public class ScriptGenerator {

    public static void doGenerate(String outputPath,String jarPath){
        //直接写入脚本文件
        //linux

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("#!/bin/bash").append("\n");
        stringBuilder.append(String.format("java -jar %s \"$@\"",jarPath)).append("\n");
        FileUtil.writeBytes(stringBuilder.toString().getBytes(StandardCharsets.UTF_8),outputPath);
        //添加可执行权限
        try {
            Set<PosixFilePermission> permissions = PosixFilePermissions.fromString("rwxrwxrwx");
            Files.setPosixFilePermissions(Paths.get(outputPath),permissions);
        }catch (Exception e){

        }

        //windows
        stringBuilder = new StringBuilder();
        stringBuilder.append("@echo off").append("\n");
        stringBuilder.append(String.format("java -jar %s %%*", jarPath)).append("\n");
        FileUtil.writeBytes(stringBuilder.toString().getBytes(StandardCharsets.UTF_8),outputPath+".bat");
    }

    public static void main(String[] args) {
        String outputPath = System.getProperty("user.dir")+File.separator+"generator";
        doGenerate(outputPath,"");
    }
}
