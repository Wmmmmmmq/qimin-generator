package com.qimin.cli.example;

import picocli.CommandLine;
import picocli.CommandLine.Option;

import java.util.Arrays;

public class Login implements Runnable {

    @Option(names = {"-u", "--user"}, description = "User name")
    String user;

    @Option(names = {"-p", "--password"}, description = "Passphrase", interactive = true, arity = "0..1", prompt = "请输入密码：")
    String password;

    @Option(names = {"-cp", "--checkPassword"}, description = "Check password", interactive = true, arity = "0..1", prompt = "确认密码：")
    String checkPassword;

    @Override
    public void run() {
        System.out.println("user = " + user);
        System.out.println("password = " + password);
        System.out.println("checkPassword = " + checkPassword);
    }

    public static void main(String[] args) throws Exception {
        CommandLine cmd = new CommandLine(new Login());
        args = new String[]{"-u", "user132", "1234"};
        // 使用反射来修改 args 数组，确保 -p 和 -cp 参数存在
        args = adjustArgs(args);
        // 执行命令行解析
        cmd.execute(args);
    }

    // 通过反射调整 args 数组，插入缺失的参数
    public static String[] adjustArgs(String[] args) throws Exception {
        // 确保命令行参数包含 -p 和 -cp
        boolean hasPassword = Arrays.asList(args).contains("-p");
        boolean hasCheckPassword = Arrays.asList(args).contains("-cp");
        // 如果没有 -p 或 -cp，修改 args 数组来加入这些参数
        if (!hasPassword || !hasCheckPassword) {
            // 新的 args 数组
            String[] newArgs = Arrays.copyOf(args, args.length + 2); // 拷贝并扩展4个位置
            // 确保插入 -p 参数
            if (!hasPassword) {
                int index = findInsertionIndex(args, "-u") + 2; // 找到 "-u" 后面的位置，插入 "-p"
                System.arraycopy(args, index, newArgs, index + 2, args.length - index); // 移动后面的元素
                newArgs[index] = "-p"; // 插入 "-p"
                if (index < args.length) {
                    newArgs[index + 1] = args[index]; // 插入密码
                }
            }
            // 确保插入 -cp 参数
            if (!hasCheckPassword) {
                int index;
                if (args.length == 2) {
                    index = findInsertionIndex(newArgs, "-p") + 1; // 找到 "-p" 后面的位置，插入 "-cp"
                } else {
                    index = findInsertionIndex(newArgs, "-p") + 2; // 找到 "-p" 后面的位置，插入 "-cp"
                }
                newArgs[index] = "-cp"; // 插入 "-cp"
                if (index < args.length) {
                    newArgs[index + 1] = args[index - 1]; // 插入确认密码
                }
            }
            return newArgs; // 返回更新后的 args 数组
        }
        return args; // 如果没有缺少的参数，直接返回原始 args
    }

    // 找到插入参数的位置
    private static int findInsertionIndex(String[] args, String target) {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals(target)) {
                return i;
            }
        }
        return -1; // 如果找不到返回 -1
    }
}
