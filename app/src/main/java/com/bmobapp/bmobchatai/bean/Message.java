package com.bmobapp.bmobchatai.bean;

import cn.bmob.v3.BmobObject;

/**
 * 聊天表类
 */
public class Message extends BmobObject {
    public static String SEND_BY_ME="me";
    public static String SEND_BY_BOT="bot";

    /**
     * 会话
     */
    String session;

    /**
     * 内容的归属权
     */
    String username;

    /**
     * 发送的内容
     */
    String message;

    /**
     * 发送者（me和bot两种类型）
     */
    String sendBy;

    /**
     * 获取用户的名字
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * 设置用户的名字
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * 获取会话信息
     * @return
     */
    public String getSession() {
        return session;
    }

    /**
     * 设置会话信息
     * @param session
     */
    public void setSession(String session) {
        this.session = session;
    }

    /**
     * 获取聊天内容
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * 设置聊天内容
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * 获取发送者
     * @return
     */
    public String getSendBy() {
        return sendBy;
    }

    /**
     * 设置发送者
     * @param sendBy
     */
    public void setSendBy(String sendBy) {
        this.sendBy = sendBy;
    }

    /**
     * 构造函数
     */
    public Message(){

    }

    /**
     * 聊天内容的构造函数
     * @param message 聊天内容
     * @param sendBy 发送者
     */
    public Message(String message, String sendBy,String session,String username) {
        this.message = message;
        this.sendBy = sendBy;
        this.session = session;
        this.username = username;
    }
}
