package com.king.dactylology.Searcher.workflow.mainutils;


import com.king.dactylology.Searcher.algorithm.AlgorithmFactory;
import com.king.dactylology.Searcher.algorithm.algroithmItems.GetSimPoint;
import com.king.dactylology.Searcher.entry.Items.Answer;
import com.king.dactylology.Searcher.entry.Items.Question;
import com.king.dactylology.Searcher.workflow.interfaces.Sorter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class MainSorter implements Sorter {

    @Autowired
    @Qualifier("algorithmFactory")
    private AlgorithmFactory algorithmFactory;


    /**
     * 排序器，此项目不需要排序
     * @param answers
     * @param question
     * @return
     */

    @Override
    public List<Answer> sort(List<Answer> answers, Question question) {
        return answers;
    }
}
