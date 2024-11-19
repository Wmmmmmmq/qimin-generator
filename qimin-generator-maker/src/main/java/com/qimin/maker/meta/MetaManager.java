package com.qimin.maker.meta;

import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.json.JSONUtil;

public class MetaManager {

    private static volatile Meta meta;

    private MetaManager() {

    }

    public static Meta getObjectMeta() {
        if (meta == null) {
            synchronized (MetaManager.class) {
                if (meta == null) {
                    meta = initMeta();
                }
            }
        }
        return meta;
    }

    private static Meta initMeta() {
        String metaJson = ResourceUtil.readUtf8Str("meta.json");
        Meta meta = JSONUtil.toBean(metaJson, Meta.class);
        Meta.FileConfig fileConfig = meta.getFileConfig();
        //todo:: 校验配置文件，处理默认值
        return meta;
    }
}