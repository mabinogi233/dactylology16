package com.king.dactylology.Searcher.entry.Items;

import org.springframework.stereotype.Component;

@Component
public class Question extends Item{
    //查询语句
    private String selectSentence;

    public String getSelectSentence() {
        return selectSentence;
    }

    public void setSelectSentence(String selectSentence) {
        this.selectSentence = selectSentence;
    }

}
