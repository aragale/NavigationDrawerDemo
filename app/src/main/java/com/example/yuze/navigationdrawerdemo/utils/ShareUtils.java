package com.example.yuze.navigationdrawerdemo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShareUtils {

    /**
     * 分享消息模板
     */
    private static final String SHARE_TEMPLATE = "%s和你分享了一个足迹，复制这条消息，快去看看吧！  #%s#";

    /**
     * 分享消息模板
     */
    private static final Pattern SHARE_PATTERN = Pattern.compile("(.+?)和你分享了一个足迹，复制这条消息，快去看看吧！  #(.+?)#");

    /**
     * 主方法，用于测试
     */
    public static void main(String[] args) {
        //测试「messageToUsernameAndFootId」方法
        String message1 = "小明和你分享了一个足迹，复制这条消息，快去看看吧！  #b02dfcb8-c62d-4c99-b2fe-643b85e75e84#";
        String[] result = messageToUsernameAndFootId(message1);
        System.out.println("用户名：" + result[0] + "\n足迹ID：" + result[1]);

        //测试「toMessage」
        String message2 = usernameAndFootIdToMessage("小东", "a82d85d5-ad77-4eaf-a0d6-da62da747ae6");
        System.out.println("生成的文字信息：" + message2);
    }

    /**
     * 将用户名和足迹ID转为分享消息
     */
    public static String usernameAndFootIdToMessage(final String userName, final String footId) {
        return String.format(SHARE_TEMPLATE, userName, footId);
    }

    /**
     * 分享消息转用户名和足迹ID，若传入消息有效，返回长度为2的字符串数组，第一个成员为用户名，第二个成员为足迹ID
     */
    public static String[] messageToUsernameAndFootId(final String message) {
        final Matcher matcher = SHARE_PATTERN.matcher(message);
        if (matcher.find()) {
            String[] result = new String[2];
            result[0] = matcher.group(1);
            result[1] = matcher.group(2);
            return result;
        } else {
            return null;
        }
    }
}
