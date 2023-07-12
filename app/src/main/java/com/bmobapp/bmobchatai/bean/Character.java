package com.bmobapp.bmobchatai.bean;

import cn.bmob.v3.BmobObject;

/**
 * 对应Bmob上面Prompt表的信息
 */
public class Character extends BmobObject {
    /**
     * 这个AI助手有多少人在用
     */
    String userNum;

    /**
     * AI助手的名称
     */
    String title;

    /**
     * 角色的英文名
     */
    String name;

    /**
     * AI助手的图片
     */
    String img;

    /**
     * AI助手的prompt
     */
    String prompt;

    /**
     * AI助手的描述信息
     */
    String description;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPrompt() {
        return prompt;
    }

    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUserNum() {
        return userNum;
    }

    public void setUserNum(String userNum) {
        this.userNum = userNum;
    }


}
