package com.king.dactylology.Searcher.handle.interfaces;


import com.king.dactylology.Searcher.entry.Items.Answer;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public interface Handle3 {

    //过滤排序前的结果
    List<Answer> handle(List<Answer> answers, Map<String, Object> parameter);
}
