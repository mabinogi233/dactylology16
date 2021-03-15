package com.king.dactylology.Searcher.entry.Items;

import org.springframework.stereotype.Component;

import java.util.List;

//代表一个搜索元
@Component
public abstract class SearchItem extends Item {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 搜索元
     * @param question
     * @return
     */
    public abstract List<Answer> search(Question question);
}
