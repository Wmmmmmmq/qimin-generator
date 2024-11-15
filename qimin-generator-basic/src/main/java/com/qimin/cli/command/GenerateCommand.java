package com.qimin.cli.command;

import cn.hutool.core.bean.BeanUtil;
import com.qimin.generator.MainGenerator;
import com.qimin.model.MainTemplateConfig;
import lombok.Data;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.concurrent.Callable;

@Command(name = "generate",description = "生成代码",mixinStandardHelpOptions = true)
@Data
public class GenerateCommand implements Callable<Integer> {


    @Option(names = {"-a","--author"},description = "作者名称", interactive = true, arity = "0..1")
    private String author = "qimin";

    @Option(names = {"-o","--outputText"},description = "输出结果", interactive = true, arity = "0..1")
    private String outputText = "输出结果";

    @Option(names = {"-l","--loop"},description = "是否循环", interactive = true, arity = "0..1")
    private boolean loop;

    @Override
    public Integer call() throws Exception {
        MainTemplateConfig mainTemplateConfig = new MainTemplateConfig();
        BeanUtil.copyProperties(this,mainTemplateConfig);
        MainGenerator.doGenerate(mainTemplateConfig);
        return 0;
    }
}
