package com.qimin.maker;

import com.qimin.maker.generator.main.GenerateTemplate;
import com.qimin.maker.generator.main.MainGenerator;
import com.qimin.maker.generator.main.ZipGenerator;
import freemarker.template.TemplateException;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws TemplateException, IOException, InterruptedException {

        //MainGenerator mainGenerator = new MainGenerator();
        GenerateTemplate generateTemplate = new ZipGenerator();
        generateTemplate.doGenerate();
    }
}
