package com.king.dactylology.Searcher.workflow.interfaces;



import com.king.dactylology.Searcher.entry.Items.Question;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SelectCreator {
    //生成相似查询
    List<Question> createSimSentence(Question question);
}
