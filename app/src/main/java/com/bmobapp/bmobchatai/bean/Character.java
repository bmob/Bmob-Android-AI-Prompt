package com.bmobapp.bmobchatai.bean;

import android.content.Intent;

import cn.bmob.v3.BmobObject;

/**
 * 角色表类
 */
public class Character extends BmobObject {
    /**
     * 多少人在用这个角色
     */
    String userNum;

    /**
     * 角色名称
     */
    String title;

    /**
     * 角色的英文名
     */
    String name;

    /**
     * 角色头像
     */
    String img;

    /**
     * 角色的prompt
     */
    String prompt;

    /**
     * 角色的描述信息
     */
    String description;


    /**
     * 开始的对话语
     */
    String startMsg;

    /**
     * 排列顺序
     */
    Integer rank;

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public String getStartMsg() {
        return startMsg;
    }

    public void setStartMsg(String startMsg) {
        this.startMsg = startMsg;
    }



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
