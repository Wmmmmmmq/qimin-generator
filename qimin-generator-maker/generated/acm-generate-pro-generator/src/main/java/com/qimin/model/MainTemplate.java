package com.qimin.model;

import lombok.Data;

/**
 * 用于生成核心模板文件
 */
@Data
public class MainTemplate {
    /**
     * 作者注释
     */
    public String author = "qimin";

    /**
     * 输出信息
     */
    public String outputText = "sum = ";
}
