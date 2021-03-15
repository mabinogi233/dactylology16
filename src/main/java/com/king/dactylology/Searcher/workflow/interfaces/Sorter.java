package com.king.dactylology.Searcher.workflow.interfaces;


import com.king.dactylology.Searcher.entry.Items.Answer;
import com.king.dactylology.Searcher.entry.Items.Question;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Sorter {
    //查询结果排序
    List<Answer> sort(List<Answer> answers, Question question);

}
