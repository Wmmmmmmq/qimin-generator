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
    private String author = "qimin";

    /**
     * 输出信息
     */
    private String outputText = "输出结果";

    /**
     * 是否循环（开关）
     */
    private boolean loop;
}
