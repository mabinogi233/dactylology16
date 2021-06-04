package com.king.dactylology.Searcher.entry.Items;

import org.springframework.stereotype.Component;

//搜索结果
@Component
public class Answer extends Item {
    //标题
    String title;
    //简介
    String introduction;
    //资源链接
    String url;
    //来源（搜索元标识符）
    String source;
    //相似度
    double sim;
    //域，存储自定义对象
    Object memory;

    public double getSim() {
        return sim;
    }

    public String getUrl() {
        return url;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getSource() {
        return source;
    }

    public String getTitle() {
        return title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSim(double sim) {
        this.sim = sim;
    }

    public Object getMemory() {
        return memory;
    }

    public void setMemory(Object memory) {
        this.memory = memory;
    }
}
