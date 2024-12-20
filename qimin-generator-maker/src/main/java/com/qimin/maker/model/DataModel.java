package com.qimin.maker.model;

import lombok.Data;

/**
 * 静态模板配置
 */
@Data
public class DataModel {

    /**
     * 作者
     */
    public String author = "qimin";

    /**
     * 输出信息
     */
    public String outputText = "输出结果";

    /**
     * 是否循环（开关）
     */
    public boolean loop;

    /**
     * 是否生成 .gitignore文件
     */
    public boolean needGit;
}
